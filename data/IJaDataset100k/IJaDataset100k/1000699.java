package org.openeye.identity.mail;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.openeye.model.User;

@Name("identityAddressResolver")
public class IdentityAddressResolver implements org.jbpm.mail.AddressResolver {

    @Override
    public Object resolveAddress(String actorId) {
        EntityManager em = (EntityManager) Component.getInstance("entityManager", true);
        Query q = em.createQuery("from User u where u.userName = :userName");
        q.setParameter("userName", actorId);
        User user = null;
        try {
            user = (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        if (user.getEmail().isEmpty()) {
            return null;
        }
        return user.getEmail();
    }
}
