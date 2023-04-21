package br.unic.ra.gerador.controller;

import java.util.ArrayList;
import br.unic.ra.gerador.bean.ObjetoBean;
import br.unic.ra.gerador.dao.ObjetoDAO;

public class ObjetoController {

    public void deleteByUsuario(int usuarioId) throws Exception {
        new ObjetoDAO().deleteByUsuario(usuarioId);
    }

    public void insert(ObjetoBean objeto) throws Exception {
        new ObjetoDAO().insert(objeto);
    }

    public ObjetoBean loadById(int id) throws Exception {
        if (id < 1) {
            throw new Exception("C�digo inv�lido");
        }
        return new ObjetoDAO().loadById(id);
    }

    public ObjetoBean loadObjetoByUsuario(int usuarioId) throws Exception {
        return new ObjetoDAO().loadObjetoByUsuario(usuarioId);
    }

    public ArrayList<ObjetoBean> loadObjetos() throws Exception {
        return new ObjetoDAO().loadObjetos();
    }

    public ArrayList<ObjetoBean> loadObjetosByUsuario(int usuarioId) throws Exception {
        return new ObjetoDAO().loadObjetosByUsuario(usuarioId);
    }
}
