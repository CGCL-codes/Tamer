package org.expasy.jpl.commons.base.render;

/**
 * A bar chart renderer is a chart renderer with configurable bar width.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public interface BarChartRenderer extends ChartRenderer {

    /** set the bar width */
    void setBarWidth(double width);
}
