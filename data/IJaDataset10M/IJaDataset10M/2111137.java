package mindprod;

import util.Comparator;

/**
 * QuickSort for objects
 */
public class QuickSort {

    private static final boolean DEBUGGING = false;

    private static final String EmbeddedCopyright = "copyright (c) 1996-2008 Roedy Green, Canadian Mind Products, http://mindprod.com";

    private Comparator comparator;

    private Object[] userArray;

    /**
     * sort the user's array
     *
     * @param userArray  Array of Objects to be sorted.
     * @param comparator Comparator delegate that can compare two Objects and tell which should come first.
     */
    public static void sort(Object[] userArray, Comparator comparator) {
        QuickSort h = new QuickSort();
        h.comparator = comparator;
        h.userArray = userArray;
        if (h.isAlreadySorted()) {
            return;
        }
        h.quicksort(0, userArray.length - 1);
        if (h.isAlreadySorted()) {
            return;
        }
        if (DEBUGGING) {
            if (!h.isAlreadySorted()) {
                System.out.println("Sort failed");
            }
        }
        return;
    }

    /**
     * dummy constructor to prevent its use. Use static method sort.
     */
    private QuickSort() {
    }

    private boolean isAlreadySorted() {
        for (int i = 1; i < userArray.length; i++) {
            if (comparator.compare(userArray[i], userArray[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    private int partition(int lo, int hi) {
        Object pivot = userArray[lo];
        while (true) {
            while (comparator.compare(userArray[hi], pivot) >= 0 && lo < hi) {
                hi--;
            }
            while (comparator.compare(userArray[lo], pivot) < 0 && lo < hi) {
                lo++;
            }
            if (lo < hi) {
                Object temp = userArray[lo];
                userArray[lo] = userArray[hi];
                userArray[hi] = temp;
            } else {
                return hi;
            }
        }
    }

    private void quicksort(int p, int r) {
        if (p < r) {
            int q = partition(p, r);
            if (q == r) {
                q--;
            }
            quicksort(p, q);
            quicksort(q + 1, r);
        }
    }
}
