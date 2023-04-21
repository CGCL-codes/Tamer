package DAOs;

import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import DAOs.exceptions.RollbackFailureException;
import beans.EmpComment;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import beans.Employee;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ioana C
 */
public class EmpCommentJpaController {

    @Resource
    private UserTransaction utx = null;

    @PersistenceUnit(unitName = "Licenta_PU")
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmpComment empComment) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employee personID = empComment.getPersonID();
            if (personID != null) {
                personID = em.getReference(personID.getClass(), personID.getPersonID());
                empComment.setPersonID(personID);
            }
            em.persist(empComment);
            if (personID != null) {
                personID.getEmpCommentCollection().add(empComment);
                personID = em.merge(personID);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEmpComment(empComment.getEmpCommentID()) != null) {
                throw new PreexistingEntityException("EmpComment " + empComment + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmpComment empComment) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EmpComment persistentEmpComment = em.find(EmpComment.class, empComment.getEmpCommentID());
            Employee personIDOld = persistentEmpComment.getPersonID();
            Employee personIDNew = empComment.getPersonID();
            if (personIDNew != null) {
                personIDNew = em.getReference(personIDNew.getClass(), personIDNew.getPersonID());
                empComment.setPersonID(personIDNew);
            }
            empComment = em.merge(empComment);
            if (personIDOld != null && !personIDOld.equals(personIDNew)) {
                personIDOld.getEmpCommentCollection().remove(empComment);
                personIDOld = em.merge(personIDOld);
            }
            if (personIDNew != null && !personIDNew.equals(personIDOld)) {
                personIDNew.getEmpCommentCollection().add(empComment);
                personIDNew = em.merge(personIDNew);
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
                Integer id = empComment.getEmpCommentID();
                if (findEmpComment(id) == null) {
                    throw new NonexistentEntityException("The empComment with id " + id + " no longer exists.");
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
            EmpComment empComment;
            try {
                empComment = em.getReference(EmpComment.class, id);
                empComment.getEmpCommentID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empComment with id " + id + " no longer exists.", enfe);
            }
            Employee personID = empComment.getPersonID();
            if (personID != null) {
                personID.getEmpCommentCollection().remove(empComment);
                personID = em.merge(personID);
            }
            em.remove(empComment);
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

    public List<EmpComment> findEmpCommentEntities() {
        return findEmpCommentEntities(true, -1, -1);
    }

    public List<EmpComment> findEmpCommentEntities(int maxResults, int firstResult) {
        return findEmpCommentEntities(false, maxResults, firstResult);
    }

    private List<EmpComment> findEmpCommentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from EmpComment as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EmpComment findEmpComment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmpComment.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpCommentCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from EmpComment as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
