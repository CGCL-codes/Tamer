package br.ufmg.saotome.arangiSecurity.controller.logic;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import br.ufmg.saotome.arangi.controller.bean.SearchLogicBean;
import br.ufmg.saotome.arangiSecurity.controller.AppButtonBarController;
import br.ufmg.saotome.arangiSecurity.dto.User;

@ManagedBean
@SessionScoped
public class UserSearch extends SearchLogicBean<User> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public UserSearch() {
        setDefaultAction(ACTION_NEW);
        setDtoBeanName("user");
        setDtoClassName("br.ufmg.saotome.arangiSecurity.dto.User");
        setControllerBeanName("userSearchController");
        setButtonBarControllerClassName(AppButtonBarController.class.getName());
        setOpenSearching(true);
        setQueryName("userQuery");
    }
}
