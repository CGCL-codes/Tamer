package com.google.api.chart.features;

import com.google.api.chart.ChartMaker;
import com.google.api.chart.ShapeMarkerMaker;

/**
 * Marks support for charts with shape marker information.
 * 
 * @author sorenad
 */
public interface ShapeMarkerFeature<T> {

    /**
	 * Specifies shape markers to show in a chart.
	 * @param shapes
	 * @see ChartMaker#shape()
	 * @see ChartMaker#shapeArrow()
	 * @see ChartMaker#shapeDiamond()
	 * @see ChartMaker#shapeCircle()
	 * @see ChartMaker#shapeCross()
	 * @see ChartMaker#shapeHorizontalLine(double)
	 * @see ChartMaker#shapeSquare()
	 * @see ChartMaker#shapeXmark()
	 * @return
	 */
    public T shapes(ShapeMarkerMaker... shapes);
}
