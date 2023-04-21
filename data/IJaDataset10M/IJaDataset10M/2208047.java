package es.iiia.shapeeditor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import es.iiia.shapeeditor.controllers.ControllerBase;

public class ViewUtils {

    public static Point[] create8ControlPointsFromBounds(Rectangle bounds) {
        Point[] pts = new Point[8];
        pts[0] = new Point(bounds.x, bounds.y);
        pts[1] = new Point(bounds.x + bounds.width / 2, bounds.y);
        pts[2] = new Point(bounds.x + bounds.width, bounds.y);
        pts[3] = new Point(bounds.x + bounds.width, bounds.y + bounds.height / 2);
        pts[4] = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
        pts[5] = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height);
        pts[6] = new Point(bounds.x, bounds.y + bounds.height);
        pts[7] = new Point(bounds.x, bounds.y + bounds.height / 2);
        return pts;
    }

    public static int getPosition(Point position, Point2D[] ctrPoints) {
        if (ctrPoints == null) {
            return -1;
        }
        int i = 0;
        for (Point2D pt : ctrPoints) {
            if (Math.abs(position.getX() - pt.getX()) <= ControllerBase.controlPointSize && Math.abs(position.getY() - pt.getY()) <= ControllerBase.controlPointSize) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static Point[] getPathPoints(GeneralPath shape) {
        ArrayList<Point> points = new ArrayList<Point>();
        PathIterator pi = shape.getPathIterator(null);
        int i = 0;
        while (pi.isDone() == false) {
            double[] coordinates = new double[6];
            pi.currentSegment(coordinates);
            points.add(new Point((int) coordinates[0], (int) coordinates[1]));
            pi.next();
        }
        Point[] pts = new Point[points.size()];
        points.toArray(pts);
        return pts;
    }

    public static double getPathDistance(Point2D pt, GeneralPath shape) {
        PathIterator pi = shape.getPathIterator(null);
        Point2D pt1 = null;
        Line2D ln;
        double distance = Integer.MAX_VALUE;
        while (pi.isDone() == false) {
            double[] coordinates = new double[6];
            int type = pi.currentSegment(coordinates);
            if (type == PathIterator.SEG_MOVETO) {
                pt1 = new Point2D.Double(coordinates[0], coordinates[1]);
            } else if (type == PathIterator.SEG_LINETO) {
                ln = new Line2D.Double(pt1, new Point2D.Double(coordinates[0], coordinates[1]));
                pt1 = (Point2D) ln.getP1().clone();
                if (distance > ln.ptSegDist(pt)) {
                    distance = ln.ptSegDist(pt);
                }
            }
            pi.next();
        }
        return distance;
    }
}
