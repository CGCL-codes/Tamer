package com.acv.dao.catalog;

import java.util.Comparator;

/**
 * Comparator of {@link ObjectWithRepOrder} objects.
 */
public class RepOrderComparator implements Comparator<ObjectWithRepOrder> {

    public int compare(com.acv.dao.catalog.ObjectWithRepOrder o1, com.acv.dao.catalog.ObjectWithRepOrder o2) {
        int compare = 0;
        if (o1 != null && o2 != null && o1.getRepOrder() != null && o2.getRepOrder() != null) {
            compare = o1.getRepOrder().compareTo(o2.getRepOrder());
        }
        if (compare == 0) {
            if (!o1.equals(o2)) {
                compare = 1;
            }
        }
        return compare;
    }
}
