package net.sf.layoutdesigner.track;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;
import net.sf.layoutdesigner.util.Geometry;

public class CurveTrack extends AbstractTrack implements Shape {

    private static final Logger log = Logger.getLogger(CurveTrack.class.getPackage().getName());

    private double x1, y1, z1, x2, y2, z2, xc, yc;

    public CurveTrack(double radius, int degrees) {
        super();
        double radians = (double) degrees * (Math.PI / 360.0);
        double xoffset = Math.sin(radians) * radius;
        double yoffset = Math.cos(radians) * radius;
        x1 = -xoffset;
        x2 = xoffset;
        y1 = 0;
        y2 = 0;
        xc = 0;
        yc = yoffset;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public boolean contains(Point2D p) {
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return false;
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return null;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;
    }

    @Override
    protected void transform(AffineTransform at) {
        double[] points = new double[] { x1, y1, x2, y2, xc, yc };
        at.transform(points, 0, points, 0, 4);
        x1 = points[0];
        y1 = points[1];
        x2 = points[2];
        y2 = points[3];
        xc = points[4];
        yc = points[5];
    }

    @Override
    protected void drawRoadbed(Graphics2D g2) {
    }

    @Override
    protected void drawTies(Graphics2D g2) {
        Arc2D arc = toArc();
        Point2D[] points = Geometry.getPointsFromShape(arc);
        Point2D unit = Geometry.calcUnitVector(points[2], points[0]);
        Line2D tie = new Line2D.Double(points[0].getX() + unit.getX() * 8.0, points[0].getY() + unit.getY() * 8.0, points[0].getX() - unit.getX() * 8.0, points[0].getY() - unit.getY() * 8.0);
        double angleStep = 5.0 / (arc.getWidth() / 2.0);
        AffineTransform at = AffineTransform.getRotateInstance(-angleStep, points[2].getX(), points[2].getY());
        angleStep = Math.toDegrees(angleStep);
        for (double step = 0; step < arc.getAngleExtent(); step += angleStep) {
            Geometry.transform(tie, at);
            g2.draw(tie);
        }
    }

    @Override
    protected void drawRails(Graphics2D g2) {
        Arc2D arc = toArc();
        double radius = arc.getWidth() / 2.0;
        double scale = (radius - 5.0) / radius;
        AffineTransform at = AffineTransform.getTranslateInstance(arc.getCenterX(), arc.getCenterY());
        at.scale(scale, scale);
        at.translate(-arc.getCenterX(), -arc.getCenterY());
        Shape rail = at.createTransformedShape(arc);
        g2.draw(rail);
        scale = (radius + 5.0) / radius;
        at.setToTranslation(arc.getCenterX(), arc.getCenterY());
        at.scale(scale, scale);
        at.translate(-arc.getCenterX(), -arc.getCenterY());
        rail = at.createTransformedShape(arc);
        g2.draw(rail);
    }

    private Arc2D toArc() {
        Point2D start = new Point2D.Double(x1, y1);
        Point2D end = new Point2D.Double(x2, y2);
        Point2D center = new Point2D.Double(xc, yc);
        double radius = start.distance(center);
        Arc2D arc = new Arc2D.Double(center.getX() - radius, center.getY() - radius, radius * 2.0, radius * 2.0, 0.0, 0.0, Arc2D.OPEN);
        arc.setAngles(start, end);
        return arc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CurveTrack [x1=");
        builder.append(x1);
        builder.append(", y1=");
        builder.append(y1);
        builder.append(", z1=");
        builder.append(z1);
        builder.append(", x2=");
        builder.append(x2);
        builder.append(", y2=");
        builder.append(y2);
        builder.append(", z2=");
        builder.append(z2);
        builder.append(", xc=");
        builder.append(xc);
        builder.append(", yc=");
        builder.append(yc);
        builder.append("]");
        return builder.toString();
    }
}
