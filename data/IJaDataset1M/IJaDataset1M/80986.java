package dsb.bar.tks.server.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import dsb.bar.tks.server.exceptions.dao.MultipleObjectsException;
import dsb.bar.tks.support.assertions.Assertion;

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
    @PersistenceContext(unitName = "TKS")
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
        Assertion.notNull(entityManager, "entityManager");
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
        Assertion.notNull(entityClass, "entityClass");
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
        final String ejbquery = "select o from " + this.getEntityName() + " o";
        return this.getByQuery(ejbquery);
    }

    @Override
    public T getById(ID id) {
        Assertion.notNull(id, "id");
        logger.debug("Querying for object with ID: " + id);
        final String query = "select obj from " + this.getEntityName() + " obj where obj.id = " + id;
        List<T> result = this.getByQuery(query);
        if (result == null || result.isEmpty()) {
            logger.debug("Found no result.");
            return null;
        }
        if (result.size() == 1) {
            logger.debug("Got result: " + result);
            return result.get(0);
        }
        logger.error("Found multiple objects.");
        throw new MultipleObjectsException(this.entityClass, "id", id.toString());
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
    protected List<T> getByQuery(String ejbQuery) {
        Assertion.notNull(ejbQuery, "ejbQuery");
        logger.debug("Executing EJB-QL query: " + ejbQuery);
        @SuppressWarnings("unchecked") List<T> result = this.getByUncheckedQuery(ejbQuery);
        logger.debug("Got result list: " + result.size() + " entries.");
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
	 * @see {@link #getByQuery(String)}
	 * @see {@link #getByUncheckedQuery(String)}
	 */
    protected T getSingleByQuery(String ejbQuery) {
        Assertion.notNull(ejbQuery, "ejbQuery");
        logger.debug("Executing EJB-QL query: " + ejbQuery);
        Query query = this.entityManager.createQuery(ejbQuery);
        @SuppressWarnings("unchecked") T result = (T) query.getSingleResult();
        logger.debug("Got result: " + result);
        return result;
    }

    /**
	 * Get an unchecked List from an EJB-QL query.
	 * <p>
	 * TODO Check whether a List of Objects or Object[]s returned.
	 * 
	 * @param ejbQuery
	 *            A legal EJB-QL query which will return a list of Object[]s(?)
	 *            (should be non-null).
	 * 
	 * @return A List of Object[]s.
	 * 
	 * @see {@link #getByQuery(String)}
	 * @see {@link #getSingleByUncheckedQuery(String)}
	 */
    @SuppressWarnings("unchecked")
    protected List getByUncheckedQuery(String ejbQuery) {
        Assertion.notNull(ejbQuery, "ejbQuery");
        logger.debug("Executing EJB-QL query: " + ejbQuery);
        Query query = this.entityManager.createQuery(ejbQuery);
        List resultList = query.getResultList();
        logger.debug("Got result list: " + resultList.size() + " entries.");
        return resultList;
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
	 * @see {@link #getByQuery(String)}
	 * @see {@link #getByUncheckedQuery(String)}
	 */
    protected Object getSingleByUncheckedQuery(String ejbQuery) {
        Assertion.notNull(ejbQuery, "ejbQuery");
        logger.debug("Executing unchecked EJB-QL query: " + ejbQuery);
        Query query = this.entityManager.createQuery(ejbQuery);
        Object result = query.getSingleResult();
        logger.debug("Got result: " + result);
        return result;
    }

    /**
	 * TODO Write comments.
	 * 
	 * @param sql
	 * 
	 * @return
	 */
    protected List<Object[]> getUncheckedByNativeQuery(String sql) {
        Assertion.notNull(sql, "sql");
        logger.debug("Executing native query: " + sql);
        @SuppressWarnings("unchecked") List<Object[]> result = this.entityManager.createNativeQuery(sql).getResultList();
        logger.debug("Got unchecked result list: " + result.size() + " entries.");
        return result;
    }

    /**
	 * TODO Write comments.
	 * 
	 * @param sql
	 * 
	 * @return
	 */
    protected List<T> getByNativeQuery(String sql) {
        Assertion.notNull(sql, "sql");
        logger.debug("Executing native query: " + sql);
        Query query = this.entityManager.createNativeQuery(sql, this.entityClass);
        @SuppressWarnings("unchecked") List<T> result = query.getResultList();
        logger.debug("Got result list: " + result.size() + " entities.");
        return result;
    }

    /**
	 * Get a single entity of type T from a native SQL query.
	 * 
	 * @param sql
	 *            A legal SQL query (should be non-null).
	 * 
	 * @return The corresponding entity of type T, or <code>null</code> if none
	 *         could be found.
	 */
    public T getSingleByNativeQuery(String sql) {
        Assertion.notNull(sql, "sql");
        List<T> result = this.getByNativeQuery(sql);
        if (result == null || result.size() == 0) return null;
        if (result.size() == 1) return result.get(0);
        throw new MultipleObjectsException(this.entityClass);
    }

    @Override
    public void persist(T entity) {
        Assertion.notNull(entity, "entity");
        this.entityManager.persist(entity);
        logger.debug("Entity persisted: " + entity);
    }

    @Override
    public T merge(T entity) {
        Assertion.notNull(entity, "entity");
        entity = this.entityManager.merge(entity);
        logger.debug("Entity merged: " + entity);
        return entity;
    }

    @Override
    public void remove(T entity) {
        Assertion.notNull(entity, "entity");
        this.entityManager.remove(entity);
        logger.debug("Removed entity: " + entity);
    }
}
