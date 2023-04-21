package org.linkedgeodata.util.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

/**
 * A map which only holds a limited set of entries.
 * Oldest entry is removed first
 * 
 * Accessing an entry renews its time.
 * 
 * @author raven
 *
 * An alternative implementation could sort by relative access counts.
 * in this case sortedTimes needs to be replaced by a TreeMap<Real, Set<K>> 
 * (e.g. 0.135533 -> {A, B})
 * 
 *
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = -4277098373746171836L;

    private BidiMap<K, Integer> keyToTime = new DualHashBidiMap<K, Integer>();

    private TreeMap<Integer, K> sortedTimes = new TreeMap<Integer, K>();

    private int currentTime = 0;

    private int maxSize = 100;

    private int hitCount = 0;

    private int missCount = 0;

    public CacheMap() {
    }

    public CacheMap(int maxSize) {
        setMaxCacheSize(maxSize);
    }

    public void setMaxCacheSize(int newMaxSize) {
        if (newMaxSize < 0) throw new IllegalArgumentException();
        int removeCount = Math.min(size() - newMaxSize, 0);
        for (int i = 0; i < removeCount; ++i) removeOldest();
        maxSize = newMaxSize;
    }

    private void removeOldest() {
        Map.Entry<Integer, K> removeItem = sortedTimes.pollFirstEntry();
        keyToTime.removeValue(removeItem.getKey());
        remove(removeItem.getValue());
    }

    /**
	 * Makes all sorted times start from 0 again
	 * (Just a function for the very unlikely case that a int overflows)
	 */
    private void retime() {
        TreeMap<Integer, K> newSortedTimes = new TreeMap<Integer, K>();
        currentTime = 0;
        for (Map.Entry<Integer, K> entry : sortedTimes.entrySet()) {
            keyToTime.put(entry.getValue(), currentTime);
            newSortedTimes.put(currentTime, entry.getValue());
            ++currentTime;
        }
        sortedTimes = newSortedTimes;
    }

    private Integer initTime(K key) {
        keyToTime.put(key, currentTime);
        sortedTimes.put(currentTime, key);
        return currentTime++;
    }

    private void updateTime(K key, Integer oldTime) {
        sortedTimes.remove(oldTime);
        initTime(key);
    }

    @Override
    public V put(K key, V value) {
        Integer keyTime = keyToTime.get(key);
        if (keyTime == null) {
            if (size() == maxSize) removeOldest();
            initTime(key);
        } else {
            if (currentTime == Integer.MAX_VALUE) retime();
            updateTime(key, keyTime);
        }
        return super.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
        return _get((K) key);
    }

    private V _get(K key) {
        Integer keyTime = keyToTime.get(key);
        if (keyTime == null) {
            ++missCount;
            return null;
        }
        ++hitCount;
        updateTime(key, keyTime);
        return super.get(key);
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getMissCount() {
        return missCount;
    }

    public void resetStats() {
        hitCount = 0;
        missCount = 0;
    }
}
