package hu.ihash.database.dao;

import hu.ihash.database.entities.Image;
import java.util.Collection;
import java.util.List;

/**
 * An interface for basic DAO functionality.
 *
 * @author Gergely Kiss
 *
 * @param <T>
 */
public interface BaseDao<T> {

    /**
     * Persists the given entity.
     *
     * @param entity
     */
    public T persist(T entity);

    /**
     * Saves or updates the given entity.
     *
     * @param entity
     */
    public T saveOrUpdate(T entity);

    /**
     * Deletes the given entity.
     *
     * @param entity
     */
    public void delete(T entity);

    /**
     * Counts the number of entities in the database.
     *
     * @return
     */
    public int count();

    /**
     * Returns <code>results</code> entities starting from <code>from</code>.
     *
     * @param from
     * @param results
     * @return
     */
    public Collection<Image> page(int from, int results);

    /**
	 * Returns the result of the given generic query.
	 * 
	 * @param q
	 * @return
	 */
    public List<T> genericQuery(GenericQuery q);
}
