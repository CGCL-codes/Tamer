package com.esl.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import org.hibernate.*;
import org.springframework.transaction.annotation.Transactional;
import com.esl.entity.IAuditable;

@Transactional
public abstract class ESLDao<T> implements IESLDao<T> {

    protected Class<?> entityClass;

    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;

    protected EntityManager em;

    public ESLDao() {
        Class clazz = getClass();
        while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) clazz = (Class) getClass().getGenericSuperclass();
        this.entityClass = (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
	 * Hibernate reattach object to session
	 * 
	 * @param practiceResult
	 */
    public Object attachSession(Object o) {
        if (o == null) return null;
        Session session = sessionFactory.getCurrentSession();
        if (!session.contains(o)) {
            session.lock(o, LockMode.NONE);
        }
        return o;
    }

    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    public void persist(T entity) {
        if (entity instanceof IAuditable) {
            ((IAuditable) entity).setLastUpdatedDate(new Date());
        }
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public void persistAll(Collection<T> entities) {
        for (T entity : entities) {
            ((IAuditable) entity).setLastUpdatedDate(new Date());
            sessionFactory.getCurrentSession().saveOrUpdate(entity);
        }
    }

    public void refresh(T entity) {
        sessionFactory.getCurrentSession().refresh(entity);
    }

    public T merge(T entity) {
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteAll(Collection<T> entities) {
        for (T entity : entities) {
            sessionFactory.getCurrentSession().delete(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public T load(Serializable id) {
        return (T) sessionFactory.getCurrentSession().load(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> loadAll() {
        return sessionFactory.getCurrentSession().createQuery("From " + entityClass.getName()).list();
    }
}
