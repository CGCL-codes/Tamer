package info.gdeDengi.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
  * Common persistence methods
  * @author icoloma
  */
@Repository
public class BasicDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
 	 * Retrieves a non-paged query. Use with care, this method could potentially
 	 * instantiate large amounts of data.
 	 */
    @SuppressWarnings("unchecked")
    public <T> List<T> find(String queryString, Object... params) {
        Query query = entityManager.createQuery(queryString);
        setParameters(query, params);
        return query.getResultList();
    }

    /**
 	 * @return a single object that satisfies the query.
 	 * @throws NoResultException
 	 *             if there is no result
 	 * @throws NonUniqueResultException
 	 *             if more than one result
 	 * @throws IllegalStateException
 	 *             if called for a Java Persistence query language UPDATE or
 	 *             DELETE statement
 	 */
    public Object findSingle(String queryString, Object... params) {
        Query query = entityManager.createQuery(queryString);
        setParameters(query, params);
        return query.getSingleResult();
    }

    /**
 	 * Sets all the parameters of a query
 	 */
    private void setParameters(Query query, Object... params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) query.setParameter(i + 1, params[i]);
        }
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    /**
 	 * Removes a persistent instance
 	 *
 	 * @param <T>
 	 *            The persistent class
 	 * @param clazz
 	 *            The persistent class
 	 * @param id
 	 *            the primary key to remove
 	 * @return the removed instance
 	 */
    public <T> T remove(Class<T> clazz, Serializable id) throws EntityNotFoundException {
        T instance = find(clazz, id);
        entityManager.remove(instance);
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> clazz) {
        return (List<T>) find("from " + clazz.getName());
    }

    public <T> T find(Class<T> clazz, Serializable id) throws EntityNotFoundException {
        T result = entityManager.find(clazz, id);
        if (result == null) {
            throw new EntityNotFoundException();
        }
        return result;
    }

    public void flush() {
        entityManager.flush();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void refresh(Object entity) {
        entityManager.refresh(entity);
    }

    /**
 	 * Retrieves a non-paged query
 	 */
    @SuppressWarnings("unchecked")
    public <T> List<T> findNamedQuery(final String namedQuery, Object... params) {
        Query query = entityManager.createNamedQuery(namedQuery);
        setParameters(query, params);
        return query.getResultList();
    }

    /**
 	 * @return a single object that satisfies the named query.
 	 * @throws NoResultException
 	 *             if there is no result
 	 * @throws NonUniqueResultException
 	 *             if more than one result
 	 * @throws IllegalStateException
 	 *             if called for a Java Persistence query language UPDATE or
 	 *             DELETE statement
 	 */
    public Object findNamedQuerySingle(String namedQuery, Object... params) {
        Query query = entityManager.createNamedQuery(namedQuery);
        setParameters(query, params);
        return query.getSingleResult();
    }

    public int bulkUpdate(String queryString, Object... params) {
        Query query = entityManager.createQuery(queryString);
        setParameters(query, params);
        return query.executeUpdate();
    }

    public int nextNum(String sequence_name) {
        String q = "select nextval ('" + sequence_name + "')";
        BigInteger ret;
        Query query = this.entityManager.createNativeQuery(q);
        ret = (BigInteger) query.getSingleResult();
        return ret.intValue();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findTop(String sql, int top, Object... params) {
        Query query = entityManager.createQuery(sql);
        query.setFirstResult(0);
        query.setMaxResults(top);
        setParameters(query, params);
        return query.getResultList();
    }
}
