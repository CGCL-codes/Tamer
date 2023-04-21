package net.sf.extcos.internal;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.RandomAccess;
import java.util.Set;
import net.sf.extcos.exception.UnsuccessfulOperationException;

/**
 * Resizable-array implementation of the <tt>Set</tt> interface.  Implements
 * all optional Collection operations, and permits all elements, excluding
 * <tt>null</tt>.  In addition to implementing the <tt>Set</tt> interface,
 * this class provides methods to manipulate the size of the array of the
 * <tt>ArrayList</tt> that is used internally to store the set.<p>
 *
 * The <tt>size</tt>, <tt>isEmpty</tt>, <tt>get</tt>, <tt>set</tt>,
 * and <tt>iterator</tt> operations run in constant time. The <tt>add</tt>
 * operation runs in <i>amortized constant time</i>, that is, adding n elements
 * requires O(n) time.  All of the other operations run in linear time (roughly
 * speaking).<p>
 *
 * The <tt>ArrayList</tt> instance of each <tt>ArraySet</tt> instance has a
 * <i>capacity</i>.  The capacity is the size of the array used to store the
 * elements of the set.  It is always at least as large as the set size. As
 * elements are added to an <tt>ArraySet</tt>, its capacity grows automatically.
 * The details of the growth policy are not specified beyond the fact that
 * adding an element has constant amortized time cost.<p>
 *
 * An application can increase the capacity of an <tt>ArraySet</tt> instance
 * before adding a large number of elements using the <tt>ensureCapacity</tt>
 * operation. This may reduce the amount of incremental reallocation.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access an <tt>ArraySet</tt> instance concurrently,
 * and at least one of the threads modifies the set structurally, it
 * <i>must</i> be synchronized externally.  (A structural modification is
 * any operation that adds or deletes one or more elements, or explicitly
 * resizes the array of the backing <tt>ArrayList</tt>; merely setting the
 * value of an element is not a structural modification.) This is typically
 * accomplished by synchronizing on some object that naturally encapsulates
 * the set.<p>
 *
 * If no such object exists, the set should be "wrapped" using the
 * {@link Collections#synchronizedSet Collections.synchronizedSet}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the set:<pre>
 *   Set set = Collections.synchronizedSet(new ArraySet(...));</pre>
 *
 * <p>The iterator returned by this class's <tt>iterator</tt> method is
 * <i>fail-fast</i>: if the set is structurally modified at any time after the
 * iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> or <tt>add</tt> methods, the iterator will throw a
 * {@link ConcurrentModificationException}. Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the future.
 * <p>
 *
 * Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i><p>
 *
 * Since this class is not provided by Sun it's not a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>, but extends it.
 *
 * @author  Matthias Rothe
 * @version 1.0, 14.04.2007
 * @see	    Collection
 * @see	    Set
 * @see	    ArrayList
 */
public class ArraySet<E> extends AbstractSet<E> implements Serializable, Cloneable, RandomAccess {

    /**
	 * The serial number
	 */
    private static final long serialVersionUID = 6518404858591999463L;

    /**
	 * The ArrayList on which this set implementation is based
	 */
    private ArrayList<E> list;

    /**
	 * Constructs an empty set with an initial capacity of ten.
	 */
    public ArraySet() {
        list = new ArrayList<E>();
    }

    /**
	 * Constructs an empty set with the specified initial capacity.
	 * 
	 * @param initialCapacity the initial capacity of the set
	 * @throws IllegalArgumentException if the specified initial capacity is negative
	 */
    public ArraySet(final int initialCapacity) {
        list = new ArrayList<E>(initialCapacity);
    }

    /**
	 * Constructs a set containing the unique elements of the specified
	 * collection, in the order they are returned by the collection's iterator.
	 *
	 * @param c the collection whose elements are to be placed into this list
	 * @throws NullPointerException if the specified collection is null
	 */
    public ArraySet(final Collection<? extends E> c) {
        if (c instanceof Set) {
            list = new ArrayList<E>(c);
        } else {
            list = new ArrayList<E>(c.size() / 2);
            for (E name : c) {
                try {
                    add(name);
                } catch (IllegalArgumentException e) {
                }
            }
            list.trimToSize();
        }
    }

    /**
	 * Add an element to the set. Will always be successful if the element
	 * isn't yet contained in the set.
	 * 
	 * @param element The element to be added
	 * @return <tt>true</tt> if the element was added, <tt>false</tt> otherwise
	 * @throws IllegalArgumentException if element was set to null
	 */
    @Override
    public boolean add(final E element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        if (contains(element)) {
            return false;
        }
        return list.add(element);
    }

    /**
	 * Inserts the specified element at the specified position in this
	 * set if it isn't yet contained in the set. Shifts the element currently
	 * at that position (if any) and any subsequent elements to the right
	 * (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws IndexOutOfBoundsException if the index is out of range (<tt>index < 0 || index > size()</tt>)
	 * @throws IllegalArgumentException if element was set to null
	 * @throws UnsuccessfulOperationException if the element is already
	 * 			contained in the set
	 */
    public void add(final int index, final E element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        if (contains(element)) {
            throw new UnsuccessfulOperationException();
        }
        list.add(index, element);
    }

    /**
	 * Checks whether this set contains the element.
	 * 
	 * @param element The element to be checked
	 * @return <tt>true</tt> if this set contains the element, <tt>false</tt> otherwise
	 * @throws IllegalArgumentException if element was set to null
	 */
    @Override
    public boolean contains(final Object element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        return list.contains(element);
    }

    /**
	 * Returns an iterator over the elements in this set in proper sequence.
	 * 
	 * @return an iterator over the elements in this set in proper sequence
	 */
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    /**
	 * Returns the number of elements in this set.
	 * 
	 * @return the number of elements in this set
	 */
    @Override
    public int size() {
        return list.size();
    }

    /**
	 * Trims the capacity of this <tt>ArraySet</tt> instance to be the
	 * set's current size.  An application can use this operation to minimize
	 * the storage of an <tt>ArraySet</tt> instance.
	 */
    public void trimToSize() {
        list.trimToSize();
    }

    /**
	 * Increases the capacity of this <tt>ArraySet</tt> instance, if
	 * necessary, to ensure that it can hold at least the number of elements
	 * specified by the minimum capacity argument.
	 *
	 * @param   minCapacity   the desired minimum capacity
	 */
    public void ensureCapacity(final int minCapacity) {
        list.ensureCapacity(minCapacity);
    }

    /**
	 * Returns the index of the specified element
	 * in this set, or -1 if this set does not contain the element.
	 * 
	 * @param element The element whose index is requested
	 * @return The index of the element or -1 if the element isn't contained in the set
	 * @throws IllegalArgumentException if element was set to null
	 */
    public int indexOf(final E element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        return list.indexOf(element);
    }

    /**
	 * Returns a shallow copy of this <tt>ArraySet</tt> instance.  (The
	 * elements themselves are not copied.)
	 *
	 * @return a clone of this <tt>ArraySet</tt> instance
	 */
    @Override
    @SuppressWarnings("unchecked")
    public ArraySet<E> clone() {
        try {
            ArraySet<E> set = (ArraySet<E>) super.clone();
            set.list = new ArrayList<E>(list);
            return set;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
	 * Returns an array containing all of the elements in this set
	 * in proper sequence (from first to last element).
	 *
	 * <p>The returned array will be "safe" in that no references to it are
	 * maintained by this set.  (In other words, this method must allocate
	 * a new array).  The caller is thus free to modify the returned array.
	 *
	 * <p>This method acts as bridge between array-based and collection-based
	 * APIs.
	 *
	 * @return an array containing all of the elements in this set in
	 *         proper sequence
	 */
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    /**
	 * Returns an array containing all of the elements in this set in proper
	 * sequence (from first to last element); the runtime type of the returned
	 * array is that of the specified array.  If the set fits in the
	 * specified array, it is returned therein.  Otherwise, a new array is
	 * allocated with the runtime type of the specified array and the size of
	 * this set.
	 *
	 * <p>If the set fits in the specified array with room to spare
	 * (i.e., the array has more elements than the set), the element in
	 * the array immediately following the end of the collection is set to
	 * <tt>null</tt>. Since this set doesn't contain a null element, this
	 * helps to determine its size in the array.
	 *
	 * @param a the array into which the elements of the set are to
	 *          be stored, if it is big enough; otherwise, a new array of the
	 *          same runtime type is allocated for this purpose.
	 * @return an array containing the elements of the set
	 * @throws ArrayStoreException if the runtime type of the specified array
	 *         is not a supertype of the runtime type of every element in
	 *         this set
	 * @throws NullPointerException if the specified array is null
	 */
    @Override
    public <T> T[] toArray(final T[] a) {
        return list.toArray(a);
    }

    /**
	 * Returns the element at the specified position in this set.
	 *
	 * @param  index index of the element to return
	 * @return the element at the specified position in this set
	 * @throws IndexOutOfBoundsException if the index is out of range (<tt>index < 0 || index > size()</tt>)
	 */
    public E get(final int index) {
        return list.get(index);
    }

    /**
	 * Replaces the element at the specified position in this set with
	 * the specified element if it isn't yet contained in the set.
	 *
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is out of range (<tt>index < 0 || index > size()</tt>)
	 * @throws IllegalArgumentException if element was set to null
	 * @throws UnsuccessfulOperationException if element is already contained
	 *			in the set
	 */
    public E set(final int index, final E element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        if (contains(element)) {
            throw new UnsuccessfulOperationException();
        }
        return list.set(index, element);
    }

    /**
	 * Removes the element at the specified position in this set.
	 * Shifts any subsequent elements to the left (subtracts one from their
	 * indices).
	 *
	 * @param index the index of the element to be removed
	 * @return the element that was removed from the list
	 * @throws IndexOutOfBoundsException if the index is out of range (<tt>index < 0 || index > size()</tt>)
	 */
    public E remove(final int index) {
        E element = list.remove(index);
        trimToSize();
        return element;
    }

    /**
	 * Removes the specified element from this set,
	 * if it is present.  If the set does not contain the element, it is
	 * unchanged. Returns <tt>true</tt> if this set
	 * contained the specified element (or equivalently, if this set
	 * changed as a result of the call).
	 *
	 * @param element Element to be removed from this list, if present
	 * @return <tt>true</tt> if this list contained the specified element
	 * @throws IllegalArgumentException if the element was set to null
	 */
    @Override
    public boolean remove(final Object element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        boolean success = list.remove(element);
        trimToSize();
        return success;
    }

    /**
	 * Removes all of the elements from this set.  The set will
	 * be empty after this call returns.
	 */
    @Override
    public void clear() {
        list.clear();
        trimToSize();
    }

    /**
	 * Appends each element of the specified collection to the end of
	 * this set if the element isn't yet contained in the set, in the order
	 * that each element is returned by the specified collection's Iterator.
	 * The behavior of this operation is undefined if the specified collection
	 * is modified while the operation is in progress. (This implies that the
	 * behavior of this call is undefined if the specified collection is this
	 * list, and this list is nonempty.)
	 *
	 * @param c collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws NullPointerException if the specified collection is null
	 */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean changed = false;
        for (E name : c) {
            try {
                changed |= add(name);
            } catch (IllegalArgumentException e) {
            }
        }
        return changed;
    }

    /**
	 * Inserts each element of the specified collection to the end of
	 * this set if the element isn't yet contained in the set, starting
	 * at the specified position.  Shifts the element currently at that
	 * position (if any) and any subsequent elements to the right
	 * (increases their indices).  The new elements will appear
	 * in the set in the order that they are returned by the
	 * specified collection's Iterator.
	 *
	 * @param index index at which to insert the first element from the
	 *              specified collection
	 * @param c collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws IndexOutOfBoundsException if the index is out of range (<tt>index < 0 || index > size()</tt>)
	 * @throws NullPointerException if the specified collection is null
	 */
    public boolean addAll(int index, final Collection<? extends E> c) {
        boolean changed = false;
        ArraySet<E> set = new ArraySet<E>(c);
        if (set.size() > 0) {
            for (E e2 : set) {
                try {
                    add(index, e2);
                    changed = true;
                    index++;
                } catch (UnsuccessfulOperationException e) {
                }
            }
        }
        return changed;
    }

    /**
	 * Replace the old element with the new one if the set contains the old,
	 * but doesn't yet contain the new element.
	 * 
	 * @param oldElement The old element to be replaced
	 * @param newElement The new element to replace the old one with.
	 * @return <tt>true</tt> if and only if the set was changed because the new
	 * 			element replaced the old one, <tt>false</tt> otherwise.
	 * @throws IllegalArgumentException if oldElement or newElement was set to null
	 */
    public boolean replace(final E oldElement, final E newElement) {
        if (oldElement == null || newElement == null) {
            throw new IllegalArgumentException();
        }
        boolean changed = false;
        int index;
        if ((index = indexOf(oldElement)) > -1) {
            E result = set(index, newElement);
            if (result.equals(oldElement)) {
                changed = true;
            }
        }
        return changed;
    }
}
