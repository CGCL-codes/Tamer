package org.isakiev.wic.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrangeManager {

    /**
	 * Arranges specified points clockwise
	 * @param points points
	 * @return arranged points
	 */
    public static List<CoordinateVector> arrangeClockwise(List<CoordinateVector> points) {
        if (points.size() == 0) {
            throw new IllegalArgumentException("List of points shouldn't be empty");
        }
        CoordinateVector startingPoint = points.get(0);
        for (CoordinateVector point : points) {
            if (point.getY() < startingPoint.getY()) {
                startingPoint = point;
            }
        }
        List<PointWithAngle> pointsWithAngles = new ArrayList<PointWithAngle>();
        for (CoordinateVector point : points) {
            pointsWithAngles.add(new PointWithAngle(point, calculateAngle(startingPoint, point)));
        }
        Collections.sort(pointsWithAngles);
        List<CoordinateVector> result = new ArrayList<CoordinateVector>();
        for (PointWithAngle pwa : pointsWithAngles) {
            result.add(pwa.getPoint());
        }
        Collections.reverse(result);
        double minX = result.get(0).getX();
        double minY = result.get(0).getY();
        int minPointIndex = 0;
        for (int i = 1; i < result.size(); i++) {
            CoordinateVector point = result.get(i);
            if (point.getX() < minX && point.getY() < minY) {
                minX = point.getX();
                minY = point.getY();
                minPointIndex = i;
            }
        }
        List<CoordinateVector> orderedResult = new ArrayList<CoordinateVector>(result.size());
        for (int i = 0; i < result.size(); i++) {
            int j = (minPointIndex + i) % result.size();
            orderedResult.add(result.get(j));
        }
        return orderedResult;
    }

    private static class PointWithAngle implements Comparable<PointWithAngle> {

        private CoordinateVector point;

        private double angle;

        public PointWithAngle(CoordinateVector point, double angle) {
            this.point = point;
            this.angle = angle;
        }

        public CoordinateVector getPoint() {
            return point;
        }

        public double getAngle() {
            return angle;
        }

        public int compareTo(PointWithAngle pwa) {
            int result = Double.compare(angle, pwa.getAngle());
            if (result != 0) {
                return result;
            } else {
                return -1 * Double.compare(point.getY(), pwa.getPoint().getY());
            }
        }
    }

    /**
	 * Assuming source point as starting point of the coordinate system, returns
	 * clockwise measured angle between x-axis and (source, destination) vector.
	 * 
	 * @param source source point
	 * @param destination destination point
	 * @return angle (0..2pi)
	 */
    protected static double calculateAngle(CoordinateVector source, CoordinateVector destination) {
        double x = destination.getX() - source.getX();
        double y = destination.getY() - source.getY();
        double r = Math.sqrt(x * x + y * y);
        if (r == 0.0) {
            return 0.0;
        }
        double angle = Math.acos(x / r);
        angle = (destination.getY() > source.getY()) || (angle == 0) ? angle : 2 * Math.PI - angle;
        return angle;
    }
}
