package com.jogamp.common.util;

import com.jogamp.common.JogampRuntimeException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Fast HashMap for primitive data. Optimized for being GC friendly.
 * Original code is based on the <a href="http://code.google.com/p/skorpios/"> skorpios project</a>
 * released under new BSD license.
 *
 * @author Michael Bien
 * @author Simon Goller
 * @author Sven Gothel
 * 
 * @see IntObjectHashMap
 * @see IntIntHashMap
 * @see LongObjectHashMap
 * @see LongLongHashMap
 * @see LongIntHashMap
 */
public class IntLongHashMap implements Cloneable, Iterable {

    private final float loadFactor;

    private Entry[] table;

    private int size;

    private int mask;

    private int capacity;

    private int threshold;

    private long keyNotFoundValue = -1;

    private static final boolean isPrimitive;

    private static final Constructor entryConstructor;

    private static final Method equalsMethod;

    static {
        final Class valueClazz = long.class;
        final Class keyClazz = int.class;
        isPrimitive = valueClazz.isPrimitive();
        Constructor c = null;
        Method m = null;
        if (!isPrimitive) {
            c = ReflectionUtil.getConstructor(Entry.class, new Class[] { keyClazz, valueClazz, Entry.class });
            try {
                m = valueClazz.getDeclaredMethod("equals", Object.class);
            } catch (NoSuchMethodException ex) {
                throw new JogampRuntimeException("Class " + valueClazz + " doesn't support equals(Object)");
            }
        }
        entryConstructor = c;
        equalsMethod = m;
    }

    public IntLongHashMap() {
        this(16, 0.75f);
    }

    public IntLongHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntLongHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity > 1 << 30) {
            throw new IllegalArgumentException("initialCapacity is too large.");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be greater than zero.");
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("loadFactor must be greater than zero.");
        }
        capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Entry[capacity];
        this.mask = capacity - 1;
    }

    private IntLongHashMap(float loadFactor, int table_size, int size, int mask, int capacity, int threshold, long keyNotFoundValue) {
        this.loadFactor = loadFactor;
        this.table = new Entry[table_size];
        this.size = size;
        this.mask = mask;
        this.capacity = capacity;
        this.threshold = threshold;
        this.keyNotFoundValue = keyNotFoundValue;
    }

    /**
     * Disclaimer: If the value type doesn't implement {@link Object#clone() clone()}, only the reference is copied.
     * Note: Due to private fields we cannot implement a copy constructor, sorry.
     * 
     * @param source the primitive hash map to copy
     */
    @Override
    public Object clone() {
        IntLongHashMap n = new IntLongHashMap(loadFactor, table.length, size, mask, capacity, threshold, keyNotFoundValue);
        for (int i = table.length - 1; i >= 0; i--) {
            final ArrayList<Entry> entries = new ArrayList();
            Entry se = table[i];
            while (null != se) {
                entries.add(se);
                se = se.next;
            }
            Entry de_next = null;
            for (int j = entries.size() - 1; j >= 0; j--) {
                se = entries.get(j);
                if (isPrimitive) {
                    de_next = new Entry(se.key, se.value, de_next);
                } else {
                    final Object v = ReflectionUtil.callMethod(se.value, getCloneMethod(se.value));
                    de_next = (Entry) ReflectionUtil.createInstance(entryConstructor, se.key, v, de_next);
                }
            }
            n.table[i] = de_next;
        }
        return n;
    }

    public boolean containsValue(long value) {
        Entry[] t = this.table;
        for (int i = t.length; i-- > 0; ) {
            for (Entry e = t[i]; e != null; e = e.next) {
                if (isPrimitive) {
                    if (e.value == value) {
                        return true;
                    }
                } else {
                    final Boolean b = (Boolean) ReflectionUtil.callMethod(value, equalsMethod, e.value);
                    if (b.booleanValue()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean containsKey(int key) {
        Entry[] t = this.table;
        int index = (int) (key & mask);
        for (Entry e = t[index]; e != null; e = e.next) {
            if (e.key == key) {
                return true;
            }
        }
        return false;
    }

    public long get(int key) {
        Entry[] t = this.table;
        int index = (int) (key & mask);
        for (Entry e = t[index]; e != null; e = e.next) {
            if (e.key == key) {
                return e.value;
            }
        }
        return keyNotFoundValue;
    }

    public long put(int key, long value) {
        final Entry[] t = this.table;
        int index = (int) (key & mask);
        for (Entry e = t[index]; e != null; e = e.next) {
            if (e.key != key) {
                continue;
            }
            long oldValue = e.value;
            e.value = value;
            return oldValue;
        }
        t[index] = new Entry(key, value, t[index]);
        if (size++ >= threshold) {
            int newCapacity = 2 * capacity;
            final Entry[] newTable = new Entry[newCapacity];
            int bucketmask = newCapacity - 1;
            for (int j = 0; j < t.length; j++) {
                Entry e = t[j];
                if (e != null) {
                    t[j] = null;
                    do {
                        Entry next = e.next;
                        index = (int) (e.key & bucketmask);
                        e.next = newTable[index];
                        newTable[index] = e;
                        e = next;
                    } while (e != null);
                }
            }
            table = newTable;
            capacity = newCapacity;
            threshold = (int) (newCapacity * loadFactor);
            mask = capacity - 1;
        }
        return keyNotFoundValue;
    }

    public void putAll(IntLongHashMap source) {
        Iterator itr = source.iterator();
        while (itr.hasNext()) {
            Entry e = (Entry) itr.next();
            put(e.key, e.value);
        }
    }

    public long remove(int key) {
        Entry[] t = this.table;
        int index = (int) (key & mask);
        Entry prev = t[index];
        Entry e = prev;
        while (e != null) {
            Entry next = e.next;
            if (e.key == key) {
                size--;
                if (prev == e) {
                    t[index] = next;
                } else {
                    prev.next = next;
                }
                return e.value;
            }
            prev = e;
            e = next;
        }
        return keyNotFoundValue;
    }

    /**
     * Returns the current number of key-value mappings in this map.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the current capacity (buckets) in this map.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Clears the entire map. The size is 0 after this operation.
     */
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    /**
     * Returns a new {@link Iterator}.
     * Note: this Iterator does not yet support removal of elements.
     */
    @Override
    public Iterator<Entry> iterator() {
        return new EntryIterator(table);
    }

    /**
     * Sets the new key not found value.
     * For primitive types (int, long) the default is -1,
     * for Object types, the default is null.
     *
     * @return the previous key not found value
     * @see #get
     * @see #put 
     */
    public long setKeyNotFoundValue(long newKeyNotFoundValue) {
        long t = keyNotFoundValue;
        keyNotFoundValue = newKeyNotFoundValue;
        return t;
    }

    /**
     * Returns the value which is returned if no value has been found for the specified key.
     * @see #get
     * @see #put
     */
    public long getKeyNotFoundValue() {
        return keyNotFoundValue;
    }

    @Override
    public String toString() {
        String str = "{";
        Iterator itr = iterator();
        while (itr.hasNext()) {
            str += itr.next();
            if (itr.hasNext()) {
                str += ", ";
            }
        }
        str += "}";
        return str;
    }

    private static final class EntryIterator implements Iterator<Entry> {

        private final Entry[] entries;

        private int index;

        private Entry next;

        private EntryIterator(Entry[] entries) {
            this.entries = entries;
            next();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Entry next() {
            Entry current = next;
            if (current != null && current.next != null) {
                next = current.next;
            } else {
                while (index < entries.length) {
                    Entry e = entries[index++];
                    if (e != null) {
                        next = e;
                        return current;
                    }
                }
                next = null;
            }
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * An entry mapping a key to a value.
     */
    public static final class Entry {

        public final int key;

        public long value;

        private Entry next;

        Entry(int k, long v, Entry n) {
            key = k;
            value = v;
            next = n;
        }

        /**
         * Returns the key of this entry.
         */
        public int getKey() {
            return key;
        }

        /**
         * Returns the value of this entry.
         */
        public long getValue() {
            return value;
        }

        /**
         * Sets the value for this entry.
         */
        public void setValue(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + key + ":" + value + "]";
        }
    }

    private static Method getCloneMethod(Object obj) {
        final Class clazz = obj.getClass();
        Method m = null;
        try {
            m = clazz.getDeclaredMethod("clone");
        } catch (NoSuchMethodException ex) {
            throw new JogampRuntimeException("Class " + clazz + " doesn't support clone()", ex);
        }
        return m;
    }
}
