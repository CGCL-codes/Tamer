package org.opensourcephysics.media.core;

/**
 * This manages an array of doubles.
 *
 * @author Douglas Brown
 * @version 1.0
 */
public class DoubleArray {

    private double[] map;

    private int length;

    /**
   * Constructs a DoubleArray object.
   * @param initialLength the initial length of the array
   * @param initialValue the initial value of all array elements
   */
    public DoubleArray(int initialLength, double initialValue) {
        length = initialLength;
        map = new double[length];
        fill(initialValue, 0);
    }

    /**
   * Gets the specified array element.
   * @param n the array index
   * @return the value at the specified index
   */
    public double get(int n) {
        if (n >= length) setLength(n + 1);
        return map[n];
    }

    /**
   * Sets the specified array element.
   * @param n the array index
   * @param value the new value of the element
   */
    public void set(int n, double value) {
        if (n >= length) setLength(n + 1);
        map[n] = value;
    }

    /**
   * Sets the length of the array.
   * @param newLength the new length of the array
   */
    public void setLength(int newLength) {
        if (newLength == length || newLength < 1) return;
        double[] newMap = new double[newLength];
        System.arraycopy(map, 0, newMap, 0, Math.min(newLength, length));
        map = newMap;
        if (newLength > length) {
            double val = map[length - 1];
            int n = length;
            length = newLength;
            fill(val, n);
        } else length = newLength;
    }

    /**
   * Fills elements of the the array with the specified value.
   * @param value the value
   */
    public void fill(double value) {
        for (int n = length - 1; n >= 0; n--) map[n] = value;
    }

    /**
   * Fills elements of the the array with the specified value,
   * starting from a specified index.
   * @param value the value
   * @param startFrame the starting array index
   */
    private void fill(double value, int startFrame) {
        for (int n = startFrame; n < length; n++) map[n] = value;
    }
}
