package net.sourceforge.processdash.net.cache;

public interface ObjectCache {

    /** Return the next available ID for use as a cached object
     * identifier.  This will not return the same number twice.
     */
    int getNextID();

    /** Retrieve a cached object by its ID. */
    public CachedObject getCachedObject(int id, double maxAge);

    /** Delete an object from the cache. */
    public void deleteCachedObject(int id);

    /** Store an object in the cache. */
    public void storeCachedObject(CachedObject obj);

    /** get a list of all the objects in the cache of the specified
     * type. If type is null, all objects are returned. */
    public CachedObject[] getObjects(String type);

    /** get a list of ids for all the objects in the cache of the
     * specified type. If type is null, all objects are returned. */
    public int[] getObjectIDs(String type);
}
