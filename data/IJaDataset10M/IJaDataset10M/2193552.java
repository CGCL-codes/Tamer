package com.bluemarsh.jswat.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Class SkipList implements a skip list that uses int primitives for
 * the element keys. The modification methods defined by <code>Set</code>
 * are not supported. Instead, methods with keys are required. Note that
 * the keys have a many to one relationship with the elements (that is,
 * a range of key values will map to a single element).
 *
 * <p><b>Note that this implementation is not synchronized.</b> If multiple
 * threads access a set concurrently, and at least one of the threads modifies
 * the set, it <i>must</i> be synchronized externally. This is typically
 * accomplished by synchronizing on some object that naturally encapsulates
 * the set. If no such object exists, the set should be "wrapped" using the
 * <code>Collections.synchronizedSet</code> method. This is best done at
 * creation time, to prevent accidental unsynchronized access to the set:</p>
 *<pre>
 *     Set s = Collections.synchronizedSet(new SkipList(...));
 *</pre>
 *
 * <p>The Iterators returned by this class's <tt>iterator</tt> method are
 * <i>fail-fast</i>: if the set is modified at any time after the iterator is
 * created, in any way except through the iterator's own <tt>remove</tt>
 * method, the iterator will throw a <tt>ConcurrentModificationException</tt>.
 * Thus, in the face of concurrent modification, the iterator fails quickly
 * and cleanly, rather than risking arbitrary, non-deterministic behavior at
 * an undetermined time in the future.</p>
 *
 * @author  Nathan Fiedler
 */
public class SkipList implements Set {

    /** Optimal probability of most skip lists. */
    public static final double OPTIMAL_P = 0.25;

    /** Maximum level of any SkipList instance. */
    private final int maxLevel;

    /** Probability value for this skip list. */
    private final double probability;

    /** Tail of this skip list. */
    private final Element tailElement;

    /** The level of this skip list. */
    private int listLevel;

    /** Header is an element with no data. */
    private Element listHeader;

    /** Number of elements in this skip list. */
    private int elementCount;

    /** Increments each time the list changes. */
    private int modCount;

    /**
     * Constructs an empty SkipList using the default probability
     * and maximum element size.
     */
    public SkipList() {
        this(OPTIMAL_P, (int) Math.ceil(Math.log(Integer.MAX_VALUE) / Math.log(1 / OPTIMAL_P)) - 1);
    }

    /**
     * Constructs an empty SkipList object using the given probability
     * and maximum level.
     *
     * @param  probability  skip list probability value.
     * @param  maxLevel     maximum skip list level.
     */
    public SkipList(double probability, int maxLevel) {
        this.probability = probability;
        this.maxLevel = maxLevel;
        listHeader = new Element(maxLevel, Integer.MIN_VALUE, null);
        tailElement = new Element(0, Integer.MAX_VALUE, null);
        clear();
    }

    /**
     * Appends the specified element to the end of this list (optional
     * operation).
     *
     * @param  o  element to be appended to this list.
     * @return  <tt>true</tt> if this collection changed as a result of
     *          the call.
     * @see #insert
     */
    public boolean add(Object o) {
        insert(o.hashCode(), o);
        return true;
    }

    /**
     * Appends all of the elements in the specified collection to the end
     * of this list, in the order that they are returned by the specified
     * collection's iterator (optional operation). The behavior of this
     * operation is unspecified if the specified collection is modified
     * while the operation is in progress. (Note that this will occur if
     * the specified collection is this list, and it's nonempty.)
     *
     * @param  c  collection whose elements are to be added to this list.
     * @return  <tt>true</tt> if this list changed as a result of the call.
     * @see #insert
     * @see #add(Object)
     */
    public boolean addAll(Collection c) {
        Iterator iter = c.iterator();
        boolean added = false;
        while (iter.hasNext()) {
            added |= add(iter.next());
        }
        return added;
    }

    /**
     * Removes all of the elements from this list (optional operation).
     * This list will be empty after this call returns (unless it throws
     * an exception).
     */
    public void clear() {
        listLevel = 1;
        for (int i = listHeader.size() - 1; i >= 0; i--) {
            listHeader.forward(i, tailElement);
        }
        elementCount = 0;
        modCount++;
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param  o  element whose presence in this list is to be tested.
     * @return  <tt>true</tt> if this list contains the specified element.
     */
    public boolean contains(Object o) {
        boolean found = false;
        Element x = listHeader;
        while (!found && x.forward(0) != tailElement) {
            if (x.getObject() == o || x.getObject() != null && x.getObject().equals(o)) {
                found = true;
                break;
            }
            x = x.forward(0);
        }
        return found;
    }

    /**
     * Returns <tt>true</tt> if this list contains all of the elements of
     * the specified collection.
     *
     * @param  c  collection to be checked for containment in this list.
     * @return  <tt>true</tt> if this list contains all of the elements of
     *          the specified collection.
     * @see #contains(Object)
     */
    public boolean containsAll(Collection c) {
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            if (!contains(iter.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the element with the given key from the list.
     *
     * @param  searchKey  key of element to remove.
     * @return  <code>true</code> if element was found and removed.
     */
    public boolean delete(int searchKey) {
        Element[] update = new Element[maxLevel];
        Element x = listHeader;
        for (int i = listLevel - 1; i >= 0; i--) {
            while (x.forward(i).getKey() < searchKey) {
                x = x.forward(i);
            }
            update[i] = x;
        }
        x = x.forward(0);
        if (x.getKey() == searchKey) {
            for (int i = 0; i < listLevel; i++) {
                if (update[i].forward(i) != x) {
                    break;
                }
                update[i].forward(i, x.forward(i));
            }
            while (listLevel > 0 && listHeader.forward(listLevel) == tailElement) {
                listLevel--;
            }
            elementCount--;
            modCount++;
            return true;
        }
        return false;
    }

    /**
     * Compares the specified object with this list for equality.
     * Returns <tt>true</tt> if and only if the specified object is
     * also a list, both lists have the same size, and all corresponding
     * pairs of elements in the two lists are <i>equal</i>. (Two elements
     * <tt>e1</tt> and <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ?
     * e2==null : e1.equals(e2))</tt>.) In other words, two lists are
     * defined to be equal if they contain the same elements in the same
     * order. This definition ensures that the equals method works
     * properly across different implementations of the <tt>List</tt>
     * interface.
     *
     * @param  o  the object to be compared for equality with this list.
     * @return  <tt>true</tt> if the specified object is equal to this list.
     */
    public boolean equals(Object o) {
        return o == this;
    }

    /**
     * Returns the hash code value for this list. The hash code of a list
     * is defined to be the result of the following calculation:
     * <pre>
     *  hashCode = 1;
     *  Iterator i = list.iterator();
     *  while (i.hasNext()) {
     *      Object obj = i.next();
     *      hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
     *  }
     * </pre>
     * This ensures that <tt>list1.equals(list2)</tt> implies that
     * <tt>list1.hashCode()==list2.hashCode()</tt> for any two lists,
     * <tt>list1</tt> and <tt>list2</tt>, as required by the general
     * contract of <tt>Object.hashCode</tt>.
     *
     * @return  the hash code value for this list.
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    public int hashCode() {
        return System.identityHashCode(this);
    }

    /**
     * Inserts the element using the given search key. If an element
     * with the same key already exists in the skip lists, its value
     * will be replaced with <code>newValue</code>.
     *
     * @param  searchKey  key for element.
     * @param  newValue   new element to insert.
     */
    public void insert(int searchKey, Object newValue) {
        Element[] update = new Element[maxLevel];
        Element x = listHeader;
        for (int i = listLevel - 1; i >= 0; i--) {
            while (x.forward(i).getKey() < searchKey) {
                x = x.forward(i);
            }
            update[i] = x;
        }
        x = x.forward(0);
        if (x.getKey() == searchKey) {
            x.setObject(newValue);
        } else {
            int lvl = randomLevel();
            if (lvl > listLevel) {
                for (int i = listLevel; i <= lvl; i++) {
                    update[i] = listHeader;
                }
                listLevel = lvl;
            }
            x = new Element(lvl, searchKey, newValue);
            for (int i = 0; i < lvl; i++) {
                x.forward(i, update[i].forward(i));
                update[i].forward(i, x);
            }
        }
        elementCount++;
        modCount++;
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements.
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence.
     */
    public Iterator iterator() {
        return new Iter();
    }

    /**
     * Return a random level.
     *
     * @return  level selected randomly.
     */
    protected int randomLevel() {
        int lvl = 1;
        while (lvl < maxLevel && Math.random() < probability) {
            lvl++;
        }
        return lvl;
    }

    /**
     * Removes the first occurrence in this list of the specified element.
     * If this list does not contain the element, it is unchanged. More
     * formally, removes the element with the lowest index i such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if such an
     * element exists).
     *
     * @param  o  element to be removed from this list, if present.
     * @return <tt>true</tt> if this list contained the specified element.
     */
    public boolean remove(Object o) {
        return delete(o.hashCode());
    }

    /**
     * Removes from this list all the elements that are contained in the
     * specified collection (optional operation).
     *
     * @param  c  collection that defines which elements will be removed
     *            from this list.
     * @return  <tt>true</tt> if this list changed as a result of the call.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection c) {
        Iterator iter = c.iterator();
        boolean removed = false;
        while (iter.hasNext()) {
            removed |= remove(iter.next());
        }
        return removed;
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection (optional operation). In other words, removes
     * from this list all the elements that are not contained in the
     * specified collection.
     *
     * @param  c  collection that defines which elements this set will retain.
     * @return  <tt>true</tt> if this list changed as a result of the call.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Searches for the element with the given key.
     *
     * @param  searchKey  key to look for.
     * @return  element if found, null if not found. Note that you may
     *          not want to store nulls in this list as it would then
     *          be difficult to know the difference.
     */
    public Object search(int searchKey) {
        Element x = listHeader;
        for (int i = listLevel - 1; i >= 0; i--) {
            while (x.forward(i).getKey() < searchKey) {
                x = x.forward(i);
            }
        }
        x = x.forward(0);
        if (x.getKey() == searchKey) {
            return x.getObject();
        }
        return null;
    }

    /**
     * Searches for the element with a key that is the least smaller
     * value of the given key.
     *
     * @param  searchKey  key to look for.
     * @return  element if found, null if not found.
     */
    public Object searchLeastSmaller(int searchKey) {
        Element x = listHeader;
        for (int i = listLevel - 1; i >= 0; i--) {
            while (x.forward(i).getKey() < searchKey) {
                x = x.forward(i);
            }
        }
        if (x.forward(0).getKey() == searchKey) {
            return x.forward(0).getObject();
        } else {
            return x.getObject();
        }
    }

    /**
     * Searches for the element just after the one found using the
     * given key (where the key value may be the least smaller of
     * the given key).
     *
     * @param  searchKey  key to look for.
     * @return  next element if found, null if not found.
     */
    public Object searchNextLarger(int searchKey) {
        Element x = listHeader;
        for (int i = listLevel - 1; i >= 0; i--) {
            while (x.forward(i).getKey() < searchKey) {
                x = x.forward(i);
            }
        }
        Element t = null;
        if (x.forward(0).getKey() == searchKey) {
            t = x.forward(0);
        } else {
            t = x;
        }
        if (t.forward(0) == tailElement) {
            return null;
        } else {
            return t.forward(0).getObject();
        }
    }

    /**
     * Returns the number of elements in this list. If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list.
     */
    public int size() {
        return elementCount;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence. Obeys the general contract of the
     * <tt>Collection.toArray()</tt> method.
     *
     * @return an array containing all of the elements in this list in
     *         proper sequence.
     * @see Arrays#asList(Object[])
     */
    public Object[] toArray() {
        Object[] result = new Object[elementCount];
        Element x = listHeader;
        for (int i = 0; i < elementCount; i++) {
            result[i] = x.forward(0).getObject();
            x = x.forward(0);
        }
        return result;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence; the runtime type of the returned array is that
     * of the specified array. Obeys the general contract of the
     * <tt>Collection.toArray(Object[])</tt> method.
     *
     * @param  a  the array into which the elements of this list are to
     *            be stored, if it is big enough; otherwise, a new array
     *            of the same runtime type is allocated for this purpose.
     * @return  an array containing the elements of this list.
     */
    public Object[] toArray(Object[] a) {
        Object[] result = toArray();
        int size = size();
        if (a.length < size) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int ii = 0; ii < size; ii++) {
            a[ii] = result[ii];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Class Element represents an element of a skip list.
     */
    protected class Element {

        /** Key of element. */
        private int key;

        /** Value of element. */
        private Object value;

        /** List of forward pointers. */
        private Element[] forward;

        /**
         * Constructs an Element for the given key and value.
         *
         * @param  level  level for this node (number of forward pointers).
         * @param  key    key for element.
         * @param  value  value for element.
         */
        public Element(int level, int key, Object value) {
            this.key = key;
            this.value = value;
            forward = new Element[level];
        }

        /**
         * Return the element <code>n</code> places forward.
         *
         * @param  n  index of desired element.
         * @return  the desired element.
         */
        public final Element forward(int n) {
            return forward[n];
        }

        /**
         * Sets the element <code>n</code> places forward to a new element.
         *
         * @param  n  index of desired location.
         * @param  o  new forward element.
         */
        public final void forward(int n, Element o) {
            forward[n] = o;
        }

        /**
         * Returns this element's key value.
         *
         * @return  key value.
         */
        public final int getKey() {
            return key;
        }

        /**
         * Returns this element's object value.
         *
         * @return  object value.
         */
        public final Object getObject() {
            return value;
        }

        /**
         * Sets this element's object value to a new value.
         *
         * @param  o  new object value.
         */
        public final void setObject(Object o) {
            value = o;
        }

        /**
         * Returns the size of this element's forward list.
         *
         * @return  size of forward list.
         */
        public final int size() {
            return forward.length;
        }
    }

    /**
     * An iterator over a skip list.
     */
    protected class Iter implements Iterator {

        /** Index into the skip list. */
        private int index;

        /** The modCount of the list at the time we were instantiated. */
        private int modCount;

        /** Current element being examined. */
        private Element elem;

        /**
         * Constructs a skip list iterator.
         */
        public Iter() {
            this.modCount = SkipList.this.modCount;
            elem = listHeader;
        }

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In
         * other words, returns <tt>true</tt> if <tt>next</tt> would return
         * an element rather than throwing an exception.)
         *
         * @return  <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext() {
            if (this.modCount != SkipList.this.modCount) {
                throw new ConcurrentModificationException();
            }
            return elem.forward(0) != tailElement;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return  the next element in the iteration.
         */
        public Object next() {
            if (this.modCount != SkipList.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (hasNext()) {
                elem = elem.forward(0);
                return elem.getObject();
            } else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Removes from the underlying collection the last element returned
         * by the iterator (optional operation).
         *
         * <p>Throws <code>UnsupportedOperationException</code>.</p>
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
