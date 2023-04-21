package org.jhotdraw.draw;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.xml.DOMInput;
import org.jhotdraw.xml.DOMOutput;

/**
 * A Rectangle2D.Double with round corners.
 * 
 * @author Werner Randelshofer
 * @version 2.2 2006-06-17 Method chop added.
 * 2.1 2006-05-29 Method basetBoundsid not work for bounds smaller
 * than 1 pixel.
 * <br>2.0 2006-01-14 Changed to support double precison coordinates.
 * <br>1.0 2004-03-02 Derived from JHotDraw 6.0b1.
 */
public class RoundRectangleFigure extends AbstractAttributedFigure {

    private RoundRectangle2D.Double roundrect;

    private static final double DEFAULT_ARC = 20;

    /** Creates a new instance. */
    public RoundRectangleFigure() {
        this(0, 0, 0, 0);
    }

    public RoundRectangleFigure(double x, double y, double width, double height) {
        roundrect = new RoundRectangle2D.Double(x, y, width, height, DEFAULT_ARC, DEFAULT_ARC);
    }

    protected void drawFill(Graphics2D g) {
        RoundRectangle2D.Double r = (RoundRectangle2D.Double) roundrect.clone();
        double grow = AttributeKeys.getPerpendicularFillGrowth(this);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        r.arcwidth += grow * 2;
        r.archeight += grow * 2;
        if (r.width > 0 && r.height > 0) {
            g.fill(r);
        }
    }

    protected void drawStroke(Graphics2D g) {
        RoundRectangle2D.Double r = (RoundRectangle2D.Double) roundrect.clone();
        double grow = AttributeKeys.getPerpendicularDrawGrowth(this);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        r.arcwidth += grow * 2;
        r.archeight += grow * 2;
        if (r.width > 0 && r.height > 0) {
            g.draw(r);
        }
    }

    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) roundrect.getBounds2D();
    }

    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D.Double r = (Rectangle2D.Double) roundrect.getBounds2D();
        double grow = AttributeKeys.getPerpendicularHitGrowth(this);
        Geom.grow(r, grow, grow);
        return r;
    }

    public double getArcWidth() {
        return roundrect.arcwidth;
    }

    public double getArcHeight() {
        return roundrect.archeight;
    }

    public void setArc(final double w, final double h) {
        @SuppressWarnings("unused") final double oldWidth = roundrect.getArcWidth();
        @SuppressWarnings("unused") final double oldHeight = roundrect.getArcHeight();
        roundrect.arcwidth = w;
        roundrect.archeight = h;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    public boolean contains(Point2D.Double p) {
        RoundRectangle2D.Double r = (RoundRectangle2D.Double) roundrect.clone();
        double grow = AttributeKeys.getPerpendicularHitGrowth(this);
        r.x -= grow;
        r.y -= grow;
        r.width += grow * 2;
        r.height += grow * 2;
        r.arcwidth += grow * 2;
        r.archeight += grow * 2;
        return r.contains(p);
    }

    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        roundrect.x = Math.min(anchor.x, lead.x);
        roundrect.y = Math.min(anchor.y, lead.y);
        roundrect.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        roundrect.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
    }

    /**
     * Transforms the figure.
     * @param tx The transformation.
     */
    public void transform(AffineTransform tx) {
        Point2D.Double anchor = getStartPoint();
        Point2D.Double lead = getEndPoint();
        setBounds((Point2D.Double) tx.transform(anchor, anchor), (Point2D.Double) tx.transform(lead, lead));
    }

    public Collection<Handle> createHandles(int detailLevel) {
        LinkedList<Handle> handles = (LinkedList<Handle>) super.createHandles(detailLevel);
        handles.add(new RoundRectangleRadiusHandle(this));
        return handles;
    }

    public void restoreTransformTo(Object geometry) {
        RoundRectangle2D.Double r = (RoundRectangle2D.Double) geometry;
        roundrect.x = r.x;
        roundrect.y = r.y;
        roundrect.width = r.width;
        roundrect.height = r.height;
    }

    public Object getTransformRestoreData() {
        return roundrect.clone();
    }

    public Connector findConnector(Point2D.Double p, ConnectionFigure prototype) {
        return new ChopRoundRectangleConnector(this);
    }

    public Connector findCompatibleConnector(Connector c, boolean isStartConnector) {
        return new ChopRoundRectangleConnector(this);
    }

    public RoundRectangleFigure clone() {
        RoundRectangleFigure that = (RoundRectangleFigure) super.clone();
        that.roundrect = (RoundRectangle2D.Double) this.roundrect.clone();
        return that;
    }

    @Override
    public void read(DOMInput in) throws IOException {
        super.read(in);
        roundrect.arcwidth = in.getAttribute("arcWidth", DEFAULT_ARC);
        roundrect.archeight = in.getAttribute("arcHeight", DEFAULT_ARC);
    }

    @Override
    public void write(DOMOutput out) throws IOException {
        super.write(out);
        out.addAttribute("arcWidth", roundrect.arcwidth);
        out.addAttribute("arcHeight", roundrect.archeight);
    }
}
