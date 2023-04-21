package org.seehawk.core.tools;

import org.seehawk.core.data.BaseImage;
import org.seehawk.core.data.Matrix;
import org.seehawk.core.data.Point;
import org.seehawk.core.data.Region;

/** this class holds calculator utility functions. */
public class Calculator {

    /** Alters the image to fit the input region. */
    public static BaseImage alterImage(BaseImage image, Region region) {
        Region regionStart = new Region(image.getWidth(), image.getHeight());
        if (regionStart.matchesShape(region)) {
            return image;
        }
        Matrix homography = Calculator.calculateHomographyMatrix(regionStart.getContainingPoints(), region.getContainingPoints());
        if (homography == null) {
            return null;
        }
        Matrix inverseHomography = homography.inverse();
        if (inverseHomography == null) {
            return null;
        }
        BaseImage newImage = image.createImage((int) region.getWidth(), (int) region.getHeight());
        Region newImageRegion = region.clone();
        newImageRegion.move(0, 0, true);
        newImage.setRegion(newImageRegion);
        Point origPoint = null;
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                origPoint = Calculator.applyHomography(inverseHomography, x, y);
                newImage.setColor(image.getColor(origPoint.x, origPoint.y), x, y);
            }
        }
        return newImage;
    }

    /** Applies the homography to the point */
    public static Point applyHomography(Matrix homography, Point point) {
        return applyHomography(homography, point.x, point.y);
    }

    /** Applies the homography to the x, y points and returns new point */
    public static Point applyHomography(Matrix homography, double x, double y) {
        Matrix oldPoint = new Matrix(3, 1);
        oldPoint.set(0, x);
        oldPoint.set(1, y);
        oldPoint.set(2, 1);
        Matrix newPoint = homography.times(oldPoint);
        if ((newPoint == null) || (newPoint.get(2) == 0)) {
            return null;
        }
        for (int i = 0; i < 3; i++) {
            newPoint.set(i, newPoint.get(i) / newPoint.get(2));
        }
        return (new Point(newPoint.get(0), newPoint.get(1)));
    }

    /** Calculates the homography matrix mapping the points */
    public static Matrix calculateHomographyMatrix(Point[] oldPoints, Point[] newPoints) {
        if (oldPoints.length != newPoints.length) {
            return null;
        }
        int points = oldPoints.length;
        Matrix A = new Matrix(2 * points, 8);
        Matrix B = new Matrix(2 * points, 1);
        int row = 0;
        for (int point = 0; point < points; point++) {
            row = 2 * point;
            A.set(row, 0, -oldPoints[point].x);
            A.set(row, 1, -oldPoints[point].y);
            A.set(row, 2, -1);
            A.set(row, 6, oldPoints[point].x * newPoints[point].x);
            A.set(row, 7, oldPoints[point].y * newPoints[point].x);
            B.set(row, 0, -newPoints[point].x);
            row = 2 * point + 1;
            A.set(row, 3, -oldPoints[point].x);
            A.set(row, 4, -oldPoints[point].y);
            A.set(row, 5, -1);
            A.set(row, 6, oldPoints[point].x * newPoints[point].y);
            A.set(row, 7, oldPoints[point].y * newPoints[point].y);
            B.set(row, 0, -newPoints[point].y);
        }
        Matrix homoParams = A.solve(B);
        if (homoParams == null) {
            return null;
        }
        Matrix homo = new Matrix(3, 3);
        homo.set(0, 0, homoParams.get(0, 0));
        homo.set(0, 1, homoParams.get(1, 0));
        homo.set(0, 2, homoParams.get(2, 0));
        homo.set(1, 0, homoParams.get(3, 0));
        homo.set(1, 1, homoParams.get(4, 0));
        homo.set(1, 2, homoParams.get(5, 0));
        homo.set(2, 0, homoParams.get(6, 0));
        homo.set(2, 1, homoParams.get(7, 0));
        homo.set(2, 2, 1);
        return homo;
    }
}
