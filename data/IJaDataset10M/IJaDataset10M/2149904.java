package org.hourglassstudios.tempuspre.server.authenticator;

import java.io.Serializable;
import com.sun.sgs.auth.Identity;

public class TpIdentity implements Identity, Serializable {

    private static final long serialVersionUID = 1L;

    String name;

    public TpIdentity(String userName) {
        name = userName;
    }

    public String getName() {
        return name;
    }

    public void notifyLoggedIn() {
    }

    public void notifyLoggedOut() {
    }
}
