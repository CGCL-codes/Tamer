package org.openscience.nmrshiftdb.util;

import java.util.Comparator;

/**
 *This is implememtation of Comparator used for sorting ValueTriples accoring to the distance to a value given in ValueTriple as ComparatorValue
 *
 * @author     shk3
 * @created    25 July 2002
 */
public class ValueTripleComparator implements Comparator {

    boolean descending = false;

    /**
   *Constructor for the ValueTripleComparator object
   */
    public ValueTripleComparator() {
    }

    /**
   *Constructor for the ValueTripleComparator object
   *
   * @param  descending  Should sorting be done descending?
   */
    public ValueTripleComparator(boolean descending) {
        this.descending = descending;
    }

    /**
   *The compare method
   *
   * @param  obj1  The first ValueTriple
   * @param  obj2  The second ValueTriple
   * @return       -1,0,1
   */
    public int compare(Object obj1, Object obj2) {
        if (Math.abs(((ValueTriple) obj1).value1 - ((ValueTriple) obj1).comparatorValue) < Math.abs(((ValueTriple) obj2).value1 - ((ValueTriple) obj1).comparatorValue)) {
            if (descending) {
                return (1);
            } else {
                return (-1);
            }
        }
        if (Math.abs(((ValueTriple) obj1).value1 - ((ValueTriple) obj1).comparatorValue) > Math.abs(((ValueTriple) obj2).value1 - ((ValueTriple) obj1).comparatorValue)) {
            if (descending) {
                return (-1);
            } else {
                return (1);
            }
        }
        return (0);
    }
}
