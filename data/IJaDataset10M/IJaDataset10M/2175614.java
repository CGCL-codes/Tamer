package org.jgroups.persistence;

import java.io.Serializable;
import java.util.Map;
import org.jgroups.annotations.Unsupported;

@Unsupported
public interface PersistenceManager {

    /**
     * Save new NV pair as serializable objects or if already exist; store 
     * new state 
     * @param key
     * @param val
     * @exception CannotPersistException; 
     */
    void save(Serializable key, Serializable val) throws CannotPersistException;

    /**
     * Remove existing NV from being persisted
     * @param key value
     * @return Serializable; gives back the value
     * @exception CannotRemoveException;
     */
    Serializable remove(Serializable key) throws CannotRemoveException;

    /**
     * Use to store a complete map into persistent state
     * @param map
     * @exception CannotPersistException;
     */
    void saveAll(Map map) throws CannotPersistException;

    /**
     * Gives back the Map in last known state
     * @return Map;
     * @exception CannotRetrieveException;
     */
    Map retrieveAll() throws CannotRetrieveException;

    /**
     * Clears the complete NV state from the DB
     * @exception CannotRemoveException;
     */
    void clear() throws CannotRemoveException;

    /**
     * Used to handle shutdown call the PersistenceManager implementation. 
     * Persistent engines can leave this implementation empty.
     */
    void shutDown();
}
