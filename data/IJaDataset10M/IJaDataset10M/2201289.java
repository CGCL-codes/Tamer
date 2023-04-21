package com.validation.manager.core.db.controller;

import com.validation.manager.core.db.CorrectiveAction;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.validation.manager.core.db.VmException;
import java.util.ArrayList;
import java.util.List;
import com.validation.manager.core.db.VmUser;
import com.validation.manager.core.db.controller.exceptions.NonexistentEntityException;
import com.validation.manager.core.db.controller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class CorrectiveActionJpaController implements Serializable {

    public CorrectiveActionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CorrectiveAction correctiveAction) throws PreexistingEntityException, Exception {
        if (correctiveAction.getVmExceptionList() == null) {
            correctiveAction.setVmExceptionList(new ArrayList<VmException>());
        }
        if (correctiveAction.getVmUserList() == null) {
            correctiveAction.setVmUserList(new ArrayList<VmUser>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<VmException> attachedVmExceptionList = new ArrayList<VmException>();
            for (VmException vmExceptionListVmExceptionToAttach : correctiveAction.getVmExceptionList()) {
                vmExceptionListVmExceptionToAttach = em.getReference(vmExceptionListVmExceptionToAttach.getClass(), vmExceptionListVmExceptionToAttach.getVmExceptionPK());
                attachedVmExceptionList.add(vmExceptionListVmExceptionToAttach);
            }
            correctiveAction.setVmExceptionList(attachedVmExceptionList);
            List<VmUser> attachedVmUserList = new ArrayList<VmUser>();
            for (VmUser vmUserListVmUserToAttach : correctiveAction.getVmUserList()) {
                vmUserListVmUserToAttach = em.getReference(vmUserListVmUserToAttach.getClass(), vmUserListVmUserToAttach.getId());
                attachedVmUserList.add(vmUserListVmUserToAttach);
            }
            correctiveAction.setVmUserList(attachedVmUserList);
            em.persist(correctiveAction);
            for (VmException vmExceptionListVmException : correctiveAction.getVmExceptionList()) {
                vmExceptionListVmException.getCorrectiveActionList().add(correctiveAction);
                vmExceptionListVmException = em.merge(vmExceptionListVmException);
            }
            for (VmUser vmUserListVmUser : correctiveAction.getVmUserList()) {
                vmUserListVmUser.getCorrectiveActionList().add(correctiveAction);
                vmUserListVmUser = em.merge(vmUserListVmUser);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCorrectiveAction(correctiveAction.getId()) != null) {
                throw new PreexistingEntityException("CorrectiveAction " + correctiveAction + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CorrectiveAction correctiveAction) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CorrectiveAction persistentCorrectiveAction = em.find(CorrectiveAction.class, correctiveAction.getId());
            List<VmException> vmExceptionListOld = persistentCorrectiveAction.getVmExceptionList();
            List<VmException> vmExceptionListNew = correctiveAction.getVmExceptionList();
            List<VmUser> vmUserListOld = persistentCorrectiveAction.getVmUserList();
            List<VmUser> vmUserListNew = correctiveAction.getVmUserList();
            List<VmException> attachedVmExceptionListNew = new ArrayList<VmException>();
            for (VmException vmExceptionListNewVmExceptionToAttach : vmExceptionListNew) {
                vmExceptionListNewVmExceptionToAttach = em.getReference(vmExceptionListNewVmExceptionToAttach.getClass(), vmExceptionListNewVmExceptionToAttach.getVmExceptionPK());
                attachedVmExceptionListNew.add(vmExceptionListNewVmExceptionToAttach);
            }
            vmExceptionListNew = attachedVmExceptionListNew;
            correctiveAction.setVmExceptionList(vmExceptionListNew);
            List<VmUser> attachedVmUserListNew = new ArrayList<VmUser>();
            for (VmUser vmUserListNewVmUserToAttach : vmUserListNew) {
                vmUserListNewVmUserToAttach = em.getReference(vmUserListNewVmUserToAttach.getClass(), vmUserListNewVmUserToAttach.getId());
                attachedVmUserListNew.add(vmUserListNewVmUserToAttach);
            }
            vmUserListNew = attachedVmUserListNew;
            correctiveAction.setVmUserList(vmUserListNew);
            correctiveAction = em.merge(correctiveAction);
            for (VmException vmExceptionListOldVmException : vmExceptionListOld) {
                if (!vmExceptionListNew.contains(vmExceptionListOldVmException)) {
                    vmExceptionListOldVmException.getCorrectiveActionList().remove(correctiveAction);
                    vmExceptionListOldVmException = em.merge(vmExceptionListOldVmException);
                }
            }
            for (VmException vmExceptionListNewVmException : vmExceptionListNew) {
                if (!vmExceptionListOld.contains(vmExceptionListNewVmException)) {
                    vmExceptionListNewVmException.getCorrectiveActionList().add(correctiveAction);
                    vmExceptionListNewVmException = em.merge(vmExceptionListNewVmException);
                }
            }
            for (VmUser vmUserListOldVmUser : vmUserListOld) {
                if (!vmUserListNew.contains(vmUserListOldVmUser)) {
                    vmUserListOldVmUser.getCorrectiveActionList().remove(correctiveAction);
                    vmUserListOldVmUser = em.merge(vmUserListOldVmUser);
                }
            }
            for (VmUser vmUserListNewVmUser : vmUserListNew) {
                if (!vmUserListOld.contains(vmUserListNewVmUser)) {
                    vmUserListNewVmUser.getCorrectiveActionList().add(correctiveAction);
                    vmUserListNewVmUser = em.merge(vmUserListNewVmUser);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = correctiveAction.getId();
                if (findCorrectiveAction(id) == null) {
                    throw new NonexistentEntityException("The correctiveAction with id " + id + " no longer exists.");
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
            CorrectiveAction correctiveAction;
            try {
                correctiveAction = em.getReference(CorrectiveAction.class, id);
                correctiveAction.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The correctiveAction with id " + id + " no longer exists.", enfe);
            }
            List<VmException> vmExceptionList = correctiveAction.getVmExceptionList();
            for (VmException vmExceptionListVmException : vmExceptionList) {
                vmExceptionListVmException.getCorrectiveActionList().remove(correctiveAction);
                vmExceptionListVmException = em.merge(vmExceptionListVmException);
            }
            List<VmUser> vmUserList = correctiveAction.getVmUserList();
            for (VmUser vmUserListVmUser : vmUserList) {
                vmUserListVmUser.getCorrectiveActionList().remove(correctiveAction);
                vmUserListVmUser = em.merge(vmUserListVmUser);
            }
            em.remove(correctiveAction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CorrectiveAction> findCorrectiveActionEntities() {
        return findCorrectiveActionEntities(true, -1, -1);
    }

    public List<CorrectiveAction> findCorrectiveActionEntities(int maxResults, int firstResult) {
        return findCorrectiveActionEntities(false, maxResults, firstResult);
    }

    private List<CorrectiveAction> findCorrectiveActionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CorrectiveAction.class));
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

    public CorrectiveAction findCorrectiveAction(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CorrectiveAction.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorrectiveActionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CorrectiveAction> rt = cq.from(CorrectiveAction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
