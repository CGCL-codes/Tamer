package javax.media.ding3d.internal;

/**
 * The FastVector object is a growable array of ints.  It's much faster
 * than the Java Vector class because it isn't synchronized.  This class
 * was created because it is needed in several places by graphics
 * utilities.
 */
public class FastVector {

    private int data[];

    private int capacity;

    private int increment;

    private int size;

    /**
   * Add an element to the end of the array.
   */
    public void addElement(int element) {
        if (size >= capacity) {
            capacity += (increment == 0) ? capacity : increment;
            int newData[] = new int[capacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
        data[size++] = element;
    }

    /**
   * Get number of ints currently stored in the array;
   */
    public int getSize() {
        return size;
    }

    /**
   * Get access to array data
   */
    public int[] getData() {
        return data;
    }

    /**
   * Constructor.
   * @param initialCapacity Number of ints the object can hold
   * without reallocating the array.
   * @param capacityIncrement Once the array has grown beyond
   * its capacity, how much larger the reallocated array should be.
   */
    public FastVector(int initialCapacity, int capacityIncrement) {
        data = new int[initialCapacity];
        capacity = initialCapacity;
        increment = capacityIncrement;
        size = 0;
    }

    /**
   * Constructor.
   * When the array runs out of space, its size is doubled.
   * @param initialCapacity Number of ints the object can hold
   * without reallocating the array.
   */
    public FastVector(int initialCapacity) {
        data = new int[initialCapacity];
        capacity = initialCapacity;
        increment = 0;
        size = 0;
    }

    /**
   * Constructor.
   * The array is constructed with initial capacity of one integer.
   * When the array runs out of space, its size is doubled.
   */
    public FastVector() {
        data = new int[1];
        capacity = 1;
        increment = 0;
        size = 0;
    }
}
