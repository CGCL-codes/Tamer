package com.google.api.chart.features;

import com.google.api.chart.ChartMaker;
import com.google.api.chart.RangeMarkerMaker;

/**
 * Marks support for charts with range marker information.
 * @author sorenad
 *
 */
public interface RangeMarkerFeature<T> {

    /**
	 * Specifies the range markers to show in a chart.
	 * @param ranges
	 * @see ChartMaker#range()
	 * @return
	 */
    public T ranges(RangeMarkerMaker... ranges);
}
