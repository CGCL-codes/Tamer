package edu.univalle.lingweb.persistence;

import java.util.List;

/**
 * Interface for CoMatrixExr2UserDAO.
 * 
 * @author LingWeb
 */
public interface ICoMatrixExr2UserDAO {

    /**
	 * Perform an initial save of a previously unsaved CoMatrixExr2User entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoMatrixExr2UserDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoMatrixExr2User entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(CoMatrixExr2User entity);

    /**
	 * Delete a persistent CoMatrixExr2User entity. This operation must be
	 * performed within the a database transaction context for the entity's data
	 * to be permanently deleted from the persistence store, i.e., database.
	 * This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ICoMatrixExr2UserDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            CoMatrixExr2User entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(CoMatrixExr2User entity);

    /**
	 * Persist a previously saved CoMatrixExr2User entity and return it or a
	 * copy of it to the sender. A copy of the CoMatrixExr2User entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity. This operation must be performed within the
	 * a database transaction context for the entity's data to be permanently
	 * saved to the persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = ICoMatrixExr2UserDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            CoMatrixExr2User entity to update
	 * @returns CoMatrixExr2User the persisted CoMatrixExr2User entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public CoMatrixExr2User update(CoMatrixExr2User entity);

    public CoMatrixExr2User findById(Long id);

    /**
	 * Find all CoMatrixExr2User entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CoMatrixExr2User property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoMatrixExr2User> found by query
	 */
    public List<CoMatrixExr2User> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByMatrixTitle(Object matrixTitle, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByMatrixXmlStudent(Object matrixXmlStudent, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByNumberRows(Object numberRows, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByNumberColumns(Object numberColumns, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByMatrixWidth(Object matrixWidth, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByMatrixHeight(Object matrixHeight, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByRowHeight(Object rowHeight, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByColumnWidht(Object columnWidht, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByVersion(Object version, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByScore(Object score, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByCommentAll(Object commentAll, int... rowStartIdxAndCount);

    public List<CoMatrixExr2User> findByCommentSection(Object commentSection, int... rowStartIdxAndCount);

    /**
	 * Find all CoMatrixExr2User entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CoMatrixExr2User> all CoMatrixExr2User entities
	 */
    public List<CoMatrixExr2User> findAll(int... rowStartIdxAndCount);
}
