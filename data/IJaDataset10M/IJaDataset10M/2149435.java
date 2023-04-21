package com.ibm.realtime.flexotask.util;

import com.ibm.realtime.flexotask.validation.StableArray;

/**
 * Encapsulates a char[] to reside in stable storage in cases when primitive arrays are not allowed there
 */
public class StableCharArray extends StableArray {

    /**
   * Create a new encapsulated char array
   * @param size the size of the array
   */
    public StableCharArray(int size) {
        super(char.class, size);
    }

    /**
   * Get a value from the encapsulated array
   * @param index the index where to get the value
   * @return the value
   */
    public char get(int index) {
        return ((char[]) getArray())[index];
    }

    /**
   * Set a value in the encapsulated array
   * @param index the index where to set the value
   * @param value the value to set
   */
    public void set(int index, char value) {
        ((char[]) getArray())[index] = value;
    }
}
