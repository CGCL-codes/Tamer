package es.us.isw2.server.persistence.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import es.us.isw2.server.domain.AlumnDAO;
import es.us.isw2.shared.exceptions.NonexistentEntityException;
import es.us.isw2.shared.exceptions.PreexistingEntityException;

/**
 *
 * @author Miguel
 */
public class AlumnDAOJpaController {

    public AlumnDAOJpaController() {
        emf = Persistence.createEntityManagerFactory("isw2.tasks");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AlumnDAO alumnDAO) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(alumnDAO);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlumnDAO(alumnDAO.getEmail()) != null) {
                throw new PreexistingEntityException("AlumnDAO " + alumnDAO + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AlumnDAO alumnDAO) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            alumnDAO = em.merge(alumnDAO);
            em.getTransaction().commit();
            System.out.println("LLEGA");
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = alumnDAO.getEmail();
                if (findAlumnDAO(id) == null) {
                    throw new NonexistentEntityException("The alumnDAO with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AlumnDAO alumnDAO;
            try {
                alumnDAO = em.getReference(AlumnDAO.class, id);
                alumnDAO.getEmail();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumnDAO with id " + id + " no longer exists.", enfe);
            }
            em.remove(alumnDAO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AlumnDAO> findAlumnDAOEntities() {
        return findAlumnDAOEntities(true, -1, -1);
    }

    public List<AlumnDAO> findAlumnDAOEntities(int maxResults, int firstResult) {
        return findAlumnDAOEntities(false, maxResults, firstResult);
    }

    private List<AlumnDAO> findAlumnDAOEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AlumnDAO.class));
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

    public AlumnDAO findAlumnDAO(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AlumnDAO.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnDAOCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AlumnDAO> rt = cq.from(AlumnDAO.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
