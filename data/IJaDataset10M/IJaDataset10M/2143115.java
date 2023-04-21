package org.jmonks.dms.versioncontrol.api;

import java.util.Date;

/**
 *
 * @author Suresh Pragada
 * This represents interface to entry in Repository. It can be either
 * Directory or File. Every entry in repository will have an ID and name.
 */
public interface RepositoryEntry {

    /**
     * This returns the unique ID of the entry. Every entry in repository will have a unique ID.
     * @return  Returns unique ID of the entry in repository. 
     */
    public long getEntryID();

    /**
     * This returns name of the entry.
     * @return  Returns the entry name.
     */
    public String getEntryName();

    /**
     * Returns the parent entry ID.
     * @return Returns the entry ID of the parent.
     */
    public long getParentEntryID();

    /**
     * Returns the entry type.
     * @return Returns the "1" if the entry type is file, "0" it entry type is directory.
     */
    public int getEntryType();

    /**
     * Gets the parent repository entry.
     *
     * @return  Returns the parent repository entry, null if parent doesnt exist.
     */
    public RepositoryEntry getParentRepositoryEntry();

    /**
     * This marks the entry for delete. It is not actually physicall delete the entry
     * from the repository. Later point of time, admin can go and recover this entry.
     * These kind of entries will be stay in repository for sepcified period and delete
     * completely when batch job runs on periodically based.
     *
     * @return  Return true if it could mark delete the entry, false otherwise.
     */
    public boolean delete();

    /**
     * Returns the deleted status of the entry.
     *
     *@return Return true if entry is deleted, false otherwise.
     */
    public boolean isDeleted();

    /**
     * This actually takes off the delete mark on the entry. When there is request to delete
     * an entry, it simply marks the entry for delete. This takes off that mark on entry.
     *
     *@return Return true if delete mark has been cancelled, false otherwise.
     */
    public boolean cancelDelete();

    /**
     * Gets the deleted date of this entry.
     *
     * @return  Returns date of the entry got deleted.
     */
    public Date getDeletedDate();

    /**
     * This process actually permanently delete the entry from repository. After this operation
     * entry will not be available in the repository.
     *
     * @return Returns true if entry got deleted permanently, false otherwise.
     */
    public boolean remove();
}
