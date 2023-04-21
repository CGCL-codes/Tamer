package com.west.testEngine.rcp.daoImpl;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.west.testEngine.rcp.dao.GenericDAO;

public abstract class GenericDaoHibernateImpl<T, ID extends Serializable> extends HibernateDaoSupport implements GenericDAO<T, ID> {

    private final Logger log = Logger.getLogger(this.getClass());

    protected final Class<T> persistentClass;

    public GenericDaoHibernateImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public void save(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id) {
        return (T) getHibernateTemplate().get(persistentClass, id);
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id, Class<? extends T> clazz) {
        if (log.isDebugEnabled()) {
            log.debug("entity class = " + clazz);
        }
        return (T) getHibernateTemplate().load(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public T findByIdReturnNull(ID id) {
        return (T) getHibernateTemplate().get(persistentClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getHibernateTemplate().loadAll(persistentClass);
    }

    public List<T> findByExample(T exampleInstance) {
        return findByCriteria(Example.create(exampleInstance).ignoreCase().enableLike(MatchMode.START));
    }

    public T findUniqueByExample(T exampleInstance) {
        Example ex = Example.create(exampleInstance);
        T res = findUniqueByCriteria(ex);
        return res;
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistentClass);
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    @SuppressWarnings("unchecked")
    protected T findUniqueByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistentClass);
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return (T) crit.uniqueResult();
    }

    public void update(T entity) {
        getHibernateTemplate().update(entity);
        if (log.isDebugEnabled()) {
            log.debug("updated : " + entity);
        }
    }
}
