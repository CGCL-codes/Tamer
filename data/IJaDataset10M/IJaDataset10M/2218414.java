package org.jhotdraw.draw;

import static org.jhotdraw.draw.AttributeKeys.TEXT_COLOR;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jhotdraw.geom.Geom;

/**
 * EllipseFigure.
 *
 * @author Werner Randelshofer
 * @version 2.4 2006-12-12 Made ellipse protected.
 * <br>2.3 2006-06-17 Added method chop(Point2D.Double).
 * <br>2.2 2006-05-19 Support for stroke placement added.
 * <br>2.1 2006-03-22 Method getFigureDrawBounds added.
 * <br>2.0 2006-01-14 Changed to support double precison coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class CircleFigure extends AbstractAttributedFigure {

    protected Ellipse2D.Double ellipse;

    /** Creates a new instance. */
    public CircleFigure() {
        this(0, 0, 0, 0);
    }

    public CircleFigure(double x, double y, double width, double height) {
        ellipse = new Ellipse2D.Double(x, y, width, height);
        setAttributeEnabled(TEXT_COLOR, false);
    }

    public Connector findConnector(Point2D.Double p, ConnectionFigure prototype) {
        return new ChopEllipseConnector(this);
    }

    public Connector findCompatibleConnector(Connector c, boolean isStartConnector) {
        return new ChopEllipseConnector(this);
    }

    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) ellipse.getBounds2D();
    }

    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D.Double r = (Rectangle2D.Double) ellipse.getBounds2D();
        double grow = AttributeKeys.getPerpendicularHitGrowth(this);
        Geom.grow(r, grow, grow);
        return r;
    }

    protected void drawFill(Graphics2D g) {
        Ellipse2D.Double r = (Ellipse2D.Double) ellipse.clone();
        double grow = AttributeKeys.getPerpendicularFillGrowth(this);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        if (r.width > 0 && r.height > 0) {
            g.fill(r);
        }
    }

    protected void drawStroke(Graphics2D g) {
        Ellipse2D.Double r = (Ellipse2D.Double) ellipse.clone();
        double grow = AttributeKeys.getPerpendicularDrawGrowth(this);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        if (r.width > 0 && r.height > 0) {
            g.draw(r);
        }
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    public boolean contains(Point2D.Double p) {
        Ellipse2D.Double r = (Ellipse2D.Double) ellipse.clone();
        double grow = AttributeKeys.getPerpendicularHitGrowth(this);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        return r.contains(p);
    }

    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        ellipse.x = Math.min(anchor.x, lead.x);
        ellipse.y = Math.min(anchor.y, lead.y);
        ellipse.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        ellipse.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
    }

    /**
     * Transforms the figure.
     *
     * @param tx the transformation.
     */
    public void transform(AffineTransform tx) {
        Point2D.Double anchor = getStartPoint();
        Point2D.Double lead = getEndPoint();
        setBounds((Point2D.Double) tx.transform(anchor, anchor), (Point2D.Double) tx.transform(lead, lead));
    }

    public CircleFigure clone() {
        CircleFigure that = (CircleFigure) super.clone();
        that.ellipse = (Ellipse2D.Double) this.ellipse.clone();
        return that;
    }

    public void restoreTransformTo(Object geometry) {
        Ellipse2D.Double r = (Ellipse2D.Double) geometry;
        ellipse.x = r.x;
        ellipse.y = r.y;
        ellipse.width = r.width;
        ellipse.height = r.height;
    }

    public Object getTransformRestoreData() {
        return ellipse.clone();
    }
}
