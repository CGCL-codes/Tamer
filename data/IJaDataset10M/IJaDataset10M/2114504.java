package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * CoResponseMchoiceE3 entities. Transaction control of the save(), update() and
 * delete() operations must be handled externally by senders of these methods or
 * must be manually added to each of these methods for data to be persisted to
 * the JPA datastore.
 * 
 * @see edu.univalle.lingweb.persistence.CoResponseMchoiceE3
 * @author LingWeb
 */
public class CoResponseMchoiceE3DAO implements ICoResponseMchoiceE3DAO {

    public static final String FLAG_SCORE = "flagScore";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved CoResponseMchoiceE3
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoResponseMchoiceE3DAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoResponseMchoiceE3 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoResponseMchoiceE3 entity) {
        EntityManagerHelper.log("saving CoResponseMchoiceE3 instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent CoResponseMchoiceE3 entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently deleted from the persistence store, i.e., database.
	 * This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoResponseMchoiceE3DAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoResponseMchoiceE3 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoResponseMchoiceE3 entity) {
        EntityManagerHelper.log("deleting CoResponseMchoiceE3 instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(CoResponseMchoiceE3.class, entity.getId());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved CoResponseMchoiceE3 entity and return it or a
	 * copy of it to the sender. A copy of the CoResponseMchoiceE3 entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently saved to the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = CoResponseMchoiceE3DAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoResponseMchoiceE3 entity to update
	 * @returns CoResponseMchoiceE3 the persisted CoResponseMchoiceE3 entity
	 *          instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoResponseMchoiceE3 update(CoResponseMchoiceE3 entity) {
        EntityManagerHelper.log("updating CoResponseMchoiceE3 instance", Level.INFO, null);
        try {
            CoResponseMchoiceE3 result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public CoResponseMchoiceE3 findById(CoResponseMchoiceE3Id id) {
        EntityManagerHelper.log("finding CoResponseMchoiceE3 instance with id: " + id, Level.INFO, null);
        try {
            CoResponseMchoiceE3 instance = getEntityManager().find(CoResponseMchoiceE3.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all CoResponseMchoiceE3 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoResponseMchoiceE3 property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<CoResponseMchoiceE3> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<CoResponseMchoiceE3> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding CoResponseMchoiceE3 instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from CoResponseMchoiceE3 model where model." + propertyName + "= :propertyValue";
            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("propertyValue", value);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }
                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find by property name failed", Level.SEVERE, re);
            throw re;
        }
    }

    public List<CoResponseMchoiceE3> findByFlagScore(Object flagScore, int... rowStartIdxAndCount) {
        return findByProperty(FLAG_SCORE, flagScore, rowStartIdxAndCount);
    }

    /**
	 * Find all CoResponseMchoiceE3 entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoResponseMchoiceE3> all CoResponseMchoiceE3 entities
	 */
    @SuppressWarnings("unchecked")
    public List<CoResponseMchoiceE3> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all CoResponseMchoiceE3 instances", Level.INFO, null);
        try {
            final String queryString = "select model from CoResponseMchoiceE3 model";
            Query query = getEntityManager().createQuery(queryString);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }
                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }
}
