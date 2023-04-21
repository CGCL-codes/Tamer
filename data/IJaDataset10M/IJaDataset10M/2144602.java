package com.metanology.mde.core.codeFactory;

import java.util.List;
import java.util.Collection;
import java.util.ListIterator;

/**
 *  A collection of CodeBlock objects.  CodeBlockCollection wraps a Vector
 *  to provide a typed collection, removing the casts required for 
 *  Vector operations.
 */
public class CodeBlockCollection implements java.io.Serializable, List {

    private java.util.Vector items = new java.util.Vector();

    /**
     *  Create an empty CodeBlockCollection.
     */
    public CodeBlockCollection() {
    }

    ;

    /**
     *  Create a CodeBlockCollection from 
     *  an existing Vector. All elements in theVector must be of type 
     *  CodeBlock. 
     */
    public CodeBlockCollection(java.util.Vector theItems) {
        this.items = theItems;
    }

    /**
	 * Inserts the specified element at the specified position in this list (optional operation). 
	 * Shifts the element currently at that position (if any) and any 
	 * subsequent elements to the right (adds one to their indices). 
	 * 
	 * @param index - index at which the specified element is to be inserted.
	 * @param element - element to be inserted. 
	 * 
	 * @throws UnsupportedOperationException - if the add method is not supported by this list. 
	 * @throws ClassCastException - if the class of the specified element prevents it from being added to this list. 
	 * @throws NullPointerException - if the specified element is null and this list does not support null elements. 
	 * @throws IllegalArgumentException - if some aspect of the specified element prevents it from being added to this list. 
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index > size()).
	 * 
	 * @since 3.0
	 */
    public void add(int index, Object element) {
        this.items.add(index, element);
    }

    /**
	 * Ensures that this collection contains the specified element (optional operation). 
	 * Returns true if this collection changed as a result of the call. 
	 * (Returns false if this collection does not permit duplicates and already contains the specified element.)
	 * Collections that support this operation may place limitations on what elements may be added to this collection. 
	 * In particular, some collections will refuse to add null elements, and others will impose restrictions on the 
	 * type of elements that may be added. Collection classes should clearly specify in their documentation any 
	 * restrictions on what elements may be added.
	 *
	 * If a collection refuses to add a particular element for any reason other than that it already contains the element, 
	 * it must throw an exception (rather than returning false). This preserves the invariant 
	 * that a collection always contains the specified element after this call returns. 
	 * @param o - element whose presence in this collection is to be ensured.
	 * @return true if this collection changed as the result of the call.
	 */
    public boolean add(Object o) {
        return items.add(o);
    }

    /** 
     * Inserts all of the elements in in the specified Collection into this
     * CodeBlockCollection at the specified position.  Shifts the element currently at
     * that position (if any) and any subsequent elements to the right
     * (increases their indices).  The new elements will appear in the Collection
     * in the order that they are returned by the specified Collection's
     * iterator.
     *
     * @param index index at which to insert first element
     *		    from the specified collection.
     * @param c elements to be inserted into this CodeBlockCollection.
     * @return <tt>true</tt> if this Vector changed as a result of the call.
     * @exception ArrayIndexOutOfBoundsException index out of range (index
     *		  &lt; 0 || index &gt; size()).
     * @throws NullPointerException if the specified collection is null.
	 */
    public boolean addAll(Collection c) {
        return items.addAll(c);
    }

    /**
	 * Inserts all of the elements in the specified collection into this
	 * list at the specified position (optional operation).  Shifts the
	 * element currently at that position (if any) and any subsequent
	 * elements to the right (increases their indices).  The new elements
	 * will appear in this list in the order that they are returned by the
	 * specified collection's iterator.  The behavior of this operation is
	 * unspecified if the specified collection is modified while the
	 * operation is in progress.  (Note that this will occur if the specified
	 * collection is this list, and it's nonempty.)
	 *
	 * @param index index at which to insert first element from the specified
	 *	            collection.
	 * @param c elements to be inserted into this list.
	 * @return <tt>true</tt> if this list changed as a result of the call.
	 * 
	 * @throws UnsupportedOperationException if the <tt>addAll</tt> method is
	 *		  not supported by this list.
	 * @throws ClassCastException if the class of one of elements of the
	 * 		  specified collection prevents it from being added to this
	 * 		  list.
	 * @throws NullPointerException if the specified collection contains one
	 *           or more null elements and this list does not support null
	 *           elements, or if the specified collection is <tt>null</tt>.
	 * @throws IllegalArgumentException if some aspect of one of elements of
	 *		  the specified collection prevents it from being added to
	 *		  this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *		  &lt; 0 || index &gt; size()).
     * @since 3.0
	 */
    public boolean addAll(int index, Collection c) {
        return items.addAll(index, c);
    }

    /**
	  * Removes all of the elements from this collection (optional operation).
	  */
    public void clear() {
        items.clear();
    }

    /**
	 * Returns the element at the specified position in this list. 
	 * 
	 * @param index - index of element to return. 
	 * @return the element at the specified position in this list. 
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size()).
	 * @since 3.0
	 */
    public Object get(int index) {
        return items.get(index);
    }

    /**
     *  The number of CodeBlock objects in this CodeBlockCollection.
     */
    public int size() {
        return this.items.size();
    }

    /**
     *  Return the nth CodeBlock object in this CodeBlockCollection.  Index
     *  must be between 0 and the number of CodeBlock - 1 in this collection.
     */
    public CodeBlock elementAt(int index) {
        return (CodeBlock) this.items.elementAt(index);
    }

    /**
     *  Add a CodeBlock to this CodeBlockCollection.
     */
    public void addElement(CodeBlock pVal) {
        items.addElement(pVal);
    }

    /**
     * Tests if the specified object is a component in this CodeBlockCollection.
     *
     * @param   o   an object.
     * @return  <code>true</code> if and only if the specified object 
     * is the same as a component in this vector, as determined by the 
     * <tt>equals</tt> method; <code>false</code> otherwise.
     */
    public boolean contains(Object o) {
        return items.contains(o);
    }

    /**
	 * Returns true if this list contains all of the elements of the specified collection. 
	 *
	 * Specified by:
	 * containsAll in interface Collection
	 *
	 * @param c - collection to be checked for containment in this list. 
	 *
	 * @return true if this list contains all of the elements of the specified collection. 
	 *
	 * @throws ClassCastException - if the types of one or more elements in the specified collection are incompatible with this list (optional). 
	 * @throws NullPointerException - if the specified collection contains one or more null elements and this list does not support null elements (optional). 
	 * @throws NullPointerException - if the specified collection is null.
	 * @see #contains(Object)
	 * @since 3.0
	 */
    public boolean containsAll(Collection c) {
        return items.containsAll(c);
    }

    /**
	 * Compares the specified object with this collection for equality.
	 */
    public boolean equals(Object o) {
        return items.equals(o);
    }

    /**
     * Returns the index of the first occurence of the given object, which must
     * be an CodeBlock, testing for equality using the equals method.
     */
    public int indexOf(Object o) {
        return items.indexOf(o);
    }

    /**
	 * @since 3.0
	 */
    public int lastIndexOf(Object o) {
        return items.lastIndexOf(o);
    }

    /**
     * Returns true if this collection contains no elements.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this collection.
     */
    public java.util.Iterator iterator() {
        return items.iterator();
    }

    /**
	 * Removes a single instance of the specified element from this collection, if it is present (optional operation).
	 */
    public boolean remove(Object o) {
        return items.remove(o);
    }

    /**
	 * @since 3.0
	 */
    public Object remove(int index) {
        return items.remove(index);
    }

    /** 
	 * Removes all this collection's elements that are also contained in the specified collection (optional operation).
	 */
    public boolean removeAll(Collection c) {
        return items.removeAll(c);
    }

    /**
	 * Retains only the elements in this collection that are contained in the specified collection (optional operation).
	 */
    public boolean retainAll(Collection c) {
        return items.retainAll(c);
    }

    /**
	 * @since 3.0
	 */
    public Object set(int index, Object element) {
        return items.set(index, element);
    }

    /**
	 * Returns an array containing all of the elements in this collection.
	 */
    public Object[] toArray() {
        return items.toArray();
    }

    /** 
	 * Returns an array containing all of the elements in this collection; the runtime type of the returned array is that of the specified array.
	 */
    public Object[] toArray(Object[] a) {
        return items.toArray(a);
    }

    /**
     *  Remove the CodeBlock at the given location from this
     *  CodeBlockCollection.
     */
    public void removeElementAt(int index) {
        items.removeElementAt(index);
    }

    /**
     *  Remove the given CodeBlock from this CodeBlockCollection.
     */
    public void removeElement(CodeBlock pVal) {
        items.removeElement(pVal);
    }

    /**
     *  Remove all the elements from this CodeBlockCollection.
     */
    public void removeAllElements() {
        items.removeAllElements();
    }

    /**
     *  Return an enumeration of all the CodeBlock objects in this 
     *  CodeBlockCollection.
     */
    public java.util.Enumeration elements() {
        return items.elements();
    }

    /**
	 * Returns a list iterator of the elements in this list (in proper sequence). 
	 * @return a list iterator of the elements in this list (in proper sequence).
	 * @since 3.0
	 */
    public ListIterator listIterator() {
        return items.listIterator();
    }

    /**
	 * Returns a list iterator of the elements in this list (in proper sequence), starting at the specified position in this list. The specified index indicates the first element that would be returned by an initial call to the next method. An initial call to the previous method would return the element with the specified index minus one. 
	 *
	 * @param index - index of first element to be returned from the list iterator (by a call to the next method). 
	 * @return a list iterator of the elements in this list (in proper sequence), starting at the specified position in this list. 
	 *
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index > size()).
	 * @since 3.0
	 */
    public ListIterator listIterator(int index) {
        return items.listIterator(index);
    }

    /**
	 * Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive. (If fromIndex and toIndex are equal, the returned list is empty.) The returned list is backed by this list, so non-structural changes in the returned list are reflected in this list, and vice-versa. The returned list supports all of the optional list operations supported by this list.
	 * This method eliminates the need for explicit range operations (of the sort that commonly exist for arrays). Any operation that expects a list can be used as a range operation by passing a subList view instead of a whole list. For example, the following idiom removes a range of elements from a list: 
	 *
	 *	    list.subList(from, to).clear();
	 * Similar idioms may be constructed for indexOf and lastIndexOf, and all of the algorithms in the Collections class can be applied to a subList.
	 * The semantics of the list returned by this method become undefined if the backing list (i.e., this list) is structurally modified in any way other than via the returned list. (Structural modifications are those that change the size of this list, or otherwise perturb it in such a fashion that iterations in progress may yield incorrect results.) 
	 *
	 *
	 * @param fromIndex - low endpoint (inclusive) of the subList.
	 * @param toIndex - high endpoint (exclusive) of the subList. 
	 *
	 * @return a view of the specified range within this list. 
	 * @throws IndexOutOfBoundsException - for an illegal endpoint index value (fromIndex < 0 || toIndex > size || fromIndex > toIndex).
	 */
    public List subList(int fromIndex, int toIndex) {
        return items.subList(fromIndex, toIndex);
    }

    public int hashCode() {
        if (items == null) return super.hashCode();
        return items.hashCode();
    }
}
