package com.eastidea.qaforum.home;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator {

    @Logger
    private Log log;

    @In
    Identity identity;

    @In
    Credentials credentials;

    public boolean authenticate() {
        log.info("authenticating {0}", credentials.getUsername());
        if ("admin".equals(credentials.getUsername())) {
            identity.addRole("admin");
            return true;
        }
        return false;
    }
}
