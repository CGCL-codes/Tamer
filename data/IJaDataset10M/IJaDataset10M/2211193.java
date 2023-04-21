package org.xith3d.utility.general;

import java.util.Arrays;

/**
 * The ArrayHelper provides static methods to manage Arrays.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public final class ArrayHelper {

    /**
     * Dumps the contents of the given array to stdout.
     * 
     * @param array
     * @param maxElemsPerLine
     */
    public static final void dumpArray(final float[] array, final int maxElemsPerLine) {
        if (array == null) {
            System.out.println((String) null);
            return;
        }
        int line = 0;
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            if ((i > 0) && ((i % maxElemsPerLine) == 0)) {
                System.out.println();
                System.out.print("  ");
                line++;
            }
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        if (line > 0) {
            System.out.println();
            System.out.println("]");
        } else {
            System.out.println(" ]");
        }
    }

    /**
     * Dumps the contents of the given array to stdout (in one line).
     * 
     * @param array
     */
    public static final void dumpArray(final float[] array) {
        dumpArray(array, Integer.MAX_VALUE);
    }

    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * 
     * @return the array with the ensured length
     */
    public static final int[] ensureCapacity(int[] array, int minCapacity) {
        final int oldCapacity = array.length;
        if (minCapacity > oldCapacity) {
            final int[] oldArray = array;
            final int newCapacity = (oldCapacity * 3) / 2 + 1;
            array = new int[newCapacity];
            System.arraycopy(oldArray, 0, array, 0, oldCapacity);
        }
        return (array);
    }

    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * @param paddValue the value to be written to appended elements
     * 
     * @return the array with the ensured length
     */
    public static final int[] ensureCapacity(int[] array, int minCapacity, int paddValue) {
        final int oldCapacity = array.length;
        if (minCapacity > oldCapacity) {
            final int[] oldArray = array;
            final int newCapacity = (oldCapacity * 3) / 2 + 1;
            array = new int[newCapacity];
            System.arraycopy(oldArray, 0, array, 0, oldCapacity);
            Arrays.fill(array, oldCapacity, newCapacity - 1, paddValue);
        }
        return (array);
    }

    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * 
     * @return the array with the ensured length
     */
    public static final float[] ensureCapacity(float[] array, int minCapacity) {
        final int oldCapacity = array.length;
        if (minCapacity > oldCapacity) {
            final float[] oldArray = array;
            final int newCapacity = (oldCapacity * 3) / 2 + 1;
            array = new float[newCapacity];
            System.arraycopy(oldArray, 0, array, 0, oldCapacity);
        }
        return (array);
    }

    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * @param paddValue the value to be written to appended elements
     * 
     * @return the array with the ensured length
     */
    public static final float[] ensureCapacity(float[] array, int minCapacity, int paddValue) {
        final int oldCapacity = array.length;
        if (minCapacity > oldCapacity) {
            final float[] oldArray = array;
            final int newCapacity = (oldCapacity * 3) / 2 + 1;
            array = new float[newCapacity];
            System.arraycopy(oldArray, 0, array, 0, oldCapacity);
            Arrays.fill(array, oldCapacity, newCapacity - 1, paddValue);
        }
        return (array);
    }

    private ArrayHelper() {
    }
}
