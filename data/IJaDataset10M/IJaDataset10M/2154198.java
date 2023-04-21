package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;

/**
 * Interface for CoCompleteE3DAO.
 * 
 * @author LingWeb
 */
public interface ICoCompleteE3DAO {

    /**
	 * Perform an initial save of a previously unsaved CoCompleteE3 entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoCompleteE3DAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoCompleteE3 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoCompleteE3 entity);

    /**
	 * Delete a persistent CoCompleteE3 entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoCompleteE3DAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoCompleteE3 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoCompleteE3 entity);

    /**
	 * Persist a previously saved CoCompleteE3 entity and return it or a copy of
	 * it to the sender. A copy of the CoCompleteE3 entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoCompleteE3DAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoCompleteE3 entity to update
	 * @returns CoCompleteE3 the persisted CoCompleteE3 entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoCompleteE3 update(CoCompleteE3 entity);

    public CoCompleteE3 findById(Long id);

    /**
	 * Find all CoCompleteE3 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoCompleteE3 property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoCompleteE3> found by query
	 */
    public List<CoCompleteE3> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoCompleteE3> findByText(Object text, int... rowStartIdxAndCount);

    public List<CoCompleteE3> findByTypeOption(Object typeOption, int... rowStartIdxAndCount);

    /**
	 * Find all CoCompleteE3 entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoCompleteE3> all CoCompleteE3 entities
	 */
    public List<CoCompleteE3> findAll(int... rowStartIdxAndCount);
}
