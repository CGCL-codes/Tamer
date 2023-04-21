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
public class InvoiceJpaController {

    public InvoiceJpaController() {
        emf = Persistence.createEntityManagerFactory("CorpodatRecordsBackendPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Invoice invoice) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(invoice);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Invoice invoice) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            invoice = em.merge(invoice);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = invoice.getId();
                if (findInvoice(id) == null) {
                    throw new NonexistentEntityException("The invoice with id " + id + " no longer exists.");
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
            Invoice invoice;
            try {
                invoice = em.getReference(Invoice.class, id);
                invoice.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The invoice with id " + id + " no longer exists.", enfe);
            }
            em.remove(invoice);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Invoice> findInvoiceEntities() {
        return findInvoiceEntities(true, -1, -1);
    }

    public List<Invoice> findInvoiceEntities(int maxResults, int firstResult) {
        return findInvoiceEntities(false, maxResults, firstResult);
    }

    private List<Invoice> findInvoiceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Invoice as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Invoice findInvoice(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Invoice.class, id);
        } finally {
            em.close();
        }
    }

    public Invoice findInvoiceByNumber(Integer number) {
        EntityManager em = getEntityManager();
        try {
            return (Invoice) em.createQuery("select i from Invoice i where i.number = :number").setParameter("number", number).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return new Invoice();
        } finally {
            em.close();
        }
    }

    public Invoice findInvoiceByClient(Integer clientId) {
        EntityManager em = getEntityManager();
        try {
            return (Invoice) em.createQuery("select i from Invoice i where i.clientId = :clientId").setParameter("clientId", clientId).getSingleResult();
        } finally {
            em.close();
        }
    }

    public Invoice findInvoiceByBudget(Integer budgetId) {
        EntityManager em = getEntityManager();
        try {
            return (Invoice) em.createQuery("select i from Invoice i where i.budgetId = :budgetId").setParameter("budgetId", budgetId).getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getInvoiceCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Invoice as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
