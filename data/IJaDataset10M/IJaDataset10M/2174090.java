package org.gruposp2p.aularest.model.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import org.gruposp2p.aularest.model.Absence;
import org.gruposp2p.aularest.model.Student;
import org.gruposp2p.aularest.model.Subject;
import org.gruposp2p.aularest.model.controller.exceptions.NonexistentEntityException;
import org.gruposp2p.aularest.model.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author jj
 */
public class AbsenceJpaController {

    public AbsenceJpaController() {
        emf = Persistence.createEntityManagerFactory("AulaRest_PU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Absence absence) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Student studentId = absence.getStudentId();
            if (studentId != null) {
                studentId = em.getReference(studentId.getClass(), studentId.getId());
                absence.setStudentId(studentId);
            }
            Subject subjectId = absence.getSubjectId();
            if (subjectId != null) {
                subjectId = em.getReference(subjectId.getClass(), subjectId.getId());
                absence.setSubjectId(subjectId);
            }
            em.persist(absence);
            if (studentId != null) {
                studentId.getAbsenceCollection().add(absence);
                studentId = em.merge(studentId);
            }
            if (subjectId != null) {
                subjectId.getAbsenceCollection().add(absence);
                subjectId = em.merge(subjectId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAbsence(absence.getId()) != null) {
                throw new PreexistingEntityException("Absence " + absence + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Absence absence) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Absence persistentAbsence = em.find(Absence.class, absence.getId());
            Student studentIdOld = persistentAbsence.getStudentId();
            Student studentIdNew = absence.getStudentId();
            Subject subjectIdOld = persistentAbsence.getSubjectId();
            Subject subjectIdNew = absence.getSubjectId();
            if (studentIdNew != null) {
                studentIdNew = em.getReference(studentIdNew.getClass(), studentIdNew.getId());
                absence.setStudentId(studentIdNew);
            }
            if (subjectIdNew != null) {
                subjectIdNew = em.getReference(subjectIdNew.getClass(), subjectIdNew.getId());
                absence.setSubjectId(subjectIdNew);
            }
            absence = em.merge(absence);
            if (studentIdOld != null && !studentIdOld.equals(studentIdNew)) {
                studentIdOld.getAbsenceCollection().remove(absence);
                studentIdOld = em.merge(studentIdOld);
            }
            if (studentIdNew != null && !studentIdNew.equals(studentIdOld)) {
                studentIdNew.getAbsenceCollection().add(absence);
                studentIdNew = em.merge(studentIdNew);
            }
            if (subjectIdOld != null && !subjectIdOld.equals(subjectIdNew)) {
                subjectIdOld.getAbsenceCollection().remove(absence);
                subjectIdOld = em.merge(subjectIdOld);
            }
            if (subjectIdNew != null && !subjectIdNew.equals(subjectIdOld)) {
                subjectIdNew.getAbsenceCollection().add(absence);
                subjectIdNew = em.merge(subjectIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = absence.getId();
                if (findAbsence(id) == null) {
                    throw new NonexistentEntityException("The absence with id " + id + " no longer exists.");
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
            Absence absence;
            try {
                absence = em.getReference(Absence.class, id);
                absence.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The absence with id " + id + " no longer exists.", enfe);
            }
            Student studentId = absence.getStudentId();
            if (studentId != null) {
                studentId.getAbsenceCollection().remove(absence);
                studentId = em.merge(studentId);
            }
            Subject subjectId = absence.getSubjectId();
            if (subjectId != null) {
                subjectId.getAbsenceCollection().remove(absence);
                subjectId = em.merge(subjectId);
            }
            em.remove(absence);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Absence> findAbsenceEntities() {
        return findAbsenceEntities(true, -1, -1);
    }

    public List<Absence> findAbsenceEntities(int maxResults, int firstResult) {
        return findAbsenceEntities(false, maxResults, firstResult);
    }

    private List<Absence> findAbsenceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Absence as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Absence findAbsence(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Absence.class, id);
        } finally {
            em.close();
        }
    }

    public int getAbsenceCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Absence as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
