package com.orms.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.orms.model.HibernateUtil;

public abstract class BaseDao {

    private EntityManager entityManager = null;

    protected void create(Object obj) throws Exception {
        try {
            entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(obj);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            handleException(ex);
        } finally {
            entityManager.close();
        }
    }

    protected void delete(Object obj) throws Exception {
        try {
            entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(obj);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            handleException(ex);
        } finally {
            entityManager.close();
        }
    }

    protected void update() throws Exception {
        try {
            entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            handleException(ex);
        } finally {
            entityManager.close();
        }
    }

    protected <T> T find(Class<T> aClass, int anId) throws Exception {
        T result = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            result = entityManager.find(aClass, anId);
        } catch (Exception ex) {
            handleException(ex);
        } finally {
            entityManager.close();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected List findByQuery(String aQuery, Map<String, Object> parameters) throws Exception {
        List result = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            Query query = entityManager.createQuery(aQuery);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            result = query.getResultList();
        } catch (Exception ex) {
            handleException(ex);
        } finally {
            entityManager.close();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> T findSingleResultByQuery(@SuppressWarnings("unused") Class<T> aClass, String aQuery, Map<String, Object> parameters) throws Exception {
        T result = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            Query query = entityManager.createQuery(aQuery);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            result = (T) query.setMaxResults(1).getSingleResult();
        } catch (Exception ex) {
            handleException(ex);
        } finally {
            entityManager.close();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> findAll(Class<T> aClass) throws Exception {
        List<T> result = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            Query query = entityManager.createQuery("from " + aClass.getName());
            result = query.getResultList();
        } catch (Exception ex) {
            handleException(ex);
        } finally {
            entityManager.close();
        }
        return result;
    }

    private void handleException(Exception ex) throws Exception {
        throw ex;
    }
}
