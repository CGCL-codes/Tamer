package de.suse.swamp.rss.auth;

import java.security.*;
import java.util.*;
import org.apache.catalina.realm.*;
import de.suse.swamp.util.*;

public class SwampRssRealm extends UserDatabaseRealm {

    protected static final String name = "SwampRssRealm";

    /**
     * authenticating the user with the ldap-auth lib against ldap server. 
     * every authenticated user is allowed to to everything atm.
     */
    public Principal authenticate(String username, String credentials) {
        try {
            de.suse.swamp.core.container.SecurityManager.getAuthenticatedUser(username, credentials);
        } catch (Exception e) {
            Logger.WARN("RSS HTTP login failed: " + e.getMessage());
            return null;
        } catch (Error e) {
            Logger.WARN("RSS HTTP login failed: " + e.getMessage());
            return null;
        }
        List roles = new ArrayList();
        roles.add("tomcat");
        return new GenericPrincipal(this, username, "", roles);
    }

    protected String getName() {
        return (name);
    }
}
