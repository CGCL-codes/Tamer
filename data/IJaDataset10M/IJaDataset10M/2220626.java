package org.amlfilter.util;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Implements a simple LRU cache using LinkedHashMap
 * which was designed for such endeavors
 * @author Harish Seshadri
 * @version $Id: LRUCache.java,v 1.1 2007/01/28 07:13:38 hseshadr Exp $
 */
public class LRUCache extends LinkedHashMap implements Serializable {

    protected int mCacheSize;

    public LRUCache(int pCacheSize) {
        super(pCacheSize, 0.75f, true);
        mCacheSize = pCacheSize;
    }

    /**
     * Simple override of linked hash map to remove the eldest entry
     * @return True if the eldest entry should be removed (based on
     *         the cache reaching capacity; false otherwise
     */
    protected boolean removeEldestEntry() {
        return size() > mCacheSize;
    }
}
