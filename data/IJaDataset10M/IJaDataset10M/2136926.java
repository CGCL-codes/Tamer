package sbc.ejb3.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;

public abstract class AbstractGenericDAO<T, ID extends Serializable> implements IGenericDAO<T, ID> {

    protected final Class<T> typeClass;

    @PersistenceContext(unitName = "sbc")
    protected EntityManager entityManager;

    /** Creates a new instance of AbstractGenericDAO */
    public AbstractGenericDAO() {
        this.typeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T getById(ID id) {
        return this.entityManager.find(this.typeClass, id);
    }

    /**
     * Uses the hibernate session API.
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll(Integer max, Integer first) {
        return getAllCriteria(max, first).list();
    }

    public List<T> getAll() {
        return getAll(null, null);
    }

    protected Criteria getAllCriteria(Integer max, Integer first) {
        Criteria crit = getSession().createCriteria(this.typeClass);
        if (max != null) {
            crit.setMaxResults(max);
        }
        if (first != null) {
            crit.setFirstResult(first);
        }
        return crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }

    public void persist(T entity) {
        this.entityManager.persist(entity);
    }

    public void persistAll(Collection<T> entities) {
        for (T entity : entities) {
            this.entityManager.persist(entity);
        }
    }

    public void persistAll(T... entities) {
        for (T entity : entities) {
            this.entityManager.persist(entity);
        }
    }

    public void remove(T entity) {
        this.entityManager.remove(entity);
    }

    public void removeAll(Collection<T> entities) {
        for (T entity : entities) {
            this.entityManager.remove(entity);
        }
    }

    public void removeAll(T... entities) {
        for (T entity : entities) {
            this.entityManager.remove(entity);
        }
    }

    public T merge(T entity) {
        try {
            return this.entityManager.merge(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return entity;
        }
    }

    protected Session getSession() {
        return ((HibernateEntityManager) this.entityManager.getDelegate()).getSession();
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(this.typeClass);
    }
}
