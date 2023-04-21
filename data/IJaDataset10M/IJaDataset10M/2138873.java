package net.suberic.util.cache;

/**
 * A class that can create SizedCacheEntries.
 */
public interface SizedCacheEntryFactory {

    /**
   * Create an appropriate SizedCacheEntry.
   */
    public SizedCacheEntry createCacheEntry(Object value);
}
