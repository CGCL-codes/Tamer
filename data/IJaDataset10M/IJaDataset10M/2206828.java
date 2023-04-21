package org.palmj.cJUI.util;

public class ArrayList {

    private Object[] storedObjects;

    private int growthFactor;

    private int size;

    /**
	 * Creates an ArrayList with the initial capacity of 10 and a growth factor
	 * of 75%
	 */
    public ArrayList() {
        this(10, 75);
    }

    /**
	 * creates an ArrayList with the given initial capacity and a growth factor
	 * of 75%
	 * 
	 * @param initialCapacity
	 *            the capacity of this array list.
	 */
    public ArrayList(int initialCapacity) {
        this(initialCapacity, 75);
    }

    /**
	 * Creates a new ArrayList
	 * 
	 * @param initialCapacity
	 *            the capacity of this array list.
	 * @param growthFactor
	 *            the factor in % for increasing the capacity when there's not
	 *            enough room in this list anymore
	 */
    public ArrayList(int initialCapacity, int growthFactor) {
        this.storedObjects = new Object[initialCapacity];
        this.growthFactor = growthFactor;
    }

    /**
	 * Retrieves the current size of this array list.
	 * 
	 * @return the number of stored elements in this list.
	 */
    public int size() {
        return this.size;
    }

    /**
	 * Determines whether the given element is stored in this list.
	 * 
	 * @param element
	 *            the element which might be stored in this list
	 * @return true when the given element is stored in this list
	 * @throws IllegalArgumentException
	 *             when the given element is null
	 * @see #remove(Object)
	 */
    public boolean contains(Object element) {
        if (element == null) {
            throw new IllegalArgumentException("ArrayList cannot contain a null element.");
        }
        for (int i = 0; i < this.size; i++) {
            Object object = this.storedObjects[i];
            if (object.equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Retrieves the index of the given object.
	 * 
	 * @param element
	 *            the object which is part of this list.
	 * @return the index of the object or -1 when the object is not part of this
	 *         list.
	 * @throws IllegalArgumentException
	 *             when the given element is null
	 */
    public int indexOf(Object element) {
        if (element == null) {
            throw new IllegalArgumentException("ArrayList cannot contain a null element.");
        }
        for (int i = 0; i < this.size; i++) {
            Object object = this.storedObjects[i];
            if (object.equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index
	 *            the position of the desired element.
	 * @return the element stored at the given position
	 * @throws IndexOutOfBoundsException
	 *             when the index < 0 || index >= size()
	 */
    public Object get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
        }
        return this.storedObjects[index];
    }

    /**
	 * Removes the element at the specified position in this list.
	 * 
	 * @param index
	 *            the position of the desired element.
	 * @return the element stored at the given position
	 * @throws IndexOutOfBoundsException
	 *             when the index < 0 || index >= size()
	 */
    public Object remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
        }
        Object removed = this.storedObjects[index];
        for (int i = index + 1; i < this.size; i++) {
            this.storedObjects[i - 1] = this.storedObjects[i];
        }
        this.size--;
        this.storedObjects[this.size] = null;
        return removed;
    }

    public void removeAll() {
        for (int i = size - 1; i >= 0; i--) {
            remove(i);
        }
    }

    /**
	 * Removes the given element.
	 * 
	 * @param element
	 *            the element which should be removed.
	 * @return true when the element was found in this list.
	 * @throws IllegalArgumentException
	 *             when the given element is null
	 * @see #contains(Object)
	 */
    public boolean remove(Object element) {
        if (element == null) {
            throw new IllegalArgumentException("ArrayList cannot contain null.");
        }
        int index = -1;
        for (int i = 0; i < this.size; i++) {
            Object object = this.storedObjects[i];
            if (object.equals(element)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false;
        }
        for (int i = index + 1; i < this.size; i++) {
            this.storedObjects[i - 1] = this.storedObjects[i];
        }
        this.size--;
        this.storedObjects[this.size] = null;
        return true;
    }

    /**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns.
	 */
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.storedObjects[i] = null;
        }
        this.size = 0;
    }

    /**
	 * Stores the given element in this list.
	 * 
	 * @param element
	 *            the element which should be appended to this list.
	 * @throws IllegalArgumentException
	 *             when the given element is null
	 * @see #add( int, Object )
	 */
    public void add(Object element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("ArrayList cannot contain null.");
        }
        if (this.size >= this.storedObjects.length) {
            increaseCapacity();
        }
        this.storedObjects[this.size] = element;
        this.size++;
    }

    /**
	 * Inserts the given element at the defined position. Any following elements
	 * are shifted one position to the back.
	 * 
	 * @param index
	 *            the position at which the element should be inserted, use 0
	 *            when the element should be inserted in the front of this list.
	 * @param element
	 *            the element which should be inserted
	 * @throws IllegalArgumentException
	 *             when the given element is null
	 * @throws IndexOutOfBoundsException
	 *             when the index < 0 || index >= size()
	 */
    public void add(int index, Object element) {
        if (element == null) {
            throw new IllegalArgumentException("ArrayList cannot contain null.");
        }
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
        }
        if (this.size >= this.storedObjects.length) {
            increaseCapacity();
        }
        for (int i = this.size; i > index; i--) {
            this.storedObjects[i] = this.storedObjects[i - 1];
        }
        this.storedObjects[index] = element;
        this.size++;
    }

    /**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * 
	 * @param index
	 *            the position of the element, the first element has the index
	 *            0.
	 * @param element
	 *            the element which should be set
	 * @return the replaced element
	 * @throws IndexOutOfBoundsException
	 *             when the index < 0 || index >= size()
	 */
    public Object set(int index, Object element) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
        }
        Object replaced = this.storedObjects[index];
        this.storedObjects[index] = element;
        return replaced;
    }

    /**
	 * Returns String containing the String representations of all objects of
	 * this list.
	 * 
	 * @return the stored elements in a String representation.
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer(this.size * 23);
        buffer.append(super.toString()).append("{\n");
        for (int i = 0; i < this.size; i++) {
            buffer.append(this.storedObjects[i]);
            buffer.append('\n');
        }
        buffer.append('}');
        return buffer.toString();
    }

    /**
	 * Returns all stored elements as an array.
	 * 
	 * @return the stored elements as an array.
	 */
    public Object[] toArray() {
        Object[] copy = new Object[this.size];
        System.arraycopy(this.storedObjects, 0, copy, 0, this.size);
        return copy;
    }

    /**
	 * Returns all stored elements in the given array.
	 * 
	 * @param target
	 *            the array in which the stored elements should be copied.
	 * @return the stored elements of this list
	 */
    public Object[] toArray(Object[] target) {
        System.arraycopy(this.storedObjects, 0, target, 0, this.size);
        return target;
    }

    /**
	 * Trims the capacity of this ArrayList instance to be the list's current
	 * size. An application can use this operation to minimize the storage of an
	 * ArrayList instance.
	 */
    public void trimToSize() {
        if (this.storedObjects.length != this.size) {
            Object[] newStore = new Object[this.size];
            System.arraycopy(this.storedObjects, 0, newStore, 0, this.size);
            this.storedObjects = newStore;
        }
    }

    /**
	 * increases the capacity of this list.
	 */
    private void increaseCapacity() {
        int currentCapacity = this.storedObjects.length;
        int newCapacity = currentCapacity + ((currentCapacity * this.growthFactor) / 100);
        if (newCapacity == currentCapacity) {
            newCapacity++;
        }
        Object[] newStore = new Object[newCapacity];
        System.arraycopy(this.storedObjects, 0, newStore, 0, this.size);
        this.storedObjects = newStore;
    }

    /**
	 * Retrieves the internal array - use with care! This method allows to
	 * access stored objects without creating an intermediate array. You really
	 * should refrain from changing any elements in the returned array unless
	 * you are 110% sure about what you are doing. It is safe to cycle through
	 * this array to access it's elements, though. Note that some array
	 * positions might contain null. Also note that the internal array is
	 * changed whenever this list has to be increased.
	 * 
	 * @return the internal array
	 */
    public Object[] getInternalArray() {
        return this.storedObjects;
    }

    public Object lastElement() {
        if (storedObjects == null || size <= 0) return null; else return storedObjects[size - 1];
    }
}
