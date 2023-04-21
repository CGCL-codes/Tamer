package moio.util;

import java.util.ConcurrentModificationException;

/**
 * A class which implements a hashtable data structure.
 * <p>
 *
 * This implementation of Hashtable uses a hash-bucket approach. That is:
 * linear probing and rehashing is avoided; instead, each hashed value maps
 * to a simple linked-list which, in the best case, only has one node.
 * Assuming a large enough table, low enough load factor, and / or well
 * implemented hashCode() methods, Hashtable should provide O(1)
 * insertion, deletion, and searching of keys.  Hashtable is O(n) in
 * the worst case for all of these (if all keys hash to the same bucket).
 * <p>
 *
 * This is a JDK-1.2 compliant implementation of Hashtable.  As such, it
 * belongs, partially, to the Collections framework (in that it implements
 * Map).  For backwards compatibility, it inherits from the obsolete and
 * utterly useless Dictionary class.
 * <p>
 *
 * Being a hybrid of old and new, Hashtable has methods which provide redundant
 * capability, but with subtle and even crucial differences.
 * For example, one can iterate over various aspects of a Hashtable with
 * either an Iterator (which is the JDK-1.2 way of doing things) or with an
 * Enumeration.  The latter can end up in an undefined state if the Hashtable
 * changes while the Enumeration is open.
 * <p>
 *
 * Unlike HashMap, Hashtable does not accept `null' as a key value. Also,
 * all accesses are synchronized: in a single thread environment, this is
 * expensive, but in a multi-thread environment, this saves you the effort
 * of extra synchronization. However, the old-style enumerators are not
 * synchronized, because they can lead to unspecified behavior even if
 * they were synchronized. You have been warned.
 * <p>
 *
 * The iterators are <i>fail-fast</i>, meaning that any structural
 * modification, except for <code>remove()</code> called on the iterator
 * itself, cause the iterator to throw a
 * <code>ConcurrentModificationException</code> rather than exhibit
 * non-deterministic behavior.
 *
 * @author Jon Zeppieri
 * @author Warren Levy
 * @author Bryce McKinlay
 * @author Eric Blake (ebb9@email.byu.edu)
 * @see HashMap
 * @see TreeMap
 * @see IdentityHashMap
 * @see LinkedHashMap
 * @since 1.0
 * @status updated to 1.4
 */
public class Hashtable extends Dictionary implements Map, Cloneable, Serializable {

    /** Default number of buckets. This is the value the JDK 1.3 uses. Some
   * early documentation specified this value as 101. That is incorrect.
   */
    private static final int DEFAULT_CAPACITY = 11;

    static final int KEYS = 0, VALUES = 1, ENTRIES = 2;

    /**
   * The default load factor; this is explicitly specified by the spec.
   */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
   * Compatible with JDK 1.0+.
   */
    private static final long serialVersionUID = 1421746759512286392L;

    /**
   * The rounded product of the capacity and the load factor; when the number
   * of elements exceeds the threshold, the Hashtable calls
   * <code>rehash()</code>.
   * @serial
   */
    private int threshold;

    /**
   * Load factor of this Hashtable:  used in computing the threshold.
   * @serial
   */
    private final float loadFactor;

    transient HashEntry[] buckets;

    transient int modCount;

    transient int size;

    /**
   * The cache for {@link #keySet()}.
   */
    private transient Set keys;

    /**
   * The cache for {@link #values()}.
   */
    private transient Collection values;

    /**
   * The cache for {@link #entrySet()}.
   */
    private transient Set entries;

    /**
   * Class to represent an entry in the hash table. Holds a single key-value
   * pair. A Hashtable Entry is identical to a HashMap Entry, except that
   * `null' is not allowed for keys and values.
   */
    private static final class HashEntry extends AbstractMap.BasicMapEntry {

        /** The next entry in the linked list. */
        HashEntry next;

        /**
     * Simple constructor.
     * @param key the key, already guaranteed non-null
     * @param value the value, already guaranteed non-null
     */
        HashEntry(Object key, Object value) {
            super(key, value);
        }

        /**
     * Resets the value.
     * @param newVal the new value
     * @return the prior value
     * @throws NullPointerException if <code>newVal</code> is null
     */
        public Object setValue(Object newVal) {
            if (newVal == null) throw new NullPointerException();
            return super.setValue(newVal);
        }
    }

    /**
   * Construct a new Hashtable with the default capacity (11) and the default
   * load factor (0.75).
   */
    public Hashtable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
   * Construct a new Hashtable from the given Map, with initial capacity
   * the greater of the size of <code>m</code> or the default of 11.
   * <p>
   *
   * Every element in Map m will be put into this new Hashtable.
   *
   * @param m a Map whose key / value pairs will be put into
   *          the new Hashtable.  <b>NOTE: key / value pairs
   *          are not cloned in this constructor.</b>
   * @throws NullPointerException if m is null, or if m contains a mapping
   *         to or from `null'.
   * @since 1.2
   */
    public Hashtable(Map m) {
        this(Math.max(m.size() * 2, DEFAULT_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAll(m);
    }

    /**
   * Construct a new Hashtable with a specific inital capacity and
   * default load factor of 0.75.
   *
   * @param initialCapacity the initial capacity of this Hashtable (&gt;= 0)
   * @throws IllegalArgumentException if (initialCapacity &lt; 0)
   */
    public Hashtable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
   * Construct a new Hashtable with a specific initial capacity and
   * load factor.
   *
   * @param initialCapacity the initial capacity (&gt;= 0)
   * @param loadFactor the load factor (&gt; 0, not NaN)
   * @throws IllegalArgumentException if (initialCapacity &lt; 0) ||
   *                                     ! (loadFactor &gt; 0.0)
   */
    public Hashtable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        if (!(loadFactor > 0)) throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        if (initialCapacity == 0) initialCapacity = 1;
        buckets = new HashEntry[initialCapacity];
        this.loadFactor = loadFactor;
        threshold = (int) (initialCapacity * loadFactor);
    }

    /**
   * Returns the number of key-value mappings currently in this hashtable.
   * @return the size
   */
    public synchronized int size() {
        return size;
    }

    /**
   * Returns true if there are no key-value mappings currently in this table.
   * @return <code>size() == 0</code>
   */
    public synchronized boolean isEmpty() {
        return size == 0;
    }

    /**
   * Return an enumeration of the keys of this table. There's no point
   * in synchronizing this, as you have already been warned that the
   * enumeration is not specified to be thread-safe.
   *
   * @return the keys
   * @see #elements()
   * @see #keySet()
   */
    public Enumeration keys() {
        return new Enumerator(KEYS);
    }

    /**
   * Return an enumeration of the values of this table. There's no point
   * in synchronizing this, as you have already been warned that the
   * enumeration is not specified to be thread-safe.
   *
   * @return the values
   * @see #keys()
   * @see #values()
   */
    public Enumeration elements() {
        return new Enumerator(VALUES);
    }

    /**
   * Returns true if this Hashtable contains a value <code>o</code>,
   * such that <code>o.equals(value)</code>.  This is the same as
   * <code>containsValue()</code>, and is O(n).
   * <p>
   *
   * @param value the value to search for in this Hashtable
   * @return true if at least one key maps to the value
   * @throws NullPointerException if <code>value</code> is null
   * @see #containsValue(Object)
   * @see #containsKey(Object)
   */
    public synchronized boolean contains(Object value) {
        for (int i = buckets.length - 1; i >= 0; i--) {
            HashEntry e = buckets[i];
            while (e != null) {
                if (value.equals(e.value)) return true;
                e = e.next;
            }
        }
        if (value == null) throw new NullPointerException();
        return false;
    }

    /**
   * Returns true if this Hashtable contains a value <code>o</code>, such that
   * <code>o.equals(value)</code>. This is the new API for the old
   * <code>contains()</code>.
   *
   * @param value the value to search for in this Hashtable
   * @return true if at least one key maps to the value
   * @see #contains(Object)
   * @see #containsKey(Object)
   * @throws NullPointerException if <code>value</code> is null
   * @since 1.2
   */
    public boolean containsValue(Object value) {
        return contains(value);
    }

    /**
   * Returns true if the supplied object <code>equals()</code> a key
   * in this Hashtable.
   *
   * @param key the key to search for in this Hashtable
   * @return true if the key is in the table
   * @throws NullPointerException if key is null
   * @see #containsValue(Object)
   */
    public synchronized boolean containsKey(Object key) {
        int idx = hash(key);
        HashEntry e = buckets[idx];
        while (e != null) {
            if (key.equals(e.key)) return true;
            e = e.next;
        }
        return false;
    }

    /**
   * Return the value in this Hashtable associated with the supplied key,
   * or <code>null</code> if the key maps to nothing.
   *
   * @param key the key for which to fetch an associated value
   * @return what the key maps to, if present
   * @throws NullPointerException if key is null
   * @see #put(Object, Object)
   * @see #containsKey(Object)
   */
    public synchronized Object get(Object key) {
        int idx = hash(key);
        HashEntry e = buckets[idx];
        while (e != null) {
            if (key.equals(e.key)) return e.value;
            e = e.next;
        }
        return null;
    }

    /**
   * Puts the supplied value into the Map, mapped by the supplied key.
   * Neither parameter may be null.  The value may be retrieved by any
   * object which <code>equals()</code> this key.
   *
   * @param key the key used to locate the value
   * @param value the value to be stored in the table
   * @return the prior mapping of the key, or null if there was none
   * @throws NullPointerException if key or value is null
   * @see #get(Object)
   * @see Object#equals(Object)
   */
    public synchronized Object put(Object key, Object value) {
        int idx = hash(key);
        HashEntry e = buckets[idx];
        if (value == null) throw new NullPointerException();
        while (e != null) {
            if (key.equals(e.key)) {
                Object r = e.value;
                e.value = value;
                return r;
            } else {
                e = e.next;
            }
        }
        modCount++;
        if (++size > threshold) {
            rehash();
            idx = hash(key);
        }
        e = new HashEntry(key, value);
        e.next = buckets[idx];
        buckets[idx] = e;
        return null;
    }

    /**
   * Removes from the table and returns the value which is mapped by the
   * supplied key. If the key maps to nothing, then the table remains
   * unchanged, and <code>null</code> is returned.
   *
   * @param key the key used to locate the value to remove
   * @return whatever the key mapped to, if present
   */
    public synchronized Object remove(Object key) {
        int idx = hash(key);
        HashEntry e = buckets[idx];
        HashEntry last = null;
        while (e != null) {
            if (key.equals(e.key)) {
                modCount++;
                if (last == null) buckets[idx] = e.next; else last.next = e.next;
                size--;
                return e.value;
            }
            last = e;
            e = e.next;
        }
        return null;
    }

    /**
   * Copies all elements of the given map into this hashtable.  However, no
   * mapping can contain null as key or value.  If this table already has
   * a mapping for a key, the new mapping replaces the current one.
   *
   * @param m the map to be hashed into this
   * @throws NullPointerException if m is null, or contains null keys or values
   */
    public synchronized void putAll(Map m) {
        Iterator itr = m.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry e = (Map.Entry) itr.next();
            if (e instanceof AbstractMap.BasicMapEntry) {
                AbstractMap.BasicMapEntry entry = (AbstractMap.BasicMapEntry) e;
                put(entry.key, entry.value);
            } else {
                put(e.getKey(), e.getValue());
            }
        }
    }

    /**
   * Clears the hashtable so it has no keys.  This is O(1).
   */
    public synchronized void clear() {
        if (size > 0) {
            modCount++;
            Arrays.fill(buckets, null);
            size = 0;
        }
    }

    /**
   * Converts this Hashtable to a String, surrounded by braces, and with
   * key/value pairs listed with an equals sign between, separated by a
   * comma and space. For example, <code>"{a=1, b=2}"</code>.<p>
   *
   * NOTE: if the <code>toString()</code> method of any key or value
   * throws an exception, this will fail for the same reason.
   *
   * @return the string representation
   */
    public synchronized String toString() {
        Iterator entries = new HashIterator(ENTRIES);
        StringBuffer r = new StringBuffer("{");
        for (int pos = size; pos > 0; pos--) {
            r.append(entries.next());
            if (pos > 1) r.append(", ");
        }
        r.append("}");
        return r.toString();
    }

    /**
   * Returns a "set view" of this Hashtable's keys. The set is backed by
   * the hashtable, so changes in one show up in the other.  The set supports
   * element removal, but not element addition.  The set is properly
   * synchronized on the original hashtable.  Sun has not documented the
   * proper interaction of null with this set, but has inconsistent behavior
   * in the JDK. Therefore, in this implementation, contains, remove,
   * containsAll, retainAll, removeAll, and equals just ignore a null key
   * rather than throwing a {@link NullPointerException}.
   *
   * @return a set view of the keys
   * @see #values()
   * @see #entrySet()
   * @since 1.2
   */
    public Set keySet() {
        if (keys == null) {
            Set r = new AbstractSet() {

                public int size() {
                    return size;
                }

                public Iterator iterator() {
                    return new HashIterator(KEYS);
                }

                public void clear() {
                    Hashtable.this.clear();
                }

                public boolean contains(Object o) {
                    if (o == null) return false;
                    return containsKey(o);
                }

                public boolean remove(Object o) {
                    return Hashtable.this.remove(o) != null;
                }
            };
            keys = new Collections.SynchronizedSet(this, r);
        }
        return keys;
    }

    /**
   * Returns a "collection view" (or "bag view") of this Hashtable's values.
   * The collection is backed by the hashtable, so changes in one show up
   * in the other.  The collection supports element removal, but not element
   * addition.  The collection is properly synchronized on the original
   * hashtable.  Sun has not documented the proper interaction of null with
   * this set, but has inconsistent behavior in the JDK. Therefore, in this
   * implementation, contains, remove, containsAll, retainAll, removeAll, and
   * equals just ignore a null value rather than throwing a
   * {@link NullPointerException}.
   *
   * @return a bag view of the values
   * @see #keySet()
   * @see #entrySet()
   * @since 1.2
   */
    public Collection values() {
        if (values == null) {
            Collection r = new AbstractCollection() {

                public int size() {
                    return size;
                }

                public Iterator iterator() {
                    return new HashIterator(VALUES);
                }

                public void clear() {
                    Hashtable.this.clear();
                }
            };
            values = new Collections.SynchronizedCollection(this, r);
        }
        return values;
    }

    /**
   * Returns a "set view" of this Hashtable's entries. The set is backed by
   * the hashtable, so changes in one show up in the other.  The set supports
   * element removal, but not element addition.  The set is properly
   * synchronized on the original hashtable.  Sun has not documented the
   * proper interaction of null with this set, but has inconsistent behavior
   * in the JDK. Therefore, in this implementation, contains, remove,
   * containsAll, retainAll, removeAll, and equals just ignore a null entry,
   * or an entry with a null key or value, rather than throwing a
   * {@link NullPointerException}. However, calling entry.setValue(null)
   * will fail.
   * <p>
   *
   * Note that the iterators for all three views, from keySet(), entrySet(),
   * and values(), traverse the hashtable in the same sequence.
   *
   * @return a set view of the entries
   * @see #keySet()
   * @see #values()
   * @see Map.Entry
   * @since 1.2
   */
    public Set entrySet() {
        if (entries == null) {
            Set r = new AbstractSet() {

                public int size() {
                    return size;
                }

                public Iterator iterator() {
                    return new HashIterator(ENTRIES);
                }

                public void clear() {
                    Hashtable.this.clear();
                }

                public boolean contains(Object o) {
                    return getEntry(o) != null;
                }

                public boolean remove(Object o) {
                    HashEntry e = getEntry(o);
                    if (e != null) {
                        Hashtable.this.remove(e.key);
                        return true;
                    }
                    return false;
                }
            };
            entries = new Collections.SynchronizedSet(this, r);
        }
        return entries;
    }

    /**
   * Returns true if this Hashtable equals the supplied Object <code>o</code>.
   * As specified by Map, this is:
   * <code>
   * (o instanceof Map) && entrySet().equals(((Map) o).entrySet());
   * </code>
   *
   * @param o the object to compare to
   * @return true if o is an equal map
   * @since 1.2
   */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        return entrySet().equals(((Map) o).entrySet());
    }

    /**
   * Returns the hashCode for this Hashtable.  As specified by Map, this is
   * the sum of the hashCodes of all of its Map.Entry objects
   *
   * @return the sum of the hashcodes of the entries
   * @since 1.2
   */
    public synchronized int hashCode() {
        Iterator itr = new HashIterator(ENTRIES);
        int hashcode = 0;
        for (int pos = size; pos > 0; pos--) hashcode += itr.next().hashCode();
        return hashcode;
    }

    /**
   * Helper method that returns an index in the buckets array for `key'
   * based on its hashCode().
   *
   * @param key the key
   * @return the bucket number
   * @throws NullPointerException if key is null
   */
    private int hash(Object key) {
        int hash = key.hashCode() % buckets.length;
        return hash < 0 ? -hash : hash;
    }

    HashEntry getEntry(Object o) {
        if (!(o instanceof Map.Entry)) return null;
        Object key = ((Map.Entry) o).getKey();
        if (key == null) return null;
        int idx = hash(key);
        HashEntry e = buckets[idx];
        while (e != null) {
            if (o.equals(e)) return e;
            e = e.next;
        }
        return null;
    }

    /**
   * A simplified, more efficient internal implementation of putAll(). clone() 
   * should not call putAll or put, in order to be compatible with the JDK 
   * implementation with respect to subclasses.
   *
   * @param m the map to initialize this from
   */
    void putAllInternal(Map m) {
        Iterator itr = m.entrySet().iterator();
        size = 0;
        while (itr.hasNext()) {
            size++;
            Map.Entry e = (Map.Entry) itr.next();
            Object key = e.getKey();
            int idx = hash(key);
            HashEntry he = new HashEntry(key, e.getValue());
            he.next = buckets[idx];
            buckets[idx] = he;
        }
    }

    /**
   * Increases the size of the Hashtable and rehashes all keys to new array
   * indices; this is called when the addition of a new value would cause
   * size() &gt; threshold. Note that the existing Entry objects are reused in
   * the new hash table.
   * <p>
   *
   * This is not specified, but the new size is twice the current size plus
   * one; this number is not always prime, unfortunately. This implementation
   * is not synchronized, as it is only invoked from synchronized methods.
   */
    protected void rehash() {
        HashEntry[] oldBuckets = buckets;
        int newcapacity = (buckets.length * 2) + 1;
        threshold = (int) (newcapacity * loadFactor);
        buckets = new HashEntry[newcapacity];
        for (int i = oldBuckets.length - 1; i >= 0; i--) {
            HashEntry e = oldBuckets[i];
            while (e != null) {
                int idx = hash(e.key);
                HashEntry dest = buckets[idx];
                if (dest != null) {
                    while (dest.next != null) dest = dest.next;
                    dest.next = e;
                } else {
                    buckets[idx] = e;
                }
                HashEntry next = e.next;
                e.next = null;
                e = next;
            }
        }
    }

    private final class HashIterator implements Iterator {

        /**
     * The type of this Iterator: {@link #KEYS}, {@link #VALUES},
     * or {@link #ENTRIES}.
     */
        final int type;

        /**
     * The number of modifications to the backing Hashtable that we know about.
     */
        int knownMod = modCount;

        /** The number of elements remaining to be returned by next(). */
        int count = size;

        /** Current index in the physical hash table. */
        int idx = buckets.length;

        /** The last Entry returned by a next() call. */
        HashEntry last;

        /**
     * The next entry that should be returned by next(). It is set to something
     * if we're iterating through a bucket that contains multiple linked
     * entries. It is null if next() needs to find a new bucket.
     */
        HashEntry next;

        /**
     * Construct a new HashIterator with the supplied type.
     * @param type {@link #KEYS}, {@link #VALUES}, or {@link #ENTRIES}
     */
        HashIterator(int type) {
            this.type = type;
        }

        /**
     * Returns true if the Iterator has more elements.
     * @return true if there are more elements
     * @throws ConcurrentModificationException if the hashtable was modified
     */
        public boolean hasNext() {
            return count > 0;
        }

        /**
     * Returns the next element in the Iterator's sequential view.
     * @return the next element
     * @throws ConcurrentModificationException if the hashtable was modified
     * @throws NoSuchElementException if there is none
     */
        public Object next() {
            if (count == 0) throw new NoSuchElementException();
            count--;
            HashEntry e = next;
            while (e == null) e = buckets[--idx];
            next = e.next;
            last = e;
            if (type == VALUES) return e.value;
            if (type == KEYS) return e.key;
            return e;
        }

        /**
     * Removes from the backing Hashtable the last element which was fetched
     * with the <code>next()</code> method.
     * @throws ConcurrentModificationException if the hashtable was modified
     * @throws IllegalStateException if called when there is no last element
     */
        public void remove() {
            if (last == null) throw new IllegalStateException();
            Hashtable.this.remove(last.key);
            last = null;
            knownMod++;
        }
    }

    /**
   * Enumeration view of this Hashtable, providing sequential access to its
   * elements; this implementation is parameterized to provide access either
   * to the keys or to the values in the Hashtable.
   *
   * <b>NOTE</b>: Enumeration is not safe if new elements are put in the table
   * as this could cause a rehash and we'd completely lose our place.  Even
   * without a rehash, it is undetermined if a new element added would
   * appear in the enumeration.  The spec says nothing about this, but
   * the "Java Class Libraries" book infers that modifications to the
   * hashtable during enumeration causes indeterminate results.  Don't do it!
   *
   * @author Jon Zeppieri
   */
    private final class Enumerator implements Enumeration {

        /**
     * The type of this Iterator: {@link #KEYS} or {@link #VALUES}.
     */
        final int type;

        /** The number of elements remaining to be returned by next(). */
        int count = size;

        /** Current index in the physical hash table. */
        int idx = buckets.length;

        /**
     * Entry which will be returned by the next nextElement() call. It is
     * set if we are iterating through a bucket with multiple entries, or null
     * if we must look in the next bucket.
     */
        HashEntry next;

        /**
     * Construct the enumeration.
     * @param type either {@link #KEYS} or {@link #VALUES}.
     */
        Enumerator(int type) {
            this.type = type;
        }

        /**
     * Checks whether more elements remain in the enumeration.
     * @return true if nextElement() will not fail.
     */
        public boolean hasMoreElements() {
            return count > 0;
        }

        /**
     * Returns the next element.
     * @return the next element
     * @throws NoSuchElementException if there is none.
     */
        public Object nextElement() {
            if (count == 0) throw new NoSuchElementException("Hashtable Enumerator");
            count--;
            HashEntry e = next;
            while (e == null) e = buckets[--idx];
            next = e.next;
            return type == VALUES ? e.value : e.key;
        }
    }
}
