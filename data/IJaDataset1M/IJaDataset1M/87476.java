package menfis.model.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import menfis.model.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 *
 * @param T
 * @param PK
 * @author lpelegrini
 */
public class GenericHibernateDAO<T, PK extends Serializable> implements GenericDAO<T, PK> {

    private Class<T> persistentClass;

    private Session session;

    public GenericHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public void setSession(Session s) {
        this.session = s;
    }

    protected Session getSession() {
        if (session == null) session = HibernateUtil.currentSession();
        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public T findById(PK id, boolean lock) {
        T entity;
        if (lock) entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE); else entity = (T) getSession().load(getPersistentClass(), id);
        return entity;
    }

    public List<T> getAll() {
        return findByCriteria();
    }

    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * @param criterion 
     * @return 
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    public Class<T> getObjectClass() {
        return persistentClass;
    }
}
