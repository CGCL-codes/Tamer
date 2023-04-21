package com.amidasoft.lincat.session;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

/**
 *
 * @author ricard
 */
@Stateful
@Name("logoutBean")
public class LogoutBean implements Logout {

    @In
    @Out
    Credentials credentials;

    @In
    Identity identity;

    @Logger
    private Log log;

    public LogoutBean() {
        super();
    }

    @Create
    public void inicialitza() {
    }

    @Remove
    public void destrueix() {
    }

    public void logout() {
        log.info("----------desloggejant...");
        identity.logout();
        identity.create();
        log.info("---------- Fet logout --------------");
        log.info("En teoria ara hauria de dir null: " + credentials.getUsername());
    }
}
