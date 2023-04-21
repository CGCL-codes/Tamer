package org.swtchart.internal.compress;

/**
 * A Compressor.
 */
public interface ICompress {

    /**
     * Gets the compressed X series
     * 
     * @return the compressed X series
     */
    public abstract double[] getCompressedXSeries();

    /**
     * Gets the compressed Y series
     * 
     * @return the compressed Y series
     */
    public abstract double[] getCompressedYSeries();

    /**
     * Gets the compressed series indexes
     * 
     * @return the compressed series indexes
     */
    public abstract int[] getCompressedIndexes();

    /**
     * Sets X series which have to be sorted.
     * 
     * @param xSeries
     *            the X series
     */
    public abstract void setXSeries(double[] xSeries);

    /**
     * sets the Y series
     * 
     * @param ySeries
     *            the Y series
     */
    public abstract void setYSeries(double[] ySeries);

    /**
     * Ignores the points which are in the same grid as the previous point.
     * 
     * @param config
     *            the configuration for compression
     * @return true if the compression succeeds
     */
    public abstract boolean compress(CompressConfig config);
}
