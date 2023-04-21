package edu.mit.osidimpl.filing.cache;

import edu.mit.osidimpl.manager.*;
import edu.mit.osidimpl.filing.shared.*;

/**
 *  <p>
 *  Implements a Cabinet for caching.
 *  </p><p>
 *  CVS $Id: Cabinet.java,v 1.3 2006/10/06 18:12:49 tom Exp $
 *  </p>
 *  
 *  @author  Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.3 $
 */
public class Cabinet implements org.osid.filing.Cabinet {

    FilingManager mgr;

    OsidLogger logger;

    org.osid.filing.Cabinet cabinet;

    private String displayName;

    private org.osid.shared.Id id;

    private long createdTime = 0;

    private long lastModifiedTime = 0;

    private long lastAccessedTime = 0;

    private org.osid.shared.Id agentId = null;

    private long lastUpdateTime = 0;

    private long lastEntriesUpdateTime = 0;

    private boolean updatingEntries = false;

    private java.util.HashMap<String, org.osid.filing.CabinetEntry> entryCache = new java.util.HashMap<String, org.osid.filing.CabinetEntry>();

    /**  
     *  Constructs a new <code>Cabinet</code>.
     *  
     *  @param mgr the FilingManager for this service
     *  @param cabinet 
     */
    protected Cabinet(FilingManager mgr, org.osid.filing.Cabinet cabinet) {
        this.mgr = mgr;
        this.logger = mgr.logger;
        this.cabinet = cabinet;
        logger.logTrace("created new cabinet entry");
        update(true);
    }

    /**
     *  Creates a new ByteStore and add it to this Cabinet under the given
     *  name The name must not include this Cabinet's separationCharacter.
     *
     *  @param displayName The name to be used
     *  @return The ByteStore created
     *  @throws org.osid.filing.FilingException if the ByteStore cannot be
     *          created
     */
    public org.osid.filing.ByteStore createByteStore(String displayName) throws org.osid.filing.FilingException {
        logger.logMethod(displayName);
        return (this.cabinet.createByteStore(displayName));
    }

    /**
     *  Create a new Cabinet and add it to this Cabinet under the given name.
     *  The name must not include this Cabinet's separationCharacter.
     *
     *  @param displayName The name to be used
     *  @return The Cabinet created
     *  @throws org.osid.filing.FilingException if Cabinet could not be created
     */
    public org.osid.filing.Cabinet createCabinet(String displayName) throws org.osid.filing.FilingException {
        logger.logMethod(displayName);
        return (this.cabinet.createCabinet(displayName));
    }

    /**
     *  Copy an existing ByteStore in this Cabinet by copying contents and the
     *  appropriate attributes of another ByteStore.
     *
     *  @param displayName
     *  @param oldByteStore
     *  @return ByteStore
     *  @throws org.osid.filing.FilingException if unable to copy file
     */
    public org.osid.filing.ByteStore copyByteStore(String displayName, org.osid.filing.ByteStore oldByteStore) throws org.osid.filing.FilingException {
        logger.logMethod(displayName, oldByteStore);
        return (this.cabinet.copyByteStore(displayName, oldByteStore));
    }

    /**
     *  Add a CabinetEntry, it must be from same Manager.
     *
     *  @param entry
     *  @param displayName
     *  @throws org.osid.filing.FilingException
     */
    public void add(org.osid.filing.CabinetEntry entry, String displayName) throws org.osid.filing.FilingException {
        logger.logMethod(entry, displayName);
        this.cabinet.add(entry, displayName);
        return;
    }

    /**
     *  Remove a CabinetEntry. Does not destroy the CabinetEntry.
     *
     *  @param entry
     *  @throws org.osid.filing.FilingException
     */
    public void remove(org.osid.filing.CabinetEntry entry) throws org.osid.filing.FilingException {
        logger.logMethod(entry);
        this.cabinet.remove(entry);
        return;
    }

    /**
     *  Get a CabinetEntry from a Cabinet by its ID.
     *
     *  @param id
     *  @return CabinetEntry which has given ID.
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link
     *          org.osid.filing.FilingException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.filing.FilingException#IO_ERROR IO_ERROR},{@link
     *          org.osid.filing.FilingException#ITEM_DOES_NOT_EXIST
     *          ITEM_DOES_NOT_EXIST}
     */
    public org.osid.filing.CabinetEntry getCabinetEntryById(org.osid.shared.Id id) throws org.osid.filing.FilingException {
        logger.logMethod(id);
        if (id == null) {
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.NULL_ARGUMENT);
        }
        updateEntries();
        try {
            org.osid.filing.CabinetEntry entry = this.mgr.entryCache.get(id.getIdString());
            if (entry != null) {
                return (entry);
            }
        } catch (org.osid.shared.SharedException se) {
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.OPERATION_FAILED);
        }
        throw new org.osid.filing.FilingException(org.osid.filing.FilingException.ITEM_DOES_NOT_EXIST);
    }

    /**
     *  Get a CabinetEntry by name.  Not all CabinetEntrys have names, but if 
     *  it has a name, the name is unique within a Cabinet.
     *
     *  @param displayName
     *  @return CabinetEntry which has given name
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link
     *          org.osid.filing.FilingException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.filing.FilingException#IO_ERROR IO_ERROR}, 
     *          {@link org.osid.filing.FilingException#ITEM_DOES_NOT_EXIST
     *          ITEM_DOES_NOT_EXIST}, {@link
     *          org.osid.filing.FilingException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public org.osid.filing.CabinetEntry getCabinetEntryByName(String displayName) throws org.osid.filing.FilingException {
        logger.logMethod(displayName);
        if (displayName == null) {
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.NULL_ARGUMENT);
        }
        updateEntries();
        org.osid.filing.CabinetEntry entry = this.entryCache.get(displayName);
        if (entry != null) {
            return (entry);
        }
        throw new org.osid.filing.FilingException(org.osid.filing.FilingException.ITEM_DOES_NOT_EXIST);
    }

    /**
     *  Get an Iterator over all CabinetEntries in this Cabinet.
     *
     *  @return CabinetEntryIterator
     *  @throws org.osid.filing.FilingException
     */
    public org.osid.filing.CabinetEntryIterator entries() throws org.osid.filing.FilingException {
        logger.logMethod();
        updateEntries();
        return (new CabinetEntryIterator(this.entryCache.values()));
    }

    /**
     *  Return the root Cabinet of this Cabinet.
     * 
     *  @return root Cabinet
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link
     *          org.osid.filing.FilingException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.filing.FilingException#IO_ERROR IO_ERROR}, {@link
     *          org.osid.filing.FilingException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public org.osid.filing.Cabinet getRootCabinet() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getRootCabinet());
    }

    /**
     *  Return true if this Cabinet is the root Cabinet.
     *
     *  @return true if and only if this Cabinet is the root Cabinet.
     *  @throws org.osid.filing.FilingException
     */
    public boolean isRootCabinet() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.isRootCabinet());
    }

    /**
     *  Return true if this Cabinet allows entries to be added or removed.
     *
     *  @return true if and only if this Cabinet allows entries to be added or
     *          removed.
     *  @throws org.osid.filing.FilingException
     */
    public boolean isManageable() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.isManageable());
    }

    /**
     *  Get number of bytes available in this Cabinet.
     *
     *  @return long Space available in this Cabinet, in bytes.
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link org.osid.filing.FilingException#IO_ERROR
     *          IO_ERROR}, {@link org.osid.filing.FilingException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public long getAvailableBytes() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getAvailableBytes());
    }

    /**
     *  Get number of bytes used in this Cabinet.
     *
     *  @return long Space used in this Cabinet, in bytes.
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link org.osid.filing.FilingException#IO_ERROR
     *          IO_ERROR}, {@link org.osid.filing.FilingException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public long getUsedBytes() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getUsedBytes());
    }

    /**
     *  Get all the Property Types for CabinetEntry.
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.filing.FilingException
     */
    public org.osid.shared.TypeIterator getPropertyTypes() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getPropertyTypes());
    }

    /**
     *  Returns true or false depending on whether this CabinetEntry exists in
     *  the file system.
     *
     *  @return boolean true if CabinetEntry exists, false otherwise
     *  @throws org.osid.filing.FilingException An exception with one of the
     *         following messages defined in org.osid.filing.FilingException
     *         may be thrown: {@link
     *         org.osid.filing.FilingException#PERMISSION_DENIED
     *         PERMISSION_DENIED}
     */
    public boolean exists() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (true);
    }

    /**
     *  Returns the Cabinet in which this CabinetEntry is an entry, or null if
     *  it has no parent (for example is the root Cabinet).
     *
     *  @return Cabinet the parent Cabinet of this entry, or null if it has no
     *          parent (e.g. is the root Cabinet)
     *  @throws org.osid.filing.FilingException
     */
    public org.osid.filing.Cabinet getParent() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getParent());
    }

    /**
     *  Get the Properties of this Type associated with this CabinetEntry.
     *
     *  @return org.osid.shared.Properties
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link
     *          org.osid.filing.FilingException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public org.osid.shared.Properties getPropertiesByType(org.osid.shared.Type propertiesType) throws org.osid.filing.FilingException {
        logger.logMethod(propertiesType);
        return (this.cabinet.getPropertiesByType(propertiesType));
    }

    /**
     *  Get the Properties associated with this CabinetEntry.
     *
     *  @return PropertiesIterator
     *  @throws org.osid.filing.FilingException
     */
    public org.osid.shared.PropertiesIterator getProperties() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getProperties());
    }

    /**
     *  Return the name of this CabinetEntry in its parent Cabinet.
     *
     *  @return name
     *  @throws org.osid.filing.FilingException
     */
    public String getDisplayName() throws org.osid.filing.FilingException {
        logger.logMethod();
        update(false);
        return (this.displayName);
    }

    /**
     * Get Id of this CabinetEntry
     *
     * @return Id
     *
     * @throws org.osid.filing.FilingException
     */
    public org.osid.shared.Id getId() throws org.osid.filing.FilingException {
        logger.logMethod();
        update(false);
        return (this.id);
    }

    /**
     *  Returns when this Cabinet was last modified.
     *
     *  @return long The time this cabinet was last modified (the number of
     *          milliseconds since January 1, 1970, 00:00:00 GMT)
     *  @throws org.osid.filing.FilingException An exception with one of the
     *         following messages defined in org.osid.filing.FilingException
     *         may be thrown: 
     *         {@link org.osid.filing.FilingException#PERMISSION_DENIED
     *         PERMISSION_DENIED}
     */
    public long getLastModifiedTime() throws org.osid.filing.FilingException {
        logger.logMethod();
        update(false);
        return (this.lastModifiedTime);
    }

    /**
     *  Returns all the times that this Cabinet was modified.
     *
     *  @return org.osid.shared.LongValueInterator The times this cabinet was
     *          modified
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown:  
     *          {@link org.osid.filing.FilingException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public org.osid.shared.LongValueIterator getAllModifiedTimes() throws org.osid.filing.FilingException {
        logger.logMethod();
        return (this.cabinet.getAllModifiedTimes());
    }

    /**
     *  Sets the last-modified time to the current time for this CabinetEntry.
     *
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link
     *          org.osid.filing.FilingException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.filing.FilingException#IO_ERROR IO_ERROR}
     */
    public void touch() throws org.osid.filing.FilingException {
        logger.logMethod();
        this.cabinet.touch();
        this.lastModifiedTime = System.currentTimeMillis();
        return;
    }

    /**
     *  Returns when this Cabinet was last accessed. Not all implementations
     *  will record last access times accurately, due to caching and
     *  performance.  The value returned will be at least the last modified
     *  time, the actual time when a read was performed may be later.
     *
     *  @return long The time the file was last accessed (the number of
     *          milliseconds since January 1, 1970, 00:00:00 GMT).
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: 
     *          {@link org.osid.filing.FilingException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public long getLastAccessedTime() throws org.osid.filing.FilingException {
        logger.logMethod();
        update(false);
        return (this.lastAccessedTime);
    }

    /**
     *  Returns when this CabinetEntry was created. Not all implementations 
     *  will record the time of creation accurately.  The value returned will
     *  be at least the last modified time, the actual creation time may be 
     *  earlier.
     *
     *  @return long The time the file was created (the number of milliseconds
     *          since January 1, 1970, 00:00:00 GMT).
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: 
     *          {@link org.osid.filing.FilingException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public long getCreatedTime() throws org.osid.filing.FilingException {
        logger.logMethod();
        update(false);
        return (this.createdTime);
    }

    /**
     *  Return the Id of the Agent that owns this CabinetEntry.
     *
     *  @return org.osid.shared.Id
     *  @throws org.osid.filing.FilingException An exception with one of the
     *          following messages defined in org.osid.filing.FilingException
     *          may be thrown: {@link
     *          org.osid.filing.FilingException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public org.osid.shared.Id getCabinetEntryAgentId() throws org.osid.filing.FilingException {
        logger.logMethod();
        update(false);
        return (this.agentId);
    }

    /**
     *  Change the name of this CabinetEntry to <code>displayName</code>
     *
     *  @param displayName the new name for the entry
     *  @throws org.osid.filing.FilingException
     */
    public void updateDisplayName(String displayName) throws org.osid.filing.FilingException {
        this.cabinet.updateDisplayName(displayName);
        this.displayName = displayName;
        return;
    }

    protected synchronized void updateEntries() {
        if (this.updatingEntries) {
            return;
        }
        if ((System.currentTimeMillis() - this.lastEntriesUpdateTime) < this.mgr.REFRESH_TIME) {
            return;
        }
        logger.logInfo("starting entry lookup to complete from " + displayName);
        UpdateEntries updater = new UpdateEntries(this);
        updater.start();
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
                logger.logError("update entries thread interrupted", ie);
            }
            logger.logInfo("waiting for entries to complete from " + displayName);
            if (updater.isAlive() == false) {
                return;
            }
        }
        return;
    }

    class UpdateEntries extends Thread {

        Cabinet cabinet;

        UpdateEntries(Cabinet cabinet) {
            this.cabinet = cabinet;
        }

        public void run() {
            updatingEntries = true;
            logger.logInfo("updating cabinet entries for " + displayName);
            java.util.ArrayList<org.osid.filing.CabinetEntry> al = new java.util.ArrayList<org.osid.filing.CabinetEntry>();
            try {
                org.osid.filing.CabinetEntryIterator entries = cabinet.entries();
                logger.logInfo("got entries for " + displayName);
                while (entries.hasNextCabinetEntry()) {
                    org.osid.filing.CabinetEntry entry = entries.nextCabinetEntry();
                    String id = entry.getId().getIdString();
                    String name = entry.getDisplayName();
                    org.osid.filing.CabinetEntry cached = entryCache.get(name);
                    al.add(cached);
                    if (cached == null) {
                        if (entry instanceof org.osid.filing.Cabinet) {
                            logger.logInfo("adding cabinet for " + name);
                            cached = new Cabinet(mgr, (org.osid.filing.Cabinet) entry);
                        } else {
                            cached = new ByteStore(cabinet, (org.osid.filing.ByteStore) entry);
                            logger.logInfo("adding bytestore for " + name);
                        }
                        synchronized (entryCache) {
                            entryCache.put(name, cached);
                        }
                        synchronized (mgr.entryCache) {
                            mgr.entryCache.put(id, cached);
                        }
                    }
                }
                for (org.osid.filing.CabinetEntry cache : entryCache.values()) {
                    logger.logInfo("cleaning up id cache");
                    boolean found = false;
                    for (org.osid.filing.CabinetEntry entry : al) {
                        if (entry.getId().isEqual(cache.getId())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        logger.logInfo("removing " + cache.getDisplayName());
                        mgr.entryCache.remove(cache.getId().getIdString());
                        entryCache.remove(cache.getDisplayName());
                    }
                }
            } catch (org.osid.shared.SharedException se) {
                se.printStackTrace();
            }
            lastEntriesUpdateTime = System.currentTimeMillis();
            updatingEntries = false;
            return;
        }
    }

    protected synchronized void update(boolean force) {
        if (((System.currentTimeMillis() - this.lastUpdateTime) < this.mgr.REFRESH_TIME) && !force) {
            return;
        }
        try {
            this.displayName = this.cabinet.getDisplayName();
            this.id = this.cabinet.getId();
        } catch (org.osid.filing.FilingException fe) {
        }
        try {
            this.createdTime = this.cabinet.getCreatedTime();
        } catch (org.osid.filing.FilingException fe) {
        }
        try {
            this.lastModifiedTime = this.cabinet.getLastModifiedTime();
        } catch (org.osid.filing.FilingException fe) {
        }
        try {
            this.lastAccessedTime = this.cabinet.getLastAccessedTime();
        } catch (org.osid.filing.FilingException fe) {
        }
        try {
            this.agentId = this.cabinet.getCabinetEntryAgentId();
        } catch (org.osid.filing.FilingException fe) {
        }
        this.lastUpdateTime = System.currentTimeMillis();
        return;
    }
}
