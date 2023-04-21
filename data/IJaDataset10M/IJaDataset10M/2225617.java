package br.ufmg.saotome.arangiSecurity.controller.logic;

import br.ufmg.saotome.arangi.controller.bean.CRUDLogicBean;
import br.ufmg.saotome.arangiSecurity.controller.AppButtonBarController;
import br.ufmg.saotome.arangiSecurity.dto.HomeDTO;

public class HomePage extends CRUDLogicBean<HomeDTO> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public HomePage() {
        setDtoBeanName("homeDTO");
        setControllerBeanName("homeController");
        setButtonBarControllerClassName(AppButtonBarController.class.getName());
        setShowButtonBar(false);
        setDefaultAction(ACTION_NEW);
    }
}
