package net.kodra.supereasy.authentication.server;

import javax.persistence.EntityManager;
import net.kodra.supereasy.jpa.server.EMF;

public final class DataStore {

    public static void save(net.kodra.openid.gwt.widget.AuthenticatedUser authenticatedUser) {
        AuthenticatedUser aUser = new AuthenticatedUser();
        aUser.setAdminFlag(authenticatedUser.getAdminFlag());
        aUser.setAuthDomain(authenticatedUser.getAuthDomain());
        aUser.setEmail(authenticatedUser.getEmail());
        aUser.setFederatedIdentity(authenticatedUser.getFederatedIdentity());
        aUser.setNickname(authenticatedUser.getNickname());
        aUser.setUserId(authenticatedUser.getUserId());
        EntityManager em = EMF.getCreatedFactory().createEntityManager();
        em.persist(aUser);
        em.close();
    }
}
