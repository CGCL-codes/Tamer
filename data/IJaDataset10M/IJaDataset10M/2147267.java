package org.opensourcephysics.stp.util;

/**
 * Dynamic int arrays.
 * @author Kipton Barros
 */
public class IntArray {

    private int length;

    private int[] data;

    /**
  * Constructs a new DoubleArray
  */
    public IntArray() {
        data = new int[32];
        length = 0;
    }

    /**
  * Returns the length of the array
  */
    public int length() {
        return length;
    }

    /**
  * Appends a value
  *
  * @param x
  */
    public void append(int x) {
        if (length >= data.length) {
            increaseCapacity();
        }
        data[length++] = x;
    }

    /**
  * Returns the last element, and decrements the array
  */
    public int unappend() {
        if (length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return data[--length];
    }

    /**
  * Gets an indexed value
  *
  * @param i
  * @return array[i]
  */
    public int get(int i) {
        if (i >= length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return data[i];
    }

    /**
  * Sets value v to index i.  Grows the array by one if i=length()
  *
  * @param i
  * @param v
  */
    public void set(int i, int v) {
        if (i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        data[i] = v;
    }

    /**
  * Gets the entire array as an int[]
  *
  * @return Entire contents of array
  */
    public int[] getArray() {
        int[] ret = new int[length];
        System.arraycopy(data, 0, ret, 0, length);
        return ret;
    }

    /**
  * Returns true if array contains value
  *
  * @param v
  * return boolean
  */
    public boolean contains(int v) {
        for (int i = 0; i < length; i++) {
            if (data[i] == v) {
                return true;
            }
        }
        return false;
    }

    private void increaseCapacity() {
        int[] temp = new int[2 * length];
        System.arraycopy(data, 0, temp, 0, length);
        data = temp;
    }
}
