package edu.univalle.lingweb.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Interface for ToCommentDAO.
 * 
 * @author LingWeb
 */
public interface IToCommentDAO {

    /**
	 * Perform an initial save of a previously unsaved ToComment entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IToCommentDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            ToComment entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void save(ToComment entity);

    /**
	 * Delete a persistent ToComment entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IToCommentDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            ToComment entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
    public void delete(ToComment entity);

    /**
	 * Persist a previously saved ToComment entity and return it or a copy of it
	 * to the sender. A copy of the ToComment entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = IToCommentDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            ToComment entity to update
	 * @returns ToComment the persisted ToComment entity instance, may not be
	 *          the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
    public ToComment update(ToComment entity);

    public ToComment findById(Long id);

    /**
	 * Find all ToComment entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ToComment property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ToComment> found by query
	 */
    public List<ToComment> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

    public List<ToComment> findByComment(Object comment, int... rowStartIdxAndCount);

    /**
	 * Find all ToComment entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ToComment> all ToComment entities
	 */
    public List<ToComment> findAll(int... rowStartIdxAndCount);
}
