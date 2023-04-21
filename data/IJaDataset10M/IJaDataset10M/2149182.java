package util.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Ordóñez
 */
public class Line2D {

    public enum PointPosition {

        BEFORE, INSIDE, AFTER
    }

    private Point2D pI;

    private Point2D pF;

    private double thickness = -1;

    public Line2D() {
        super();
        this.pI = new Point2D();
        this.pF = new Point2D();
    }

    public Line2D(Point2D pI, Point2D pF) {
        super();
        this.pI = pI;
        this.pF = pF;
    }

    public Line2D(Point2D pI, double slope) {
        super();
        this.pI = pI;
        if (!Double.isInfinite(slope)) this.pF = new Point2D(0, pI.getY() - slope * pI.getX()); else this.pF = new Point2D(pI.getX(), 0);
    }

    public Point2D getPI() {
        return pI;
    }

    public void setPI(Point2D pi) {
        pI = pi;
    }

    public Point2D getPF() {
        return pF;
    }

    public void setPF(Point2D pf) {
        pF = pf;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getLength() {
        return pI.getDistance(pF);
    }

    public double getSlope() {
        return (pF.getY() - pI.getY()) / (pF.getX() - pI.getX());
    }

    public double getAngle() {
        return Math.atan2(pF.getY() - pI.getY(), pF.getX() - pI.getX());
    }

    public double getYIntersect() {
        return this.getPI().getY() - this.getSlope() * this.getPI().getX();
    }

    public double getFunction(double x) {
        return this.getSlope() * x + this.getYIntersect();
    }

    public double getParameter(Point2D p) {
        if (Math.abs(pF.getX() - pI.getX()) > 10 * Double.MIN_VALUE) return (p.getX() - pI.getX()) / (pF.getX() - pI.getX()); else return (p.getY() - pI.getY()) / (pF.getY() - pI.getY());
    }

    public boolean isFromLine(Point2D p) {
        if (Math.abs(p.getY() - (p.getX() * this.getSlope() + this.getYIntersect())) < 2 * Double.MIN_VALUE) return true; else return false;
    }

    public boolean isInside(Point2D p) {
        if (this.isFromLine(p) && isInside2(p)) return true; else return false;
    }

    public boolean isInside2(Point2D p) {
        double parameter = getParameter(p);
        if (-0.01 <= parameter && parameter <= 1.01) return true; else return false;
    }

    public boolean isInside3(Point2D p) {
        if (p.getDistance(pI) < this.getLength() && p.getDistance(pF) < this.getLength()) return true; else return false;
    }

    public boolean isNearestInside(Point2D p) {
        return isInside2(getNearestPoint(p));
    }

    public PointPosition getPointPosition(Point2D p) {
        Point2D nearest = getNearestPoint(p);
        double parameter = getParameter(nearest);
        if (parameter < 0) return PointPosition.BEFORE; else if (parameter <= 1) return PointPosition.INSIDE; else return PointPosition.AFTER;
    }

    public double[] getCoefficients() {
        double[] resp = new double[3];
        if (pF.getX() == pI.getX()) {
            resp[0] = -1;
            resp[1] = 0;
            resp[2] = pF.getX();
        } else {
            double p = this.getSlope();
            resp[0] = p;
            resp[1] = -1;
            resp[2] = pI.getY() - p * pI.getX();
        }
        return resp;
    }

    public Point2D intersect(Line2D l2) {
        double[] c = this.getCoefficients();
        double[] c2 = l2.getCoefficients();
        return new Point2D((c[1] * c2[2] - c[2] * c2[1]) / (c[0] * c2[1] - c[1] * c2[0]), (c[2] * c2[0] - c[0] * c2[2]) / (c[0] * c2[1] - c[1] * c2[0]));
    }

    public boolean isIntersected(Line2D l2) {
        Point2D p = intersect(l2);
        double d = this.getLength();
        if (p.getDistance(pI) < d && p.getDistance(pF) < d) return true; else return false;
    }

    public Point2D getNearestPoint(Point2D p) {
        if (pI.getX() == pF.getX()) return new Point2D(pF.getX(), p.getY()); else {
            double x = (p.getX() + this.getSlope() * (p.getY() - this.getYIntersect())) / (Math.pow(this.getSlope(), 2) + 1);
            double y = this.getFunction(x);
            return new Point2D(x, y);
        }
    }

    public double getDistanceToPoint(Point2D p) {
        return p.getDistance(this.getNearestPoint(p));
    }

    public double getDistanceToPoint2(Point2D p) {
        Point2D p2 = this.getNearestPoint(p);
        double d = this.getLength();
        if (p2.getDistance(pI) < d && p2.getDistance(pF) < d) return getDistanceToPoint(p); else return Math.min(p.getDistance(pI), p.getDistance(pF));
    }

    public Point2D getCenter() {
        return new Point2D((pI.getX() + pF.getX()) / 2, (pI.getY() + pF.getY()) / 2);
    }

    public Line2D getOpposite() {
        return new Line2D(pF, pI);
    }

    public Vector2D getNormalVector() {
        return new Vector2D(2 * (pI.getY() - pF.getY()), 2 * (pF.getX() - pI.getX()));
    }

    public double getPerpendicularAngle() {
        double angP = this.getAngle() + Math.PI / 2;
        if (angP > Math.PI) angP -= 2 * Math.PI;
        return angP;
    }

    public List<int[]> getDiscreteLine2D(double tamCell) {
        List<int[]> cells = new ArrayList<int[]>();
        int x, y, dx, dy, p, incE, incNE, stepx, stepy;
        int x0 = (int) Math.floor(pI.getX() / tamCell), y0 = (int) Math.floor(pI.getY() / tamCell);
        int x1 = (int) Math.floor(pF.getX() / tamCell), y1 = (int) Math.floor(pF.getY() / tamCell);
        dx = (x1 - x0);
        dy = (y1 - y0);
        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else stepy = 1;
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else stepx = 1;
        x = x0;
        y = y0;
        int[] a = { y, x };
        cells.add(a);
        if (dx > dy) {
            p = 2 * dy - dx;
            incE = 2 * dy;
            incNE = 2 * (dy - dx);
            while (x != x1) {
                x = x + stepx;
                if (p < 0) p = p + incE; else {
                    y = y + stepy;
                    p = p + incNE;
                }
                int[] b = { x, y };
                cells.add(b);
            }
        } else {
            p = 2 * dx - dy;
            incE = 2 * dx;
            incNE = 2 * (dx - dy);
            while (y != y1) {
                y = y + stepy;
                if (p < 0) p = p + incE; else {
                    x = x + stepx;
                    p = p + incNE;
                }
                int[] b = { x, y };
                cells.add(b);
            }
        }
        return cells;
    }

    public String toString() {
        return pI.toString() + "#" + pF.toString();
    }

    public static Line2D parseLine2D(String line2D) {
        String[] parts = line2D.split("#");
        return new Line2D(Point2D.parsePoint2D(parts[0]), Point2D.parsePoint2D(parts[1]));
    }
}
