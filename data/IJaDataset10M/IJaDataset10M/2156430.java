package br.com.findstore.loja.controller;

import br.com.findstore.loja.basica.Loja;
import br.com.findstore.loja.dao.ILojaHibernateDAO;
import br.com.findstore.util.controller.ControllerException;
import br.com.findstore.util.dao.DAOException;
import br.com.findstore.util.dao.DAOFactory;

public class LojaController implements ILojaController {

    private ILojaHibernateDAO lojaDAO;

    public LojaController() {
        super();
        this.lojaDAO = DAOFactory.getLojaDAO();
    }

    @Override
    public void cadastrarLoja(Loja l) throws ControllerException {
        if (l != null && l.getNumeroLoja() != 0) {
            try {
                l.setAtivo("1");
                this.lojaDAO.cadastrarLojaDAO(l);
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void alterarStatusLoja(Loja loja) throws ControllerException {
        if (loja != null && loja.getNumeroLoja() != 0 && loja.getNumeroLoja() > 0) {
            try {
                if (loja.getAtivo() == "1" || loja.getAtivo() != null || loja.getAtivo() != "null") {
                    loja.setAtivo("0");
                    this.lojaDAO.alterarStatusLojaDAO(loja);
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
    }
}
