package uk.ac.ebi.intact.update.persistence.dao;

import org.hibernate.Session;
import uk.ac.ebi.intact.annotation.Mockable;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * This interface contains basic methods for database management
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>19-May-2010</pre>
 */
@Mockable
public interface UpdateBaseDao<T> {

    /**
     *
     * @return the total number of entries in the database
     */
    public int countAll();

    /**
     *
     * @return all the entries in the database
     */
    public List<T> getAll();

    /**
     *
     * @return the entries in the database having this id
     */
    public T getById(long id);

    /**
     * Persist an object in the database
     * @param entity
     */
    public void persist(T entity);

    /**
     * Delete an object in the database
     * @param entity
     */
    public void delete(T entity);

    /**
     * Update an object in the database
     * @param entity
     */
    public void update(T entity);

    /**
     * Save or update an object in the database
     * @param entity
     */
    public void saveOrUpdate(T entity);

    /**
     * Flush
     */
    public void flush();

    /**
     *
     * @return the entity manager
     */
    public EntityManager getEntityManager();

    /**
     * The session
     * @return
     */
    public Session getSession();

    /**
     * Set the entity class
     * @param entityClass
     */
    public void setEntityClass(Class<T> entityClass);
}
