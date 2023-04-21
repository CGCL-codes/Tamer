package biblioteca.dao.interfaces;

import biblioteca.entidades.UsuarioBean;

/**
 *
 * @author aluno
 */
public interface UsuarioInterface {

    public void adicionaUsuario(UsuarioBean usuario);

    public boolean excluirUsuario(String CPF);

    public void aterarUsuario(String CPF);

    public UsuarioBean pesquisarUsuario(String cpf);

    public boolean login(String login, String senha);

    public String[] getNomeFromLogin(String login, String senha);
}
