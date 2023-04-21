package net.sourceforge.roboswarm.util;

public class DoubleCircularBuffer {

    private double[] array;

    private int nextIndex = 0;

    /**
	 * Constructor for DoubleCircularBuffer.
	 */
    public DoubleCircularBuffer(int size, double initialValue) {
        array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = initialValue;
        }
    }

    public void add(double value) {
        array[nextIndex] = value;
        nextIndex++;
        if (nextIndex >= array.length) nextIndex = 0;
    }

    public double get(int offset) {
        if (offset < 0) return get(0);
        int index = nextIndex - 1 - offset;
        if (index < 0) index += array.length;
        if (index < 0) index = nextIndex;
        return array[index];
    }
}
