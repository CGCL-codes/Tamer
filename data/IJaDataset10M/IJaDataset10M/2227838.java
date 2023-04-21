package com.bulletphysics.linearmath;

import java.util.Comparator;
import java.util.List;
import com.bulletphysics.util.FloatArrayList;
import com.bulletphysics.util.IntArrayList;

/**
 * Miscellaneous utility functions.
 * 
 * @author jezek2
 */
public class MiscUtil {

    public static int getListCapacityForHash(List<?> list) {
        return getListCapacityForHash(list.size());
    }

    public static int getListCapacityForHash(int size) {
        int n = 2;
        while (n < size) {
            n <<= 1;
        }
        return n;
    }

    /**
	 * Ensures valid index in provided list by filling list with provided values
	 * until the index is valid.
	 */
    public static <T> void ensureIndex(List<T> list, int index, T value) {
        while (list.size() <= index) {
            list.add(value);
        }
    }

    /**
	 * Resizes list to exact size, filling with given value when expanding.
	 */
    public static void resize(IntArrayList list, int size, int value) {
        while (list.size() < size) {
            list.add(value);
        }
        while (list.size() > size) {
            list.remove(list.size() - 1);
        }
    }

    /**
	 * Resizes list to exact size, filling with given value when expanding.
	 */
    public static void resize(FloatArrayList list, int size, float value) {
        while (list.size() < size) {
            list.add(value);
        }
        while (list.size() > size) {
            list.remove(list.size() - 1);
        }
    }

    /**
	 * Resizes list to exact size, filling with new instances of given class type
	 * when expanding.
	 */
    public static <T> void resize(List<T> list, int size, Class<T> valueCls) {
        try {
            while (list.size() < size) {
                list.add(valueCls != null ? valueCls.newInstance() : null);
            }
            while (list.size() > size) {
                list.remove(list.size() - 1);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
	 * Searches object in array.
	 * 
	 * @return first index of match, or -1 when not found
	 */
    public static <T> int indexOf(T[] array, T obj) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == obj) return i;
        }
        return -1;
    }

    public static float GEN_clamped(float a, float lb, float ub) {
        return a < lb ? lb : (ub < a ? ub : a);
    }

    private static <T> void downHeap(List<T> pArr, int k, int n, Comparator<T> comparator) {
        T temp = pArr.get(k - 1);
        while (k <= n / 2) {
            int child = 2 * k;
            if ((child < n) && comparator.compare(pArr.get(child - 1), pArr.get(child)) < 0) {
                child++;
            }
            if (comparator.compare(temp, pArr.get(child - 1)) < 0) {
                pArr.set(k - 1, pArr.get(child - 1));
                k = child;
            } else {
                break;
            }
        }
        pArr.set(k - 1, temp);
    }

    /**
	 * Sorts list using heap sort.<p>
	 * 
	 * Implementation from: http://www.csse.monash.edu.au/~lloyd/tildeAlgDS/Sort/Heap/
	 */
    public static <T> void heapSort(List<T> list, Comparator<T> comparator) {
        int k;
        int n = list.size();
        for (k = n / 2; k > 0; k--) {
            downHeap(list, k, n, comparator);
        }
        while (n >= 1) {
            swap(list, 0, n - 1);
            n = n - 1;
            downHeap(list, 1, n, comparator);
        }
    }

    private static <T> void swap(List<T> list, int index0, int index1) {
        T temp = list.get(index0);
        list.set(index0, list.get(index1));
        list.set(index1, temp);
    }

    /**
	 * Sorts list using quick sort.<p>
	 */
    public static <T> void quickSort(List<T> list, Comparator<T> comparator) {
        if (list.size() > 1) {
            quickSortInternal(list, comparator, 0, list.size() - 1);
        }
    }

    private static <T> void quickSortInternal(List<T> list, Comparator<T> comparator, int lo, int hi) {
        int i = lo, j = hi;
        T x = list.get((lo + hi) / 2);
        do {
            while (comparator.compare(list.get(i), x) < 0) i++;
            while (comparator.compare(x, list.get(j)) < 0) j--;
            if (i <= j) {
                swap(list, i, j);
                i++;
                j--;
            }
        } while (i <= j);
        if (lo < j) {
            quickSortInternal(list, comparator, lo, j);
        }
        if (i < hi) {
            quickSortInternal(list, comparator, i, hi);
        }
    }
}
