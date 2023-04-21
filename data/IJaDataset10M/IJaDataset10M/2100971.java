package br.unifor.metahlib.base;

/**
 * Class implementing some simple utility methods. Some methods here are the
 * same found in Weka software {@link http://www.cs.waikato.ac.nz/ml/weka/}.
 * 
 * @author Eibe Frank
 * @author Yong Wang
 * @author Len Trigg
 * @author Julien Prados
 * @author Nathanael de Castro Costa
 */
public class Utils {

    /**
	 * Sorts a given array of doubles in ascending order and returns an array of
	 * integers with the positions of the elements of the original array in the
	 * sorted array. NOTE THESE CHANGES: the sort is no longer stable and it
	 * doesn't use safe floating-point comparisons anymore. Occurrences of
	 * Double.NaN are treated as Double.MAX_VALUE
	 * 
	 * @param array
	 *            this array is not changed by the method!
	 * @return an array of integers with the positions in the sorted array.
	 */
    public static int[] sort(double[] array) {
        int[] index = new int[array.length];
        array = (double[]) array.clone();
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
            if (Double.isNaN(array[i])) {
                array[i] = Double.MAX_VALUE;
            }
        }
        quickSort(array, index, 0, array.length - 1);
        return index;
    }

    private static void quickSort(double[] array, int[] index, int left, int right) {
        if (left < right) {
            int middle = partition(array, index, left, right);
            quickSort(array, index, left, middle);
            quickSort(array, index, middle + 1, right);
        }
    }

    /**
	 * Partitions the instances around a pivot. Used by quicksort and
	 * kthSmallestValue.
	 * 
	 * @param array
	 *            the array of doubles to be sorted
	 * @param index
	 *            the index into the array of doubles
	 * @param l
	 *            the first index of the subset
	 * @param r
	 *            the last index of the subset
	 * @return the index of the middle element
	 */
    private static int partition(double[] array, int[] index, int l, int r) {
        double pivot = array[index[(l + r) / 2]];
        int help;
        while (l < r) {
            while ((array[index[l]] < pivot) && (l < r)) {
                l++;
            }
            while ((array[index[r]] > pivot) && (l < r)) {
                r--;
            }
            if (l < r) {
                help = index[l];
                index[l] = index[r];
                index[r] = help;
                l++;
                r--;
            }
        }
        if ((l == r) && (array[index[r]] > pivot)) {
            r--;
        }
        return r;
    }

    /**
	 * Computes the mean for an array of doubles.
	 * 
	 * @param vector
	 *            the array
	 * @return the mean
	 */
    public static double mean(double[] vector) {
        double sum = 0;
        if (vector.length == 0) {
            return 0;
        }
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum / (double) vector.length;
    }

    /**
	 * Normalizes the doubles in the array by their sum.
	 * 
	 * @param doubles
	 *            the array of double
	 * @exception IllegalArgumentException
	 *                if sum is Zero or NaN
	 */
    public static void normalize(double[] doubles) {
        double sum = 0;
        for (int i = 0; i < doubles.length; i++) {
            sum += doubles[i];
        }
        normalize(doubles, sum);
    }

    /**
	 * Normalizes the doubles in the array using the given value.
	 * 
	 * @param doubles
	 *            the array of double
	 * @param sum
	 *            the value by which the doubles are to be normalized
	 * @exception IllegalArgumentException
	 *                if sum is zero or NaN
	 */
    public static void normalize(double[] doubles, double sum) {
        if (Double.isNaN(sum)) {
            throw new IllegalArgumentException("Can't normalize array. Sum is NaN.");
        }
        if (sum == 0) {
            throw new IllegalArgumentException("Can't normalize array. Sum is zero.");
        }
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] /= sum;
        }
    }
}
