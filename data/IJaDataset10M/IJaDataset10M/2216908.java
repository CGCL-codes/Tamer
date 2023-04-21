package org.jsecurity;

import java.io.Serializable;
import java.security.Principal;

public class UsernamePrincipal implements Principal, Serializable {

    private String username;

    public UsernamePrincipal(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return String.valueOf(username);
    }
}
