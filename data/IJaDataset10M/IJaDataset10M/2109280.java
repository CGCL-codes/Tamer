package anyware.common.dao;

import java.util.Collection;

/**
 * A Generic DAO interface
 *
 * @author keke<keke@codehaus.org>
 * @revision $Revision: 107 $
 * @version 0.0.1
 * @param <E>
 *            type of Entity
 * @param <ID>
 *            type of Entity ID
 * @param
 *            <Q> type of query criteria.
 * @see http://www.hibernate.org/328.html
 * @see http://www.ibm.com/developerworks/java/library/j-genericdao.html
 */
public interface GenericDao<E, ID, Q> {

    /**
     * Delete an entity
     *
     * @param entity
     * @return this instance.
     * @exception IllegalArgumentException
     *                if <code>entity</code> is <code>null</code>.
     */
    GenericDao<E, ID, Q> delete(E entity);

    Collection<E> find(Q queryCriteria);

    /**
     * Find a collection of entities by specified <code>example</code>.
     *
     * @param example
     *            the example
     * @return collection of entities
     * @exception IllegalArgumentException
     *                if <code>example</code> is <code>null</code>.
     */
    Collection<E> findByExample(E example);

    /**
     * Find one entity by specified <code>example</code>. If more than one
     * entities were found, only the first one will be returned.
     *
     * @param example
     *            the example
     * @return the found entity, or {@code null} if no entity was found.
     * @exception IllegalArgumentException
     *                if <code>example</code> is <code>null</code>.
     */
    E findOneByExample(E example);

    E findOnly(Q queryCriteria);

    /**
     * Get an instance of Entity by its {@code id}
     *
     * @param id
     * @return
     * @exception IllegalArgumentException
     *                if <code>id</code> is <code>null</code>.
     */
    E get(ID id);

    /**
     * This method is deprecated. Use {{@link #findByExample(Object)}. This
     * method will be removed in next version.
     *
     * @param e
     * @return
     * @see #findByExample(Object)
     */
    @Deprecated
    Collection<E> getByExample(E e);

    /**
     * This method is deprecated. Use {{@link #findOneByExample(Object)}. This
     * method will be removed in next version.
     *
     * @param e
     * @return
     * @see #findOneByExample(Object)
     */
    @Deprecated
    E getOnlyByExample(E e);

    /**
     * @return collection of all entities.
     */
    Collection<E> listAll();

    /**
     * @param entity
     *            entity to be saved
     * @return this instance
     */
    GenericDao<E, ID, Q> save(E entity);
}
