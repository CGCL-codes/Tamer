package org.icenigrid.gridsam.core.plugin;

import java.util.ConcurrentModificationException;

/**
 * Interface defining a storage of persistent objects
 * 
 * @author wwhl
 */
public interface PersistenceStore {

    /**
     * begin a persistence store transaction
     * 
     * @throws org.icenigrid.gridsam.core.plugin.PersistenceException
     *             if the transaction cannot be started
     */
    public Transaction begin() throws PersistenceException;

    /**
     * flush all updates to persistent storage
     * 
     * @throws PersistenceException
     *             if data fails to be flushed
     */
    public void flush() throws PersistenceException, ConcurrentModificationException;

    /**
     * close the persistence store
     */
    public void close();
}
