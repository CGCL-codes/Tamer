package util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;

public class MathTools {

    /**
	 * Determines the centroid of a set of 2D points.
	 * @param points
	 * @return
	 */
    public static Point2D centroid(int[][] points) {
        Point2D centroid = null;
        float avgX = 0;
        float avgY = 0;
        for (int i = 0; i < points.length; i++) {
            avgX += points[i][0];
            avgY += points[i][1];
        }
        avgX /= points.length;
        avgY /= points.length;
        centroid = new Point2D.Float(avgX, avgY);
        return centroid;
    }

    /**
	 * Returns the slope of the two points.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
    public static float slope(float x1, float y1, float x2, float y2) {
        float slope = 0;
        float dx = x2 - x1;
        float dy = y2 - y1;
        slope = dy / dx;
        return slope;
    }

    /**
	 * Returns the slope of the line. For vertical lines this is infinite (use Float.isInfinite()).
	 * @param line
	 * @return
	 */
    public static float slope(Line2D line) {
        return slope((float) line.getX1(), (float) line.getY1(), (float) line.getX2(), (float) line.getY2());
    }

    /**
	 * Determines the value of a linear function of one variable.
	 * @param line  The line representing the function.
	 * @param x  The x-value at which to retrieve the value.
	 * @return
	 */
    public static double getFuncVal(Line2D line, double x) {
        double funcVal = Double.NaN;
        double slope = slope(line);
        double intercept = line.getY1() - slope * line.getX1();
        funcVal = slope * x + intercept;
        return funcVal;
    }

    /**
	 * Determines the midpoint of a Line2D.
	 * @param line
	 * @return
	 */
    public static Point2D midpoint(Line2D line) {
        return new Point2D.Double(mean(line.getX1(), line.getX2()), mean(line.getY1(), line.getY2()));
    }

    /**
	 * 
	 * @param line1
	 * @param line2
	 * @param threshold
	 * @return
	 */
    public static boolean areParallel(Line2D line1, Line2D line2, float threshold) {
        float slope1 = slope(line1);
        float slope2 = slope(line2);
        if (Float.isInfinite(slope1) && Float.isInfinite(slope2)) {
            return true;
        }
        Point2D midP1 = midpoint(line1);
        Point2D midP2 = midpoint(line2);
        double transXLine2 = midP2.getX() - midP1.getX();
        double transYLine2 = midP2.getY() - midP1.getY();
        Point2D p1Line2 = new Point2D.Double(line2.getX1() - transXLine2, line2.getY1() - transYLine2);
        Point2D p2Line2 = new Point2D.Double(line2.getX2() - transXLine2, line2.getY2() - transYLine2);
        Line2D transLine2 = new Line2D.Double(p1Line2, p2Line2);
        double dist1To2 = transLine2.ptLineDist(line1.getX1(), line1.getY1());
        double dist2To1 = line1.ptLineDist(transLine2.getX1(), transLine2.getY1());
        double avgPtLnDist = (dist1To2 + dist2To1) / 2;
        if (avgPtLnDist <= threshold) return true; else return false;
    }

    /**
	 * Returns the average distance between each consecutive point in the sample.
	 * @param samplePts
	 * @return
	 */
    public static float avgDistBetweenPts(int[][] samplePts) {
        float avgDist = 0;
        float sumDist = 0;
        for (int i = 0; i < samplePts.length - 1; i++) {
            sumDist += Point2D.distance(samplePts[i][0], samplePts[i][1], samplePts[i + 1][0], samplePts[i + 1][1]);
        }
        avgDist = sumDist / samplePts.length;
        return avgDist;
    }

    /**
	 * Determines the simple linear best fit of the points.
	 * @param points
	 * @param xBar  The expected value of x.
	 * @param yBar  The expected value of y.
	 * @return
	 */
    public static Line2D linearFit(int[][] points, float xBar, float yBar) {
        Line2D bestFit = null;
        float slopeNum = 0;
        float slopeDenom = 0;
        if (points.length < 1) points.hashCode();
        if (points[0].length != 2) points.hashCode();
        float lowestX = points[0][0];
        float largestX = lowestX;
        int lowestObservedY = points[0][1];
        int largestObservedY = points[0][1];
        for (int i = 0; i < points.length; i++) {
            if (lowestX > points[i][0]) lowestX = points[i][0];
            if (largestX < points[i][0]) largestX = points[i][0];
            if (lowestObservedY > points[i][1]) lowestObservedY = points[i][1];
            if (largestObservedY < points[i][1]) largestObservedY = points[i][1];
            slopeNum += (points[i][0] - xBar) * (points[i][1] - yBar);
            slopeDenom += (float) Math.pow((points[i][0] - xBar), 2);
        }
        float slope = slopeNum / slopeDenom;
        float intercept = yBar - slope * xBar;
        float lowestY = intercept + slope * lowestX;
        float largestY = intercept + slope * largestX;
        if (Float.isNaN(slope)) {
            bestFit = new Line2D.Float(xBar, lowestObservedY, xBar, largestObservedY);
        } else {
            bestFit = new Line2D.Float(lowestX, lowestY, largestX, largestY);
        }
        return bestFit;
    }

    public static Line2D linearFit(Line2D line1, Line2D line2) {
        Point2D midP1 = midpoint(line1);
        Point2D midP2 = midpoint(line2);
        Line2D secant = new Line2D.Float(midP1, midP2);
        Point2D newCentroid = midpoint(secant);
        int[][] points = { { (int) midP1.getX(), (int) midP1.getY() }, { (int) midP2.getX(), (int) midP2.getY() } };
        Line2D bestFit = linearFit(points, (float) newCentroid.getX(), (float) newCentroid.getY());
        return bestFit;
    }

    /**
	 * Determines the standard deviation of the set of points.
	 * @param points
	 * @return
	 */
    public static float standardDeviation(int[][] points, Line2D lineBestFit) {
        double stdDev = 0;
        float slope = slope((float) lineBestFit.getX1(), (float) lineBestFit.getY1(), (float) lineBestFit.getX2(), (float) lineBestFit.getY2());
        float intercept = (float) (lineBestFit.getY1() - slope * lineBestFit.getX1());
        double sumVariance = 0;
        for (int i = 0; i < points.length; i++) {
            float x = points[i][0];
            float yBar = intercept + slope * x;
            sumVariance += Math.pow(points[i][1] - yBar, 2);
        }
        stdDev = Math.sqrt(sumVariance / points.length);
        return (float) stdDev;
    }

    /**
	 * Determines the average distance of the points to the line.
	 * 
	 * @param pts
	 * @param line
	 * @return
	 */
    public static double avgPtLnDist(int[][] points, Line2D line) {
        double avgPtLnDist = -1;
        double sumVariance = 0;
        for (int i = 0; i < points.length; i++) {
            sumVariance += line.ptLineDist(points[i][0], points[i][1]);
        }
        avgPtLnDist = sumVariance / (double) points.length;
        return avgPtLnDist;
    }

    /**
	 * Finds the trimmed average of each 4-point subset in the list of points.
	 * The points are assumed to be in order around the list of points.
	 * 
	 * @param points
	 * @param trimPercentage  a number between 0 and 1
	 * @return
	 */
    public static double avgPtLnDist(int[][] points, float trimPercentage) {
        double avgPtLnDist = 0;
        LinkedList<Double> stdDevs = new LinkedList<Double>();
        int setSize = 4;
        for (int i = 0; i < points.length - setSize + 1; i++) {
            int[][] triplet = new int[setSize][2];
            for (int j = 0; j < setSize; j++) {
                triplet[j][0] = points[i + j][0];
                triplet[j][1] = points[i + j][1];
            }
            Point2D ctrd = centroid(triplet);
            Line2D bestFit = linearFit(triplet, (float) ctrd.getX(), (float) ctrd.getY());
            stdDevs.add(new Double(avgPtLnDist(triplet, bestFit)));
        }
        Collections.sort(stdDevs);
        int trimAmt = (int) (trimPercentage * stdDevs.size());
        for (int i = trimAmt; i < stdDevs.size(); i++) {
            avgPtLnDist += stdDevs.get(i);
        }
        avgPtLnDist /= stdDevs.size() - trimAmt;
        return avgPtLnDist;
    }

    /**
	 * Determines the intersection point between two straight lines.
	 * It is assumed the lines are not the same.
	 * 
	 * @param line1
	 * @param line2
	 * @return  The intersection point of the two lines.  
	 */
    public static Point2D intersection(Line2D line1, Line2D line2) {
        Point2D intersection = null;
        double isectX = Double.NaN;
        double isectY = Double.NaN;
        if (Float.isInfinite(slope(line1))) {
            if (Float.isInfinite(slope(line2))) return new Point2D.Double(isectX, isectY);
            isectX = line1.getX1();
            isectY = getFuncVal(line2, isectX);
        } else if (Float.isInfinite(slope(line2))) {
            if (Float.isInfinite(slope(line1))) return new Point2D.Double(isectX, isectY);
            isectX = line2.getX1();
            isectY = getFuncVal(line1, isectX);
        } else {
            double slope1 = slope(line1);
            double slope2 = slope(line2);
            if (slope1 == slope2) return null;
            double intercept1 = line1.getY1() - slope1 * line1.getX1();
            double intercept2 = line2.getY1() - slope2 * line2.getX1();
            isectX = (intercept1 - intercept2) / (slope2 - slope1);
            isectY = (slope2 * intercept1 - slope1 * intercept2) / (slope2 - slope1);
        }
        intersection = new Point2D.Double(isectX, isectY);
        return intersection;
    }

    /**
	 * Returns the mean value of x and y.
	 * @param x
	 * @param y
	 * @return
	 */
    public static double mean(double x, double y) {
        return (x + y) / 2;
    }
}
