package cu.edu.cujae.biowh.parser.kegg.controller;

import cu.edu.cujae.biowh.parser.utility.controller.exceptions.NonexistentEntityException;
import cu.edu.cujae.biowh.parser.utility.controller.exceptions.PreexistingEntityException;
import cu.edu.cujae.biowh.parser.kegg.entities.KEGGGene;
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
 * This class is the KEGGGene Jpa Controller
 * @author rvera
 * @version 1.0
 * @since Nov 17, 2011
 */
public class KEGGGeneJpaController extends AbstractJpaController<KEGGGene> implements Serializable {

    public KEGGGeneJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(KEGGGene KEGGGene) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(KEGGGene);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKEGGGene(KEGGGene.getWid()) != null) {
                throw new PreexistingEntityException("KEGGGene " + KEGGGene + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KEGGGene KEGGGene) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KEGGGene = em.merge(KEGGGene);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = KEGGGene.getWid();
                if (findKEGGGene(id) == null) {
                    throw new NonexistentEntityException("The kEGGGene with id " + id + " no longer exists.");
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
            KEGGGene KEGGGene;
            try {
                KEGGGene = em.getReference(KEGGGene.class, id);
                KEGGGene.getWid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The KEGGGene with id " + id + " no longer exists.", enfe);
            }
            em.remove(KEGGGene);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<KEGGGene> findKEGGGeneEntities() {
        return findKEGGGeneEntities(true, -1, -1);
    }

    public List<KEGGGene> findKEGGGeneEntities(int maxResults, int firstResult) {
        return findKEGGGeneEntities(false, maxResults, firstResult);
    }

    private List<KEGGGene> findKEGGGeneEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KEGGGene.class));
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

    public KEGGGene findKEGGGene(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KEGGGene.class, id);
        } finally {
            em.close();
        }
    }

    public int getKEGGGeneCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KEGGGene> rt = cq.from(KEGGGene.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
