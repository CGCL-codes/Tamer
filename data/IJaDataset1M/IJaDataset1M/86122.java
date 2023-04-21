package com.googlecode.myseam.service.common;

import javax.faces.application.FacesMessage;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator {

    @Logger
    Log log;

    @In
    Identity identity;

    public boolean authenticate() {
        try {
            return true;
        } catch (Exception e) {
            String msg = FacesMessages.createFacesMessage(FacesMessage.SEVERITY_WARN, "org.jboss.seam.loginFailed", "loginFailed", new Object[] {}).getDetail();
            log.warn(msg, e);
            return false;
        }
    }
}
