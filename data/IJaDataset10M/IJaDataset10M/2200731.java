package edu.univalle.lingweb.persistence;

import java.util.List;

/**
 * Interface for CoCourseUserDAO.
 * 
 * @author LingWeb
 */
public interface ICoCourseUserDAO {

    /**
	 * Perform an initial save of a previously unsaved CoCourseUser entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoCourseUserDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoCourseUser entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoCourseUser entity);

    /**
	 * Delete a persistent CoCourseUser entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoCourseUserDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoCourseUser entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoCourseUser entity);

    /**
	 * Persist a previously saved CoCourseUser entity and return it or a copy of
	 * it to the sender. A copy of the CoCourseUser entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoCourseUserDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoCourseUser entity to update
	 * @returns CoCourseUser the persisted CoCourseUser entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoCourseUser update(CoCourseUser entity);

    public CoCourseUser findById(CoCourseUserId id);

    /**
	 * Find all CoCourseUser entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoCourseUser property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoCourseUser> found by query
	 */
    public List<CoCourseUser> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoCourseUser> findByFlagDeleted(Object flagDeleted, int... rowStartIdxAndCount);

    /**
	 * Find all CoCourseUser entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoCourseUser> all CoCourseUser entities
	 */
    public List<CoCourseUser> findAll(int... rowStartIdxAndCount);
}
