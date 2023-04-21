package br.com.churrascariabrasadeouro.regras;

import java.util.ArrayList;
import br.com.churrascariabrasadeouro.negocio.Usuario;
import br.com.churrascariabrasadeouro.persistencia.UsuarioDAO;

public class RegraUsuario {

    private UsuarioDAO dao = new UsuarioDAO();

    public boolean validaUsuario(Usuario usuario) {
        return dao.validarUsuario(usuario);
    }

    public void cadastroUsuario(Usuario usuario) {
        dao.inserirUsuario(usuario);
    }

    public ArrayList<Usuario> listaUsuarios() {
        return dao.listarTodosUsuarios();
    }

    public void excluirUsuario(Usuario u) {
    }
}
