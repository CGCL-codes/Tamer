package com.peralex.utilities.ui.graphs.polarGraph;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ColorModel;

/**
 * I use a gradient paint because it's about 5x faster than painting lots of lines.
 * 
 * @author Noel Grandin
 */
class CompassBackgroundGradientPaint implements Paint {

    private final Point2D mPoint;

    private final double radius;

    public CompassBackgroundGradientPaint(double x, double y, double radius) {
        this.mPoint = new Point2D.Double(x, y);
        this.radius = radius;
    }

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        Point2D transformedPoint = xform.transform(mPoint, null);
        return new CompassBackgroundGradientContext(transformedPoint, radius);
    }

    public int getTransparency() {
        return OPAQUE;
    }
}
