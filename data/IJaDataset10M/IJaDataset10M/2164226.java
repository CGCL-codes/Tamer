package com.jrefinery.chart;

import java.awt.*;
import java.awt.geom.*;

/**
 * An interface that must be supported by all vertical axes - used for layout purposes.
 */
public interface VerticalAxis {

    /**
     * Returns the area required to draw the axis in the specified draw area.
     * @param g2 The graphics device;
     * @param drawArea The area within which the plot should be drawn;
     * @param reservedHeight The height reserved by the horizontal axis.
     */
    public Rectangle2D reserveAxisArea(Graphics2D g2, Plot plot, Rectangle2D drawArea, double reservedHeight);

    /**
     * Returns the width required to draw the axis in the specified draw area.
     * @param g2 The graphics device;
     * @param drawArea The area within which the plot should be drawn.
     */
    public double reserveWidth(Graphics2D g2, Plot plot, Rectangle2D drawArea);
}
