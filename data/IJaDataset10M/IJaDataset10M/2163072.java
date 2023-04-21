package org.hsqldb.lib;

import java.util.NoSuchElementException;
import org.hsqldb.store.BaseHashMap;

/**
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class LongKeyLongValueHashMap extends BaseHashMap {

    private Set keySet;

    private Collection values;

    public LongKeyLongValueHashMap() {
        this(8);
    }

    public LongKeyLongValueHashMap(boolean minimize) {
        this(8);
        minimizeOnEmpty = minimize;
    }

    public LongKeyLongValueHashMap(int initialCapacity) throws IllegalArgumentException {
        super(initialCapacity, BaseHashMap.longKeyOrValue, BaseHashMap.longKeyOrValue, false);
    }

    public long get(long key) throws NoSuchElementException {
        int lookup = getLookup(key);
        if (lookup != -1) {
            return longValueTable[lookup];
        }
        throw new NoSuchElementException();
    }

    public long get(long key, long defaultValue) {
        int lookup = getLookup(key);
        if (lookup != -1) {
            return longValueTable[lookup];
        }
        return defaultValue;
    }

    public boolean get(long key, long[] value) {
        int lookup = getLookup(key);
        if (lookup != -1) {
            value[0] = longValueTable[lookup];
            return true;
        }
        return false;
    }

    public boolean put(long key, long value) {
        int oldSize = size();
        super.addOrRemove(key, value, null, null, false);
        return oldSize != size();
    }

    public boolean remove(long key) {
        int oldSize = size();
        super.addOrRemove(key, 0, null, null, true);
        return oldSize != size();
    }

    public Set keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

    public Collection values() {
        if (values == null) {
            values = new Values();
        }
        return values;
    }

    class KeySet implements Set {

        public Iterator iterator() {
            return LongKeyLongValueHashMap.this.new BaseHashIterator(true);
        }

        public int size() {
            return LongKeyLongValueHashMap.this.size();
        }

        public boolean contains(Object o) {
            throw new RuntimeException();
        }

        public Object get(Object key) {
            throw new RuntimeException();
        }

        public boolean add(Object value) {
            throw new RuntimeException();
        }

        public boolean addAll(Collection c) {
            throw new RuntimeException();
        }

        public boolean remove(Object o) {
            throw new RuntimeException();
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public void clear() {
            LongKeyLongValueHashMap.this.clear();
        }
    }

    class Values implements Collection {

        public Iterator iterator() {
            return LongKeyLongValueHashMap.this.new BaseHashIterator(false);
        }

        public int size() {
            return LongKeyLongValueHashMap.this.size();
        }

        public boolean contains(Object o) {
            throw new RuntimeException();
        }

        public boolean add(Object value) {
            throw new RuntimeException();
        }

        public boolean addAll(Collection c) {
            throw new RuntimeException();
        }

        public boolean remove(Object o) {
            throw new RuntimeException();
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public void clear() {
            LongKeyLongValueHashMap.this.clear();
        }
    }
}
