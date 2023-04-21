package net.sourceforge.openwebarch.app.thetimeloop.client.login.data;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    public boolean isLoggedIn;

    public String loginUrl;

    public String logoutUrl;

    public String eMailAddress;

    public String nickname;
}
