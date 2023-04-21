package com.sltech.punchclock.dao;

import com.sltech.punchclock.dao.exceptions.NonexistentEntityException;
import com.sltech.punchclock.entities.ClkPunch;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.sltech.punchclock.entities.ClkPerson;
import com.sltech.punchclock.entities.ClkShift;
import java.util.Date;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

/**
 *
 * @author Juanjo
 */
public class ClkPunchJpaController {

    public ClkPunchJpaController() {
        emf = SLPersistence.getInstance().getEntityManagerFactory();
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClkPunch clkPunch) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClkPerson idPerson = clkPunch.getIdPerson();
            if (idPerson != null) {
                idPerson = em.getReference(idPerson.getClass(), idPerson.getIdPerson());
                clkPunch.setIdPerson(idPerson);
            }
            ClkShift idShift = clkPunch.getIdShift();
            if (idShift != null) {
                idShift = em.getReference(idShift.getClass(), idShift.getIdShift());
                clkPunch.setIdShift(idShift);
            }
            em.persist(clkPunch);
            if (idPerson != null) {
                idPerson.getClkPunchList().add(clkPunch);
                idPerson = em.merge(idPerson);
            }
            if (idShift != null) {
                idShift.getClkPunchList().add(clkPunch);
                idShift = em.merge(idShift);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClkPunch clkPunch) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClkPunch persistentClkPunch = em.find(ClkPunch.class, clkPunch.getIdPunch());
            ClkPerson idPersonOld = persistentClkPunch.getIdPerson();
            ClkPerson idPersonNew = clkPunch.getIdPerson();
            ClkShift idShiftOld = persistentClkPunch.getIdShift();
            ClkShift idShiftNew = clkPunch.getIdShift();
            if (idPersonNew != null) {
                idPersonNew = em.getReference(idPersonNew.getClass(), idPersonNew.getIdPerson());
                clkPunch.setIdPerson(idPersonNew);
            }
            if (idShiftNew != null) {
                idShiftNew = em.getReference(idShiftNew.getClass(), idShiftNew.getIdShift());
                clkPunch.setIdShift(idShiftNew);
            }
            clkPunch = em.merge(clkPunch);
            if (idPersonOld != null && !idPersonOld.equals(idPersonNew)) {
                idPersonOld.getClkPunchList().remove(clkPunch);
                idPersonOld = em.merge(idPersonOld);
            }
            if (idPersonNew != null && !idPersonNew.equals(idPersonOld)) {
                idPersonNew.getClkPunchList().add(clkPunch);
                idPersonNew = em.merge(idPersonNew);
            }
            if (idShiftOld != null && !idShiftOld.equals(idShiftNew)) {
                idShiftOld.getClkPunchList().remove(clkPunch);
                idShiftOld = em.merge(idShiftOld);
            }
            if (idShiftNew != null && !idShiftNew.equals(idShiftOld)) {
                idShiftNew.getClkPunchList().add(clkPunch);
                idShiftNew = em.merge(idShiftNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clkPunch.getIdPunch();
                if (findClkPunch(id) == null) {
                    throw new NonexistentEntityException("The clkPunch with id " + id + " no longer exists.");
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
            ClkPunch clkPunch;
            try {
                clkPunch = em.getReference(ClkPunch.class, id);
                clkPunch.getIdPunch();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clkPunch with id " + id + " no longer exists.", enfe);
            }
            ClkPerson idPerson = clkPunch.getIdPerson();
            if (idPerson != null) {
                idPerson.getClkPunchList().remove(clkPunch);
                idPerson = em.merge(idPerson);
            }
            ClkShift idShift = clkPunch.getIdShift();
            if (idShift != null) {
                idShift.getClkPunchList().remove(clkPunch);
                idShift = em.merge(idShift);
            }
            em.remove(clkPunch);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClkPunch> findClkPunchEntities() {
        return findClkPunchEntities(true, -1, -1);
    }

    public List<ClkPunch> findClkPunchEntities(int maxResults, int firstResult) {
        return findClkPunchEntities(false, maxResults, firstResult);
    }

    private List<ClkPunch> findClkPunchEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ClkPunch as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
   * 
   * @param initialDate
   * @param finalDate
   * @return
   */
    public List<ClkPunch> findClkPunchByPunchInDate(Date initialDate, Date finalDate) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ClkPunch as o  where o.punchIn between :initialDate and :finalDate");
            q.setParameter("initialDate", initialDate, TemporalType.TIMESTAMP);
            q.setParameter("finalDate", finalDate, TemporalType.TIMESTAMP);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
   *
   * @param initialDate
   * @param finalDate
   * @return
   */
    public List<ClkPunch> findClkPunchByNotOut(Date initialDate, Date finalDate) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ClkPunch as o  where o.punchIn between :initialDate and :finalDate " + "and o.punchOut is null order by o.idPerson, o.punchInActual");
            q.setParameter("initialDate", initialDate, TemporalType.TIMESTAMP);
            q.setParameter("finalDate", finalDate, TemporalType.TIMESTAMP);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
   *
   * @param initialID
   * @param finalID
   */
    public List<ClkPunch> findClkPunchByID(Integer initialID, Integer finalID) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ClkPunch as o  where o.idPunch between :initialID and :finalID");
            q.setParameter("initialID", initialID);
            q.setParameter("finalID", finalID);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
   * 
   * @param weekNumber
   * @return
   */
    public List<ClkPunch> findClkPunchEntitiesByWeek(Integer weekNumber) {
        EntityManager em = getEntityManager();
        String ql = "SELECT c " + "FROM   ClkPunch c " + "WHERE  c.weekNumber = :weekNumber";
        Query q = em.createQuery(ql);
        q.setParameter("weekNumber", weekNumber);
        return q.getResultList();
    }

    public ClkPunch findClkPunchInWithOutPunchOut(ClkPerson clkPerson) {
        EntityManager em = getEntityManager();
        ClkPunch clkPunch = null;
        try {
            String ql = "SELECT c FROM ClkPunch c " + "WHERE c.idPerson = :idPerson " + "and   c.punchOutActual is null";
            Query q = em.createQuery(ql);
            q.setParameter("idPerson", clkPerson);
            clkPunch = (ClkPunch) q.getSingleResult();
        } catch (NoResultException e) {
            clkPerson = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return clkPunch;
    }

    public ClkPunch findClkPunch(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClkPunch.class, id);
        } finally {
            em.close();
        }
    }

    public int getClkPunchCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from ClkPunch as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
