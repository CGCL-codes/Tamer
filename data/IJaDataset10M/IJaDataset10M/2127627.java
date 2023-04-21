package org.twdata.pipeline.cache;

/**
 *  This interface allows for multiple implementations of a cache presumably
 *  with different caching algrithms.
 */
public interface Cache {

    /**
     *  Gets the name of the cache
     *
     *@return    The name value
     */
    public String getName();

    /**
     *  Sets the name of the cache
     *
     *@param  name  The name
     */
    public void setName(String name);

    /**
     *  Gets the maximum number of objects it can cache
     *
     *@return    The size value
     */
    public int getSize();

    /**
     *  Sets the maximum number of objects it will cache
     *
     *@param  size  The size of the cache
     */
    public void setSize(int size);

    /**
     *  Gets a cached object
     *
     *@param  identifier  The key to find the object
     *@return             The cacheable value
     */
    public Cacheable getCacheable(Object identifier);

    /**
     *  Removes a cached object
     *
     *@param  identifier  The key of the object to remove
     */
    public void removeCacheable(Object identifier);

    /**  Removes all the expired objects from the cache */
    public void expire();

    /**
     *  Adds an object to the cache
     *
     *@param  cacheable  The object to add
     */
    public void addCacheable(Cacheable cacheable);

    /**  Clears the cache of all cacheables */
    public void clear();
}
