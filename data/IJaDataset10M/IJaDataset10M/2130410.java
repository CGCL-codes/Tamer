package br.ufrj.cad.model.usuario.xmllattes.DTD;

import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;
import java.util.LinkedList;

/**
 *  A <code>java.util.List</code> containing only instances 
 *  of <code>ProcessosOuTecnicas</code>.
 *  Every attempt to add something different will result in
 *  a <code>ClassCastException</code>.<p>
 *  It is backed by the list implementation of class
 *  <code>java.util.LinkedList</code>,
 *  so the support of the optional opperations depend
 *  on this implementation.
 *  <p>
 *  @author Generated with jNerd's XML2Java tool Version 1.2 Preview 2 on Tue Apr 14 16:30:38 BRT 2009
 *  @author <br>Please send BugReports to <a href="mailto:xml2java@jNerd.de">xml2java@jNerd.de</a>
 */
public class ProcessosOuTecnicasList implements List, XMLContent {

    /** This list's implementing class,
	 *  which is an instance of <code>java.util.LinkedList</code>.
	 */
    private final List impl;

    /**
	 * Constructs an empty <code>ProcessosOuTecnicasList</code>.
	 */
    public ProcessosOuTecnicasList() {
        this.impl = new java.util.LinkedList();
    }

    /**
	 * Constructs a list containing the elements of the specified
	 * collection, in the order they are returned by the collection's
	 * iterator.
	 *
	 * @param c the collection whose elements are to be placed into this list.
	 *
	 * @throws UnsupportedOperationException if the <code>add</code> method is
	 *         not supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * 
	 * @throws ClassCastException if the class of an element in the specified
	 * 	       collection prevents it from being added to this list.
	 *         This is the case if an element of <code>Collection c</code> is not an instance 
	 *         of <code>ProcessosOuTecnicas</code>. 	 
	 * 
	 * @throws IllegalArgumentException if some aspect of an element in the
	 *         specified collection prevents it from being added to this
	 *         list.
	 *
	 * @throws NullPointerException if the specified collection is <code>null</code> 
	 *         or it contains an element which is <code>null</code>.	 
	 */
    public ProcessosOuTecnicasList(Collection col) {
        this();
        this.addAll(col);
    }

    /**
	 * Returns the number of elements in this list.  If this list contains
	 * more than <code>Integer.MAX_VALUE</code> elements, returns
	 * <code>Integer.MAX_VALUE</code>.
	 *
	 * @return the number of elements in this list.
	 */
    public int size() {
        return this.impl.size();
    }

    /**
	 * Returns <code>true</code> if this list contains no elements.
	 *
	 * @return <code>true</code> if this list contains no elements.
	 */
    public boolean isEmpty() {
        return this.impl.isEmpty();
    }

    /**
	 * 
	 * Returns <code>true</code> if this list contains the specified element.
	 * More formally, returns <code>true</code> if and only if this list contains
	 * at least one element <code>e</code> such that
	 * <code>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</code>.
	 *
	 * @param o element whose presence in this list is to be tested.
	 * @return <code>true</code> if this list contains the specified element.
	 */
    public boolean contains(Object o) {
        return this.impl.contains(o);
    }

    /**
	 * Returns an iterator over the elements in this list in proper sequence.
	 *
	 * @return an iterator over the elements in this list in proper sequence.
	 */
    public Iterator iterator() {
        return this.impl.iterator();
    }

    /**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence.  Obeys the general contract of the
	 * <code>Collection.toArray</code> method.
	 *
	 * @return an array containing all of the elements in this list in proper
	 *	       sequence.
	 */
    public Object[] toArray() {
        return this.impl.toArray();
    }

    /**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence; the runtime type of the returned array is that of the
	 * specified array.  Obeys the general contract of the
	 * <code>Collection.toArray(Object[])</code> method.
	 *
	 * @param a the array into which the elements of this list are to
	 *		be stored, if it is big enough; otherwise, a new array of the
	 * 		same runtime type is allocated for this purpose.
	 * @return  an array containing the elements of this list.
	 * 
	 * @throws ArrayStoreException if the runtime type of the specified array
	 * 		  is not a supertype of the runtime type of every element in
	 * 		  this list.
	 */
    public Object[] toArray(Object a[]) {
        return this.impl.toArray(a);
    }

    /**
	 * Appends the specified element to the end of this list (optional
	 * operation). <p>
	 *
	 * This list is solely cappable of adding instances of class <code>ProcessosOuTecnicas</code>
	 *
	 * @param o instance of <code>ProcessosOuTecnicas</code> to be appended to this list.
	 * @return <code>true</code> (as per the general contract of the
	 *            <code>Collection.add</code> method).
	 * 
	 * @throws UnsupportedOperationException if the <code>add</code> method is not
	 * 		  supported by this list's implementing class (<code>java.util.LinkedList</code>).
	 * @throws ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 *        This is the case if <code>Object o</code> is not an instance 
	 *        of class <code>ProcessosOuTecnicas</code>. 
	 * @throws IllegalArgumentException if some aspect of this element
	 *            prevents it from being added to this collection.
	 * @throws NullPointerException if the specified element is <code>null</code>.
	 */
    public boolean add(Object o) {
        if (!(o instanceof ProcessosOuTecnicas)) {
            throw new ClassCastException("Cannot add a " + o.getClass() + " to a " + this.getClass() + ".");
        }
        return this.add((ProcessosOuTecnicas) o);
    }

    /**
	 * Appends the specified <code>ProcessosOuTecnicas</code> to the end of this list (optional
	 * operation). <p>
	 *
	 * @param o instance of <code>ProcessosOuTecnicas</code> to be appended to this list.
	 * @return <code>true</code> (as per the general contract of the
	 *            <code>Collection.add</code> method).
	 * 
	 * @throws UnsupportedOperationException if the <code>add</code> method is not
	 * 		  supported by this list's implementing class (<code>java.util.LinkedList</code>).
	 * @throws IllegalArgumentException if some aspect of this element
	 *            prevents it from being added to this collection.
	 * @throws NullPointerException if the specified element is <code>null</code>.
	 */
    public boolean add(ProcessosOuTecnicas o) {
        return this.impl.add(o);
    }

    /**
	 * Removes the first occurrence in this list of the specified element 
	 * (optional operation).  If this list does not contain the element, it is
	 * unchanged.  More formally, removes the element with the lowest index i
	 * such that <code>(o==null ? get(i)==null : o.equals(get(i)))</code> (if
	 * such an element exists).
	 *
	 * @param o element to be removed from this list, if present.
	 * @return <code>true</code> if this list contained the specified element.
	 * 
	 * @throws UnsupportedOperationException if the <code>remove</code> method is
	 *		  not supported by this lists implementing class (<code>java.util.LinkedList</code>).
	 */
    public boolean remove(Object o) {
        if (o instanceof ProcessosOuTecnicas) {
            return this.remove((ProcessosOuTecnicas) o);
        }
        return false;
    }

    /**
	 * Removes the first occurrence in this list of the specified element 
	 * (optional operation).  If this list does not contain the element, it is
	 * unchanged.  More formally, removes the element with the lowest index i
	 * such that <code>(o==null ? get(i)==null : o.equals(get(i)))</code> (if
	 * such an element exists).
	 *
	 * @param o element to be removed from this list, if present.
	 * @return <code>true</code> if this list contained the specified element.
	 * 
	 * @throws UnsupportedOperationException if the <code>remove</code> method is
	 *		  not supported by this lists implementing class (<code>java.util.LinkedList</code>).
	 */
    public boolean remove(ProcessosOuTecnicas o) {
        if (this.impl.remove(o)) {
            return true;
        }
        return false;
    }

    /**
	 * 
	 * Returns <code>true</code> if this list contains all of the elements of the
	 * specified collection.
	 *
	 * @param c collection to be checked for containment in this list.
	 * @return <code>true</code> if this list contains all of the elements of the
	 * 	       specified collection.
	 * 
	 * @see #contains(Object)
	 */
    public boolean containsAll(Collection c) {
        return this.impl.containsAll(c);
    }

    /**
	 * Appends all of the elements in the specified collection to the end of
	 * this list, in the order that they are returned by the specified
	 * collection's iterator (optional operation).  The behavior of this
	 * operation is unspecified if the specified collection is modified while
	 * the operation is in progress.  (Note that this will occur if the
	 * specified collection is this list, and it's nonempty.)
	 *
	 * @param c collection whose elements are to be added to this list.
	 * @return <code>true</code> if this list changed as a result of the call.
	 * 
	 * @throws UnsupportedOperationException if the <code>add</code> method is
	 *         not supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * 
	 * @throws ClassCastException if the class of an element in the specified
	 * 	       collection prevents it from being added to this list.
	 *         This is the case if an element of <code>Collection c</code> is not an instance 
	 *         of class <code>ProcessosOuTecnicas</code>. 	 
	 * 
	 * @throws IllegalArgumentException if some aspect of an element in the
	 *         specified collection prevents it from being added to this
	 *         list.
	 *
	 * @throws NullPointerException if the specified collection or an element 
	 *         within the collection is <code>null</code>.
	 * 
	 * @see #add(Object)
	 */
    public boolean addAll(Collection c) {
        Iterator itr = c.iterator();
        while (itr.hasNext()) {
            this.add(itr.next());
        }
        return !c.isEmpty();
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
	 * @return <code>true</code> if this list changed as a result of the call.
	 * 
	 * @throws UnsupportedOperationException if the <code>add</code> method is
	 *		  not supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * @throws ClassCastException if the class of one of elements of the
	 * 		  specified collection prevents it from being added to this
	 * 		  list.
	 *         This is the case if an element of <code>Collection c</code> is not an instance 
	 *         of class <code>ProcessosOuTecnicas</code>. 	 
	 * @throws IllegalArgumentException if some aspect of one of elements of
	 *		  the specified collection prevents it from being added to
	 *		  this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *		  &lt; 0 || index &gt; size()).
	 *
	 * @throws NullPointerException if the specified collection or an element 
	 *         within the collection is <code>null</code>.	 
	 */
    public boolean addAll(int index, Collection c) {
        Iterator itr = c.iterator();
        while (itr.hasNext()) {
            this.add(index++, itr.next());
        }
        return !c.isEmpty();
    }

    /**
	 * Removes from this list all the elements that are contained in the
	 * specified collection (optional operation).
	 *
	 * @param c collection that defines which elements will be removed from
	 *          this list.
	 * @return <code>true</code> if this list changed as a result of the call.
	 * 
	 * @throws UnsupportedOperationException if the <code>removeAll</code> method
	 * 		  is not supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * 
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
    public boolean removeAll(Collection c) {
        boolean ret = false;
        Iterator itr = c.iterator();
        while (itr.hasNext()) {
            ret |= this.remove(itr.next());
        }
        return ret;
    }

    /**
	 * Retains only the elements in this list that are contained in the
	 * specified collection (optional operation).  In other words, removes
	 * from this list all the elements that are not contained in the specified
	 * collection.
	 *
	 * @param c collection that defines which elements this set will retain.
	 * 
	 * @return <code>true</code> if this list changed as a result of the call.
	 * 
	 * @throws UnsupportedOperationException if the <code>retainAll</code> method
	 * 		  is not supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * 
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
    public boolean retainAll(Collection c) {
        Object o;
        boolean ret = false;
        for (int i = 0; i < this.size(); ) {
            o = this.get(i);
            if (!c.contains(o)) {
                ret |= this.remove(o);
            } else {
                i++;
            }
        }
        return ret;
    }

    /**
	 * Removes all of the elements from this list (optional operation).  This
	 * list will be empty after this call returns (unless it throws an
	 * exception).
	 *
	 * @throws UnsupportedOperationException if the <code>clear</code> method is
	 * 		  not supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 */
    public void clear() {
        this.impl.clear();
    }

    /**
	 * Compares the specified object with this list for equality.  Returns
	 * <code>true</code> if and only if the specified object is also a list, both
	 * lists have the same size, and all corresponding pairs of elements in
	 * the two lists are <i>equal</i>.  (Two elements <code>e1</code> and
	 * <code>e2</code> are <i>equal</i> if <code>(e1==null ? e2==null :
	 * e1.equals(e2))</code>.)  In other words, two lists are defined to be
	 * equal if they contain the same elements in the same order.  This
	 * definition ensures that the equals method works properly across
	 * different implementations of the <code>List</code> interface.
	 *
	 * @param o the object to be compared for equality with this list.
	 * @return <code>true</code> if the specified object is equal to this list.
	 */
    public boolean equals(Object o) {
        return (o instanceof ProcessosOuTecnicasList) && this.impl.equals(o);
    }

    /**
	 * Returns the hash code value for this list.  The hash code of a list
	 * is defined to be the result of the following calculation:
	 * <pre>
	 *  hashCode = 1;
	 *  Iterator i = list.iterator();
	 *  while (i.hasNext()) {
	 *      Object obj = i.next();
	 *      hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
	 *  }
	 * </pre>
	 * This ensures that <code>list1.equals(list2)</code> implies that
	 * <code>list1.hashCode()==list2.hashCode()</code> for any two lists,
	 * <code>list1</code> and <code>list2</code>, as required by the general
	 * contract of <code>Object.hashCode</code>.
	 *
	 * @return the hash code value for this list.
	 * @see Object#hashCode()
	 * @see Object#equals(Object)
	 * @see #equals(Object)
	 */
    public int hashCode() {
        return this.impl.hashCode();
    }

    /**
	 * Returns the element at the specified position in this list.
	 * This element is an instance of <code>ProcessosOuTecnicas</code>.
	 *
	 * @param index index of element to return.
	 * @return the element at the specified position in this list.
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 * 		  &lt; 0 || index &gt;= size()).
	 */
    public Object get(int index) {
        return this.impl.get(index);
    }

    /**
	 * Replaces the element at the specified position in this list with the
	 * specified element (optional operation).<p>
	 * The specified element must be an instance of <code>ProcessosOuTecnicas</code>.
	 *
	 * @param index index of element to replace.
	 * @param o element to be stored at the specified position.
	 * @return the element previously at the specified position.
	 * 
	 * @throws UnsupportedOperationException if the <code>set</code> method is not
	 *		  supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * @throws    ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 *         This is the case if a <code>Object element</code> is not an instance 
	 *         of <code>ProcessosOuTecnicas</code>. 
	 * @throws    IllegalArgumentException if some aspect of the specified
	 *		  element prevents it from being added to this list.
	 * @throws    IndexOutOfBoundsException if the index is out of range
	 *		  (index &lt; 0 || index &gt;= size()).  
	 * @throws NullPointerException if the specified element is <code>null</code>.
	 */
    public Object set(int index, Object o) {
        if (!(o instanceof ProcessosOuTecnicas)) {
            throw new ClassCastException("Cannot insert a " + o.getClass() + " to a " + this.getClass() + ".");
        }
        return this.impl.set(index, (ProcessosOuTecnicas) o);
    }

    /**
	 * Replaces the element at the specified position in this list with the
	 * specified element (optional operation).<p>
	 * The specified element must be an instance of <code>ProcessosOuTecnicas</code>.
	 *
	 * @param index index of element to replace.
	 * @param o element to be stored at the specified position.
	 * @return the element previously at the specified position.
	 * 
	 * @throws UnsupportedOperationException if the <code>set</code> method is not
	 *		  supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * @throws    ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 *         This is the case if a <code>Object element</code> is not an instance 
	 *         of <code>ProcessosOuTecnicas</code>. 
	 * @throws    IllegalArgumentException if some aspect of the specified
	 *		  element prevents it from being added to this list.
	 * @throws    IndexOutOfBoundsException if the index is out of range
	 *		  (index &lt; 0 || index &gt;= size()).  
	 * @throws NullPointerException if the specified element is <code>null</code>.
	 */
    public Object set(int index, ProcessosOuTecnicas o) {
        return this.impl.set(index, o);
    }

    /**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation).  Shifts the element currently at that position
	 * (if any) and any subsequent elements to the right (adds one to their
	 * indices).
	 *  <br>
	 *  This list is only cappable of adding instances of class <code>ProcessosOuTecnicas</code>
	 *
	 * @param index index at which the specified element is to be inserted.
	 * @param element element to be inserted.
	 * 
	 * @throws UnsupportedOperationException if the <code>add</code> method is not
	 *		  supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * @throws    ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 *         This is the case if a <code>Object element</code> is not an instance 
	 *         of <code>ProcessosOuTecnicas</code>. 
	 * @throws    IllegalArgumentException if some aspect of the specified
	 *		  element prevents it from being added to this list.
	 * @throws    IndexOutOfBoundsException if the index is out of range
	 *		  (index &lt; 0 || index &gt; size()).
	 * @throws NullPointerException if the specified element is <code>null</code>.
	 */
    public void add(int index, Object element) {
        if (!(element instanceof ProcessosOuTecnicas)) {
            throw new ClassCastException("Cannot add a " + element.getClass() + " to a " + this.getClass() + ".");
        }
        this.impl.add(index, (ProcessosOuTecnicas) element);
    }

    /**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation).  Shifts the element currently at that position
	 * (if any) and any subsequent elements to the right (adds one to their
	 * indices).
	 *  <br>
	 *  This list is only cappable of adding instances of class <code>ProcessosOuTecnicas</code>
	 *
	 * @param index index at which the specified element is to be inserted.
	 * @param o element to be inserted.
	 * 
	 * @throws UnsupportedOperationException if the <code>add</code> method is not
	 *		  supported by this list's implementing class 
	 *         <code>java.util.LinkedList</code>.
	 * @throws    ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 *         This is the case if a <code>Object element</code> is not an instance 
	 *         of <code>ProcessosOuTecnicas</code>. 
	 * @throws    IllegalArgumentException if some aspect of the specified
	 *		  element prevents it from being added to this list.
	 * @throws    IndexOutOfBoundsException if the index is out of range
	 *		  (index &lt; 0 || index &gt; size()).
	 * @throws NullPointerException if the specified element is <code>null</code>.
	 */
    public void add(int index, ProcessosOuTecnicas o) {
        this.impl.add(index, o);
    }

    /**
	 * Removes the element at the specified position in this list (optional
	 * operation).  Shifts any subsequent elements to the left (subtracts one
	 * from their indices).  Returns the element that was removed from the
	 * list.
	 *
	 * @param index the index of the element to removed.
	 * @return the element previously at the specified position.
	 * 
	 * @throws UnsupportedOperationException if the <code>remove</code> method is
	 *		  not supported by this list.
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *            &lt; 0 || index &gt;= size()).
	 */
    public Object remove(int index) {
        ProcessosOuTecnicas o = (ProcessosOuTecnicas) this.impl.remove(index);
        return o;
    }

    /**
	 * Returns the index in this list of the first occurrence of the specified
	 * element, or -1 if this list does not contain this element.
	 * More formally, returns the lowest index <code>i</code> such that
	 * <code>(o==null ? get(i)==null : o.equals(get(i)))</code>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for.
	 * @return the index in this list of the first occurrence of the specified
	 * 	       element, or -1 if this list does not contain this element.
	 */
    public int indexOf(Object o) {
        return this.impl.indexOf(o);
    }

    /**
	 * Returns the index in this list of the last occurrence of the specified
	 * element, or -1 if this list does not contain this element.
	 * More formally, returns the highest index <code>i</code> such that
	 * <code>(o==null ? get(i)==null : o.equals(get(i)))</code>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for.
	 * @return the index in this list of the last occurrence of the specified
	 * 	       element, or -1 if this list does not contain this element.
	 */
    public int lastIndexOf(Object o) {
        return this.impl.lastIndexOf(o);
    }

    /**
	 * Returns a list iterator of the elements in this list (in proper
	 * sequence).
	 *
	 * @return a list iterator of the elements in this list (in proper
	 * 	       sequence).
	 */
    public ListIterator listIterator() {
        return this.impl.listIterator();
    }

    /**
	 * Returns a list iterator of the elements in this list (in proper
	 * sequence), starting at the specified position in this list.  The
	 * specified index indicates the first element that would be returned by
	 * an initial call to the <code>next</code> method.  An initial call to
	 * the <code>previous</code> method would return the element with the
	 * specified index minus one.
	 *
	 * @param index index of first element to be returned from the
	 *		    list iterator (by a call to the <code>next</code> method).
	 * @return a list iterator of the elements in this list (in proper
	 * 	       sequence), starting at the specified position in this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *         &lt; 0 || index &gt; size()).
	 */
    public ListIterator listIterator(int index) {
        return this.impl.listIterator(index);
    }

    /**
	 * Returns a view of the portion of this list between the specified
	 * <code>fromIndex</code>, inclusive, and <code>toIndex</code>, exclusive.  (If
	 * <code>fromIndex</code> and <code>toIndex</code> are equal, the returned list is
	 * empty.)  The returned list is backed by this list, so changes in the
	 * returned list are reflected in this list, and vice-versa.  The returned
	 * list supports all of the optional list operations supported by this
	 * list.<p>
	 *
	 * This method eliminates the need for explicit range operations (of
	 * the sort that commonly exist for arrays).   Any operation that expects
	 * a list can be used as a range operation by passing a subList view
	 * instead of a whole list.  For example, the following idiom
	 * removes a range of elements from a list:
	 * <pre>
	 *	    list.subList(from, to).clear();
	 * </pre>
	 * Similar idioms may be constructed for <code>indexOf</code> and
	 * <code>lastIndexOf</code>, and all of the algorithms in the
	 * <code>Collections</code> class can be applied to a subList.<p>
	 *
	 * The semantics of this list returned by this method become undefined if
	 * the backing list (i.e., this list) is <i>structurally modified</i> in
	 * any way other than via the returned list.  (Structural modifications are
	 * those that change the size of this list, or otherwise perturb it in such
	 * a fashion that iterations in progress may yield incorrect results.)
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList.
	 * @param toIndex high endpoint (exclusive) of the subList.
	 * @return a view of the specified range within this list.
	 * 
	 * @throws IndexOutOfBoundsException for an illegal endpoint index value
	 *     (fromIndex &lt; 0 || toIndex &gt; size || fromIndex &gt; toIndex).
	 */
    public List subList(int fromIndex, int toIndex) {
        return new ProcessosOuTecnicasList(this.impl.subList(fromIndex, toIndex), this);
    }

    /** A private constructor to wrap the Sublist returned by the <code>subList</code> method */
    private ProcessosOuTecnicasList(List impl, ProcessosOuTecnicasList backend) {
        this.impl = impl;
    }
}
