package org.plr.visual;

import java.util.Comparator;

public class ColumnSorter implements Comparator<Object> {

    int colIndex;

    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else if (o1 instanceof Comparable) {
            return extracted(o1, o2);
        } else {
            return o1.toString().compareTo(o2.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private int extracted(Object o1, Object o2) {
        return ((Comparable) o1).compareTo(o2);
    }
}
