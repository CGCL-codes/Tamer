package com.naildrivin5.inlaw;

import java.util.*;

/** A comparator to sort items by their name
 */
public class ItemNameComparator implements Comparator<Item> {

    public int compare(Item i1, Item i2) {
        if (i1 == i2) return 0;
        if (i1 == null) return -1;
        if (i2 == null) return 1;
        if (i1.equals(i2)) return 0;
        return i1.getName().compareTo(i2.getName());
    }
}
