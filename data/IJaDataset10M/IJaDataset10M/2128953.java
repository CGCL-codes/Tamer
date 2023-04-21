package sk.bielyvlk.gpsdb;

import java.util.Vector;

/**
 * Static class used for providing static methods for sorting arrays of Objects
 * @see java.util.Collections
 */
public class Sort {

    private static void mergeSort(Object src[], Object dest[], int low, int high, Comparator c) {
        int length = high - low;
        if (length < 3) {
            for (int i = low; i < high; i++) for (int j = i; j > low && c.compare(dest[j - 1], dest[j]) > 0; j--) swap(dest, j, j - 1);
            return;
        }
        int mid = (low + high) / 2;
        mergeSort(dest, src, low, mid, c);
        mergeSort(dest, src, mid, high, c);
        if (c.compare(src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, low, dest, low, length);
            return;
        }
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0) dest[i] = src[p++]; else dest[i] = src[q++];
        }
    }

    private static void swap(Object x[], int a, int b) {
        Object t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static void mergeSort(Object[] a, Comparator c) {
        mergeSort(a, a, 0, a.length, c);
    }

    public static void mergeSort(Vector v, Comparator c) {
        if (v.size() <= 1) return;
        Object[] src = new Object[v.size()];
        v.copyInto(src);
        Object[] dest = new Object[v.size()];
        v.copyInto(dest);
        mergeSort(src, dest, 0, src.length, c);
        for (int i = 0; i < dest.length; i++) {
            v.setElementAt(dest[i], i);
        }
    }

    public static void mergeSortUnique(Vector v, Comparator c) {
        Object[] a = new Object[v.size()];
        v.copyInto(a);
        mergeSort(a, a, 0, a.length, c);
        Object current = null;
        v = new Vector();
        for (int i = 0; i < a.length; i++) {
            if (current == null || c.compare(a[i], current) != 0) {
                v.addElement(a[i]);
                current = a[i];
            }
        }
    }
}
