package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * CoCompleteE1 entities. Transaction control of the save(), update() and
 * delete() operations must be handled externally by senders of these methods or
 * must be manually added to each of these methods for data to be persisted to
 * the JPA datastore.
 * 
 * @see edu.univalle.lingweb.persistence.CoCompleteE1
 * @author LingWeb
 */
public class CoCompleteE1DAO implements ICoCompleteE1DAO {

    public static final String TEXT = "text";

    public static final String TYPE_OPTION = "typeOption";

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
	 * Perform an initial save of a previously unsaved CoCompleteE1 entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoCompleteE1DAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoCompleteE1 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoCompleteE1 entity) {
        EntityManagerHelper.log("saving CoCompleteE1 instance", Level.INFO, null);
        try {
            getEntityManager().persist(entity);
            EntityManagerHelper.log("save successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("save failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Delete a persistent CoCompleteE1 entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CoCompleteE1DAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoCompleteE1 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoCompleteE1 entity) {
        EntityManagerHelper.log("deleting CoCompleteE1 instance", Level.INFO, null);
        try {
            entity = getEntityManager().getReference(CoCompleteE1.class, entity.getCompleteE1Id());
            getEntityManager().remove(entity);
            EntityManagerHelper.log("delete successful", Level.INFO, null);
        } catch (RuntimeException re) {
            EntityManagerHelper.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Persist a previously saved CoCompleteE1 entity and return it or a copy of
	 * it to the sender. A copy of the CoCompleteE1 entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = CoCompleteE1DAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoCompleteE1 entity to update
	 * @returns CoCompleteE1 the persisted CoCompleteE1 entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoCompleteE1 update(CoCompleteE1 entity) {
        EntityManagerHelper.log("updating CoCompleteE1 instance", Level.INFO, null);
        try {
            CoCompleteE1 result = getEntityManager().merge(entity);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

    public CoCompleteE1 findById(Long id) {
        EntityManagerHelper.log("finding CoCompleteE1 instance with id: " + id, Level.INFO, null);
        try {
            CoCompleteE1 instance = getEntityManager().find(CoCompleteE1.class, id);
            return instance;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }

    /**
	 * Find all CoCompleteE1 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoCompleteE1 property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<CoCompleteE1> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<CoCompleteE1> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding CoCompleteE1 instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
        try {
            final String queryString = "select model from CoCompleteE1 model where model." + propertyName + "= :propertyValue";
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

    public List<CoCompleteE1> findByText(Object text, int... rowStartIdxAndCount) {
        return findByProperty(TEXT, text, rowStartIdxAndCount);
    }

    public List<CoCompleteE1> findByTypeOption(Object typeOption, int... rowStartIdxAndCount) {
        return findByProperty(TYPE_OPTION, typeOption, rowStartIdxAndCount);
    }

    /**
	 * Find all CoCompleteE1 entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoCompleteE1> all CoCompleteE1 entities
	 */
    @SuppressWarnings("unchecked")
    public List<CoCompleteE1> findAll(final int... rowStartIdxAndCount) {
        EntityManagerHelper.log("finding all CoCompleteE1 instances", Level.INFO, null);
        try {
            final String queryString = "select model from CoCompleteE1 model";
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
