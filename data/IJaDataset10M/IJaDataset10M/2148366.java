package geomss.geom;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.measure.unit.Unit;
import javax.measure.quantity.Length;
import javolution.util.FastTable;
import javolution.text.Text;
import javolution.text.TextBuilder;

/**
*  Partial implementation of a named list of {@link GeomElement} objects.
*
*  <p>  Modified by:  Joseph A. Huwaldt   </p>
*
*  @author Joseph A. Huwaldt    Date:  March 31, 2000
*  @version August 9, 2011
**/
public abstract class AbstractGeomList<T extends AbstractGeomList, E extends GeomElement> extends AbstractGeomElement<T> implements GeometryList<T, E> {

    private FastTable<E> _list = FastTable.newInstance();

    /**
	*  Return the list underlying this geometry list.  Subclasses that change the
	*  list referenced by this object should override this method to return
	*  the appropriate list.
	**/
    protected List<E> getList() {
        return _list;
    }

    /**
	*  Return the input index normalized into the range 0 <= index < size().
	*  This allows negative indexing (-1 refering to the last element in the list),
	*  but does not allow wrap-around positive indexing.
	**/
    protected int normalizeIndex(int index) {
        int size = size();
        while (index < 0) index += size;
        return index;
    }

    /**
	*  Returns the number of elements in this list. If the list contains more
	*  than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	*
	*  @return the number of elements in this list.
	**/
    public int size() {
        return getList().size();
    }

    /**
	*  Returns <code>true</code> if this collection contains no elements.
	**/
    public boolean isEmpty() {
        return getList().isEmpty();
    }

    /**
	*  Returns <code>true</code> if this list actually contains any geometry and
	*  <code>false</code> if this list is empty or contains only non-geometry
	*  items such as empty lists.
	**/
    public boolean containsGeometry() {
        boolean output = false;
        for (GeomElement element : this) {
            try {
                element.getBoundsMin();
                output = true;
                break;
            } catch (Exception e) {
            }
        }
        return output;
    }

    /**
	*  Returns the element at the specified position in this list.
	*
	*  @param index  index of element to return (0 returns the 1st element, -1 returns the last,
	*                -2 returns the 2nd from last, etc).
	*  @return the element at the specified position in this list.
	*  @throws IndexOutOfBoundsException if the given index is out of range: <code>index > size()</code>
	**/
    public E get(int index) {
        index = normalizeIndex(index);
        return getList().get(index);
    }

    /**
	*  Returns the range of elements in this list from the specified start and ending indexes.
	*
	*  @param first index of the first element to return (0 returns the 1st element, -1 returns the last, etc).
	*  @param last  index of the last element to return (0 returns the 1st element, -1 returns the last, etc).
	*  @return the list of elements in the given range from this list.
	*  @throws IndexOutOfBoundsException if the given index is out of range: <code>index > size()</code>
	**/
    public abstract T getRange(int first, int last);

    /**
	* Returns a view of the portion of this list between fromIndex, inclusive, and toIndex, exclusive. (If fromIndex and toIndex are equal, the returned list is empty.) The returned list is backed by this list, so changes in the returned list are reflected in this list, and vice-versa. The returned list supports all of the optional list operations supported by this list.
	*
	*  @param fromIndex  low endpoint (inclusive) of the subList.
	*  @param toIndex  high endpoint (exclusive) of the subList.
	*  @return a view of the specified range within this list.
	*  @throws IndexOutOfBoundsException if the given index is out of range: <code>index > size()</code>
	**/
    public List<E> subList(int fromIndex, int toIndex) {
        fromIndex = normalizeIndex(fromIndex);
        toIndex = normalizeIndex(toIndex);
        return getList().subList(fromIndex, toIndex);
    }

    /**
	*  Returns the first element from this list.
	*
	*  @return the first element in this list.
	**/
    public E getFirst() {
        return get(0);
    }

    /**
	*  Returns the last element from this list.
	*
	*  @return the last element in this list.
	**/
    public E getLast() {
        return get(size() - 1);
    }

    /**
	*  Returns the element with the specified name from this list.
	*
	*  @param   name  The name of the element we are looking for in the list.
	*  @return  The element matching the specified name.  If the specified
	*           element name isn't found in the list, then <code>null</code> is returned.
	**/
    public E get(String name) {
        E element = null;
        int index = getIndexFromName(name);
        if (index >= 0) element = this.get(index);
        return element;
    }

    /**
	*  Replaces the <@link GeomElement> at the specified position in this list
	*  with the specified element.  Null elements are ignored.
	*
	*  @param   index   The index of the element to replace (0 returns the 1st element, -1 returns the last,
	*                   -2 returns the 2nd from last, etc).
	*  @param   element The element to be stored at the specified position.  <code>null</code> elements are ignored.
	*  @return  The element previously at the specified position in this list.
	*  @throws  java.lang.IndexOutOfBoundsException - if  <code>index > size()</code>
	**/
    public E set(int index, E element) {
        if (element == null) return null;
        if (!(element instanceof GeomElement)) throw new ClassCastException(RESOURCES.getString("listGeomElementsOnly"));
        index = normalizeIndex(index);
        E old = getList().set(index, element);
        fireChangeEvent();
        return old;
    }

    /**
	* Inserts the specified {@link GeomElement} at the specified position in this list.
	* Shifts the element currently at that position (if any) and any subsequent
	* elements to the right (adds one to their indices).  Null values are ignored.
	*
	* <p>Note: If this method is used concurrent access must be synchronized
	*          (the list is not thread-safe).</p>
	*
	* @param index  the index at which the specified element is to be inserted.
	*               (0 returns the 1st element, -1 returns the last, -2 returns the 2nd from last, etc).
	* @param value  the element to be inserted.
	* @throws IndexOutOfBoundsException if <code>index > size()</code>
	**/
    public void add(int index, E value) {
        if (value == null) return;
        if (!(value instanceof GeomElement)) throw new ClassCastException(RESOURCES.getString("listGeomElementsOnly"));
        index = normalizeIndex(index);
        getList().add(index, value);
        fireChangeEvent();
    }

    /**
	* Appends the specified {@link GeomElement} to the end of this list.
	* Null values are ignored.
	*
	* <p>Note: If this method is used concurrent access must be synchronized
	*          (the table is not thread-safe).</p>
	*
	* @param value  the element to be inserted.
	* @return <code>true</code> if this collection changed as a result of the call
	* @throws  DimensionException if the input element's dimensions are different
	*          from this list's dimensions.
	**/
    public boolean add(E value) {
        if (value == null) return false;
        add(size(), value);
        return true;
    }

    /**
	* Adds all of the elements in the specified collection to this geometry element list.
	* The behavior of this operation is undefined if the specified collection is
	* modified while the operation is in progress. (This implies that the behavior
	* of this call is undefined if the specified collection is this collection,
	* and this collection is nonempty.)
	*
	* @param c  elements to be inserted into this collection.
	* @return <code>true</code> if this collection changed as a result of the call
	**/
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size(), c);
    }

    /**
	* Inserts all of the {@link GeomElement} objects in the specified collection into this list at the
	* specified position. Shifts the element currently at that
	* position (if any) and any subsequent elements to the right (increases their indices).
	* The new elements will appear in this list in the order that they are returned
	* by the specified collection's iterator. The behavior of this operation is
	* unspecified if the specified collection is modified while the operation is in
	* progress. (Note that this will occur if the specified collection is this list,
	* and it's nonempty.)
	*
	* @param index  index at which to insert first element from the specified collection.
	* @param c  elements to be inserted into this collection.
	* @return <code>true</code> if this collection changed as a result of the call
	**/
    public boolean addAll(int index, Collection<? extends E> c) {
        index = normalizeIndex(index);
        for (Object element : c) {
            if (!(element instanceof GeomElement)) throw new ClassCastException(RESOURCES.getString("listGeomElementsOnly"));
        }
        boolean retVal = getList().addAll(index, c);
        if (retVal) fireChangeEvent();
        return retVal;
    }

    /**
	* Adds all of the elements in the specified list of arguments to this geometry element list.
	*
	* @param array  elements to be inserted into this collection.
	* @return <code>true</code> if this collection changed as a result of the call
	**/
    public boolean add(E... array) {
        return add(size(), array);
    }

    /**
	* Inserts all of the {@link GeomElement} objects in the specified list of arguments into this list at the
	* specified position. Shifts the element currently at that
	* position (if any) and any subsequent elements to the right (increases their indices).
	* The new elements will appear in this list in the order that they are appeared in the array.
	*
	* @param index  index at which to insert first element from the specified array.
	* @param array  elements to be inserted into this collection.
	* @return <code>true</code> if this collection changed as a result of the call
	**/
    public boolean add(int index, E... array) {
        index = normalizeIndex(index);
        boolean retVal = false;
        for (E value : array) {
            if (value != null) {
                add(index, value);
                retVal = true;
                ++index;
            }
        }
        if (retVal) fireChangeEvent();
        return retVal;
    }

    /**
	*  Removes the element at the specified position in this list.
	*  Shifts any subsequent elements to the left (subtracts one from their indices).
	*  Returns the element that was removed from the list.
	*
	*  @param index  the index of the element to remove. (0 returns the 1st element, -1 returns the last,
	*                -2 returns the 2nd from last, etc).
	*  @return the element previously at the specified position.
	**/
    public E remove(int index) {
        index = normalizeIndex(index);
        E old = getList().remove(index);
        fireChangeEvent();
        return old;
    }

    /**
	*  Removes a single instance of the specified element from this collection,
	*  if it is present (optional operation). More formally, removes an element e 
	*  such that (o==null ? e==null : o.equals(e)), if this collection contains
	*  one or more such elements. Returns true if this collection contained the
	*  specified element (or equivalently, if this collection changed as a result of the call).
	*
	*  @param o element to be removed from this collection, if present.
	*  @return <code>true</code> if this collection changed as a result of the call
	**/
    public boolean remove(Object o) {
        boolean retVal = getList().remove(o);
        if (retVal) fireChangeEvent();
        return retVal;
    }

    /**
	*  Removes the element at the specified name in this list.
	*  Shifts any subsequent elements to the left (subtracts one from their indices).
	*  Returns the element that was removed from the list.
	*
	*  @param name  the name of the element to remove.
	*  @return the element previously at the specified position.
	**/
    public E remove(String name) {
        E element = null;
        int index = getIndexFromName(name);
        if (index >= 0) element = this.remove(index);
        return element;
    }

    /**
	*  Removes all of the elements from this collection.
	*  The collection will be empty after this call returns.
	**/
    public void clear() {
        getList().clear();
        fireChangeEvent();
    }

    /**
	*  Returns an iterator over the elements in this list.
	*
	*  @return an iterator over this list values.
	**/
    public java.util.Iterator<E> iterator() {
        return getList().iterator();
    }

    /**
	*  Returns a list iterator over the elements in this list.
	*
	*  @return an iterator over this list values.
	**/
    public java.util.ListIterator<E> listIterator() {
        return getList().listIterator();
    }

    /**
	*  Returns a list iterator from the specified position.
	*
	*  @param index  the index of first value to be returned from the
	*                list iterator (by a call to the next method).
	*  @return a list iterator of the values in this table starting at the specified position in this list.
	**/
    public java.util.ListIterator<E> listIterator(int index) {
        return getList().listIterator(index);
    }

    /**
	*  Returns an unmodifiable list view associated to this list.
	*  Attempts to modify the returned collection result in an
	*  UnsupportedOperationException being thrown.
	*
	*  @return the unmodifiable view over this list.
	**/
    public List<E> unmodifiableList() {
        return _list.unmodifiable();
    }

    /**
	*  Returns an new {@link GeomList} with the elemnts in this list.
	**/
    public GeomList<E> getAll() {
        GeomList list = GeomList.newInstance();
        list.addAll(this);
        return list;
    }

    /**
	*  Removes from this list all the elements that are contained in the specified collection.
	*
	*  @param c collection that defines which elements will be removed from this list.
	*  @return <code>true</code> if this list changed as a result of the call.
	**/
    public boolean removeAll(Collection<?> c) {
        boolean retVal = getList().removeAll(c);
        if (retVal) fireChangeEvent();
        return retVal;
    }

    /**
	*  Retains only the elements in this list that are contained in the
	*  specified collection. In other words, removes from this list all
	*  the elements that are not contained in the specified collection.
	*
	*  @param c collection that defines which elements this set will retain.
	*  @return <code>true</code> if this list changed as a result of the call.
	**/
    public boolean retainAll(Collection<?> c) {
        boolean retVal = getList().retainAll(c);
        if (retVal) fireChangeEvent();
        return retVal;
    }

    /**
	*  Returns an new {@link GeomList} with the elements in this list
	*  in reverse order.
	**/
    public abstract T reverse();

    /**
	*  Returns the index in this list of the first occurence of the specified element,
	*  or -1 if the list does not contain this element.
	*
	*  @param element The element to search for.
	*  @return the index in this List of the first occurence of the specified element,
	*          or -1 if the List does not contain this element.
	**/
    public int indexOf(Object element) {
        return getList().indexOf(element);
    }

    /**
	*  Returns the index in this list of the last occurence of the specified element,
	*  or -1 if the list does not contain this element. More formally, returns the 
	*  highest index i such that (o==null ? get(i)==null : o.equals(get(i))),
	*  or -1 if there is no such index.
	*
	*  @param element The element to search for.
	*  @return the index in this list of the last occurence of the specified element,
	*          or -1 if the list does not contain this element.
	**/
    public int lastIndexOf(Object element) {
        return getList().lastIndexOf(element);
    }

    /**
	*  Return the index to the 1st geometry element in this list with the specified
	*  name. Objects with <code>null</code> names are ignored.
	*
	*  @param name  The name of the geometry element to find in this list
	*  @return The index to the named geometry element or -1 if it is not found.
	**/
    public int getIndexFromName(String name) {
        if (name == null) return -1;
        List<E> list = getList();
        int result = -1;
        int size = this.size();
        for (int i = 0; i < size; ++i) {
            GeomElement element = list.get(i);
            String eName = element.getName();
            if (name.equals(eName)) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
	*  Returns true if this collection contains the specified element.
	*  More formally, returns true if and only if this collection contains at
	*  least one element e such that (o==null ? e==null : o.equals(e)).
	*
	*  @param o object to be checked for containment in this collection.
	*  @return <code>true</code> if this collection contains the specified element.
	**/
    public boolean contains(Object o) {
        return getList().contains(o);
    }

    /**
	*  Returns true if this collection contains all of the elements in the specified collection.
	*
	*  @param c  collection to be checked for containment in this collection.
	*  @return <code>true</code> if this collection contains all of the elements in
	*          the specified collection.
	**/
    public boolean containsAll(Collection<?> c) {
        return getList().containsAll(c);
    }

    /**
	*  Returns an array containing all of the elements in this collection.
	**/
    public Object[] toArray() {
        return getList().toArray();
    }

    /**
	*  Returns an array containing all of the elements in this collection.
	*  If the collection fits in the specified array, it is returned therein.
	*  Otherwise, a new array is allocated with the runtime type of the specified
	*  array and the size of this collection.
	*
	*  @param a the array into which the elements of the collection are to be stored,
	*         if it is big enough; otherwise, a new array of the same type is
	*         allocated for this purpose.
	*  @return an array containing the elements of the collection.
	**/
    public <T> T[] toArray(T[] a) {
        return getList().toArray(a);
    }

    /**
	*  Returns the unit in which the <I>first</I> geometry element in this list is stated.
	*  If the list contains no geometry elements, then the default units of either CENTIMETER or INCH,
	*  depending on the user's locale, is returned.
	**/
    public Unit<Length> getUnit() {
        if (size() == 0 || !containsGeometry()) return GeomUtil.getDefaultUnit();
        return elementWithGeometry().getUnit();
    }

    /**
 	* Compares the specified object with this list of <code>GeomElement</code>
	* objects for equality. Returns true if and only if both collections are
	* of the same type and both collections contain
	* equal elements in the same order.
	*
 	* @param  obj the object to compare with.
 	* @return <code>true</code> if this list is identical to that
 	* 		list; <code>false</code> otherwise.
	**/
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (obj.getClass() != this.getClass())) return false;
        AbstractGeomList that = (AbstractGeomList) obj;
        if (!this._list.equals(that._list)) return false;
        return super.equals(obj);
    }

    /**
 	* Returns the hash code for this <code>AbstractGeomList</code>.
 	* 
 	* @return the hash code value.
 	*/
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + _list.hashCode();
        hash = hash * 31 + super.hashCode();
        return hash;
    }

    /**
	* Returns a deep copy of this <code>AbstractGeomList</code> instance.
	*
	* @return a clone of this <code>AbstractGeomList</code> instance
	**/
    public Object clone() {
        return copy();
    }

    /**
	* Returns the text representation of this geometry element.
	**/
    public Text toText() {
        TextBuilder tmp = TextBuilder.newInstance();
        String className = this.getClass().getName();
        tmp.append(className.substring(className.lastIndexOf(".") + 1));
        tmp.append(": {\n");
        int size = this.size();
        for (int i = 0; i < size; ++i) {
            GeomElement e = this.get(i);
            if (e instanceof AbstractGeomList) {
                className = e.getClass().getName();
                tmp.append(className.substring(className.lastIndexOf(".") + 1));
                String name = e.getName();
                if (name != null) {
                    tmp.append("(\"");
                    tmp.append(name);
                    tmp.append("\")");
                }
                tmp.append(": size = ");
                tmp.append(((AbstractGeomList) e).size());
            } else {
                tmp.append(e.toText());
            }
            if (i < size - 1) tmp.append(",\n"); else tmp.append("\n");
        }
        tmp.append("}");
        Text txt = tmp.toText();
        TextBuilder.recycle(tmp);
        return txt;
    }

    /**
	* Returns the number of physical dimensions of the geometry element.
	* This implementation always returns 0.
	*/
    public int getPhyDimension() {
        return 0;
    }

    /**
	* Returns the number of parametric dimensions of the geometry element.
	* This implementation always returns 0.
	*/
    public int getParDimension() {
        return 0;
    }

    /**
	*  Return the coordinate point representing the
	*  minimum bounding box corner of this geometry element (e.g.: min X, min Y, min Z).
	*  The physical dimension of the returned point will be that of the highest physical
	*  dimension object in this list.
	*
	*  @return The minimum bounding box coordinate for this geometry element.
	*  @throws IndexOutOfBoundsException if this list contains no geometry.
	**/
    public GeomPoint getBoundsMin() {
        GeomElement geom = elementWithGeometry();
        if (geom == null) throw new IndexOutOfBoundsException(RESOURCES.getString("listNoGeometry"));
        GeomPoint minPoint = geom.getBoundsMin();
        int dim = minPoint.getPhyDimension();
        GeomPoint emin = null;
        for (GeomElement element : this) {
            try {
                emin = element.getBoundsMin();
                int eDim = emin.getPhyDimension();
                if (eDim > dim) {
                    minPoint = minPoint.toDimension(eDim);
                    dim = eDim;
                } else if (eDim < dim) emin = emin.toDimension(dim);
                minPoint = minPoint.min(emin);
            } catch (Exception e) {
            }
        }
        return minPoint;
    }

    /**
	*  Return the coordinate point representing the
	*  maximum bounding box corner (e.g.: max X, max Y, max Z).
	*  The physical dimension of the returned point will be that of the highest physical
	*  dimension object in this list.
	*
	*  @return The maximum bounding box coordinate for this geometry element.
	*  @throws IndexOutOfBoundsException if this list contains no elements.
	**/
    public GeomPoint getBoundsMax() {
        GeomElement geom = elementWithGeometry();
        if (geom == null) throw new IndexOutOfBoundsException(RESOURCES.getString("listNoGeometry"));
        GeomPoint maxPoint = geom.getBoundsMax();
        int dim = maxPoint.getPhyDimension();
        GeomPoint emax = null;
        for (GeomElement element : this) {
            try {
                emax = element.getBoundsMax();
                int eDim = emax.getPhyDimension();
                if (eDim > dim) {
                    maxPoint = maxPoint.toDimension(eDim);
                    dim = eDim;
                } else if (eDim < dim) emax = emax.toDimension(dim);
                maxPoint = maxPoint.max(emax);
            } catch (Exception e) {
            }
        }
        return maxPoint;
    }

    /**
	*  Resets the internal state of this object to its default values.
	*  Subclasses that override this method must call <code>super.reset();</code> to
	*  ensure that the state is reset properly.
	**/
    public void reset() {
        _list.reset();
        super.reset();
    }

    /**
	*  Returns an element from this list that actually contains geometry
	*  or null if there is none.
	**/
    private E elementWithGeometry() {
        GeomPoint minPoint = null;
        for (E element : this) {
            try {
                minPoint = element.getBoundsMin();
                return element;
            } catch (Exception e) {
            }
        }
        return null;
    }
}
