package org.sapiente.magnus.dao;

import java.util.Collection;
import org.asoft.sapiente.dao.Dao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Transactional;

/**
 * Jan 16, 2009
 * 
 * @author Alex
 */
@Transactional
public class HibernateDao<T> implements Dao<T> {

    private SessionFactory sessionFactory;

    public HibernateDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(T obj) {
        sessionFactory.getCurrentSession().save(obj);
    }

    public void update(T obj) {
        sessionFactory.getCurrentSession().saveOrUpdate(obj);
    }

    public void delete(T obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @SuppressWarnings("unchecked")
    public Collection<T> get(String query, Object... parms) {
        return createQuery(query, parms).list();
    }

    @SuppressWarnings("unchecked")
    public T getUnique(String query, Object... parms) {
        return (T) createQuery(query, parms).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public Collection<T> getByExample(T example) {
        return forExample(example).list();
    }

    private Query createQuery(String query, Object... parms) {
        Query q = sessionFactory.getCurrentSession().createQuery(query);
        for (int i = 0; i < parms.length; i++) q.setParameter(i, parms[i]);
        return q;
    }

    private Criteria forExample(T object) {
        DetachedCriteria dc = DetachedCriteria.forClass(object.getClass());
        Example ex = Example.create(object);
        dc.add(ex);
        return dc.getExecutableCriteria(sessionFactory.getCurrentSession());
    }

    @SuppressWarnings("unchecked")
    public T getUniqueByExample(T example) {
        return (T) forExample(example).uniqueResult();
    }
}
