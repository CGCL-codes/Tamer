package quickfinance.jpa.controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import quickfinance.jpa.controllers.exceptions.IllegalOrphanException;
import quickfinance.jpa.controllers.exceptions.NonexistentEntityException;
import quickfinance.jpa.controllers.exceptions.PreexistingEntityException;
import quickfinance.entities.Budgets;
import quickfinance.entities.User;
import quickfinance.entities.Budgetcategories;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author epal
 */
public class BudgetsJpaController {

    public BudgetsJpaController() {
        emf = Persistence.createEntityManagerFactory("QuickFinancePU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Budgets budgets) throws PreexistingEntityException, Exception {
        if (budgets.getBudgetcategoriesCollection() == null) {
            budgets.setBudgetcategoriesCollection(new ArrayList<Budgetcategories>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userId = budgets.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                budgets.setUserId(userId);
            }
            List<Budgetcategories> attachedBudgetcategoriesCollection = new ArrayList<Budgetcategories>();
            for (Budgetcategories budgetcategoriesCollectionBudgetcategoriesToAttach : budgets.getBudgetcategoriesCollection()) {
                budgetcategoriesCollectionBudgetcategoriesToAttach = em.getReference(budgetcategoriesCollectionBudgetcategoriesToAttach.getClass(), budgetcategoriesCollectionBudgetcategoriesToAttach.getId());
                attachedBudgetcategoriesCollection.add(budgetcategoriesCollectionBudgetcategoriesToAttach);
            }
            budgets.setBudgetcategoriesCollection(attachedBudgetcategoriesCollection);
            em.persist(budgets);
            if (userId != null) {
                userId.getBudgetsCollection().add(budgets);
                userId = em.merge(userId);
            }
            for (Budgetcategories budgetcategoriesCollectionBudgetcategories : budgets.getBudgetcategoriesCollection()) {
                Budgets oldBudgetIdOfBudgetcategoriesCollectionBudgetcategories = budgetcategoriesCollectionBudgetcategories.getBudgetId();
                budgetcategoriesCollectionBudgetcategories.setBudgetId(budgets);
                budgetcategoriesCollectionBudgetcategories = em.merge(budgetcategoriesCollectionBudgetcategories);
                if (oldBudgetIdOfBudgetcategoriesCollectionBudgetcategories != null) {
                    oldBudgetIdOfBudgetcategoriesCollectionBudgetcategories.getBudgetcategoriesCollection().remove(budgetcategoriesCollectionBudgetcategories);
                    oldBudgetIdOfBudgetcategoriesCollectionBudgetcategories = em.merge(oldBudgetIdOfBudgetcategoriesCollectionBudgetcategories);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBudgets(budgets.getId()) != null) {
                throw new PreexistingEntityException("Budgets " + budgets + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Budgets budgets) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Budgets persistentBudgets = em.find(Budgets.class, budgets.getId());
            User userIdOld = persistentBudgets.getUserId();
            User userIdNew = budgets.getUserId();
            List<Budgetcategories> budgetcategoriesCollectionOld = persistentBudgets.getBudgetcategoriesCollection();
            List<Budgetcategories> budgetcategoriesCollectionNew = budgets.getBudgetcategoriesCollection();
            List<String> illegalOrphanMessages = null;
            for (Budgetcategories budgetcategoriesCollectionOldBudgetcategories : budgetcategoriesCollectionOld) {
                if (!budgetcategoriesCollectionNew.contains(budgetcategoriesCollectionOldBudgetcategories)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Budgetcategories " + budgetcategoriesCollectionOldBudgetcategories + " since its budgetId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                budgets.setUserId(userIdNew);
            }
            List<Budgetcategories> attachedBudgetcategoriesCollectionNew = new ArrayList<Budgetcategories>();
            for (Budgetcategories budgetcategoriesCollectionNewBudgetcategoriesToAttach : budgetcategoriesCollectionNew) {
                budgetcategoriesCollectionNewBudgetcategoriesToAttach = em.getReference(budgetcategoriesCollectionNewBudgetcategoriesToAttach.getClass(), budgetcategoriesCollectionNewBudgetcategoriesToAttach.getId());
                attachedBudgetcategoriesCollectionNew.add(budgetcategoriesCollectionNewBudgetcategoriesToAttach);
            }
            budgetcategoriesCollectionNew = attachedBudgetcategoriesCollectionNew;
            budgets.setBudgetcategoriesCollection(budgetcategoriesCollectionNew);
            budgets = em.merge(budgets);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getBudgetsCollection().remove(budgets);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getBudgetsCollection().add(budgets);
                userIdNew = em.merge(userIdNew);
            }
            for (Budgetcategories budgetcategoriesCollectionNewBudgetcategories : budgetcategoriesCollectionNew) {
                if (!budgetcategoriesCollectionOld.contains(budgetcategoriesCollectionNewBudgetcategories)) {
                    Budgets oldBudgetIdOfBudgetcategoriesCollectionNewBudgetcategories = budgetcategoriesCollectionNewBudgetcategories.getBudgetId();
                    budgetcategoriesCollectionNewBudgetcategories.setBudgetId(budgets);
                    budgetcategoriesCollectionNewBudgetcategories = em.merge(budgetcategoriesCollectionNewBudgetcategories);
                    if (oldBudgetIdOfBudgetcategoriesCollectionNewBudgetcategories != null && !oldBudgetIdOfBudgetcategoriesCollectionNewBudgetcategories.equals(budgets)) {
                        oldBudgetIdOfBudgetcategoriesCollectionNewBudgetcategories.getBudgetcategoriesCollection().remove(budgetcategoriesCollectionNewBudgetcategories);
                        oldBudgetIdOfBudgetcategoriesCollectionNewBudgetcategories = em.merge(oldBudgetIdOfBudgetcategoriesCollectionNewBudgetcategories);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = budgets.getId();
                if (findBudgets(id) == null) {
                    throw new NonexistentEntityException("The budgets with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Budgets budgets;
            try {
                budgets = em.getReference(Budgets.class, id);
                budgets.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The budgets with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Budgetcategories> budgetcategoriesCollectionOrphanCheck = budgets.getBudgetcategoriesCollection();
            for (Budgetcategories budgetcategoriesCollectionOrphanCheckBudgetcategories : budgetcategoriesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Budgets (" + budgets + ") cannot be destroyed since the Budgetcategories " + budgetcategoriesCollectionOrphanCheckBudgetcategories + " in its budgetcategoriesCollection field has a non-nullable budgetId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userId = budgets.getUserId();
            if (userId != null) {
                userId.getBudgetsCollection().remove(budgets);
                userId = em.merge(userId);
            }
            em.remove(budgets);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Budgets> findBudgetsEntities() {
        return findBudgetsEntities(true, -1, -1);
    }

    public List<Budgets> findBudgetsEntities(int maxResults, int firstResult) {
        return findBudgetsEntities(false, maxResults, firstResult);
    }

    private List<Budgets> findBudgetsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Budgets as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Budgets findBudgets(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Budgets.class, id);
        } finally {
            em.close();
        }
    }

    public int getBudgetsCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Budgets as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
