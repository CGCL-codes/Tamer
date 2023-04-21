package org.nex.ts.smp.api;

/**
 * 
 * @author jackpark
 *
 */
public interface IRemovableCache {

    /**
	 * Instances of {@link org.nex.util.LRUCache} use this method
	 * @param key
	 */
    void remove(Object key);
}
