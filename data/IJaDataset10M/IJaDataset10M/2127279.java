package dsc.util;

import java.util.*;

/** Quicksort for Sortable objects.
 *
 * @author Dodekaedron Software Creations, Inc. -- Wraith
 * @see Sortable
 */
public class Quicksort {

    public static final int CUTOFF = 20;

    /** Sorts vector into ascending order. Items in the vector must
   * implement Sortable interface.
   *
   * @param v Vector of Sortable items.
   * @see Sortable
   */
    public static void quicksort(Vector v) {
        v.trimToSize();
        if (v.size() < 2) return;
        doQuicksort(v, 0, v.size() - 1);
    }

    protected static Sortable median3(Vector v, int l, int r) {
        int center = (l + r) / 2;
        Object temp;
        if (((Sortable) v.elementAt(l)).greaterThan(((Sortable) v.elementAt(center))) > 0) {
            temp = v.elementAt(l);
            v.setElementAt(v.elementAt(center), l);
            v.setElementAt(temp, center);
        }
        if (((Sortable) v.elementAt(l)).greaterThan(((Sortable) v.elementAt(r))) > 0) {
            temp = v.elementAt(l);
            v.setElementAt(v.elementAt(r), l);
            v.setElementAt(temp, r);
        }
        if (((Sortable) v.elementAt(center)).greaterThan(((Sortable) v.elementAt(r))) > 0) {
            temp = v.elementAt(center);
            v.setElementAt(v.elementAt(r), center);
            v.setElementAt(temp, r);
        }
        temp = v.elementAt(center);
        v.setElementAt(v.elementAt(r - 1), center);
        v.setElementAt(temp, r - 1);
        return ((Sortable) v.elementAt(r - 1));
    }

    private static void doQuicksort(Vector v, int l, int r) {
        int i, j;
        Sortable pivot;
        if (l + CUTOFF < r) {
            pivot = median3(v, l, r);
            i = l;
            j = r - 1;
            for (; ; ) {
                while (((Sortable) v.elementAt(++i)).greaterThan(pivot) < 0) {
                }
                while (((Sortable) v.elementAt(--j)).greaterThan(pivot) > 0) {
                }
                if (i < j) {
                    Object temp = v.elementAt(i);
                    v.setElementAt(v.elementAt(j), i);
                    v.setElementAt(temp, j);
                } else break;
            }
            Object temp = v.elementAt(i);
            v.setElementAt(v.elementAt(r - 1), i);
            v.setElementAt(temp, r - 1);
            doQuicksort(v, l, i - 1);
            doQuicksort(v, i + 1, r);
        } else {
            InsertionSort.doInsertionSort(v, l, r);
        }
    }
}
