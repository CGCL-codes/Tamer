package br.usp.ime.forum.business;

import java.util.List;
import br.usp.ime.forum.dao.UsuarioDAO;
import br.usp.ime.forum.model.Usuario;

public class BuscaBusiness {

    UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    public List<Usuario> buscarUsuarios(String piece) {
        return usuarioDAO.findByNameOrNickname(piece);
    }
}
