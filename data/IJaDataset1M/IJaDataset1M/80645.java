package com.miladinovicmarko.boxingworld.core.persistence.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import com.miladinovicmarko.boxingworld.core.persistence.PersistenceFacade;

public class PersistenceFacadeHibernate extends HibernateDaoSupport implements PersistenceFacade {

    @Transactional
    @SuppressWarnings("unchecked")
    public <T> List<T> read(Class<T> clazz) {
        Criteria criteria = getManager().createCriteria(clazz);
        Set<T> set = new HashSet<T>();
        set.addAll(criteria.list());
        List<T> list = new ArrayList<T>();
        list.addAll(set);
        return list;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public <T> T readUnique(Class<T> clazz, Long id) {
        Criteria criteria = getManager().createCriteria(clazz).add(Restrictions.idEq(id));
        return (T) criteria.uniqueResult();
    }

    @Transactional
    public <T> T write(T input) {
        getManager().saveOrUpdate(input);
        return input;
    }

    @Transactional
    public <T> T writeUsingMerge(T input) {
        getManager().merge(input);
        return input;
    }

    @Transactional
    public <T> List<T> write(List<T> inputs) {
        for (Object o : inputs) {
            write(o);
        }
        return inputs;
    }

    @Transactional
    public <T> T delete(T forgotten) {
        getManager().delete(forgotten);
        getManager().flush();
        return forgotten;
    }

    @Transactional
    public <T> List<T> delete(List<T> forgottenOnes) {
        for (Object o : forgottenOnes) {
            delete(o);
        }
        return forgottenOnes;
    }

    @Transactional
    public List<?> attachCriteria(DetachedCriteria detached) {
        Criteria criteria = detached.getExecutableCriteria(getManager());
        return criteria.list();
    }

    @Transactional
    public Object attachCriteriaReturnUniqueResult(DetachedCriteria detached) {
        Criteria criteria = detached.getExecutableCriteria(getManager());
        return criteria.uniqueResult();
    }

    protected Session getManager() {
        return getSessionFactory().getCurrentSession();
    }
}
