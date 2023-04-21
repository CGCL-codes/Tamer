package edu.univalle.lingweb.persistence;

import java.util.List;
import java.util.Set;

/**
 * Interface for CoTechnicalDAO.
 * 
 * @author LingWeb
 */
public interface ICoTechnicalDAO {

    /**
	 * Perform an initial save of a previously unsaved CoTechnical entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoTechnicalDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoTechnical entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoTechnical entity);

    /**
	 * Delete a persistent CoTechnical entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoTechnicalDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoTechnical entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoTechnical entity);

    /**
	 * Persist a previously saved CoTechnical entity and return it or a copy of
	 * it to the sender. A copy of the CoTechnical entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoTechnicalDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoTechnical entity to update
	 * @returns CoTechnical the persisted CoTechnical entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoTechnical update(CoTechnical entity);

    public CoTechnical findById(Long id);

    /**
	 * Find all CoTechnical entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoTechnical property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoTechnical> found by query
	 */
    public List<CoTechnical> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoTechnical> findByTechnicalName(Object technicalName, int... rowStartIdxAndCount);

    public List<CoTechnical> findByTechnicalNameEn(Object technicalNameEn, int... rowStartIdxAndCount);

    public List<CoTechnical> findByTechnicalNameFr(Object technicalNameFr, int... rowStartIdxAndCount);

    /**
	 * Find all CoTechnical entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoTechnical> all CoTechnical entities
	 */
    public List<CoTechnical> findAll(int... rowStartIdxAndCount);
}
