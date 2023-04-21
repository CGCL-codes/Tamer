package org.progeeks.repository;

import java.io.File;

/**
 * Interface that defines lock managers for providing various types
 * of repository item locking.
 *
 * @version     $Revision: 1.3 $
 * @author      Paul Wisneskey
 *
 */
public interface LockManager {

    /**
     * Initialize the locking manager, obtaining any repository level
     * lock if one is required.
     */
    public void initialize(File repositoryDir);

    /**
     * Method invoked when a new repository is created.  Can be used
     * by the lock manager to set up any persistent storage.  Does not
     * actually initialize the lock manager (i.e. initialize will be
     * called after the create).
     */
    public void createRepository(File repositoryDir);

    /**
     * Terminate the locking manager.
     */
    public void terminate();

    /**
     * Returns the next item id.  Id returned must be guaranteed to be
     * unique across all processes acccessing the repository.
     */
    public int getNextItemId();

    /**
     * Lock the given item for reading and/or writing.
     */
    public Lock lockItem(RepositoryItem item, boolean writeLock);

    /**
     * Lock the given itemId for reading and/or writing.
     */
    public Lock lockItem(String itemId, boolean writeLock);

    /**
     * Release the lock held on the given item.
     */
    public void releaseLock(Lock lock);
}
