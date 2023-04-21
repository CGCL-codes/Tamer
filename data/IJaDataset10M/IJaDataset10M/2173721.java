package springsecurity.dao;

import java.util.List;
import springsecurity.modelo.Usuario;

/**
 *
 * @author caio
 */
public interface UsuarioDao extends AbstractDao<Usuario, Long> {

    public Usuario getPorCpf(String cpf);

    public List<Usuario> getListaOrdenada();

    public Usuario getPorUsername(String username);
}
