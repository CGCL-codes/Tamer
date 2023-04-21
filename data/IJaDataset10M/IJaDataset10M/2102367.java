package net.cepra.core.server.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.cepra.core.domain.Resource;
import net.cepra.core.server.dao.IResourceDAO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ResourceDAO implements IResourceDAO {

    private EntityManager entityManager;

    /**
   * Sets the associated Entity manager factory
   * 
   * @param factory
   */
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
   * Load the user with the argument ID
   */
    @Transactional(propagation = Propagation.SUPPORTS)
    @SuppressWarnings("unchecked")
    public Resource findUser(String username) {
        Query query = entityManager.createQuery("from " + Resource.class.getName() + " as r where r.username = :username");
        query.setParameter("username", username);
        List<Resource> activityList = query.getResultList();
        if (activityList.size() == 1) return activityList.get(0);
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Resource authenticate(String username, String password) {
        Query query = entityManager.createQuery("select r from Resource r, Password p where r.username = :username and p.password = :password and r.id = p.id");
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            return (Resource) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
