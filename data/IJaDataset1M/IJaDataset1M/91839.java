package com.wrupple.muba.common.domain;

public class HistogramModel {

    private int[][] matrix;

    private int highestFrequency;

    private final int elementCount;

    private final double intervalWidth;

    public HistogramModel(int elementCount, double intervalWidth) {
        super();
        this.elementCount = elementCount;
        this.intervalWidth = intervalWidth;
        highestFrequency = -1;
    }

    /**
	 * @return position each index (int) separated into histogram bins
	 */
    public int[][] getMatrix() {
        return matrix;
    }

    /**
	 * @return total element count
	 */
    public int getElementCount() {
        return elementCount;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public double getIntervalWidth() {
        return intervalWidth;
    }

    public void setHighestFrequency(int highestFrequency) {
        this.highestFrequency = highestFrequency;
    }

    public int getHighestFrequency() {
        return highestFrequency;
    }
}
