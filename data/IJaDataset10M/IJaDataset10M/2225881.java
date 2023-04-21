package org.simplecart.base;

import java.util.Comparator;

/**
 *
 * @author Daniel Watrous
 */
public class PrimaryKeyComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        int result;
        Persistent pobj1 = (Persistent) obj1;
        Persistent pobj2 = (Persistent) obj2;
        return pobj1.getId().compareTo(pobj2.getId());
    }

    public boolean equals(Object obj) {
        return this.equals(obj);
    }
}
