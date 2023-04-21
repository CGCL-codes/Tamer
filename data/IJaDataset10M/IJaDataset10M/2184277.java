package es.ugr.cursows.basededatos;

import es.ugr.cursows.basededatos.exceptions.ElementoNoEnconrtradoException;
import es.ugr.cursows.basededatos.exceptions.ElementoRepetidoException;
import es.ugr.cursows.modelo.Usuario;
import java.util.List;

/**
 *
 * @author pgarcia
 */
public interface BaseDatos {

    public List<Usuario> listarUsuarios();

    void guardarUsuario(Usuario u) throws ElementoRepetidoException;

    Usuario obtenerUsuario(String username) throws ElementoNoEnconrtradoException;
}
