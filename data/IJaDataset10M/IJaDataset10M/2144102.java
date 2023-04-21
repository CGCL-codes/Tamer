package org.expasy.jpl.commons.collection.sorter;

import java.util.Comparator;

/**
 * Objects implementing this interface can compare two arguments values for
 * order, via the method to implement {@code int compare(Integer o1, Integer
 * o2)}, found at given indices of a given array.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public interface ArrayIndexComparator extends Comparator<Integer> {

    /** the number of values to sort */
    int getArraySize();

    /** get the array to sort */
    Object getArray();

    /** set the array to sort */
    void setArray(Object array);
}
