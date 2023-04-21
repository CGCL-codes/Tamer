package org.exist.management;

/**
 * Provides access to some properties of the internal page caches
 * ({@link org.exist.storage.cache.Cache}).
 */
public interface CacheMBean {

    public String getType();

    public int getSize();

    public int getUsed();

    public int getHits();

    public int getFails();

    public String getFileName();
}
