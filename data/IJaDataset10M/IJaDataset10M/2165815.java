package pe.edu.cibertec.ventas.dao.interfaces;

import pe.edu.cibertec.security.bean.Usuario;

/**
 *
 * @author Instructor
 */
public interface UsuarioDAO {

    Usuario validarUsuario(String login, String password);
}
