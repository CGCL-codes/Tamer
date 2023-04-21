package de.toolforge.googlechartwrapper.util;

/**
 * Operations on arrays
 * @author martin
 *
 */
public class ArrayUtils {

    /**
	 * Performs a linear search through the object array and returns 
	 * the index which of the searched element. 
	 * If the specified object is not a object in the array, returns -1.
	 * @param array object array where to search for the object
	 * @param searched object to search
	 * @return index of the searched index, 
	 * where <code>array[index].equals(searched)</code> is <code>true</code>; 
	 * <code>-1</code> otherwise
	 */
    public static int linearSearch(Object[] array, Object searched) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].equals(searched)) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * Returns the greatest value in the array. If the array is empty,
	 * {@link Integer#MIN_VALUE} is returned. 
	 * @param values array where to find the greatest value
	 * @return greatest value of the array
	 */
    public static int maxValue(int[] values) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) max = values[i];
        }
        return max;
    }

    /**
	 * Returns the smallest value in the array. If the array is empty,
	 * {@link Integer#MAX_VALUE} is returned. 
	 * @param values array where to find the greatest value
	 * @return greatest value of the array
	 */
    public static int minValue(int[] values) {
        int max = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < max) max = values[i];
        }
        return max;
    }

    /**
	 * Returns the greatest value in the array. If the array is empty,
	 * {@link Float#MIN_VALUE} is returned. 
	 * @param values array where to find the greatest value
	 * @return greatest value of the array
	 */
    public static float maxValue(float[] values) {
        float max = Float.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) max = values[i];
        }
        return max;
    }
}
