package org.fonteditor.utilities.sort;

public class QuickSort {

    private static final boolean DEBUGGING = false;

    private Comparator comparator;

    private Object[] array;

    public static void sort(Object[] user_array, Comparator delegate) {
        QuickSort quick_sort = new QuickSort();
        quick_sort.comparator = delegate;
        quick_sort.array = user_array;
        if (quick_sort.isAlreadySorted()) {
            return;
        }
        quick_sort.sort(0, user_array.length - 1);
        if (DEBUGGING) {
            if (!quick_sort.isAlreadySorted()) {
                System.out.println("Sort failed");
            }
        }
    }

    private void sort(int p, int r) {
        if (p < r) {
            int q = partition(p, r);
            if (q == r) {
                q--;
            }
            sort(p, q);
            sort(q + 1, r);
        }
    }

    private int partition(int low, int high) {
        Object pivot = array[low];
        while (true) {
            while (comparator.compare(array[high], pivot) >= 0 && low < high) {
                high--;
            }
            while (comparator.compare(array[low], pivot) < 0 && low < high) {
                low++;
            }
            if (low < high) {
                Object temp = array[low];
                array[low] = array[high];
                array[high] = temp;
            } else {
                return high;
            }
        }
    }

    private boolean isAlreadySorted() {
        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i], array[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }
}
