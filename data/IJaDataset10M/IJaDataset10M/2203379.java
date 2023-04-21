package moten.david.util.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;

public class ComponentUtil {

    public static Point[] getBestJoin(Set<Point> usedPoints, Component aComponent, Component bComponent) {
        return getBestJoin(usedPoints, aComponent.getBounds(), bComponent.getBounds());
    }

    public static Point[] getBestJoin(Set<Point> usedPoints, Rectangle aComponent, Rectangle bComponent) {
        int numPoints = 4;
        Point[] aPoints = getPoints(aComponent, numPoints);
        Point[] bPoints = getPoints(bComponent, numPoints);
        Double shortestDistance = null;
        Point centreA = getCentre(aComponent.getBounds());
        Point centreB = getCentre(bComponent.getBounds());
        Point bestA = null;
        Point bestB = null;
        for (Point a : aPoints) {
            for (Point b : bPoints) {
                if (!usedPoints.contains(a) && !usedPoints.contains(b) && !a.equals(b)) {
                    double distance = getDistance(a, b) + getDistance(a, centreA) + getDistance(b, centreB);
                    if (shortestDistance == null || distance < shortestDistance) {
                        shortestDistance = distance;
                        bestA = a;
                        bestB = b;
                    }
                }
            }
        }
        usedPoints.add(bestA);
        usedPoints.add(bestB);
        return new Point[] { bestA, bestB };
    }

    public static double getDistance(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    public static Point[] getPoints(Component c, int numPoints) {
        return getPoints(c.getBounds(), numPoints);
    }

    public static Point[] getPoints(Rectangle c, int numPoints) {
        Point[] points = new Point[4 * numPoints];
        for (int i = 0; i < numPoints; i++) {
            points[i] = new Point((int) Math.round(c.getX() + i * c.getWidth() / numPoints), (int) Math.round(c.getY()));
            points[numPoints + i] = new Point((int) Math.round(c.getX() + i * c.getWidth() / numPoints), (int) Math.round(c.getY() + c.getHeight()));
            points[2 * numPoints + i] = new Point((int) Math.round(c.getX()), (int) Math.round(c.getY() + i * c.getHeight() / numPoints));
            points[3 * numPoints + i] = new Point((int) Math.round(c.getX() + c.getWidth()), (int) Math.round(c.getY() + i * c.getHeight() / numPoints));
        }
        return points;
    }

    public static Point getCentre(Rectangle r) {
        Point p = new Point();
        p.x = r.x + r.width / 2;
        p.y = r.y + r.height / 2;
        return p;
    }

    public static Point getCentre(Point p1, Point p2) {
        Point p = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        return p;
    }

    public static void drawArrowedLine(Graphics g, Point a, Point b, double angleDegrees, int arrowLength) {
        g.drawLine(a.x, a.y, b.x, b.y);
        double theta = Math.atan2(a.y - b.y, a.x - b.x);
        double angle = Math.toRadians(angleDegrees);
        Point end1 = new Point();
        end1.x = (int) Math.round(b.x + arrowLength * Math.cos(theta + angle));
        end1.y = (int) Math.round(b.y + arrowLength * Math.sin(theta + angle));
        Point end2 = new Point();
        end2.x = (int) Math.round(b.x + arrowLength * Math.cos(theta - angle));
        end2.y = (int) Math.round(b.y + arrowLength * Math.sin(theta - angle));
        g.drawLine(b.x, b.y, end1.x, end1.y);
        g.drawLine(b.x, b.y, end2.x, end2.y);
    }
}
