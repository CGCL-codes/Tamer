package org.ws4d.java.platform;

import java.util.NoSuchElementException;

/**
 * This interface provides vector functionality with byte arrays to a class.
 */
public interface IByteVector {

    /**
	 * Adds the specified component with given length to the end of this vector,
	 * increasing its size by one.
	 * 
	 * @param b byte[] to add.
	 * @param len length of the array.
	 */
    void addElement(byte[] b, int len);

    /**
	 * Adds the specified component to the end of this vector, increasing its
	 * size by one.
	 * 
	 * @param b byte[] to add.
	 */
    void addElement(byte[] b);

    /**
	 * Returns the number of components in this vector.
	 * 
	 * @return size the number of components in this vector.
	 */
    int size();

    /**
	 * Tests if this vector has no components.
	 * 
	 * @return true if and only if this vector has no components, that is, its
	 *         size is zero; false otherwise.
	 */
    boolean isEmpty();

    /**
	 * Removes all of the elements from this Vector.
	 */
    void clear();

    /**
	 * Removes the element at specified position.
	 * 
	 * @param index of the element.
	 */
    void removeElementAt(int index) throws ArrayIndexOutOfBoundsException;

    /**
	 * Removes all components from this vector and sets its size to zero.
	 */
    void removeAllElements();

    /**
	 * Returns the first component (the item at index 0) of this vector.
	 * 
	 * @return byte[] first component.
	 * @throws NoSuchElementException java.util.NoSuchElementException.
	 */
    byte[] firstElement() throws NoSuchElementException;

    /**
	 * Returns the last component of the vector.
	 * 
	 * @return byte[] last component.
	 * @throws NoSuchElementException java.util.NoSuchElementException
	 */
    byte[] lastElement() throws NoSuchElementException;

    /**
	 * Sets the component at the specified index of this vector to be the
	 * specified object. The previous component at that position is discarded.
	 * 
	 * @param b byte[] component at the specified index.
	 * @param index of the element.
	 * @throws ArrayIndexOutOfBoundsException index is out of range (index < 0 ||
	 *             index >= size()).
	 */
    void setElementAt(byte[] b, int index) throws ArrayIndexOutOfBoundsException;

    /**
	 * Returns the component at the specified index.
	 * 
	 * @param index of the element.
	 * @return byte[] component at the specified index.
	 * @throws ArrayIndexOutOfBoundsException index is out of range (index < 0 ||
	 *             index >= size()).
	 */
    byte[] elementAt(int index) throws ArrayIndexOutOfBoundsException;

    /**
	 * Removes all components from this vector and sets its size to zero. Return
	 * the used resources to the system.
	 */
    void dispose();
}
