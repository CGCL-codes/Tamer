package net.sourceforge.jetdog.gfx.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is a {@link Collection} representing an unordered group
 * of elements. The benefit of using it is that it's fast.
 * 
 * @author oNyx
 */
public class Bag<E> implements Collection<E> {

    private E[] array;

    private int size = 0;

    /**
	 * Constructs an empty bag with an initial capacity of 10
	 */
    public Bag() {
        this(10);
    }

    /**
	 * Constructs an empty bag with the specified initial capacity
	 * 
	 * @param initialCapacity The number of elements the bag can hold without growth operations
	 */
    @SuppressWarnings("unchecked")
    public Bag(int initialCapacity) {
        array = (E[]) new Object[initialCapacity];
    }

    /**
	 * Constructs a bag that holds the elements in the specified
	 * collection
	 * 
	 * @param c A collection of elements to insert in the new bag
	 */
    public Bag(Collection<E> c) {
        this(c.size());
        addAll(c);
    }

    public boolean add(E o) {
        ensureCapacity(size + 1);
        array[size] = o;
        size++;
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    public void clear() {
        if (size > 0) {
            Arrays.fill(array, 0, size - 1, null);
            size = 0;
        }
    }

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Ensures that this bag can hold {@code capacity} items
	 * 
	 * @param capacity
	 *           The number of items that the bag should be able to
	 *           hold without additional growth operations
	 */
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int capacity) {
        if (capacity > array.length) {
            int grownCapacity = (array.length * 3) / 2 + 1;
            if (grownCapacity < capacity) {
                grownCapacity = capacity;
            }
            E[] grownArray = (E[]) new Object[grownCapacity];
            System.arraycopy(array, 0, grownArray, 0, array.length);
            array = grownArray;
        }
    }

    /**
	 * Finds the index of the specified element in this {@link Bag}.
	 * Use with care, the index of a given element is liable to change
	 * without warning.
	 * 
	 * @param o
	 *           The element to find
	 * @return The index of the element, or -1 if the element is not
	 *         present in this bag
	 */
    private int indexOf(Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[i])) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<E> iterator() {
        return new BagIterator<E>();
    }

    /**
	 * Gets the element at the specified index.
	 * 
	 * @param index
	 * @return The element at the specifed index.
	 */
    public E get(int index) {
        assert index >= 0 && index < size : "0 <= " + index + " < " + size;
        return array[index];
    }

    /**
	 * Removes the object at the specified index from this {@link Bag}
	 * 
	 * @param index
	 *           The index of the object to remove
	 * @return The object at the specified index
	 */
    public E remove(int index) {
        assert index >= 0 && index < size : "0 <= " + index + " < " + size;
        size--;
        E o = array[index];
        array[index] = array[size];
        array[size] = null;
        return o;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(array[i])) {
                remove(i);
                i--;
                changed = true;
            }
        }
        return changed;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        Object[] a = new Object[size];
        System.arraycopy(array, 0, a, 0, a.length);
        return a;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    private class BagIterator<T> implements Iterator<T> {

        private int nextIndex = 0;

        public boolean hasNext() {
            return nextIndex < size;
        }

        @SuppressWarnings("unchecked")
        public T next() {
            nextIndex++;
            return (T) array[nextIndex - 1];
        }

        public void remove() {
            nextIndex--;
            Bag.this.remove(nextIndex);
        }
    }
}
