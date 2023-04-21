package com.wuala.loader2.copied.trove;

import java.util.Arrays;

/**
 * An open addressed hashing implementation for Object types.
 *
 * Created: Sun Nov  4 08:56:06 2001
 *
 * @author Eric D. Friedman
 * @version $Id: TObjectHash.java,v 1.1 2008/09/26 11:39:18 luziusmeisser Exp $
 */
public abstract class TObjectHash<T> extends THash implements TObjectHashingStrategy<T> {

    static final long serialVersionUID = -3461112548087185871L;

    /** the set of Objects */
    protected transient Object[] _set;

    /** the strategy used to hash objects in this collection. */
    protected TObjectHashingStrategy<T> _hashingStrategy;

    protected static final Object REMOVED = new Object(), FREE = new Object();

    /**
     * Creates a new <code>TObjectHash</code> instance with the
     * default capacity and load factor.
     */
    public TObjectHash() {
        super();
        this._hashingStrategy = this;
    }

    /**
     * Creates a new <code>TObjectHash</code> instance with the
     * default capacity and load factor and a custom hashing strategy.
     *
     * @param strategy used to compute hash codes and to compare objects.
     */
    public TObjectHash(TObjectHashingStrategy<T> strategy) {
        super();
        this._hashingStrategy = strategy;
    }

    /**
     * Creates a new <code>TObjectHash</code> instance whose capacity
     * is the next highest prime above <tt>initialCapacity + 1</tt>
     * unless that value is already prime.
     *
     * @param initialCapacity an <code>int</code> value
     */
    public TObjectHash(int initialCapacity) {
        super(initialCapacity);
        this._hashingStrategy = this;
    }

    /**
     * Creates a new <code>TObjectHash</code> instance whose capacity
     * is the next highest prime above <tt>initialCapacity + 1</tt>
     * unless that value is already prime.  Uses the specified custom
     * hashing strategy.
     *
     * @param initialCapacity an <code>int</code> value
     * @param strategy used to compute hash codes and to compare objects.
     */
    public TObjectHash(int initialCapacity, TObjectHashingStrategy<T> strategy) {
        super(initialCapacity);
        this._hashingStrategy = strategy;
    }

    /**
     * Creates a new <code>TObjectHash</code> instance with a prime
     * value at or near the specified capacity and load factor.
     *
     * @param initialCapacity used to find a prime capacity for the table.
     * @param loadFactor used to calculate the threshold over which
     * rehashing takes place.
     */
    public TObjectHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = this;
    }

    /**
     * Creates a new <code>TObjectHash</code> instance with a prime
     * value at or near the specified capacity and load factor.  Uses
     * the specified custom hashing strategy.
     *
     * @param initialCapacity used to find a prime capacity for the table.
     * @param loadFactor used to calculate the threshold over which
     * rehashing takes place.
     * @param strategy used to compute hash codes and to compare objects.
     */
    public TObjectHash(int initialCapacity, float loadFactor, TObjectHashingStrategy<T> strategy) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = strategy;
    }

    /**
     * @return a shallow clone of this collection
     */
    public TObjectHash<T> clone() {
        TObjectHash<T> h = (TObjectHash<T>) super.clone();
        h._set = (Object[]) this._set.clone();
        return h;
    }

    protected int capacity() {
        return _set.length;
    }

    protected void removeAt(int index) {
        _set[index] = REMOVED;
        super.removeAt(index);
    }

    /**
     * initializes the Object set of this hash table.
     *
     * @param initialCapacity an <code>int</code> value
     * @return an <code>int</code> value
     */
    protected int setUp(int initialCapacity) {
        int capacity;
        capacity = super.setUp(initialCapacity);
        _set = new Object[capacity];
        Arrays.fill(_set, FREE);
        return capacity;
    }

    /**
     * Searches the set for <tt>obj</tt>
     *
     * @param obj an <code>Object</code> value
     * @return a <code>boolean</code> value
     */
    public boolean contains(Object obj) {
        return index((T) obj) >= 0;
    }

    /**
     * Locates the index of <tt>obj</tt>.
     *
     * @param obj an <code>Object</code> value
     * @return the index of <tt>obj</tt> or -1 if it isn't in the set.
     */
    protected int index(T obj) {
        final TObjectHashingStrategy<T> hashing_strategy = _hashingStrategy;
        final Object[] set = _set;
        final int length = set.length;
        final int hash = hashing_strategy.computeHashCode(obj) & 0x7fffffff;
        int index = hash % length;
        Object cur = set[index];
        if (cur == FREE) return -1;
        if (cur == REMOVED || !hashing_strategy.equals((T) cur, obj)) {
            final int probe = 1 + (hash % (length - 2));
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
                cur = set[index];
            } while (cur != FREE && (cur == REMOVED || !_hashingStrategy.equals((T) cur, obj)));
        }
        return cur == FREE ? -1 : index;
    }

    /**
     * Locates the index at which <tt>obj</tt> can be inserted.  if
     * there is already a value equal()ing <tt>obj</tt> in the set,
     * returns that value's index as <tt>-index - 1</tt>.
     *
     * @param obj an <code>Object</code> value
     * @return the index of a FREE slot at which obj can be inserted
     * or, if obj is already stored in the hash, the negative value of
     * that index, minus 1: -index -1.
     */
    protected int insertionIndex(T obj) {
        final TObjectHashingStrategy<T> hashing_strategy = _hashingStrategy;
        final Object[] set = _set;
        final int length = set.length;
        final int hash = hashing_strategy.computeHashCode(obj) & 0x7fffffff;
        int index = hash % length;
        Object cur = set[index];
        if (cur == FREE) {
            return index;
        } else if (cur != REMOVED && hashing_strategy.equals((T) cur, obj)) {
            return -index - 1;
        } else {
            final int probe = 1 + (hash % (length - 2));
            if (cur != REMOVED) {
                do {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                    cur = set[index];
                } while (cur != FREE && cur != REMOVED && !hashing_strategy.equals((T) cur, obj));
            }
            if (cur == REMOVED) {
                int firstRemoved = index;
                while (cur != FREE && (cur == REMOVED || !hashing_strategy.equals((T) cur, obj))) {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                    cur = set[index];
                }
                return (cur != FREE) ? -index - 1 : firstRemoved;
            }
            return (cur != FREE) ? -index - 1 : index;
        }
    }

    /**
     * This is the default implementation of TObjectHashingStrategy:
     * it delegates hashing to the Object's hashCode method.
     *
     * @param o for which the hashcode is to be computed
     * @return the hashCode
     * @see Object#hashCode()
     */
    public final int computeHashCode(T o) {
        return o == null ? 0 : o.hashCode();
    }

    /**
     * This is the default implementation of TObjectHashingStrategy:
     * it delegates equality comparisons to the first parameter's
     * equals() method.
     *
     * @param o1 an <code>Object</code> value
     * @param o2 an <code>Object</code> value
     * @return true if the objects are equal
     * @see Object#equals(Object)
     */
    public final boolean equals(T o1, T o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    /**
     * Convenience methods for subclasses to use in throwing exceptions about
     * badly behaved user objects employed as keys.  We have to throw an
     * IllegalArgumentException with a rather verbose message telling the
     * user that they need to fix their object implementation to conform
     * to the general contract for java.lang.Object.
     *
     * @param o1 the first of the equal elements with unequal hash codes.
     * @param o2 the second of the equal elements with unequal hash codes.
     * @exception IllegalArgumentException the whole point of this method.
     */
    protected final void throwObjectContractViolation(Object o1, Object o2) throws IllegalArgumentException {
        throw new IllegalArgumentException("Equal objects must have equal hashcodes. " + "During rehashing, Trove discovered that " + "the following two objects claim to be " + "equal (as in java.lang.Object.equals()) " + "but their hashCodes (or those calculated by " + "your TObjectHashingStrategy) are not equal." + "This violates the general contract of " + "java.lang.Object.hashCode().  See bullet point two " + "in that method's documentation. " + "object #1 =" + o1 + "; object #2 =" + o2);
    }
}
