package org.happy.commons.util;

/**
 * helper class to create some arrays for testing
 * 
 * @author Andreas Hollmann, Wjatscheslaw Stoljarski, Eugen Lofing
 * 
 */
public class ArraysHelper {

    /**
	 * clones the deeplly the array
	 * 
	 * @param a
	 * @return
	 */
    public static Integer[] clone(Integer[] a) {
        Integer[] a_copy = new Integer[a.length];
        for (int i = 0; i < a.length; i++) a_copy[i] = new Integer(a[i]);
        return a_copy;
    }

    /**
	 * creates integer array with random elements
	 * 
	 * @param arrayLength
	 *            length of array
	 * @param max
	 *            max random value
	 * @param min
	 *            min random value
	 * @return
	 */
    public static Integer[] createRandomArray(int arrayLength, int min, int max) {
        Integer[] a = new Integer[arrayLength];
        for (int i = 0; i < arrayLength; i++) a[i] = (int) (Math.random() * (max - min) + min);
        return a;
    }

    /**
	 * sum all elements in the array
	 * 
	 * @param integers
	 * @return
	 */
    public static long sum(Integer[] integers) {
        long sum = 0;
        for (int elem : integers) sum += elem;
        return sum;
    }

    /**
	 * multiply all integers in the integer array
	 * 
	 * @param numbers
	 *            array with integer numbers
	 * @return
	 */
    public static long mult(Integer[] numbers) {
        long result = 1;
        for (int n : numbers) {
            result *= n;
        }
        return result;
    }

    /**
	 * solves the order of elements in an array, if the array is sorted then the
	 * order is maximal, if it is inverse sorted it is minimal Usually array is
	 * randomly unsorted, in that case the sortedOrder is about zerro
	 * 
	 * @param a
	 * @return
	 */
    public static long sortedOrder(Integer[] a) {
        long r = 0;
        for (int i = 1; i < a.length; i++) {
            r += a[i] - a[i - 1];
        }
        return r;
    }
}
