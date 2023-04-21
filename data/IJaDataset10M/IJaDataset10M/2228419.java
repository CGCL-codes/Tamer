package org.openeccos;

import java.io.Serializable;
import org.openeccos.dao.EccosRepository;
import org.openeccos.model.MUser;
import com.sas.framework.expojo.ModelExposer;

public class EccosUserSession implements Serializable {

    public static final String USER_LOGIN_PANEL = "user_login_panel";

    public static final String USER_MAIN_PANEL = "user_main_panel";

    protected MUser user;

    public EccosUserSession() {
    }

    public void doLogin(String login, String password) {
        EccosRepository repo = (EccosRepository) ModelExposer.get().getRepository(EccosRepository.NAME);
        user = repo.findUserByLogin(login, password);
    }

    public void doLogout() {
        user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public MUser getUser() {
        return user;
    }

    public void setUser(MUser user) {
        this.user = user;
    }
}
