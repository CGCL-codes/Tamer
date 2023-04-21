package corpodatrecordsbackend;

import corpodatrecordsbackend.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author nemesis
 */
public class ConfigurationValueJpaController {

    public ConfigurationValueJpaController() {
        emf = Persistence.createEntityManagerFactory("CorpodatRecordsBackendPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConfigurationValue configurationValue) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(configurationValue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConfigurationValue configurationValue) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            configurationValue = em.merge(configurationValue);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configurationValue.getId();
                if (findConfigurationValue(id) == null) {
                    throw new NonexistentEntityException("The configurationValue with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConfigurationValue configurationValue;
            try {
                configurationValue = em.getReference(ConfigurationValue.class, id);
                configurationValue.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configurationValue with id " + id + " no longer exists.", enfe);
            }
            em.remove(configurationValue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConfigurationValue> findConfigurationValueEntities() {
        return findConfigurationValueEntities(true, -1, -1);
    }

    public List<ConfigurationValue> findConfigurationValueEntities(int maxResults, int firstResult) {
        return findConfigurationValueEntities(false, maxResults, firstResult);
    }

    private List<ConfigurationValue> findConfigurationValueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ConfigurationValue as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ConfigurationValue findConfigurationValue(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConfigurationValue.class, id);
        } finally {
            em.close();
        }
    }

    public String findItemValue(String item) {
        EntityManager em = getEntityManager();
        try {
            return (String) em.createQuery("select cv.itemValue from ConfigurationValue cv where cv.item = :item").setParameter("item", item).getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public int getConfigurationValueCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from ConfigurationValue as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
