package dsb.bar.flowmeter.server.persistence.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import dsb.bar.flowmeter.server.persistence.exceptions.MultipleObjectsException;

/**
 * Abstract DAO class for EJB persistence.
 * 
 * @param <T>
 *            The persistence bean type.
 * @param <ID>
 *            The identifier type of the bean.
 */
@Stateless
public abstract class AbstractEJBDAOBean<T, ID extends Serializable> implements AbstractEJBDAO<T, ID> {

    private static final Logger logger = Logger.getLogger(AbstractEJBDAOBean.class);

    /** Associated EntityManager instance. */
    @PersistenceContext(unitName = "flowmeter")
    protected EntityManager entityManager;

    /** Type of managed entity class. */
    @SuppressWarnings("unchecked")
    protected Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
	 * Create a new AbstractDAO without an associated EntityManager.
	 */
    public AbstractEJBDAOBean() {
        this(null);
    }

    /**
	 * Create a new AbstractDAO with associated EntityManager.
	 * 
	 * @param entityManager
	 *            The EntityManager.
	 */
    public AbstractEJBDAOBean(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        Validate.notNull(entityManager, "entityManager should not be null");
        this.entityManager = entityManager;
    }

    /**
	 * Set the entity class of this DAO.
	 * <p>
	 * <i>Warning: Only use this method if you know what you are doing!</i>
	 * 
	 * @param entityClass
	 *            The new entity class (should be non-null).
	 */
    protected void setEntityClass(Class<T> entityClass) {
        Validate.notNull(entityClass, "entityClass should not be null");
        this.entityClass = entityClass;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public String getEntityName() {
        return this.entityClass.getSimpleName();
    }

    @Override
    public List<T> getAll() {
        logger.trace("Querying for all " + this.getEntityName() + " objects.");
        final String ejbquery = "select o from " + this.getEntityName() + " o";
        return this.getByCheckedQuery(ejbquery);
    }

    @Override
    public T getById(ID id) {
        logger.trace("Querying for " + this.getEntityName() + " object with ID: " + id);
        Validate.notNull(id, "id should not be null");
        return this.entityManager.find(this.entityClass, id);
    }

    /**
	 * Get a list of entities of type T from an EJB-QL query.
	 * 
	 * @param ejbQuery
	 *            A legal EJB-QL query which will return a list of entities of
	 *            type T (should be non-null).
	 * 
	 * @return A List of objects of type T.
	 * 
	 * @see {@link #getByUncheckedQuery(String)}
	 */
    protected List<T> getByCheckedQuery(String ejbQuery) {
        logger.trace("Executing EJB-QL query: " + ejbQuery);
        Validate.notNull(ejbQuery, "ejbQuery should not be null");
        @SuppressWarnings("unchecked") List<T> result = this.getByUncheckedQuery(ejbQuery);
        return result;
    }

    /**
	 * Get a single entity of type T from an EJB-QL query.
	 * 
	 * @param ejbQuery
	 *            A legal EJB-QL query which will return a single entity of type
	 *            T (should be non-null).
	 * 
	 * @return A single entity of type T, or <code>null</code> if none could be
	 *         found.
	 * 
	 * @see {@link #getSingleByUncheckedQuery(String)}
	 */
    protected T getSingleByCheckedQuery(String ejbQuery) {
        logger.trace("Executing EJB-QL query: " + ejbQuery);
        Validate.notNull(ejbQuery, "ejbQuery should not be null");
        Object o = this.getSingleByUncheckedQuery(ejbQuery);
        if (o != null) {
            @SuppressWarnings("unchecked") T result = (T) o;
            return result;
        }
        return null;
    }

    /**
	 * Get an unchecked List from an EJB-QL query.
	 * 
	 * @param ejbQuery
	 *            A legal EJB-QL query which will return result set (should be
	 *            non-null).
	 * 
	 * @return An unchecked List.
	 */
    @SuppressWarnings("unchecked")
    protected List getByUncheckedQuery(String ejbQuery) {
        logger.trace("Executing EJB-QL query: " + ejbQuery);
        Validate.notNull(ejbQuery, "ejbQuery should not be null");
        Query query = this.entityManager.createQuery(ejbQuery);
        List resultList = query.getResultList();
        logger.trace("Got result list: " + resultList.size() + " entries.");
        return resultList;
    }

    /**
	 * Get a single entity of type T from an EJB-QL query.
	 * 
	 * @param ejbQuery
	 *            A legal EJB-QL query which will return a single result (should
	 *            be non-null).
	 * 
	 * @return A single entity, or <code>null</code> if none could be found.
	 */
    protected Object getSingleByUncheckedQuery(String ejbQuery) {
        Validate.notNull(ejbQuery, "ejbQuery should not be null");
        logger.trace("Executing unchecked EJB-QL query: " + ejbQuery);
        Query query = this.entityManager.createQuery(ejbQuery);
        Object result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } catch (NonUniqueResultException e) {
            throw new MultipleObjectsException(this.entityClass, "EJB-QL query", ejbQuery);
        }
        if (result == null) logger.trace("Got no result."); else logger.trace("Got result: " + result);
        return result;
    }

    /**
	 * Get a result set corresponding to a given native SQL query.
	 * 
	 * @param sql
	 *            The native SQL query (should be non-null).
	 * 
	 * @return A List of arrays of objects representing the result set.
	 */
    protected List<Object[]> getByNativeUncheckedQuery(String sql) {
        logger.trace("Executing native query: " + sql);
        Validate.notNull(sql, "sql should not be null");
        @SuppressWarnings("unchecked") List<Object[]> result = this.entityManager.createNativeQuery(sql).getResultList();
        logger.trace("Got unchecked result list: " + result.size() + " entries.");
        return result;
    }

    /**
	 * Get a list of objects of type T corresponding to the result set from the
	 * given native SQL query.
	 * 
	 * @param sql
	 *            The native SQL query (should be non-null).
	 * 
	 * @return A list of objects of type T corresponding to the result set of
	 *         the given SQL query.
	 */
    protected List<T> getByNativeCheckedQuery(String sql) {
        logger.trace("Executing native query: " + sql);
        Validate.notNull(sql, "sql should not be null");
        Query query = this.entityManager.createNativeQuery(sql, this.entityClass);
        @SuppressWarnings("unchecked") List<T> result = (List<T>) query.getResultList();
        return result;
    }

    /**
	 * Get a single entity of type T from a native SQL query.
	 * 
	 * @param sql
	 *            The native SQL query (should be non-null and legal SQL,
	 *            returning a single result).
	 * 
	 * @return The corresponding entity of type T, or <code>null</code> if none
	 *         could be found.
	 */
    public T getSingleByNativeCheckedQuery(String sql) {
        logger.trace("Executing native query: " + sql);
        Validate.notNull(sql, "sql should not be null");
        Query query = this.entityManager.createNativeQuery(sql, this.entityClass);
        T result = null;
        try {
            @SuppressWarnings("unchecked") T res = (T) query.getSingleResult();
            result = res;
        } catch (NoResultException e) {
            result = null;
        } catch (NonUniqueResultException e) {
            throw new MultipleObjectsException(this.entityClass, "Native SQL", sql);
        }
        logger.trace("Found result: " + result);
        return result;
    }

    /**
	 * Get a single unchecked result from a native query. Note that this is a
	 * plain Object, but it could be anything, depending on your query!
	 * 
	 * @param sql
	 *            The native SQL query (should be non-null and legal SQL,
	 *            returning a single result).
	 * 
	 * @return The object that was found, or <code>null</code> if none was
	 *         found.
	 */
    public Object getSingleByNativeUncheckedQuery(String sql) {
        logger.trace("Executing native query: " + sql);
        Validate.notNull(sql, "sql should not be null");
        Query query = this.entityManager.createNativeQuery(sql);
        Object result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } catch (NonUniqueResultException e) {
            throw new MultipleObjectsException(this.entityClass, "Native SQL", sql);
        }
        logger.trace("Got result: " + result);
        return result;
    }

    @Override
    public void persist(T entity) {
        logger.trace("Persisting entity: " + entity);
        Validate.notNull(entity, "entity should not be null");
        this.entityManager.persist(entity);
        logger.trace("Entity persisted: " + entity);
    }

    @Override
    public T merge(T entity) {
        logger.trace("Merging entity: " + entity);
        Validate.notNull(entity, "entity should not be null");
        entity = this.entityManager.merge(entity);
        logger.trace("Entity merged: " + entity);
        return entity;
    }

    @Override
    public void remove(T entity) {
        logger.trace("Removing entity: " + entity);
        Validate.notNull(entity, "entity should not be null");
        this.entityManager.remove(entity);
        logger.trace("Removed entity: " + entity);
    }
}
