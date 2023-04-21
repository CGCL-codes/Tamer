package com.intel.util.strings;

import java.util.Comparator;

/**
 * @author Ralf Ratering
 * @version $Id$
 */
public class StringComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
