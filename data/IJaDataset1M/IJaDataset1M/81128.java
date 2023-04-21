package org.kf.grid;

public interface Grid {

    /**
	 * Compute the grid index index for value. if value < 0 always return 0
	 * if value > 1 always return the last index
	 *
	 * @param value the value
	 *
	 * @return the index of the bin for this value
	 */
    public int computeIndexForValue(double value);

    public double getDensityByGridIndex(int index);

    public void updateFrequency(int index, double frequencyToAdd);

    /**
	 * Clear/reset all the frequencies
	 */
    public void clear();

    public double[] getDensities();

    /**
	 * Gets the standard iterator for inserting values into the grid that
	 * have no particular properties
	 *
	 * @return the standard iterator
	 */
    public GridInsertionIterator getStandardIterator();

    /**
	 * Gets an insertion iterator optimized for inserting values sorted in ascending order
	 *
	 *
	 * @return the sorted values iterator
	 */
    public GridInsertionIterator getSortedValuesIterator();

    /**
	 * Gets the size i.e. the number of grid indexes
	 *
	 * @return the size
	 */
    public int getSize();

    /**
	 * Gets the grid value for a given index
	 *
	 * @param index the index
	 *
	 * @return the grid value
	 */
    public double getGridValue(int index);
}
