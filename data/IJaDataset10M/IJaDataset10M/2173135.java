package org.tunesremote.daap;

/**
 * POJO representing a Playlist object.
 */
public class Playlist {

    private final long ID;

    private final String name, persistentId;

    private final long count;

    public Playlist(final long ID, final String name, final long count, final String persistentId) {
        this.ID = ID;
        this.name = name;
        this.count = count;
        this.persistentId = persistentId;
    }

    /**
    * Gets the iD.
    * <p>
    * @return Returns the iD.
    */
    public long getID() {
        return ID;
    }

    /**
    * Gets the name.
    * <p>
    * @return Returns the name.
    */
    public String getName() {
        return name;
    }

    /**
    * Gets the persistentId.
    * <p>
    * @return Returns the persistentId.
    */
    public String getPersistentId() {
        return persistentId;
    }

    /**
    * Gets the count.
    * <p>
    * @return Returns the count.
    */
    public long getCount() {
        return count;
    }
}
