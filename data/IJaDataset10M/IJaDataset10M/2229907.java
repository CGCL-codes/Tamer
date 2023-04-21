package org.thenesis.planetino2.util;

import java.util.Vector;

/**
 * A quick sort demonstration algorithm
 * SortAlgorithm.java
 *
 * @author James Gosling
 * @author Kevin A. Smith
 * @author Guillaume Legris, Mathieu Legris
 */
public class QSortAlgorithm {

    /** This is a generic version of C.A.R Hoare's Quick Sort
	 * algorithm.  This will handle arrays that are already
	 * sorted, and arrays with duplicate keys.<BR>
	 *
	 * If you think of a one dimensional array as going from
	 * the lowest index on the left to the highest index on the right
	 * then the parameters to this function are lowest index or
	 * left and highest index or right.  The first time you call
	 * this function it will be with the parameters 0, a.length - 1.
	 *
	 * @param a       an integer array
	 * @param lo0     left boundary of array partition
	 * @param hi0     right boundary of array partition
	 */
    static void QuickSort(Vector v, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        Comparable mid;
        if (hi0 > lo0) {
            mid = (Comparable) v.elementAt((lo0 + hi0) / 2);
            while (lo <= hi) {
                while ((lo < hi0) && (((Comparable) v.elementAt(lo)).compareTo(mid) < 0)) {
                    ++lo;
                }
                while ((hi > lo0) && (((Comparable) v.elementAt(hi)).compareTo(mid) > 0)) {
                    --hi;
                }
                if (lo <= hi) {
                    swap(v, lo, hi);
                    ++lo;
                    --hi;
                }
            }
            if (lo0 < hi) QuickSort(v, lo0, hi);
            if (lo < hi0) QuickSort(v, lo, hi0);
        }
    }

    private static void swap(Vector v, int i, int j) {
        Object tmp = v.elementAt(i);
        v.setElementAt(v.elementAt(j), i);
        v.setElementAt(tmp, j);
    }

    public static void sort(Vector v) {
        QuickSort(v, 0, v.size() - 1);
    }
}
