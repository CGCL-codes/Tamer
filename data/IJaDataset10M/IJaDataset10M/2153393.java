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
 * @author luis
 */
public class ModulesIndexJpaController {

    public ModulesIndexJpaController() {
        emf = Persistence.createEntityManagerFactory("CorpodatRecordsBackendPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ModulesIndex modulesIndex) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(modulesIndex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ModulesIndex modulesIndex) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            modulesIndex = em.merge(modulesIndex);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = modulesIndex.getId();
                if (findModulesIndex(id) == null) {
                    throw new NonexistentEntityException("The modulesIndex with id " + id + " no longer exists.");
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
            ModulesIndex modulesIndex;
            try {
                modulesIndex = em.getReference(ModulesIndex.class, id);
                modulesIndex.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modulesIndex with id " + id + " no longer exists.", enfe);
            }
            em.remove(modulesIndex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ModulesIndex> findModulesIndexEntities() {
        return findModulesIndexEntities(true, -1, -1);
    }

    public List<ModulesIndex> findModulesIndexEntities(int maxResults, int firstResult) {
        return findModulesIndexEntities(false, maxResults, firstResult);
    }

    private List<ModulesIndex> findModulesIndexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ModulesIndex as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ModulesIndex findModulesIndex(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ModulesIndex.class, id);
        } finally {
            em.close();
        }
    }

    public int getModulesIndexCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from ModulesIndex as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
