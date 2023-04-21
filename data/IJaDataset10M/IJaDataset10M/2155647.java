package javasrc.util;

import java.util.*;
import cmp.QuickSort.*;

public class JSCollections {

    public static Vector sortEnumeration(Enumeration unsortedItems) {
        Object element;
        Vector sortedItems;
        JSComparable value;
        if (unsortedItems == null) return (null);
        sortedItems = new Vector();
        while (unsortedItems.hasMoreElements()) {
            element = unsortedItems.nextElement();
            if (element instanceof JSComparable == false) return (null);
            value = (JSComparable) element;
            sortedItems.addElement(value);
        }
        Object[] a = new Object[sortedItems.size()];
        for (int i = 0; i < a.length; i++) a[i] = sortedItems.elementAt(i);
        QSComparator compare = new QSComparator();
        QuickSort.sort(a, compare);
        sortedItems.removeAllElements();
        for (int i = 0; i < a.length; i++) sortedItems.addElement(a[i]);
        return (sortedItems);
    }

    public static Object[] sortVector(Vector unsortedItems) {
        Object element;
        Vector sortedItems;
        JSComparable value;
        if (unsortedItems == null) return (null);
        Object[] a = new Object[unsortedItems.size()];
        for (int i = 0; i < a.length; i++) a[i] = unsortedItems.elementAt(i);
        QSComparator compare = new QSComparator();
        QuickSort.sort(a, compare);
        return (a);
    }
}

class QSComparator implements cmp.QuickSort.Comparator {

    public int compare(Object a, Object b) {
        return (((JSComparable) a).compareTo((JSComparable) b));
    }

    public boolean equals(Object a, Object b) {
        if (((JSComparable) a).compareTo((JSComparable) b) == 0) return true; else return false;
    }
}
