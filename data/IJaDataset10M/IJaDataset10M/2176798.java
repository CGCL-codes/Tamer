package com.validation.manager.core.db.controller;

import com.validation.manager.core.db.TestCaseExecution;
import com.validation.manager.core.db.controller.exceptions.NonexistentEntityException;
import com.validation.manager.core.db.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class TestCaseExecutionJpaController implements Serializable {

    public TestCaseExecutionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestCaseExecution testCaseExecution) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(testCaseExecution);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTestCaseExecution(testCaseExecution.getId()) != null) {
                throw new PreexistingEntityException("TestCaseExecution " + testCaseExecution + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestCaseExecution testCaseExecution) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            testCaseExecution = em.merge(testCaseExecution);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = testCaseExecution.getId();
                if (findTestCaseExecution(id) == null) {
                    throw new NonexistentEntityException("The testCaseExecution with id " + id + " no longer exists.");
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
            TestCaseExecution testCaseExecution;
            try {
                testCaseExecution = em.getReference(TestCaseExecution.class, id);
                testCaseExecution.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testCaseExecution with id " + id + " no longer exists.", enfe);
            }
            em.remove(testCaseExecution);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestCaseExecution> findTestCaseExecutionEntities() {
        return findTestCaseExecutionEntities(true, -1, -1);
    }

    public List<TestCaseExecution> findTestCaseExecutionEntities(int maxResults, int firstResult) {
        return findTestCaseExecutionEntities(false, maxResults, firstResult);
    }

    private List<TestCaseExecution> findTestCaseExecutionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestCaseExecution.class));
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

    public TestCaseExecution findTestCaseExecution(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestCaseExecution.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestCaseExecutionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestCaseExecution> rt = cq.from(TestCaseExecution.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
