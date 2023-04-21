package net.sf.openbugproject.controller;

import net.sf.openbugproject.interfaces.controller.IStateTransitionJpaController;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.transaction.UserTransaction;
import net.sf.openbugproject.controller.exceptions.NonexistentEntityException;
import net.sf.openbugproject.controller.exceptions.RollbackFailureException;
import net.sf.openbugproject.persistence.StateTransition;

/**
 *
 * @author slavo
 */
public class StateTransitionJpaController implements IStateTransitionJpaController {

    @Resource
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "OpenBugProject-ejbPU")
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StateTransition stateTransition) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(stateTransition);
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

    public void edit(StateTransition stateTransition) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            stateTransition = em.merge(stateTransition);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stateTransition.getIdStateTransition();
                if (findStateTransition(id) == null) {
                    throw new NonexistentEntityException("The stateTransition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            StateTransition stateTransition;
            try {
                stateTransition = em.getReference(StateTransition.class, id);
                stateTransition.getIdStateTransition();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stateTransition with id " + id + " no longer exists.", enfe);
            }
            em.remove(stateTransition);
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

    public List<StateTransition> findStateTransitionEntities() {
        return findStateTransitionEntities(true, -1, -1);
    }

    public List<StateTransition> findStateTransitionEntities(int maxResults, int firstResult) {
        return findStateTransitionEntities(false, maxResults, firstResult);
    }

    private List<StateTransition> findStateTransitionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from StateTransition as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public StateTransition findStateTransition(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StateTransition.class, id);
        } finally {
            em.close();
        }
    }

    public int getStateTransitionCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from StateTransition as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
