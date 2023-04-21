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
public class BudgetJpaController {

    public BudgetJpaController() {
        emf = Persistence.createEntityManagerFactory("CorpodatRecordsBackendPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Budget budget) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(budget);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Budget budget) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            budget = em.merge(budget);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = budget.getId();
                if (findBudget(id) == null) {
                    throw new NonexistentEntityException("The budget with id " + id + " no longer exists.");
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
            Budget budget;
            try {
                budget = em.getReference(Budget.class, id);
                budget.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The budget with id " + id + " no longer exists.", enfe);
            }
            em.remove(budget);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Budget> findBudgetEntities() {
        return findBudgetEntities(true, -1, -1);
    }

    public List<Budget> findBudgetEntities(int maxResults, int firstResult) {
        return findBudgetEntities(false, maxResults, firstResult);
    }

    private List<Budget> findBudgetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Budget as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Budget> findBudgetEntities(Integer clientId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select b from Budget b where b.clientId = :client").setParameter("client", clientId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Budget> findAcceptedBudgetEntities(Integer clientId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select b from Budget b where b.clientId = :client and b.accepted = true").setParameter("client", clientId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Budget findBudget(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Budget.class, id);
        } finally {
            em.close();
        }
    }

    public Budget findBudgetByNumber(Integer n) {
        EntityManager em = getEntityManager();
        try {
            return (Budget) em.createQuery("select b from Budget b where b.number = :n").setParameter("n", n).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return new Budget();
        } catch (javax.persistence.NonUniqueResultException e) {
            return (Budget) em.createQuery("select b from Budget b where b.number = :n").setParameter("n", n).getResultList().get(0);
        } finally {
            em.close();
        }
    }

    public boolean existsBudgetByNumber(Integer number) {
        EntityManager em = getEntityManager();
        try {
            try {
                Budget aux = (Budget) em.createQuery("select b from Budget b where b.number = :number").setParameter("number", number).getSingleResult();
                return true;
            } catch (javax.persistence.NonUniqueResultException e) {
                return true;
            } catch (javax.persistence.NoResultException e) {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public int getBudgetCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Budget as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
