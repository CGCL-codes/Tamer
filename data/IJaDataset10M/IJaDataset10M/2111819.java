package ca.whu.taxman.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 * @author Peter Wu <peterwu@hotmail.com>
 */
public class Transformer {

    public static Point2D transformPoint(Point2D ptSrc) {
        Point2D ptDst = new Point2D.Double();
        AffineTransform at = new AffineTransform();
        at.setTransform(1.25, 0, 0, -1.25, 0, 990);
        at.transform(ptSrc, ptDst);
        return ptDst;
    }

    public static Point2D transformPoint(double x, double y) {
        Point2D ptSrc = new Point2D.Double(x, y);
        return transformPoint(ptSrc);
    }
}
