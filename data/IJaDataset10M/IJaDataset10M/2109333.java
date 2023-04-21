package org.reprap.scanning.Geometry;

import Jama.*;

public class Point2d {

    private static final double tau = Math.PI * 2;

    public double x, y;

    public Point2d() {
        x = 0;
        y = 0;
    }

    public Point2d(double newX, double newY) {
        x = newX;
        y = newY;
    }

    public Point2d(Matrix M) {
        x = 0;
        y = 0;
        ApplyTransform(M);
    }

    public void scale(double s) {
        x = x * s;
        y = y * s;
    }

    public Point2d timesEquals(double n) {
        return new Point2d(n * x, n * y);
    }

    public Point2d plusEquals(Point2d v) {
        return new Point2d(x + v.x, y + v.y);
    }

    public void plus(Point2d v) {
        x = x + v.x;
        y = y + v.y;
    }

    public void minus(Point2d v) {
        x = x - v.x;
        y = y - v.y;
    }

    public Point2d minusEquals(Point2d v) {
        return new Point2d(x - v.x, y - v.y);
    }

    public double dot(Point2d v) {
        return (x * v.x + y * v.y);
    }

    public double crossLength(Point2d v) {
        return Math.abs(x * v.y - y * v.x);
    }

    public double lengthSquared() {
        return (x * x + y * y);
    }

    public Point2d clone() {
        Point2d returnvalue = new Point2d(x, y);
        return returnvalue;
    }

    public Point3d ExportAs3DPoint(double z) {
        return new Point3d(x, y, z);
    }

    public double CalculateDistanceSquared(Point2d other) {
        return (((x - other.x) * (x - other.x)) + ((y - other.y) * (y - other.y)));
    }

    public boolean isCollinear(Point2d x0, Point2d x1) {
        LineSegment2D a = new LineSegment2D(this, x0);
        LineSegment2D b = new LineSegment2D(this, x1);
        LineSegment2D c = new LineSegment2D(x0, x1);
        return (a.Intersect(x1) || b.Intersect(x0) || c.Intersect(this));
    }

    public double GetAngleMeasuredAntiClockwiseFromPositiveX(Point2d C) {
        double angle;
        double cosineangle = Math.abs(y - C.y) / Math.sqrt(CalculateDistanceSquared(C));
        angle = Math.acos(cosineangle);
        if (x < C.x) angle = (tau * 0.25) - angle; else angle = (tau * 0.25) + angle;
        if (y > C.y) angle = tau - angle;
        return angle;
    }

    public int GetOctant(Point2d C) {
        int octant;
        if (C.x == x) octant = 1; else {
            double BA = x - C.x;
            double BAsquaredtimes2 = BA * BA * 2;
            double BCsquared = CalculateDistanceSquared(C);
            if (BAsquaredtimes2 <= BCsquared) octant = 1; else octant = 0;
        }
        if (x > C.x) octant = 3 - octant;
        if (y < C.y) octant = 7 - octant;
        return octant;
    }

    public Point2d GetOtherPoint(double angle, double distance) {
        Point2d returnvalue;
        angle = angle % tau;
        if (angle == (tau * 0.25)) returnvalue = new Point2d(x, y + distance); else if (angle == (tau * 0.75)) returnvalue = new Point2d(x, y - distance); else {
            double m = Math.tan(angle);
            double c = y - (m * x);
            double deltax = Math.sqrt((distance * distance) / (1 + (m * m)));
            if ((angle > (tau * 0.25)) && (angle < (tau * 0.75))) deltax = deltax * -1;
            double newx = x + deltax;
            double newy = (m * newx) + c;
            returnvalue = new Point2d(newx, newy);
        }
        return returnvalue;
    }

    public boolean isNearCollinear(Point2d x0, Point2d x1, double coslimit) {
        double asquared = CalculateDistanceSquared(x0);
        double bsquared = CalculateDistanceSquared(x1);
        double csquared = x0.CalculateDistanceSquared(x1);
        if (asquared > bsquared) {
            double temp = bsquared;
            bsquared = asquared;
            asquared = temp;
        }
        if (csquared > bsquared) {
            double temp = bsquared;
            bsquared = csquared;
            csquared = temp;
        }
        double cosb = (asquared + csquared - bsquared) / Math.sqrt(4 * asquared * csquared);
        return (cosb <= coslimit);
    }

    public boolean isEqual(Point2d v) {
        return ((x == v.x) && (y == v.y));
    }

    public boolean isApproxEqual(Point2d v, double threshold) {
        double dx = Math.abs(x - v.x);
        double dy = Math.abs(y - v.y);
        return ((dx < threshold) && (dy < threshold));
    }

    public boolean ApplyTransform(Matrix M) {
        int columns = M.getColumnDimension();
        int rows = M.getRowDimension();
        boolean success = false;
        if (columns == 1) {
            if (rows == 2) {
                x = x + M.get(0, 0);
                y = y + M.get(1, 0);
                success = true;
            }
            if (rows == 3) {
                x = x + (M.get(0, 0) / M.get(2, 0));
                y = y + (M.get(1, 0) / M.get(2, 0));
                success = true;
            }
        }
        if (rows == 1) {
            if (columns == 2) {
                x = x + M.get(0, 0);
                y = y + M.get(0, 1);
                success = true;
            }
            if (columns == 3) {
                x = x + (M.get(0, 0) / M.get(0, 2));
                y = y + (M.get(0, 1) / M.get(0, 2));
                success = true;
            }
        }
        if (columns == 2) {
            Matrix coord = new Matrix(2, 1);
            coord.set(0, 0, x);
            coord.set(1, 0, y);
            coord = M.times(coord);
            if ((rows == 2) || (rows == 3)) {
                x = coord.get(0, 0);
                y = coord.get(1, 0);
                success = true;
                if (rows == 3) {
                    x = x / coord.get(2, 0);
                    y = y / coord.get(2, 0);
                }
            }
        }
        if (columns == 3) {
            Matrix coord = new Matrix(3, 1);
            coord.set(0, 0, x);
            coord.set(1, 0, y);
            coord.set(2, 0, 1);
            if ((rows == 2) || (rows == 3)) {
                coord = M.times(coord);
                x = coord.get(0, 0);
                y = coord.get(1, 0);
                success = true;
                if (rows == 3) {
                    x = x / coord.get(2, 0);
                    y = y / coord.get(2, 0);
                }
            }
        }
        return success;
    }

    public Matrix ConvertPointTo2x1Matrix() {
        Matrix p = new Matrix(2, 1);
        p.set(0, 0, x);
        p.set(1, 0, y);
        return p;
    }

    public Matrix ConvertPointTo3x1Matrix() {
        Matrix p = new Matrix(3, 1);
        p.set(0, 0, x);
        p.set(1, 0, y);
        p.set(2, 0, 1);
        return p;
    }

    public void print() {
        System.out.print("(" + x + "," + y + ")");
    }
}
