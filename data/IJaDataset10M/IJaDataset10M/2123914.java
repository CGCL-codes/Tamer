package com.lts.util;

public class ArraySorter {

    public static void sort(Object[] theData, CompareMethod c) {
        if (null == theData || theData.length <= 1) return;
        quickSort(theData, c, 0, theData.length - 1);
    }

    public static void quickSort(Object[] a, CompareMethod c, int start, int stop) {
        if (start >= stop) return;
        int low = start;
        int high = stop;
        Object pivot = a[(start + stop) / 2];
        a[(start + stop) / 2] = a[stop];
        a[stop] = pivot;
        while (low < high) {
            int theResult = c.compare(a[low], pivot);
            while (low < high && theResult <= 0) {
                low++;
                theResult = c.compare(a[low], pivot);
            }
            theResult = c.compare(a[high], pivot);
            while (low < high && theResult >= 0) {
                high--;
                theResult = c.compare(a[high], pivot);
            }
            if (low < high) {
                Object temp = a[low];
                a[low] = a[high];
                a[high] = temp;
            }
        }
        a[stop] = a[high];
        a[high] = pivot;
        quickSort(a, c, start, low - 1);
        quickSort(a, c, high + 1, stop);
    }
}
