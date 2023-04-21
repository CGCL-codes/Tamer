package org.hsqldb.lib;

import java.util.NoSuchElementException;
import org.hsqldb.store.BaseHashMap;

/**
 * @author fredt@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class IntKeyIntValueHashMap extends BaseHashMap {

    private Set keySet;

    private Collection values;

    public IntKeyIntValueHashMap() {
        this(16, 0.75f);
    }

    public IntKeyIntValueHashMap(int initialCapacity) throws IllegalArgumentException {
        this(initialCapacity, 0.75f);
    }

    public IntKeyIntValueHashMap(int initialCapacity, float loadFactor) throws IllegalArgumentException {
        super(initialCapacity, loadFactor, BaseHashMap.intKeyOrValue, BaseHashMap.intKeyOrValue, false);
    }

    public int get(int key) throws NoSuchElementException {
        int lookup = getLookup(key);
        if (lookup != -1) {
            return intValueTable[lookup];
        }
        throw new NoSuchElementException();
    }

    public int get(int key, int defaultValue) {
        int lookup = getLookup(key);
        if (lookup != -1) {
            return intValueTable[lookup];
        }
        return defaultValue;
    }

    public boolean get(int key, int[] value) {
        int lookup = getLookup(key);
        if (lookup != -1) {
            value[0] = intValueTable[lookup];
            return true;
        }
        return false;
    }

    public boolean put(int key, int value) {
        int oldSize = size();
        super.addOrRemove(key, value, null, null, false);
        return oldSize != size();
    }

    public boolean remove(int key) {
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
            return IntKeyIntValueHashMap.this.new BaseHashIterator(true);
        }

        public int size() {
            return IntKeyIntValueHashMap.this.size();
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
            IntKeyIntValueHashMap.this.clear();
        }
    }

    class Values implements Collection {

        public Iterator iterator() {
            return IntKeyIntValueHashMap.this.new BaseHashIterator(false);
        }

        public int size() {
            return IntKeyIntValueHashMap.this.size();
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
            IntKeyIntValueHashMap.this.clear();
        }
    }
}
