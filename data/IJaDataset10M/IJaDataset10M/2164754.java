package com.loribel.commons.util.comparator;

import java.util.Comparator;

/**
 * Abstract Comparator to sort ascending and descending.
 */
public abstract class GB_Comparator implements Comparator {

    /**
     * True to use ascending order.
     */
    private boolean ascending = true;

    private int nullPosition = 1;

    private boolean treatNull = true;

    /**
     * Constructor of ABBComparator without parameter.
     */
    public GB_Comparator() {
        super();
    }

    public GB_Comparator(boolean a_ascending) {
        super();
        ascending = a_ascending;
    }

    /**
     * Compare two objects.
     *
     * @param o1 Object - the object 1 to compare to object 2
     * @param o2 Object - the object 2 to compare to object 1
     * @return int
     */
    public final int compare(Object o1, Object o2) {
        if (treatNull) {
            if (o1 == null) {
                if (o2 == null) {
                    return 0;
                } else {
                    return nullPosition;
                }
            }
            if (o2 == null) {
                return -nullPosition;
            }
        }
        int r = compareAscending(o1, o2);
        if (ascending) {
            return r;
        } else {
            return -r;
        }
    }

    /**
     * Compare two objects.
     *
     * @param o1 Object - the object 1 to compare to object 2
     * @param o2 Object - the object 2 to compare to object 1
     * @return int
     */
    public abstract int compareAscending(Object o1, Object o2);

    public boolean isAscending() {
        return ascending;
    }

    public boolean isNullFirst() {
        return (nullPosition == -1);
    }

    public boolean isTreatNull() {
        return treatNull;
    }

    public void setAscending(boolean a_ascending) {
        ascending = a_ascending;
    }

    public void setNullFirst(boolean a_flag) {
        if (a_flag) {
            nullPosition = -1;
        } else {
            nullPosition = 1;
        }
    }

    public void setTreatNull(boolean a_treatNull) {
        treatNull = a_treatNull;
    }
}
