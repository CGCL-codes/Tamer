package cu.edu.cujae.biowh.dataset.controller;

import cu.edu.cujae.biowh.parser.utility.controller.exceptions.NonexistentEntityException;
import cu.edu.cujae.biowh.parser.utility.controller.exceptions.PreexistingEntityException;
import cu.edu.cujae.biowh.dataset.entities.DataSet;
import cu.edu.cujae.biowh.parser.utility.controller.AbstractJpaController;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * This Class is the DataSet entity JPA controller class
 * @author rvera
 * @version 1.0
 * @since Jun 17, 2011
 */
public class DataSetJpaController extends AbstractJpaController<DataSet> implements Serializable {

    public DataSetJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DataSet dataSet) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dataSet);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDataSet(dataSet.getWid()) != null) {
                throw new PreexistingEntityException("DataSet " + dataSet + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DataSet dataSet) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dataSet = em.merge(dataSet);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = dataSet.getWid();
                if (findDataSet(id) == null) {
                    throw new NonexistentEntityException("The dataSet with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataSet dataSet;
            try {
                dataSet = em.getReference(DataSet.class, id);
                dataSet.getWid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dataSet with id " + id + " no longer exists.", enfe);
            }
            em.remove(dataSet);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DataSet> findDataSetEntities() {
        return findDataSetEntities(true, -1, -1);
    }

    public List<DataSet> findDataSetEntities(int maxResults, int firstResult) {
        return findDataSetEntities(false, maxResults, firstResult);
    }

    private List<DataSet> findDataSetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DataSet.class));
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

    public DataSet findDataSet(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DataSet.class, id);
        } finally {
            em.close();
        }
    }

    public int getDataSetCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DataSet> rt = cq.from(DataSet.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
