package org.isurf.spmiddleware.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.isurf.spmiddleware.dao.SubscriptionDAO;
import org.isurf.spmiddleware.model.Subscription;

/**
 * JPA implemantation of SubscriptionDAO.
 */
public class SubscriptionDAOImpl implements SubscriptionDAO {

    @PersistenceUnit
    private EntityManagerFactory emf;

    private static Logger logger = Logger.getLogger(SubscriptionDAOImpl.class);

    /**
	 * Sets the EntityManagerFactory.
	 *
	 * @param emf The EntityManagerFactory to be set.
	 */
    public void setEntityManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Subscription> findByECSpecName(String ecSpecName) {
        logger.debug("findByECSpecName: ecSpecName = " + ecSpecName);
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("findSubscriptionByECSPecName");
            query.setParameter(1, ecSpecName);
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Subscription findByNotificationURIandECSpecName(String notificationURI, String ecSpecName) {
        logger.debug("findByNotificationURIandECSpecName: notificationURI = " + notificationURI + "; ecSpecName = " + ecSpecName);
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("findSubscriptionByNotificationURIandECSpecName");
            query.setParameter(1, notificationURI);
            query.setParameter(2, ecSpecName);
            return (Subscription) query.getSingleResult();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<String> findNotificationURIsByECSpecName(String ecSpecName) {
        logger.debug("findByECSpecName: ecSpecName = " + ecSpecName);
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("findNotificationURIsByECSpecName");
            query.setParameter(1, ecSpecName);
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void saveOrUpdate(Subscription subscription) {
        logger.debug("saveOrUpdate: subscription = " + subscription);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(subscription);
            em.flush();
            em.getTransaction().commit();
            logger.debug("saveOrUpdate: commited");
        } finally {
            if (em != null) {
            }
        }
    }

    public void unsubscribe(String eventCycleSpecificationName) {
        logger.debug("unsubscribe: unsubscribe = " + eventCycleSpecificationName);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createNamedQuery("unsubscribe");
            query.setParameter(1, Subscription.State.INACTIVE);
            query.setParameter(2, eventCycleSpecificationName);
            query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
	 * @see SubscriptionDAO.findByNotificationURI
	 */
    public List<Subscription> findByNotificationURI(String notificationURI) {
        logger.debug("findByNotificationURI: notificationURI = " + notificationURI);
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("findSubscriptionByNotificationURI");
            query.setParameter(1, notificationURI);
            List<Subscription> subscriptions = query.getResultList();
            if (subscriptions == null) {
                subscriptions = new ArrayList<Subscription>();
            }
            return subscriptions;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
