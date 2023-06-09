package org.jhotdraw.draw;

import static org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT;
import static org.jhotdraw.draw.AttributeKeys.getStrokeTotalWidth;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jhotdraw.geom.Geom;

/**
 * A ChopDiamondConnector locates connection points by choping the
 * connection between the centers of the two figures at the edge of
 * a diamond figure.
 *
 * @author Werner Randelshofer
 * @version 1.0 27. March 2006 Created.
 */
public class ChopDiamondConnector extends ChopRectangleConnector {

    public ChopDiamondConnector() {
    }

    public ChopDiamondConnector(Figure owner) {
        super(owner);
    }

    /**
     * Return an appropriate connection point on the edge of a diamond figure
     */
    protected Point2D.Double chop(Figure target, Point2D.Double from) {
        target = getConnectorTarget(target);
        Rectangle2D.Double r = target.getBounds();
        if (DiamondFigure.IS_QUADRATIC.get(target)) {
            double side = Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double growx;
        double growy;
        switch(STROKE_PLACEMENT.get(target)) {
            case INSIDE:
                {
                    growx = growy = 0f;
                    break;
                }
            case OUTSIDE:
                {
                    double lineLength = Math.sqrt(r.width * r.width + r.height * r.height);
                    double scale = getStrokeTotalWidth(target) * 2d / lineLength;
                    growx = scale * r.height;
                    growy = scale * r.width;
                    break;
                }
            case CENTER:
            default:
                double lineLength = Math.sqrt(r.width * r.width + r.height * r.height);
                double scale = getStrokeTotalWidth(target) / lineLength;
                growx = scale * r.height;
                growy = scale * r.width;
                break;
        }
        Geom.grow(r, growx, growy);
        Point2D.Double c1 = new Point2D.Double(r.x + r.width / 2, r.y + (r.height / 2));
        Point2D.Double p2 = new Point2D.Double(r.x + r.width / 2, r.y + r.height);
        Point2D.Double p4 = new Point2D.Double(r.x + r.width / 2, r.y);
        if (r.contains(from)) {
            if (from.y > r.y && from.y < (r.y + r.height / 2)) {
                return p2;
            } else {
                return p4;
            }
        }
        double ang = Geom.pointToAngle(r, from);
        Point2D.Double p1 = new Point2D.Double(r.x + r.width, r.y + (r.height / 2));
        Point2D.Double p3 = new Point2D.Double(r.x, r.y + (r.height / 2));
        Point2D.Double rp = null;
        if (ang > 0 && ang < 1.57) {
            rp = Geom.intersect(p1.x, p1.y, p2.x, p2.y, c1.x, c1.y, from.x, from.y);
        } else if (ang > 1.575 && ang < 3.14) {
            rp = Geom.intersect(p2.x, p2.y, p3.x, p3.y, c1.x, c1.y, from.x, from.y);
        } else if (ang > -3.14 && ang < -1.575) {
            rp = Geom.intersect(p3.x, p3.y, p4.x, p4.y, c1.x, c1.y, from.x, from.y);
        } else if (ang > -1.57 && ang < 0) {
            rp = Geom.intersect(p4.x, p4.y, p1.x, p1.y, c1.x, c1.y, from.x, from.y);
        }
        if (rp == null) {
            rp = Geom.angleToPoint(r, ang);
        }
        return rp;
    }
}
