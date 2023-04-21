package org.hsqldb.lib;

import java.util.Comparator;

/**
 * FastQSorts the [l,r] partition (inclusive) of the specfied array of
 * Rows, using the comparator.<p>
 *
 * Searches an ordered array.<p>
 *
 * @author Tony Lai
 * @author Fred Toussi
 * @version 1.9.0
 * @since 1.9.0
 */
public class ArraySort {

    /**
     * Returns the index of the lowest element == the given search target,
     * or -1
     * @return index or (- insert pos -1) if not found
     */
    public static int searchFirst(Object[] array, int start, int limit, Object value, Comparator c) {
        int low = start;
        int high = limit;
        int mid = start;
        int compare = 0;
        int found = limit;
        while (low < high) {
            mid = (low + high) / 2;
            compare = c.compare(value, array[mid]);
            if (compare < 0) {
                high = mid;
            } else if (compare > 0) {
                low = mid + 1;
            } else {
                high = mid;
                found = mid;
            }
        }
        return found == limit ? -low - 1 : found;
    }

    public static int deDuplicate(Object[] array, int start, int limit, Comparator comparator) {
        int baseIndex = start;
        int currentIndex = start + 1;
        for (; currentIndex < limit; currentIndex++) {
            int compare = comparator.compare(array[baseIndex], array[currentIndex]);
            if (compare == 0) {
                continue;
            }
            baseIndex++;
            array[baseIndex] = array[currentIndex];
        }
        return baseIndex + 1;
    }

    public static void sort(Object[] array, int start, int limit, Comparator comparator) {
        if (start + 1 >= limit) {
            return;
        }
        quickSort(array, comparator, start, limit - 1);
        insertionSort(array, comparator, start, limit - 1);
    }

    static void quickSort(Object[] array, Comparator comparator, int l, int r) {
        int M = 16;
        int i;
        int j;
        int v;
        if ((r - l) > M) {
            i = (r + l) / 2;
            if (comparator.compare(array[i], array[l]) < 0) {
                swap(array, l, i);
            }
            if (comparator.compare(array[r], array[l]) < 0) {
                swap(array, l, r);
            }
            if (comparator.compare(array[r], array[i]) < 0) {
                swap(array, i, r);
            }
            j = r - 1;
            swap(array, i, j);
            i = l;
            v = j;
            for (; ; ) {
                while (comparator.compare(array[++i], array[v]) < 0) {
                }
                while (comparator.compare(array[v], array[--j]) < 0) {
                }
                if (j < i) {
                    break;
                }
                swap(array, i, j);
            }
            swap(array, i, r - 1);
            quickSort(array, comparator, l, j);
            quickSort(array, comparator, i + 1, r);
        }
    }

    public static void insertionSort(Object[] array, Comparator comparator, int lo0, int hi0) {
        int i;
        int j;
        for (i = lo0 + 1; i <= hi0; i++) {
            j = i;
            while ((j > lo0) && comparator.compare(array[i], array[j - 1]) < 0) {
                j--;
            }
            if (i != j) {
                moveAndInsertRow(array, i, j);
            }
        }
    }

    private static void swap(Object[] array, int i1, int i2) {
        Object val = array[i1];
        array[i1] = array[i2];
        array[i2] = val;
    }

    private static void moveAndInsertRow(Object[] array, int i, int j) {
        Object val = array[i];
        moveRows(array, j, j + 1, i - j);
        array[j] = val;
    }

    private static void moveRows(Object[] array, int fromIndex, int toIndex, int rows) {
        System.arraycopy(array, fromIndex, array, toIndex, rows);
    }
}
