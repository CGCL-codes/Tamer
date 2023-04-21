package net.sf.esims.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;

public interface EsimsBaseDataAccessObject<T, ID> {

    /**
	 * Save an object.
	 * 
	 * @param obj
	 *            the object to save
	 */
    public void save(T obj) throws DataAccessException;

    /**
	 * Delete an object.
	 * 
	 * @param obj
	 *            the object to delete
	 */
    public void delete(T obj) throws DataAccessException;

    /**
	 * Retrieve an object.
	 * 
	 * @param subjectId
	 *            the subjectId
	 * @return the object or null if not found
	 */
    public T get(ID id) throws DataAccessException;

    /**
	 * Retrieve all objects.
	 * 
	 * @return a list of objects
	 */
    public List<T> getAll() throws DataAccessException;

    /**
	 * Retrieve all objects.
	 * 
	 * @param orderBy
	 *            the field name to order results by
	 * @return a list of objects
	 */
    public List<T> getAll(String orderBy) throws DataAccessException;

    /**
	 * Retrieves object based on  a HQL query
	 */
    public List<T> getByQuery(String query) throws DataAccessException;

    public void saveAll(List<T> objects);
}
