package huf.data;

import huf.data.compare.IComparator;
import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/**
 * Class that holds objects.
 *
 * <p>
 * All operations preserves element order - whenever you add, replace or remove element from this
 * container other element's order is not changed. <code>null</code> is a valid container element.
 * </p>
 *
 * @param <T> Type of objects stored in this container
 */
public class Container<T> implements IContainer<T> {

    /** Container's initial capacity (10). Can be overriden with constructor parameter. */
    private static final int INITIAL_CAPACITY = 10;

    /** Maximum number of entries container can grow at a time. */
    private static final int MAX_CAPACITY_INCREMENT = 100000;

    /**
	 * Variable holding actual initial capacity for this container. Used in constructors and
	 * in {@link #clear() clear()} method.
	 */
    private int initialCapacity = 0;

    /**
	 * Variable holding actual capacity increment for this container or
	 * zero for default capacity increment estimation algorithm.
	 * Can be overriden with {@link #setIncrement(int) setIncrement()} method.
	 */
    private int capacityIncrement = 0;

    /** Array holding this container's elements */
    private T[] data = null;

    /** Number of elements stored in this container. */
    private int count = 0;

    private final ContainerHelper<T> helper;

    /** Creates an empty Container. */
    public Container() {
        this(INITIAL_CAPACITY);
    }

    /**
	 * Creates an empty Container.
	 *
	 * @param comparator comparator used by this container
	 */
    public Container(IComparator<T> comparator) {
        this(INITIAL_CAPACITY, comparator);
    }

    /**
	 * Creates new Container with specified capacity.
	 *
	 * @param initialCapacity initial capacity of this container
	 */
    public Container(int initialCapacity) {
        this(initialCapacity, 0);
    }

    /**
	 * Creates new Container with specified capacity.
	 *
	 * @param initialCapacity initial capacity of this container
	 * @param comparator comparator used by this container
	 */
    public Container(int initialCapacity, IComparator<T> comparator) {
        this(initialCapacity, 0, comparator);
    }

    /**
	 * Creates new Container with specified capacity.
	 *
	 * @param initialCapacity initial capacity of this container
	 * @param capacityIncrement by how many entries will be internal buffer incremented when it reaches its capacity
	 */
    public Container(int initialCapacity, int capacityIncrement) {
        this.initialCapacity = initialCapacity;
        data = createArray(initialCapacity);
        this.capacityIncrement = capacityIncrement;
        helper = new ContainerHelper<T>(this);
    }

    /**
	 * Creates new Container with specified capacity.
	 *
	 * @param initialCapacity initial capacity of this container
	 * @param capacityIncrement by how many entries will be internal buffer incremented when it reaches its capacity
	 * @param comparator comparator used by this container
	 */
    public Container(int initialCapacity, int capacityIncrement, IComparator<T> comparator) {
        this(initialCapacity, capacityIncrement);
        setComparator(comparator);
    }

    /**
	 * Creates new Container with elements copied from iterator.
	 *
	 * @param elements iterator containing elements to copy
	 */
    public Container(Iterator<? extends T> elements) {
        this();
        set(elements);
    }

    /**
	 * Creates new Container with elements copied from iterator.
	 *
	 * @param elements iterator containing elements to copy
	 * @param comparator comparator used by this container
	 */
    public Container(Iterator<? extends T> elements, IComparator<T> comparator) {
        this();
        setComparator(comparator);
        set(elements);
    }

    /**
	 * Sets contents of this container to elements of iterator.
	 *
	 * @param iterator iterator to obtain elements from
	 */
    @Override
    public void set(Iterator<? extends T> iterator) {
        helper.set(iterator);
    }

    /**
	 * Sets contents of this container to contents of an array.
	 *
	 * @param elements array to obtain elements from
	 */
    @Override
    public void set(T[] elements) {
        helper.set(elements);
    }

    /**
	 * Adds contents of iterator to this container.
	 *
	 * @param iterator iterator to obtain elements from
	 */
    @Override
    public void merge(Iterator<? extends T> iterator) {
        helper.merge(iterator);
    }

    /**
	 * Adds contents of an array to this container.
	 *
	 * @param elements array to obtain elements from
	 */
    @Override
    public void merge(T[] elements) {
        helper.merge(elements);
    }

    /**
	 * Adds <code>element</code> to this container.
	 *
	 * @param element element to add
	 */
    @Override
    public void add(T element) {
        if (count == data.length) {
            growArray();
        }
        data[count++] = element;
    }

    /**
	 * Adds <code>element</code> to this container and returns index at which it is stored.
	 *
	 * @param element element to add
	 * @return index at which object is stored
	 */
    public int addGetIdx(T element) {
        if (count == data.length) {
            growArray();
        }
        data[count] = element;
        return count++;
    }

    /**
	 * Adds object <code>element</code> to this container at specified offset
	 * pushing all elements above up by one position.
	 *
	 * <p>
	 * Effect of inserting elements is explained below:
	 * <pre>
	 * 0 1 2 3 4 5
	 * A B C D
	 *
	 * addAt(2, "X");
	 *
	 * 0 1 2 3 4 5
	 * A B X C D
	 * </pre>
	 * </p>
	 *
	 * @param index index at which element will be inserted
	 * @param element element to add
	 */
    public void addAt(int index, T element) {
        if (index > count || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of range [0 - " + (count - 1) + "]");
        }
        if (index == count) {
            add(element);
            return;
        }
        if (count == data.length) {
            growArray();
        }
        System.arraycopy(data, index, data, index + 1, (count++) - index);
        data[index] = element;
        return;
    }

    /**
	 * Sets (replaces) element stored at index <code>index</code> with specified <code>element</code>.
	 *
	 * @param index index to put object at
	 * @param element element to replace with
	 * @throws ArrayIndexOutOfBoundsException if index is out of range 0-(count-1)
	 */
    public void setAt(int index, T element) {
        if (index >= count || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of range [0 - " + (count - 1) + "]");
        }
        data[index] = element;
    }

    /**
	 * Puts element <code>element</code> at index <code>index</code>, possibly replacing old value and
	 * growing container if necessary.
	 *
	 * @param index index to put object at
	 * @param element element to put into container
	 * @throws ArrayIndexOutOfBoundsException if index is < 0
	 */
    public void putAt(int index, T element) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of range [0 - " + (count - 1) + "]");
        }
        ensureSize(index);
        data[index] = element;
    }

    /**
	 * Returns element stored at index <code>index</code>.
	 *
	 * @param index index of returned object
	 * @return object at index <code>index</code>
	 */
    public T getAt(int index) {
        if (index >= count || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of range [0 - " + (count - 1) + "]");
        }
        return data[index];
    }

    /**
	 * Removes all elements equal to object <code>element</code>.
	 *
	 * <p>
	 * Equality of the objects is determined using comparator used by this container.
	 * </p>
	 *
	 * @param element object to remove
	 * @return number of elements removed
	 */
    @Override
    public int remove(T element) {
        int idx = indexOf(element);
        int numRemoved = 0;
        while (idx >= 0) {
            removeAt(idx);
            idx = indexOf(element);
            numRemoved++;
        }
        return numRemoved;
    }

    /**
	 * Removes element stored at index <code>index</code>.
	 *
	 * <p>
	 * Effect of this command is depicted below:
	 * <pre>
	 * 0 1 2 3 4 5
	 * A B C D
	 *
	 * removeAt(2);
	 *
	 * 0 1 2 3 4 5
	 * A B D
	 * </pre>
	 * </p>
	 *
	 * @param index index to remove element from
	 * @return a reference to the container for easy operation chaining
	 */
    public IContainer<T> removeAt(int index) {
        if (index >= count || index < 0) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of range [0 - " + (count - 1) + "]");
        }
        System.arraycopy(data, index + 1, data, index, count-- - index - 1);
        return this;
    }

    /**
	 * Remove all elements from this container.
	 */
    @Override
    public void clear() {
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] = null;
        }
        data = createArray(initialCapacity);
        count = 0;
    }

    /**
	 * Sets comparator used by the object to check equality of two objects.
	 *
	 * <p>
	 * Comparator is used by methods like {@link #has(Object)} or containers which
	 * may contain only one object equal to any other.
	 * Objects are considered equal when {@link IComparator#compare(Object, Object)} returns
	 * {@link IComparator#EQUAL} and not equal if that method returns anything else.
	 * </p>
	 *
	 * <p>
	 * This method sets default comparator if invoked with <code>null</code> argument.
	 * </p>
	 *
	 * @param comparator comparator used by this container;
	 *        use <code>null</code> for default comparator
	 */
    @Override
    public void setComparator(IComparator<T> comparator) {
        helper.setComparator(comparator);
    }

    /**
	 * Gets comparator currently used by this container.
	 *
	 * @return comparator currently used by this container
	 */
    @Override
    public IComparator<T> getComparator() {
        return helper.getComparator();
    }

    /**
	 * Sets default comparator to be used by this container.
	 *
	 * <p>
	 * Default comparator uses {@link Object#equals(Object)} method to check equality of two elements.
	 * </p>
	 */
    @Override
    public void setDefaultComparator() {
        helper.setDefaultComparator();
    }

    /**
	 * Return iterator.
	 *
	 * @return iterator
	 */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            int ii = -1;

            @Override
            public boolean hasNext() {
                return ii < (getSize() - 1);
            }

            @Override
            public T next() {
                if (++ii == getSize()) {
                    --ii;
                    throw new NoSuchElementException("Tried to go past the end of iterator");
                }
                return getAt(ii);
            }

            @Override
            public T get() {
                try {
                    return getAt(ii);
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    throw new NoSuchElementException("Iterator does not point to any item");
                }
            }

            @Override
            public void remove() {
                if (ii < 0) {
                    throw new IllegalStateException("Tried to go before start of iterator");
                }
                removeAt(ii--);
            }

            @Override
            public Iterator<T> iterator() {
                return this;
            }
        };
    }

    /**
	 * Returns true if this container contains at least one element that is <i>equal</i> to object <code>element</code>
	 *
	 * <p>
	 * Equality of the objects is determined using comparator used by this container.
	 * </p>
	 *
	 * @param element object in question
	 * @return true if this container contains at least one element equal to o
	 */
    @Override
    public boolean has(T element) {
        for (int i = count - 1; i >= 0; i--) {
            if (getComparator().compare(element, data[i]) == IComparator.EQUAL) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Returns index of first element that is <i>equal</i> to object <code>element</code>
	 *
	 * <p>
	 * Equality of the objects is determined using comparator used by this container.
	 * </p>
	 *
	 * @param element object in question
	 * @return index of first element that is equal to object element or -1 if not found
	 */
    public int indexOf(T element) {
        for (int i = count - 1; i >= 0; i--) {
            if (getComparator().compare(element, data[i]) == IComparator.EQUAL) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * Returns contents of this container as an array of objects.
	 *
	 * @param array array in which to store elements of this container; must be big enough to hold all elements
	 * @return array containing elements of this container
	 */
    @Override
    public Object[] getAsArray(Object[] array) {
        System.arraycopy(data, 0, array, 0, count);
        return array;
    }

    /**
	 * Returns number of elements in this container.
	 *
	 * @return number of elements in this container
	 */
    @Override
    public int getSize() {
        return count;
    }

    /**
	 * Returns true if this container is empty (doesn't contain any elements).
	 *
	 * @return true if this container is empty
	 */
    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    /**
	 * Returns contents of this container as an array of objects.
	 *
	 * <p>
	 * Note that this is usually slow operation and should be avoided in applications requiring high performance.
	 * Implementations are not required to return elements in any particular order.
	 * </p>
	 *
	 * @return array containing elements of this container
	 */
    @Override
    public Object[] getAsArray() {
        return getAsArray(new Object[getSize()]);
    }

    /**
	 * Returns contents of this container as an array of specified type.
	 *
	 * <p>
	 * Note that this is usually slow operation and should be avoided in applications requiring high performance.
	 * Implementations are not required to return elements in any particular order.
	 * </p>
	 *
	 * @param arrayType type of component of resulting array -
	 *        ie. to get an array of Strings (<code>String[]</code>) you need to pass <code>String.class</code>
	 *        as an argument here
	 * @return array containing elements of this container
	 */
    @Override
    public Object[] getAsArray(Class<?> arrayType) {
        return getAsArray((Object[]) Array.newInstance(arrayType, getSize()));
    }

    private void setSize(int size) {
        System.arraycopy(data, 0, (data = createArray(size)), 0, count);
    }

    /** Grow size of internal array used to store elements to specified size. */
    private void growArray() {
        int increment = capacityIncrement > 0 ? capacityIncrement : (data.length > MAX_CAPACITY_INCREMENT ? MAX_CAPACITY_INCREMENT : (data.length > 0 ? data.length : 10));
        setSize(data.length + increment);
    }

    /** Grow size of internal array used to store elements to specified size. */
    private void growArray(int minSize) {
        int increment = capacityIncrement > 0 ? capacityIncrement : (data.length > MAX_CAPACITY_INCREMENT ? MAX_CAPACITY_INCREMENT : (data.length > 0 ? data.length : 10));
        setSize(minSize + increment);
    }

    /**
	 * Ensure this container has at least <code>size</code> elements so all operations like {@link #getAt(int)}
	 * can be called safely.
	 *
	 * @param size minimum size
	 * @return reference to this container for easy operation chaining
	 */
    public Container<T> ensureSize(int size) {
        if (size >= count) {
            growArray(size + 1);
            count = size + 1;
        }
        return this;
    }

    /**
	 * Makes sure that this container has enough space for <code>space</code> new elements.
	 * Use this when you're about to add large, previously known number of elements. If you
	 * don't know exactly how many element you're about to add, better use
	 * {@link #setIncrement(int) setIncrement()}.
	 *
	 * @param space space needed for new elements
	 * @return reference to this container for easy operation chaining
	 */
    public Container<T> ensureFreeSpace(int space) {
        setSize(count + space);
        return this;
    }

    /**
	 * Trims size of array in which this container holds its element to actual number of
	 * elements stored with small amount of spare space.
	 *
	 * <p>
	 * Use this method to reclaim memory after you have removed large number of elements from the container.
	 * </p>
	 *
	 * @return a reference to the container for easy operation chaining
	 */
    public IContainer<T> trim() {
        setSize(count + capacityIncrement);
        return this;
    }

    /**
	 * It is a <i>hint</i> type method; it sets capacity increment for this container.
	 *
	 * <p>
	 * Calling this method with some large value is a good idea when you plan to add lots of elements.
	 * </p>
	 *
	 * @param increment new capacity increment
	 * @return a reference to the container for easy operation chaining
	 */
    public IContainer<T> setIncrement(int increment) {
        if (increment > MAX_CAPACITY_INCREMENT) {
            throw new IllegalArgumentException("Capacity increment must be at most Container.MAX_CAPACITY_INCREMENT (" + MAX_CAPACITY_INCREMENT + ")");
        }
        if (increment < 0) {
            throw new IllegalArgumentException("Capacity increment must be zero or positive number");
        }
        capacityIncrement = increment;
        return this;
    }

    /**
	 * Standard toString() method implementation.
	 *
	 * @return textual description of container
	 */
    @Override
    public String toString() {
        int idx = 0;
        StringBuilder ret = new StringBuilder("[ ");
        Iterator<T> i = iterator();
        while (i.hasNext()) {
            ret.append("(");
            ret.append("" + idx);
            ret.append(": ");
            ret.append(i.next().toString());
            ret.append(") ");
            idx++;
        }
        ret.append(" ]");
        return new String(ret);
    }

    @SuppressWarnings(value = { "unchecked" })
    private T[] createArray(int size) {
        return (T[]) new Object[size];
    }
}
