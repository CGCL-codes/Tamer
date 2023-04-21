package DAOs;

import DAOs.exceptions.IllegalOrphanException;
import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import DAOs.exceptions.RollbackFailureException;
import beans.StatusOfApp;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import beans.JobApp;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ioana C
 */
public class StatusOfAppJpaController {

    @Resource
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "Licenta_PU")
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StatusOfApp statusOfApp) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (statusOfApp.getJobAppCollection() == null) {
            statusOfApp.setJobAppCollection(new ArrayList<JobApp>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<JobApp> attachedJobAppCollection = new ArrayList<JobApp>();
            for (JobApp jobAppCollectionJobAppToAttach : statusOfApp.getJobAppCollection()) {
                jobAppCollectionJobAppToAttach = em.getReference(jobAppCollectionJobAppToAttach.getClass(), jobAppCollectionJobAppToAttach.getJobAppID());
                attachedJobAppCollection.add(jobAppCollectionJobAppToAttach);
            }
            statusOfApp.setJobAppCollection(attachedJobAppCollection);
            em.persist(statusOfApp);
            for (JobApp jobAppCollectionJobApp : statusOfApp.getJobAppCollection()) {
                StatusOfApp oldStatusIDOfJobAppCollectionJobApp = jobAppCollectionJobApp.getStatusID();
                jobAppCollectionJobApp.setStatusID(statusOfApp);
                jobAppCollectionJobApp = em.merge(jobAppCollectionJobApp);
                if (oldStatusIDOfJobAppCollectionJobApp != null) {
                    oldStatusIDOfJobAppCollectionJobApp.getJobAppCollection().remove(jobAppCollectionJobApp);
                    oldStatusIDOfJobAppCollectionJobApp = em.merge(oldStatusIDOfJobAppCollectionJobApp);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findStatusOfApp(statusOfApp.getStatusID()) != null) {
                throw new PreexistingEntityException("StatusOfApp " + statusOfApp + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StatusOfApp statusOfApp) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            StatusOfApp persistentStatusOfApp = em.find(StatusOfApp.class, statusOfApp.getStatusID());
            List<JobApp> jobAppCollectionOld = persistentStatusOfApp.getJobAppCollection();
            List<JobApp> jobAppCollectionNew = statusOfApp.getJobAppCollection();
            List<String> illegalOrphanMessages = null;
            for (JobApp jobAppCollectionOldJobApp : jobAppCollectionOld) {
                if (!jobAppCollectionNew.contains(jobAppCollectionOldJobApp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain JobApp " + jobAppCollectionOldJobApp + " since its statusID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<JobApp> attachedJobAppCollectionNew = new ArrayList<JobApp>();
            for (JobApp jobAppCollectionNewJobAppToAttach : jobAppCollectionNew) {
                jobAppCollectionNewJobAppToAttach = em.getReference(jobAppCollectionNewJobAppToAttach.getClass(), jobAppCollectionNewJobAppToAttach.getJobAppID());
                attachedJobAppCollectionNew.add(jobAppCollectionNewJobAppToAttach);
            }
            jobAppCollectionNew = attachedJobAppCollectionNew;
            statusOfApp.setJobAppCollection(jobAppCollectionNew);
            statusOfApp = em.merge(statusOfApp);
            for (JobApp jobAppCollectionNewJobApp : jobAppCollectionNew) {
                if (!jobAppCollectionOld.contains(jobAppCollectionNewJobApp)) {
                    StatusOfApp oldStatusIDOfJobAppCollectionNewJobApp = jobAppCollectionNewJobApp.getStatusID();
                    jobAppCollectionNewJobApp.setStatusID(statusOfApp);
                    jobAppCollectionNewJobApp = em.merge(jobAppCollectionNewJobApp);
                    if (oldStatusIDOfJobAppCollectionNewJobApp != null && !oldStatusIDOfJobAppCollectionNewJobApp.equals(statusOfApp)) {
                        oldStatusIDOfJobAppCollectionNewJobApp.getJobAppCollection().remove(jobAppCollectionNewJobApp);
                        oldStatusIDOfJobAppCollectionNewJobApp = em.merge(oldStatusIDOfJobAppCollectionNewJobApp);
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
                Short id = statusOfApp.getStatusID();
                if (findStatusOfApp(id) == null) {
                    throw new NonexistentEntityException("The statusOfApp with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            StatusOfApp statusOfApp;
            try {
                statusOfApp = em.getReference(StatusOfApp.class, id);
                statusOfApp.getStatusID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statusOfApp with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<JobApp> jobAppCollectionOrphanCheck = statusOfApp.getJobAppCollection();
            for (JobApp jobAppCollectionOrphanCheckJobApp : jobAppCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StatusOfApp (" + statusOfApp + ") cannot be destroyed since the JobApp " + jobAppCollectionOrphanCheckJobApp + " in its jobAppCollection field has a non-nullable statusID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(statusOfApp);
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

    public List<StatusOfApp> findStatusOfAppEntities() {
        return findStatusOfAppEntities(true, -1, -1);
    }

    public List<StatusOfApp> findStatusOfAppEntities(int maxResults, int firstResult) {
        return findStatusOfAppEntities(false, maxResults, firstResult);
    }

    private List<StatusOfApp> findStatusOfAppEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from StatusOfApp as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public StatusOfApp findStatusOfApp(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatusOfApp.class, id);
        } finally {
            em.close();
        }
    }

    public int getStatusOfAppCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from StatusOfApp as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
