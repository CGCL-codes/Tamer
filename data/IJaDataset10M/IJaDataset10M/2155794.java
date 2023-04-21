package com.google.api.chart;

import java.math.BigDecimal;

/**
 * Representation of one dataseries.
 * 
 * @author sorenad
 */
public interface DataSeriesMaker {

    /**
	 * Accessor to get the data in the dataseries. This is not called before
	 * {@link ChartMaker#getURL()} is called.
	 * @return Array of data, null for missing data.
	 * @see ChartMaker#dataSeries(double...)
	 * @see ChartMaker#data(DataSeriesMaker...)
	 */
    public BigDecimal[] getData();
}
