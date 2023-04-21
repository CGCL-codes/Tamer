package org.dict.kernel;

/**
 * Insert the type's description here.
 * Creation date: (28.08.01 22:39:16)
 * @author: Administrator
 */
public class ListUtil {

    private static void mergeSort(Object src[], Object dest[], int low, int high, IComparator c) {
        int length = high - low;
        if (length < 7) {
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

    /**
 * search method comment.
 */
    public static int search(IList ls, Object k, IComparator c) {
        return search(ls, k, c, 0, ls.size() - 1);
    }

    public static int search(IList ls, Object k, IComparator c, int low, int high) {
        while (low <= high) {
            int mid = (low + high) / 2;
            Object midVal = ls.get(mid);
            int cmp = c.compare(midVal, k);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }

    public static void sort(Object[] a, IComparator c) {
        Object aux[] = (Object[]) a.clone();
        mergeSort(aux, a, 0, a.length, c);
    }

    /**
 * Swaps x[a] with x[b].
 */
    private static void swap(Object x[], int a, int b) {
        Object t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
}
