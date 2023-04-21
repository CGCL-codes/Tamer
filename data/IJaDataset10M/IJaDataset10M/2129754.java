package br.com.findstore.facade;

import br.com.findstore.admin.basica.Admin;
import br.com.findstore.admin.controller.IAdminController;
import br.com.findstore.loja.basica.Loja;
import br.com.findstore.loja.controller.ILojaController;
import br.com.findstore.util.controller.ControllerException;
import br.com.findstore.util.controller.FactoryController;

public class Facade {

    private ILojaController controllerLoja;

    private IAdminController controllerAdmin;

    private static Facade facade;

    private Facade() {
        controllerLoja = FactoryController.getLojaController();
        controllerAdmin = FactoryController.getAdminController();
    }

    public static Facade getInstance() {
        if (facade == null) {
            facade = new Facade();
        }
        return facade;
    }

    public void cadastrarLoja(Loja l) throws ControllerException {
        this.controllerLoja.cadastrarLoja(l);
    }

    public void alterarStatusLoja(Loja loja) throws ControllerException {
        this.controllerLoja.alterarStatusLoja(loja);
    }

    public void cadastrarAdmin(Admin admin) throws ControllerException {
        this.controllerAdmin.cadastrarAdmin(admin);
    }

    public Admin efetuarLogin(Admin admin) throws ControllerException {
        return this.controllerAdmin.efetuarLogin(admin);
    }
}
