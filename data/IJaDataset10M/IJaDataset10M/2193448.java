package it.javalinux.wise.seam.actions;

import it.javalinux.wise.seam.entities.UserInformation;
import java.util.List;
import javax.persistence.EntityManager;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator {

    @Logger
    Log log;

    @In
    Identity identity;

    @In
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public boolean authenticate() {
        log.info("authenticating #0", identity.getUsername());
        List<UserInformation> results = entityManager.createQuery("select u from UserInformation u where u.name=:username and u.pwd=:password").setParameter("username", identity.getUsername()).setParameter("password", identity.getPassword()).getResultList();
        if (results.size() == 0) {
            return false;
        } else {
            identity.addRole("admin");
            return true;
        }
    }
}
