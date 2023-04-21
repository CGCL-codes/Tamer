package org.mbari.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * <p>ArrayList with sorted contents. Each Object added to the list is added in
 * the order determined by <code>Collections.sort(obj)</code></p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 332 $
 *
 */
public class SortedArrayList extends ArrayList {

    /**
     * 
     */
    private static final long serialVersionUID = -1131961127138657379L;

    /**
	 * @uml.property  name="comparator"
	 */
    private Comparator comparator;

    /**
     * Constructs ...
     *
     */
    public SortedArrayList() {
        super();
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.  The <tt>ArrayList</tt> instance has an initial capacity of
     * 110% the size of the specified collection.
     *
     * @param c the collection whose elements are to be placed into this list.
     */
    public SortedArrayList(Collection c) {
        addAll(c);
    }

    /**
     * Constructs ...
     *
     *
     * @param comparator
     */
    public SortedArrayList(Comparator comparator) {
        setComparator(comparator);
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param   initialCapacity   the initial capacity of the list.
     */
    public SortedArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs ...
     *
     *
     * @param c
     * @param comparator
     */
    public SortedArrayList(Collection c, Comparator comparator) {
        addAll(c);
        setComparator(comparator);
    }

    /**
     * Constructs ...
     *
     *
     * @param initialCapacity
     * @param comparator
     */
    public SortedArrayList(int initialCapacity, Comparator comparator) {
        super(initialCapacity);
        setComparator(comparator);
    }

    /**
     * Inserts the specified element in order into the list.
     *
     *
     * @param obj
     * @return <tt>true</tt> (as per the general contract of Collection.add).
     */
    public boolean add(Object obj) {
        return (addObject(obj) < 0) ? true : false;
    }

    /**
     * @deprecated use <code>addAll(Arrays.asList(items))</code> instead
     * @param items
     * @return
     * @since Apr 29, 2003
     */
    public boolean add(Object[] items) {
        for (int i = 0; i < items.length; i++) {
            addObject(items[i]);
        }
        return true;
    }

    /**
     * Throws UnsupportedOperationException!! Insertinion at a specified index
     * is not allowed.
     *
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param item
     */
    public void add(int index, Object item) {
        throw new UnsupportedOperationException("Specifying index not allowed. " + "List maintains sorted ordering.");
    }

    /**
     * Inserts all of the elements in the specified Collection into the list in the order
     * specified by <code>Collections.binarySearch()</code>. The behavior of this operation is
     * undefined if the specified Collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified Collection is this list, and this
     * list is nonempty.)
     *
     *
     * @param collection
     * @return <tt>true</tt> if this list changed as a result of the call.
     */
    public boolean addAll(Collection collection) {
        removeAll(collection);
        boolean result = super.addAll(collection);
        Collections.sort(this, comparator);
        return result;
    }

    /**
     * Inserts all of the elements in the specified Collection into the list in the order
     * specified by <code>Collections.binarySearch()</code>. The behavior of this operation is
     * undefined if the specified Collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified Collection is this list, and this
     * list is nonempty.)
     *
     *
     *
     * @param index index at which to insert first element
     *          from the specified collection. This argument is ignored.
     * @param collection
     * @return <tt>true</tt> if this list changed as a result of the call.
     */
    public boolean addAll(int index, Collection collection) {
        return addAll(collection);
    }

    /**
     * TBD: Negative return value means the item is already in the list
     * @deprecated Prefered use is to call add(item). If index needs to be
     *             returned use addObject(item)
     *
     * @param item
     *
     * @return
     */
    public int addItem(Object item) {
        return addObject(item);
    }

    /**
     * Add an Object and return it's index. It's prefered that a user use
     * <pre>
     *  List list = new SortedArrayList();
     *  list.add(obj);
     *  int i = list.indexOf(obj);
     * </pre>
     * but if you need to insert an object and keep track of its index a call to
     * addObject() is faster. <font color="ff3333">NOTE: Duplicates of an object
     * will not be inserted. instead the index of the Object already in thelist will
     * be returned. </font>
     *
     * @param obj The object to be inserted.
     * @return index of the search key, if it is contained in the list;
     *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>.  The
     *         <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the list: the index of the first
     *         element greater than the key, or <tt>list.size()</tt>, if all
     *         elements in the list are less than the specified key.  Note
     *         that this guarantees that the return value will be &gt;= 0 if
     *         and only if the key is found.
     * @since Apr 29, 2003
     * @see java.util.Collections#binarySearch
     */
    public int addObject(Object obj) {
        int insertionIndex = Collections.binarySearch(this, obj, comparator);
        if (insertionIndex < 0) {
            super.add((-insertionIndex - 1), obj);
        }
        return insertionIndex;
    }

    /**
	 * @return
	 * @uml.property  name="comparator"
	 */
    public Comparator getComparator() {
        return comparator;
    }

    /**
	 * <p><!-- Method description --></p>
	 * @param  comparator
	 * @uml.property  name="comparator"
	 */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
        Collections.sort(this, comparator);
    }
}
