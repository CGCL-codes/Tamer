package br.ufmg.lcc.arangiSecurity.controller.bean;

import br.ufmg.lcc.arangi.controller.bean.UserContext;
import br.ufmg.lcc.arangiSecurity.dto.User;

/**

 * @author Salazar
 *
 */
public class ArangiSecurityUserContext extends UserContext {

    private User user;

    private boolean userConfigured = false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUserConfigured() {
        return userConfigured;
    }

    public void setUserConfigured(boolean userConfigured) {
        this.userConfigured = userConfigured;
    }
}
