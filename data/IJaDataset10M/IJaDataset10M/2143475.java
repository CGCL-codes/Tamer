package org.jfcx.util.compare;

import java.util.Comparator;

/**
 * DoubleComparator.java
 * <p>
 * Compares instances of Double.
 * 
 * @author Matt Linde
 * 
 * @since Apr 19, 2005
 */
public class DoubleComparator implements Comparator {

    /**
     *  
     */
    public DoubleComparator() {
        super();
    }

    public int compare(Object first, Object second) {
        return ((Double) first).compareTo((Double) second);
    }
}
