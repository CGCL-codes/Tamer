package de.progra.charting;

import java.awt.geom.*;

/**
 * This interface defines the methods necessary to translate data values
 * into pixel coordinates.
 * @author  mueller
 * @version 1.0
 * @deprecated
 */
public interface PointToPixelTranslator {

    /** Transforms a given value into an absolute pixel coordinate.
     * @param pt a <CODE>Point2D</CODE> object containing the value - column pair that should be painted
     * @return a <CODE>Point2D</CODE> object containing the x-y pair which define the absolute pixel-coordinate the values should be painted at.
     * @deprecated
     */
    public Point2D getPixelCoord(Point2D pt);
}
