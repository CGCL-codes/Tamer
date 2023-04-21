package org.arch4j.persistence;

import java.util.Collection;

/**
 *
 * @author  alwick
 */
public interface PersistenceManager {

    /**
   * Update the object state.
   * 
   * @param isDeepUpdate
   *  If true then navigate the object graph and mark the root and all
   *  children to be updated. Otherwise, update only the root object.
   */
    public void update(Object anObject, boolean isDeepUpdate) throws PersistenceException;

    /**
   * Create a new object.
   * 
   * @param isDeepCreate
   *  If true then navigate the object graph and mark the root and all
   *  children to be created. Otherwise, create only the root object.
   */
    public void create(Object anObject, boolean isDeepCreate) throws PersistenceException;

    /**
   * Specifies if upcoming transactions will perform deep or shallow updates and creates.
   */
    public void setDefaultDeepOrShallow(boolean isDeep) throws PersistenceException;

    /**
   * Update the object state.
   */
    public void update(Object anObject) throws PersistenceException;

    /**
   * Create a new object.
   */
    public void create(Object anObject) throws PersistenceException;

    /**
   * Is this object recognized by the current transaction.
   */
    public boolean isPersistent(Object anObject) throws PersistenceException;

    /**
   * Load the specified instance. This is the same as querying for the single
   * instance specified by the primary key.
   * 
   * @param type
   *  the class type to populate and return
   * @param identity
   *  the primary key used to identify the instance (note that complex
   *  primary keys will be supported in a future release)
   * 
   * @return
   *  the instance if it exists, otherwise null
   */
    public Object load(Class type, Object identity) throws PersistenceException;

    /**
   * Close the connection.
   */
    public void close() throws PersistenceException;

    /**
   * Remove the object.
   */
    public void remove(Object anObject) throws PersistenceException;

    /**
   * Get all objects for the given query string.
   */
    public Collection getObjects(String aQueryString) throws PersistenceException;

    /**
   * Get all objects for the given query object.
   *
   * @param aQuery The query object to use for the query.
   *
   * @return The objects that match the query.
   */
    public Collection getObjects(ObjectQuery aQuery) throws PersistenceException;

    /**
   * Begin a transaction.
   */
    public void beginTransaction() throws PersistenceException;

    /**
   * Commit the transaction.
   */
    public void commit() throws PersistenceException;

    /**
   * Rollback the transaction.
   */
    public void rollback() throws PersistenceException;

    /**
   * Gets the query object for a given query.
   */
    public ObjectQuery getQuery(String anOQLQueryString) throws PersistenceException;

    public boolean validateTransaction() throws PersistenceException;
}
