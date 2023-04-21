package org.clin4j.framework.domain.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;

public abstract class GenericDao<T, ID extends Serializable> implements IGenericDao<T, ID> {

    private Class<T> persistentClass;

    protected EntityManager manager;

    protected GenericDao() {
        Type t = getClass().getGenericSuperclass();
        if (t instanceof Class) {
            throw new DaoException("Class is not Parameterized Type e.g. subclass of GenericDao<String>! The class is '" + t + "'.");
        } else if (t instanceof ParameterizedType) {
            this.persistentClass = (Class<T>) ((ParameterizedType) t).getActualTypeArguments()[0];
        } else {
            throw new DaoException("Can not handle type: '" + t + "'!");
        }
    }

    public void setEntityManager(EntityManager manager) {
        this.manager = manager;
    }

    protected EntityManager getEntityManager() {
        if (null == this.manager) throw new DaoException("No EntityManager found!");
        return this.manager;
    }

    /**
	 * This method will return parameterized type e.g., Integer when a subclass 
	 * e.g. Child<Integer, String> instantiates with the parameterized type 
	 * Integer.
	 */
    @SuppressWarnings("unchecked")
    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T find(ID key, boolean lock) {
        T entity;
        if (lock) {
            throw new DaoException("Not yet implemented!");
        } else {
            entity = find(key);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public T find(ID key) {
        return this.manager.find(getPersistentClass(), key);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ");
        sb.append(getPersistentClass().getName() + " c ");
        return findByQuery(sb.toString());
    }

    public void flush() {
        this.manager.flush();
    }

    public void clear() {
        this.manager.clear();
    }

    @SuppressWarnings("unchecked")
    public T persist(T entity) {
        this.manager.persist(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        this.manager.merge(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public void remove(T entity) {
        this.manager.remove(entity);
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByQuery(String ql) {
        return this.manager.createQuery(ql).getResultList();
    }
}
