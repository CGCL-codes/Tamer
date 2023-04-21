package es.us.isw2.server.persistence.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import es.us.isw2.server.domain.DeliveryFileDAO;
import es.us.isw2.shared.exceptions.NonexistentEntityException;

/**
 *
 * @author Miguel
 */
public class DeliveryFileDAOJpaController {

    public DeliveryFileDAOJpaController() {
        emf = Persistence.createEntityManagerFactory("isw2.tasks");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DeliveryFileDAO deliveryFileDAO) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(deliveryFileDAO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DeliveryFileDAO deliveryFileDAO) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            deliveryFileDAO = em.merge(deliveryFileDAO);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = deliveryFileDAO.getId();
                if (findDeliveryFileDAO(id) == null) {
                    throw new NonexistentEntityException("The deliveryFileDAO with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DeliveryFileDAO deliveryFileDAO;
            try {
                deliveryFileDAO = em.getReference(DeliveryFileDAO.class, id);
                deliveryFileDAO.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deliveryFileDAO with id " + id + " no longer exists.", enfe);
            }
            em.remove(deliveryFileDAO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DeliveryFileDAO> findDeliveryFileDAOEntities() {
        return findDeliveryFileDAOEntities(true, -1, -1);
    }

    public List<DeliveryFileDAO> findDeliveryFileDAOEntities(int maxResults, int firstResult) {
        return findDeliveryFileDAOEntities(false, maxResults, firstResult);
    }

    private List<DeliveryFileDAO> findDeliveryFileDAOEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DeliveryFileDAO.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DeliveryFileDAO findDeliveryFileDAO(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DeliveryFileDAO.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeliveryFileDAOCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DeliveryFileDAO> rt = cq.from(DeliveryFileDAO.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
