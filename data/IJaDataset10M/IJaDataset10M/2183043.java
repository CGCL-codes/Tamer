package chart;

import java.awt.geom.Rectangle2D;

/**
 * @author Dan Phifer
 *         <p>
 *         Dec 12, 2008
 *         <P>
 */
public interface IChartSeriesDataBuffer {

    /**
	 * @param visibleRectangle2D
	 * @param maxPoints
	 * @return an array, {x1, y1, x2, y2, ..., xn, yn}, orer by x values.
	 */
    public double[] getDataForRegion(Rectangle2D visibleRectangle2D);

    public double getMinX();

    public double getMaxX();
}
