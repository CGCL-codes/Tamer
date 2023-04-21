package com.szczytowski.genericdao.api;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityNotFoundException;

/**
 * Interface represents implementation of generic DAO.
 * 
 * @param <T> object's type, it must implements at least <code>IEntity</code>
 * @param <I> primary key's type
 * 
 * @author Maciej Szczytowski <mszczytowski-genericdao@gmail.com>
 * @since 1.0
 */
public interface IDao<T extends IEntity<I>, I extends Serializable> {

    /**
   * Retrieve an persisted object using the given id as primary key.
   * 
   * @param id object's primary key
   * @return object
   * @throws EntityNotFoundException -
   *           if not found
   */
    T load(I id) throws EntityNotFoundException;

    /**
   * Retrieve an persisted object using the given id as primary key.
   * 
   * Returns null if not found.
   * 
   * @param id object's primary key
   * @return object
   */
    T get(I id);

    /**
   * Retrieve an persisted objects using the given ids as primary keys.
   * 
   * @param ids objects's ids
   * @return list of objects
   */
    List<T> get(I... ids);

    /**
   * Retrieve all persisted objects with given parents.
   * 
   * If parent is null, method returns root objects (with no parent).
   * 
   * @param parent objects' parent
   * @return list of objects
   */
    List<T> get(IInheritable<T> parent);

    /**
   * Retrieve all persisted objects.
   * 
   * @return list of objects
   */
    List<T> getAll();

    /**
   * Find objects comparing them with examplary object using all not-null properties.
   * 
   * Properties <code>IEntity.id</code>, <code>IDefaultable.isDefault</code>,
   * <code>IActivable.isActive</code> and <code>IHiddenable.isHidden</code> are ignored.
   * 
   * @param example examplary object
   * @return list of objects
   */
    List<T> findByExample(T example);

    /**
   * Set object as default one.
   * 
   * Check if there is only one default object - it uses
   * <code>IDefaultable#getExample()</code> and <code>#findByExample(IEntity)</code> methods to
   * get objects and mark them as not-default.
   * 
   * It is possible to have more
   * than one default object manipulating <code>IDefaultable#getExample()</code>
   * method.
   * 
   * @param object default object
   * @see IDefaultable#getExample()
   * @see #findByExample(IEntity)
   */
    void setAsDefault(IDefaultable object);

    /**
   * Save all changes made to an object.
   * 
   * @param object object
   */
    T save(T object);

    /**
   * Save all changes made to objects.
   * 
   * @param objects objects
   */
    void save(T... objects);

    /**
   * Remove an object by given id. Check if object is not default one.
   * 
   * If entity implements <code>IHiddenable</code> then it is hidden instead of deleted.
   * 
   * @param id object's pk
   * @throws UnsupportedOperationException - if entity is default one
   * @see IHiddenable
   */
    void delete(I id) throws UnsupportedOperationException;

    /**
   * Remove objects by given ids. Check if all objects are not mark as default one.
   * 
   * If objects implement <code>IHiddenable</code> then ther are hidden instead of deleted.
   * 
   * @param ids objects's pk
   * @throws UnsupportedOperationException - if one of entities is default one
   * @see IHiddenable
   */
    void delete(I... ids) throws UnsupportedOperationException;

    /**
   * Remove an object. Check if object is not default one.
   * 
   * If object implements <code>IHiddenable</code> then it is hidden instead of deleted.
   * 
   * @param object object
   * @throws UnsupportedOperationException - if entity is default one
   * @see IHiddenable
   */
    void delete(T object) throws UnsupportedOperationException;

    /**
   * Remove objects. Check if all objects are not mark as default one.
   * 
   * If objects implement <code>IHiddenable</code> then they are hidden instead of deleted.
   * 
   * @param objects objects
   * @throws UnsupportedOperationException - if one of entities is default one
   * @see IHiddenable
   */
    void delete(T... objects) throws UnsupportedOperationException;

    /**
   * Delete all objects, including default one.
   * 
   * If objects implement <code>IHiddenable</code> then they are hidden instead of deleted.
   * 
   * @see IHiddenable
   */
    void deleteAll();

    /**
   * Refresh a persistant object that may have changed in another thread/transaction.
   * 
   * @param entity transient entity
   */
    void refresh(T entity);

    /**
   * Write to database anything that is pending operation and clear it.
   */
    void flushAndClear();

    /**
   * Check whether the DAO represents <code>IActivable</code> implementation.
   * 
   * @return true if DAO's object are activable
   * @see IActivable
   */
    public boolean isActivable();

    /**
   * Check whether the DAO represents <code>IDefaultable</code> implementation.
   * 
   * @return true if DAO's object are defaultable
   * @see IDefaultable
   */
    public boolean isDefaultable();

    /**
   * Check whether the DAO represents <code>IHiddenable</code> implementation.
   * 
   * @return true if DAO's object are hiddenable
   * @see IHiddenable
   */
    public boolean isHiddenable();

    /**
   * Check whether the DAO represents <code>IInheritable</code> implementation.
   * 
   * @return true if DAO's object are inheritable
   * @see IInheritable
   */
    public boolean isInheritable();
}
