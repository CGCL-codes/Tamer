package com.marce.remiseria.core.persistencia.dao;

import java.util.List;
import com.marce.remiseria.core.roles.Usuario;

public class UsuarioDaoImpl extends GenericDaoImpl implements UsuarioDao {

    @Override
    public Usuario load(String nick) {
        return (Usuario) dao.load(nick, Usuario.class);
    }

    @Override
    public void update(Usuario usuario) {
        dao.update(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        return (List<Usuario>) dao.findAll(Usuario.class);
    }
}
