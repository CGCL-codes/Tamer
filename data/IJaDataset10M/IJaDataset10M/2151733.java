package org.datanucleus.store;

import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.exceptions.NucleusObjectNotFoundException;

/**
 * Interface defining persistence operations of a StoreManager.
 * This performs the low level communication with the actual datastore.
 */
public interface StorePersistenceHandler {

    /**
     * Method to close the persistence handler, and release any resources.
     */
    void close();

    /**
     * Returns if the datastore uses referential integrity.
     * Using referential integrity in this context means that any flush of datastore operations means that
     * the operations are ordered; otherwise the persistence handler will receive any deletes as a batch,
     * then any inserts as a batch, followed by any updates.
     * @return Datastore referential integrity capability
     */
    boolean useReferentialIntegrity();

    /**
     * Signal that a batch of operations are starting for the specified ExecutionContext.
     * The batch type allows the store plugin to create whatever type of batch it needs.
     * @param ec The ExecutionContext
     * @param batchType Type of this batch that is starting
     */
    void batchStart(ExecutionContext ec, PersistenceBatchType batchType);

    /**
     * Signal that the current batch of operations are ending for the specified ExecutionContext.
     * @param ec The ExecutionContext
     * @param type Type of batch that is ending
     */
    void batchEnd(ExecutionContext ec, PersistenceBatchType type);

    /**
     * Inserts a persistent object into the database.
     * @param op The ObjectProvider of the object to be inserted.
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    void insertObject(ObjectProvider op);

    /**
     * Method to insert an array of objects to the datastore.
     * @param ops ObjectProviders for the objects to insert
     */
    void insertObjects(ObjectProvider... ops);

    /**
     * Updates a persistent object in the datastore.
     * @param op The ObjectProvider of the object to be updated.
     * @param fieldNumbers The numbers of the fields to be updated.
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    void updateObject(ObjectProvider op, int fieldNumbers[]);

    /**
     * Deletes a persistent object from the datastore.
     * @param op The ObjectProvider of the object to be deleted.
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    void deleteObject(ObjectProvider op);

    /**
     * Method to delete an array of objects from the datastore.
     * @param ops ObjectProviders for the objects to delete
     */
    void deleteObjects(ObjectProvider... ops);

    /**
     * Fetches a persistent object from the database.
     * @param op The ObjectProvider of the object to be fetched.
     * @param fieldNumbers The numbers of the fields to be fetched.
     * @throws NucleusObjectNotFoundException if the object doesn't exist
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    void fetchObject(ObjectProvider op, int fieldNumbers[]);

    /**
     * Locates this object in the datastore.
     * @param op The ObjectProvider for the object to be found
     * @throws NucleusObjectNotFoundException if the object doesn't exist
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    void locateObject(ObjectProvider op);

    /**
     * Locates object(s) in the datastore.
     * @param ops ObjectProvider(s) for the object(s) to be found
     * @throws NucleusObjectNotFoundException if an object doesn't exist
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    void locateObjects(ObjectProvider[] ops);

    /**
     * Method to find a persistable object with the specified id from the datastore, if the StoreManager 
     * supports this operation (optional). This allows for datastores that perform the instantiation of 
     * objects directly (such as ODBMS). With other types of datastores (e.g RDBMS) this method returns null.
     * @param ec The ExecutionContext
     * @param id the id of the object in question.
     * @return a persistable object with a valid object state (for example: hollow) or null, 
     *     indicating that the implementation leaves the instantiation work to DataNucleus.
     * @throws NucleusObjectNotFoundException if this route is supported yet the object doesn't exist
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    public Object findObject(ExecutionContext ec, Object id);

    /**
     * Method to find an array of objects with the specified identities from the datastore.
     * This allows for datastores that perform the instantiation of objects directly (such as ODBMS). 
     * With other types of datastores (e.g RDBMS) this method returns null.
     * @param ec The ExecutionContext
     * @param ids identities of the object(s) to retrieve
     * @return The persistable objects with these identities (in the same order as <pre>ids</pre>)
     * @throws NucleusObjectNotFoundException if an object doesn't exist
     * @throws NucleusDataStoreException when an error occurs in the datastore communication
     */
    public Object[] findObjects(ExecutionContext ec, Object[] ids);
}
