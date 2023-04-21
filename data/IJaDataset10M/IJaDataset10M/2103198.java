package net.sf.openbugproject.controller;

import net.sf.openbugproject.interfaces.controller.IStateJpaController;
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
import net.sf.openbugproject.persistence.State;

/**
 *
 * @author slavo
 */
public class StateJpaController implements IStateJpaController {

    @Resource
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "OpenBugProject-ejbPU")
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(State state) throws RollbackFailureException, Exception {
        if (state.getBugCollection() == null) {
            state.setBugCollection(new ArrayList<Bug>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Bug> attachedBugCollection = new ArrayList<Bug>();
            for (Bug bugCollectionBugToAttach : state.getBugCollection()) {
                bugCollectionBugToAttach = em.getReference(bugCollectionBugToAttach.getClass(), bugCollectionBugToAttach.getIdBug());
                attachedBugCollection.add(bugCollectionBugToAttach);
            }
            state.setBugCollection(attachedBugCollection);
            em.persist(state);
            for (Bug bugCollectionBug : state.getBugCollection()) {
                State oldIdStateOfBugCollectionBug = bugCollectionBug.getIdState();
                bugCollectionBug.setIdState(state);
                bugCollectionBug = em.merge(bugCollectionBug);
                if (oldIdStateOfBugCollectionBug != null) {
                    oldIdStateOfBugCollectionBug.getBugCollection().remove(bugCollectionBug);
                    oldIdStateOfBugCollectionBug = em.merge(oldIdStateOfBugCollectionBug);
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

    public void edit(State state) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            State persistentState = em.find(State.class, state.getIdState());
            List<Bug> bugCollectionOld = persistentState.getBugCollection();
            List<Bug> bugCollectionNew = state.getBugCollection();
            List<String> illegalOrphanMessages = null;
            for (Bug bugCollectionOldBug : bugCollectionOld) {
                if (!bugCollectionNew.contains(bugCollectionOldBug)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Bug " + bugCollectionOldBug + " since its idState field is not nullable.");
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
            state.setBugCollection(bugCollectionNew);
            state = em.merge(state);
            for (Bug bugCollectionNewBug : bugCollectionNew) {
                if (!bugCollectionOld.contains(bugCollectionNewBug)) {
                    State oldIdStateOfBugCollectionNewBug = bugCollectionNewBug.getIdState();
                    bugCollectionNewBug.setIdState(state);
                    bugCollectionNewBug = em.merge(bugCollectionNewBug);
                    if (oldIdStateOfBugCollectionNewBug != null && !oldIdStateOfBugCollectionNewBug.equals(state)) {
                        oldIdStateOfBugCollectionNewBug.getBugCollection().remove(bugCollectionNewBug);
                        oldIdStateOfBugCollectionNewBug = em.merge(oldIdStateOfBugCollectionNewBug);
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
                Integer id = state.getIdState();
                if (findState(id) == null) {
                    throw new NonexistentEntityException("The state with id " + id + " no longer exists.");
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
            State state;
            try {
                state = em.getReference(State.class, id);
                state.getIdState();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The state with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Bug> bugCollectionOrphanCheck = state.getBugCollection();
            for (Bug bugCollectionOrphanCheckBug : bugCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This State (" + state + ") cannot be destroyed since the Bug " + bugCollectionOrphanCheckBug + " in its bugCollection field has a non-nullable idState field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(state);
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

    /**
     * ziskanie nasledujuceho mozneho stavu
     */
    public List<State> findStateEntitiesAvailable(int fromId) {
        EntityManager em = getEntityManager();
        try {
            Query qu = em.createNamedQuery("State.findByIdState").setParameter("idState", fromId);
            State fromState = (State) qu.getSingleResult();
            Query q = em.createNamedQuery("StateTransition.findByFrom").setParameter("fromStateTransition", fromState);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<State> findStateEntities() {
        return findStateEntities(true, -1, -1);
    }

    public List<State> findStateEntities(int maxResults, int firstResult) {
        return findStateEntities(false, maxResults, firstResult);
    }

    private List<State> findStateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from State as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public State findState(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(State.class, id);
        } finally {
            em.close();
        }
    }

    public int getStateCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from State as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
