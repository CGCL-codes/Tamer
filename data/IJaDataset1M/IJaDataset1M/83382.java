package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;

/**
 * Interface for CoItemsE1DAO.
 * 
 * @author LingWeb
 */
public interface ICoItemsE1DAO {

    /**
	 * Perform an initial save of a previously unsaved CoItemsE1 entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoItemsE1DAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoItemsE1 entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoItemsE1 entity);

    /**
	 * Delete a persistent CoItemsE1 entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoItemsE1DAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoItemsE1 entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoItemsE1 entity);

    /**
	 * Persist a previously saved CoItemsE1 entity and return it or a copy of it
	 * to the sender. A copy of the CoItemsE1 entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoItemsE1DAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoItemsE1 entity to update
	 * @returns CoItemsE1 the persisted CoItemsE1 entity instance, may not be
	 *          the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoItemsE1 update(CoItemsE1 entity);

    public CoItemsE1 findById(Long id);

    /**
	 * Find all CoItemsE1 entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoItemsE1 property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoItemsE1> found by query
	 */
    public List<CoItemsE1> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoItemsE1> findByTextQuestionItem(Object textQuestionItem, int... rowStartIdxAndCount);

    public List<CoItemsE1> findByTextAnswerItem(Object textAnswerItem, int... rowStartIdxAndCount);

    public List<CoItemsE1> findByImageQuestionItem(Object imageQuestionItem, int... rowStartIdxAndCount);

    public List<CoItemsE1> findByImageAnswerItem(Object imageAnswerItem, int... rowStartIdxAndCount);

    public List<CoItemsE1> findBySoundQuestionItem(Object soundQuestionItem, int... rowStartIdxAndCount);

    public List<CoItemsE1> findBySoundAnswerItem(Object soundAnswerItem, int... rowStartIdxAndCount);

    /**
	 * Find all CoItemsE1 entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoItemsE1> all CoItemsE1 entities
	 */
    public List<CoItemsE1> findAll(int... rowStartIdxAndCount);
}
