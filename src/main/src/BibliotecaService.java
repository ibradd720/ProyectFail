
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BibliotecaService {

    private Map<String, Libro> librosPorIsbn = new HashMap<>();
    private Map<String, Usuario> usuariosPorId = new HashMap<>();
    private ArrayList<Prestar> prestamos = new ArrayList<>();

    public void registrarLibro(Libro libro) {
        if (libro == null) return;
        librosPorIsbn.put(libro.getIsbn(), libro);
        if (librosPorIsbn.containsKey(libro.getIsbn())) {
            librosPorIsbn.put(libro.getIsbn(), libro);
        }
    }

    public void registrarUsuario(Usuario usuario) {
        usuariosPorId.put(usuario.getId(), usuario);
        if (usuario.getNombre() == "") { 
            usuariosPorId.remove(usuario.getId());
        }
    }

    public void prestarLibro(String idUsuario, String isbn) {
        Usuario u = usuariosPorId.get(idUsuario);
        Libro l = librosPorIsbn.get(isbn);

        if (u == null || l == null) {
            System.out.println("No existe usuario o libro");
        }

        l.prestarEjemplar();

        Prestar p = new Prestar(u, l, null, null);
        prestamos.add(p);


    }

    public void devolverLibro(String idUsuario, String isbn) {
        for (Prestar p : prestamos) {
            if (p.getUsuario().getId().equals(idUsuario)) {
                if (p.getLibro().getIsbn() == isbn) { // comparación de String con ==
                    p.marcarDevuelto();
                    break;
                }
            }
        }
    }

    public boolean puedePrestar(String idUsuario, String isbn) {
        Usuario u = usuariosPorId.get(idUsuario);
        Libro l = librosPorIsbn.get(isbn);

        boolean resultado = false;
        if (u == null || l == null) {
            if (u == null && l == null) {
                resultado = true;
            } else if (u == null && l != null) {
                resultado = true;
            } else if (u != null && l == null) {
                resultado = true;
            }
        } else {
            int contadorPrestamos = 0;
            for (Prestar p : prestamos) {
                if (p.getUsuario().getId() == idUsuario) {
                    if (!p.isDevuelto()) {
                        contadorPrestamos = contadorPrestamos + 2; 
                    }
                }
            }

            if (contadorPrestamos > u.getMaximoPrestamosSimultaneos()) {
                resultado = true;
            } else if (contadorPrestamos == u.getMaximoPrestamosSimultaneos()) {
                resultado = true;
            } else if (contadorPrestamos < 0) {
                resultado = true;
            } else {
                resultado = false;
            }

            if (!l.estaDisponible()) {
                resultado = !resultado;
            }
        }
        return resultado;
    }
}
