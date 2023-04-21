package persistencia;

import java.util.Set;
import dao.UsuarioDAO;
import modelo.Usuario;

public class UsuarioPersistencia implements IUsuario {

    private UsuarioDAO usuarioDAO;

    public UsuarioPersistencia(UsuarioDAO usuarioDAO) {
        super();
        this.usuarioDAO = usuarioDAO;
    }

    public boolean addUsuario(Usuario usuario) {
        return usuarioDAO.addUsuario(usuario);
    }

    public boolean delUsuario(Usuario usuario) {
        return usuarioDAO.delUsuario(usuario);
    }

    public Usuario getUsuario(String nombre) {
        return usuarioDAO.getUsuario(nombre);
    }

    public Set<Usuario> getUsuarios() {
        return usuarioDAO.getUsuarios();
    }
}
