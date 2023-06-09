package org.apache.harmony.unpack200;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * The SegmentConstantPool spends a lot of time searching
 * through large arrays of Strings looking for matches.
 * This can be sped up by caching the arrays in HashMaps
 * so the String keys are looked up and resolve to positions
 * in the array rather than iterating through the arrays
 * each time.
 *
 * Because the arrays only grow (never shrink or change)
 * we can use the last known size as a way to determine if
 * the array has changed.
 *
 * Note that this cache must be synchronized externally
 * if it is shared.
 */
public class SegmentConstantPoolArrayCache {

    protected IdentityHashMap knownArrays = new IdentityHashMap(1000);

    protected List lastIndexes;

    protected String[] lastArray;

    protected String lastKey;

    /**
     * Answer the indices for the given key in the given
     * array. If no such key exists in the cached array,
     * answer -1.
     * @param array String[] array to search for the value
     * @param key String value for which to search
     * @return List collection of index positions in the array
     */
    public List indexesForArrayKey(String[] array, String key) {
        if (!arrayIsCached(array)) {
            cacheArray(array);
        }
        if ((lastArray == array) && (lastKey == key)) {
            return lastIndexes;
        }
        lastArray = array;
        lastKey = key;
        lastIndexes = ((CachedArray) knownArrays.get(array)).indexesForKey(key);
        return lastIndexes;
    }

    /**
     * Given a String array, answer true if the
     * array is correctly cached. Answer false
     * if the array is not cached, or if the
     * array cache is outdated.
     *
     * @param array of String
     * @return boolean true if up-to-date cache,
     *   otherwise false.
     */
    protected boolean arrayIsCached(String[] array) {
        if (!knownArrays.containsKey(array)) {
            return false;
        }
        CachedArray cachedArray = (CachedArray) knownArrays.get(array);
        if (cachedArray.lastKnownSize() != array.length) {
            return false;
        }
        return true;
    }

    /**
     * Cache the array passed in as the argument
     * @param array String[] to cache
     */
    protected void cacheArray(String[] array) {
        if (arrayIsCached(array)) {
            throw new IllegalArgumentException("Trying to cache an array that already exists");
        }
        knownArrays.put(array, new CachedArray(array));
        lastArray = null;
    }

    /**
     * CachedArray keeps track of the last known size of
     * an array as well as a HashMap that knows the mapping
     * from element values to the indices of the array
     * which contain that value.
     */
    protected class CachedArray {

        String[] primaryArray;

        int lastKnownSize;

        HashMap primaryTable;

        public CachedArray(String[] array) {
            super();
            this.primaryArray = array;
            this.lastKnownSize = array.length;
            this.primaryTable = new HashMap(lastKnownSize);
            cacheIndexes();
        }

        /**
         * Answer the last known size of the array cached.
         * If the last known size is not the same as the
         * current size, the array must have changed.
         * @return int last known size of the cached array
         */
        public int lastKnownSize() {
            return lastKnownSize;
        }

        /**
         * Given a particular key, answer a List of
         * index locations in the array which contain that
         * key.
         *
         * If no elements are found, answer an empty list.
         *
         * @param key String element of the array
         * @return List of indexes containing that key
         *   in the array.
         */
        public List indexesForKey(String key) {
            if (!primaryTable.containsKey(key)) {
                return Collections.EMPTY_LIST;
            }
            return (List) primaryTable.get(key);
        }

        /**
         * Given a primaryArray, cache its values in a HashMap
         * to provide a backwards mapping from element values
         * to element indexes. For instance, a primaryArray
         * of:
         *  {"Zero", "Foo", "Two", "Foo"}
         *  would yield a HashMap of:
         *   "Zero" -> 0
         *   "Foo" -> 1, 3
         *   "Two" -> 2
         * which is then cached.
         */
        protected void cacheIndexes() {
            for (int index = 0; index < primaryArray.length; index++) {
                String key = primaryArray[index];
                if (!primaryTable.containsKey(key)) {
                    primaryTable.put(key, new ArrayList());
                }
                ((ArrayList) primaryTable.get(key)).add(new Integer(index));
            }
        }
    }
}
