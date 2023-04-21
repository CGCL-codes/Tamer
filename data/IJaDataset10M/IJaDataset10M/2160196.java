package org.icehockeymanager.ihm.lib;

import java.io.*;

/**
 * Custom comparator
 * 
 * @author Arik Dasen
 * @created December 29, 2001
 */
public abstract class IhmCustomComparator implements Comparable<IhmCustomComparator>, Serializable, Cloneable {

    /** Description of the Field */
    protected int sortCriteria = 0;

    /** Description of the Field */
    protected boolean ascending = true;

    /** Constructor for the CustomComparator object */
    public IhmCustomComparator() {
    }

    /**
   * set sort criteria
   * 
   * @param sc
   *          The new sortCriteria value
   */
    public void setSortCriteria(int sc) {
        sortCriteria = sc;
    }

    /**
   * set sort orderd
   * 
   * @param ascending
   *          The new sortOrder value
   */
    public void setSortOrder(boolean ascending) {
        this.ascending = ascending;
    }

    /**
   * has to be implemented
   * 
   * @return The sortValue value
   */
    protected abstract double getSortValue();

    /**
   * implementation of Comparable
   * 
   * @param o
   *          Object to compare
   * @return Std compare value
   */
    public int compareTo(IhmCustomComparator o) {
        IhmCustomComparator tmp = o;
        if (ascending) {
            if (tmp.getSortValue() > getSortValue()) {
                return -1;
            }
            if (tmp.getSortValue() < getSortValue()) {
                return 1;
            }
        } else {
            if (tmp.getSortValue() > getSortValue()) {
                return 1;
            }
            if (tmp.getSortValue() < getSortValue()) {
                return -1;
            }
        }
        return 0;
    }

    /**
   * Clone the object
   * 
   * @return Clone of this object
   * @exception CloneNotSupportedException
   *              Clone not supportet Exception
   */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
