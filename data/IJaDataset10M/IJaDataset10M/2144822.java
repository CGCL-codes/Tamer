package com.ibm.realtime.flexotask.util;

import com.ibm.realtime.flexotask.validation.StableArray;

/**
 * Encapsulates a double[] to reside in stable storage in cases when primitive arrays are not allowed there
 */
public class StableDoubleArray extends StableArray {

    /**
   * Create a new encapsulated double array
   * @param size the size of the array
   */
    public StableDoubleArray(int size) {
        super(double.class, size);
    }

    /**
   * Get a value from the encapsulated array
   * @param index the index where to get the value
   * @return the value
   */
    public double get(int index) {
        return ((double[]) getArray())[index];
    }

    /**
   * Set a value in the encapsulated array
   * @param index the index where to set the value
   * @param value the value to set
   */
    public void set(int index, double value) {
        ((double[]) getArray())[index] = value;
    }
}
