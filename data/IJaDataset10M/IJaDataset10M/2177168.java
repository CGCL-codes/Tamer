package controlador;

import java.io.Serializable;
import java.util.List;
import modelo.UsuarioModel;
import modelo.entidades.Usuario;
import modelo.entidades.UsuarioImpl;
import vista.usuario.UsuarioView;

/**
 *
 * @author carlos
 * 
 */
public class UsuarioControllerImpl extends AbstractControllerImpl<UsuarioModel, UsuarioView, Serializable> implements UsuarioController {

    protected Usuario generaEntidad(List<Serializable> datos) {
        String nif = (String) datos.get(0);
        String nombre = (String) datos.get(1);
        String apellido1 = (String) datos.get(2);
        String apellido2 = (String) datos.get(3);
        String tipo = (String) datos.get(4);
        Usuario u = new UsuarioImpl(nif);
        u.setNombre(nombre);
        u.setApellido1(apellido1);
        u.setApellido2(apellido2);
        u.setTipo(tipo);
        return u;
    }

    protected Usuario generaEntidad(Serializable pk) {
        return new UsuarioImpl((String) pk);
    }
}
