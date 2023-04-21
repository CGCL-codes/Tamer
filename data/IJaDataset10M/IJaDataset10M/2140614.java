package ch.headshot.photomap.client.editor.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.headshot.photomap.client.gpx.WayPoint;
import com.google.gwt.user.client.Window;

public class Simplifier {

    public List<WayPoint> simplify(List<WayPoint> points) {
        String prompt = Window.prompt("Tolerance", ".0001");
        try {
            Double.valueOf(prompt);
        } catch (NumberFormatException e) {
            Window.alert(prompt + " is not a number");
            return points;
        }
        return DouglasPeuckerReduction(points, .0005);
    }

    public static List<WayPoint> DouglasPeuckerReduction(List<WayPoint> Points, Double Tolerance) {
        if (Points == null || Points.size() < 3) return Points;
        int firstPoint = 0;
        int lastPoint = Points.size() - 1;
        List<Integer> pointIndexsToKeep = new ArrayList<Integer>();
        pointIndexsToKeep.add(firstPoint);
        pointIndexsToKeep.add(lastPoint);
        while (Points.get(firstPoint).equals(Points.get(lastPoint))) {
            lastPoint--;
        }
        DouglasPeuckerReduction(Points, firstPoint, lastPoint, Tolerance, pointIndexsToKeep);
        List<WayPoint> returnPoints = new ArrayList<WayPoint>();
        Collections.sort(pointIndexsToKeep);
        for (int index : pointIndexsToKeep) {
            returnPoints.add(Points.get(index));
        }
        return returnPoints;
    }

    private static void DouglasPeuckerReduction(List<WayPoint> points, int firstPoint, int lastPoint, Double tolerance, List<Integer> pointIndexsToKeep) {
        Double maxDistance = 0.0;
        int indexFarthest = 0;
        for (int index = firstPoint; index < lastPoint; index++) {
            Double distance = PerpendicularDistance(points.get(firstPoint), points.get(lastPoint), points.get(index));
            if (distance > maxDistance) {
                maxDistance = distance;
                indexFarthest = index;
            }
        }
        if (maxDistance > tolerance && indexFarthest != 0) {
            pointIndexsToKeep.add(indexFarthest);
            DouglasPeuckerReduction(points, firstPoint, indexFarthest, tolerance, pointIndexsToKeep);
            DouglasPeuckerReduction(points, indexFarthest, lastPoint, tolerance, pointIndexsToKeep);
        }
    }

    public static Double PerpendicularDistance(WayPoint Point1, WayPoint Point2, WayPoint Point) {
        Double area = Math.abs(.5 * (Point1.getLatitude() * Point2.getLongitude() + Point2.getLatitude() * Point.getLongitude() + Point.getLatitude() * Point1.getLongitude() - Point2.getLatitude() * Point1.getLongitude() - Point.getLatitude() * Point2.getLongitude() - Point1.getLatitude() * Point.getLongitude()));
        Double bottom = Math.sqrt(Math.pow(Point1.getLatitude() - Point2.getLatitude(), 2) + Math.pow(Point1.getLongitude() - Point2.getLongitude(), 2));
        Double height = area / bottom * 2;
        return height;
    }

    public List<WayPoint> concat(List<WayPoint> points1, List<WayPoint> points2) {
        List<WayPoint> concat = new ArrayList<WayPoint>();
        if (points1.isEmpty() || points2.isEmpty() || distance(points1.get(points1.size() - 1), points2.get(0)) < distance(points2.get(points2.size() - 1), points1.get(0))) {
            concat.addAll(points1);
            concat.addAll(points2);
        } else {
            concat.addAll(points2);
            concat.addAll(points1);
        }
        return concat;
    }

    private double distance(WayPoint wayPoint, WayPoint wayPoint2) {
        double a = Math.abs(wayPoint.getLatitude() - wayPoint2.getLatitude());
        double b = Math.abs(wayPoint.getLongitude() - wayPoint2.getLongitude());
        return Math.sqrt(a * a + b * b);
    }

    public List<WayPoint> concatMultiple(List<WayPoint> first, List<List<WayPoint>> points) {
        List<WayPoint> result = first;
        double nearestDist;
        List<WayPoint> nearest;
        while (!points.isEmpty()) {
            nearestDist = Double.MAX_VALUE;
            nearest = null;
            for (List<WayPoint> p : points) {
                double dist = distance(result.get(result.size() - 1), p.get(0));
                if (nearestDist > dist) {
                    nearestDist = dist;
                    nearest = p;
                }
            }
            result = concat(result, nearest);
            points.remove(nearest);
        }
        return result;
    }
}
