package com.ryanm.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * A garbage-free randomised quicksort
 * 
 * @author ryanm
 */
public class QuickSort {

    private static final Random rng = new Random();

    private QuickSort() {
    }

    /**
	 * @param <T>
	 * @param array
	 * @param comp
	 */
    public static <T> void sort(T[] array, Comparator<T> comp) {
        sort(array, comp, 0, array.length - 1);
    }

    /**
	 * @param <T>
	 * @param array
	 * @param comp
	 * @param lo
	 *           lowest index to sort
	 * @param hi
	 *           highest index to sort
	 */
    public static <T> void sort(T[] array, Comparator<T> comp, int lo, int hi) {
        if (hi > lo) {
            int pivotIndex = rng.nextInt(hi - lo) + lo;
            pivotIndex = partition(array, comp, lo, hi, pivotIndex);
            sort(array, comp, lo, pivotIndex - 1);
            sort(array, comp, pivotIndex + 1, hi);
        }
    }

    /**
	 * Shuffle elements around to partially-sorted low/pivot/high order
	 * 
	 * @param <T>
	 * @param array
	 * @param comp
	 * @param lo
	 * @param hi
	 * @param pivotIndex
	 *           the starting index of the pivot element
	 * @return The new index of the pivot element
	 */
    private static <T> int partition(T[] array, Comparator<T> comp, int lo, int hi, int pivotIndex) {
        T pivot = array[pivotIndex];
        swap(array, pivotIndex, hi);
        int index = lo;
        for (int i = lo; i < hi; i++) {
            if (comp.compare(array[i], pivot) <= 0) {
                swap(array, i, index);
                index++;
            }
        }
        swap(array, hi, index);
        return index;
    }

    /**
	 * Swaps elements
	 * 
	 * @param <T>
	 * @param array
	 * @param i
	 * @param j
	 */
    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Integer[] array = new Integer[15];
        Random rng = new Random();
        int split = 10;
        for (int i = 0; i < split; i++) {
            array[i] = new Integer(rng.nextInt(100));
        }
        for (int i = split; i < array.length; i++) {
            array[i] = null;
        }
        System.out.println(Arrays.toString(array));
        QuickSort.sort(array, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }

            ;
        }, 0, split - 1);
        System.out.println(Arrays.toString(array));
    }
}
