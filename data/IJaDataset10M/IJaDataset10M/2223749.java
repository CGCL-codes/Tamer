package com.odontosis.service;

import java.util.Collection;
import com.odontosis.as.OdontosisApplicationService;
import com.odontosis.dao.UsuarioDAO;
import com.odontosis.entidade.GrupoUsuario;
import com.odontosis.entidade.Usuario;

public class UsuarioService extends OdontosisApplicationService<Usuario> {

    private final UsuarioDAO usuarioDao = new UsuarioDAO();

    public UsuarioService() throws Exception {
        super(Usuario.class, new UsuarioDAO());
    }

    public Usuario buscarPorLogin(String login) {
        return usuarioDao.buscarPorLogin(login);
    }

    public Collection<Usuario> pesquisarPorNomeLoginGrupo(String nome, String login, GrupoUsuario grupo) {
        return usuarioDao.pesquisarPorNomeLoginGrupo(nome, login, grupo);
    }
}
