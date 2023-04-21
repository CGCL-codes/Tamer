package net.sf.openbugproject.controller;

import net.sf.openbugproject.interfaces.controller.IBugSeverityJpaController;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.transaction.UserTransaction;
import net.sf.openbugproject.controller.exceptions.IllegalOrphanException;
import net.sf.openbugproject.controller.exceptions.NonexistentEntityException;
import net.sf.openbugproject.controller.exceptions.RollbackFailureException;
import net.sf.openbugproject.persistence.Bug;
import java.util.ArrayList;
import java.util.List;
import net.sf.openbugproject.persistence.BugSeverity;

/**
 *
 * @author slavo
 */
public class BugSeverityJpaController implements IBugSeverityJpaController {

    @Resource
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "OpenBugProject-ejbPU")
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BugSeverity bugSeverity) throws RollbackFailureException, Exception {
        if (bugSeverity.getBugCollection() == null) {
            bugSeverity.setBugCollection(new ArrayList<Bug>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Bug> attachedBugCollection = new ArrayList<Bug>();
            for (Bug bugCollectionBugToAttach : bugSeverity.getBugCollection()) {
                bugCollectionBugToAttach = em.getReference(bugCollectionBugToAttach.getClass(), bugCollectionBugToAttach.getIdBug());
                attachedBugCollection.add(bugCollectionBugToAttach);
            }
            bugSeverity.setBugCollection(attachedBugCollection);
            em.persist(bugSeverity);
            for (Bug bugCollectionBug : bugSeverity.getBugCollection()) {
                BugSeverity oldIdBugSeverityOfBugCollectionBug = bugCollectionBug.getIdBugSeverity();
                bugCollectionBug.setIdBugSeverity(bugSeverity);
                bugCollectionBug = em.merge(bugCollectionBug);
                if (oldIdBugSeverityOfBugCollectionBug != null) {
                    oldIdBugSeverityOfBugCollectionBug.getBugCollection().remove(bugCollectionBug);
                    oldIdBugSeverityOfBugCollectionBug = em.merge(oldIdBugSeverityOfBugCollectionBug);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BugSeverity bugSeverity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            BugSeverity persistentBugSeverity = em.find(BugSeverity.class, bugSeverity.getIdBugSeverity());
            List<Bug> bugCollectionOld = persistentBugSeverity.getBugCollection();
            List<Bug> bugCollectionNew = bugSeverity.getBugCollection();
            List<String> illegalOrphanMessages = null;
            for (Bug bugCollectionOldBug : bugCollectionOld) {
                if (!bugCollectionNew.contains(bugCollectionOldBug)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bug " + bugCollectionOldBug + " since its idBugSeverity field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Bug> attachedBugCollectionNew = new ArrayList<Bug>();
            for (Bug bugCollectionNewBugToAttach : bugCollectionNew) {
                bugCollectionNewBugToAttach = em.getReference(bugCollectionNewBugToAttach.getClass(), bugCollectionNewBugToAttach.getIdBug());
                attachedBugCollectionNew.add(bugCollectionNewBugToAttach);
            }
            bugCollectionNew = attachedBugCollectionNew;
            bugSeverity.setBugCollection(bugCollectionNew);
            bugSeverity = em.merge(bugSeverity);
            for (Bug bugCollectionNewBug : bugCollectionNew) {
                if (!bugCollectionOld.contains(bugCollectionNewBug)) {
                    BugSeverity oldIdBugSeverityOfBugCollectionNewBug = bugCollectionNewBug.getIdBugSeverity();
                    bugCollectionNewBug.setIdBugSeverity(bugSeverity);
                    bugCollectionNewBug = em.merge(bugCollectionNewBug);
                    if (oldIdBugSeverityOfBugCollectionNewBug != null && !oldIdBugSeverityOfBugCollectionNewBug.equals(bugSeverity)) {
                        oldIdBugSeverityOfBugCollectionNewBug.getBugCollection().remove(bugCollectionNewBug);
                        oldIdBugSeverityOfBugCollectionNewBug = em.merge(oldIdBugSeverityOfBugCollectionNewBug);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bugSeverity.getIdBugSeverity();
                if (findBugSeverity(id) == null) {
                    throw new NonexistentEntityException("The bugSeverity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            BugSeverity bugSeverity;
            try {
                bugSeverity = em.getReference(BugSeverity.class, id);
                bugSeverity.getIdBugSeverity();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bugSeverity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Bug> bugCollectionOrphanCheck = bugSeverity.getBugCollection();
            for (Bug bugCollectionOrphanCheckBug : bugCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BugSeverity (" + bugSeverity + ") cannot be destroyed since the Bug " + bugCollectionOrphanCheckBug + " in its bugCollection field has a non-nullable idBugSeverity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(bugSeverity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BugSeverity> findBugSeverityEntities() {
        return findBugSeverityEntities(true, -1, -1);
    }

    public List<BugSeverity> findBugSeverityEntities(int maxResults, int firstResult) {
        return findBugSeverityEntities(false, maxResults, firstResult);
    }

    private List<BugSeverity> findBugSeverityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from BugSeverity as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public BugSeverity findBugSeverity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BugSeverity.class, id);
        } finally {
            em.close();
        }
    }

    public int getBugSeverityCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from BugSeverity as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
