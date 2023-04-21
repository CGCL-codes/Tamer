package view.acims.Math;

import java.awt.Point;

/**
 * Math2D contains static methods that execute many common
 * calculations useful when defining points, lines, and
 * shapes in a 2D coordinate system.
 * <p>
 * The mathematical operations are consistent with the 
 * java.awt.Graphics2D coordinate system. The x-axis is positive to
 * the right and negative to the left. The y-axis is positive
 * in the downward direction and negative in the upward
 * direction. As a consequence of this arrangement, negative
 * angle values occur in a counter-clockwise rotation and
 * positive values occur with a clockwise rotation.The origin 
 * is located at the upper left hand corner.
 * 
 * @author Robert Flasher
 * @version 1.0
 */
public class Math2D {

    protected Math2D() {
    }

    private static class UnitVector extends Point {

        public static UnitVector getUnitVector(Point p) {
            return new UnitVector(p);
        }

        private double x;

        private double y;

        UnitVector(Point p) {
            setUnitVector(p);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        private void setUnitVector(Point p) {
            double magnitude = distanceToPoint(p);
            x = (p.getX() / magnitude);
            y = (p.getY() / magnitude);
        }
    }

    /**
	 * Adds two vectors which begin at the origin and end
	 * at the points specified by the input points.
	 * <p>
	 * Formula: vecA + vecB = (vecA.x + vecB.x, vecA.y + vecB.y)
	 * 
	  * @param vecA The endpoint of the first vector
	 * @param vecB The endpoint of the second vector.
	 * @return The point calculated using the above formula.
	 */
    public static Point addVectors(Point vecA, Point vecB) {
        return new Point((int) (vecA.getX() + vecB.getX()), (int) (vecA.getY() + vecB.getY()));
    }

    /**
	 * Calculates the angle of a line with start point a and endpoint b 
	 * in radians.
	 * <p>
	 * The output range is -PI to PI radians.
	 * 
	 * @param a Start point of the line.
	 * @param b Endpoint of the line.
	 * @return The angular orientation of the line in radians.
	 */
    public static double angleOfLine(Point a, Point b) {
        Point inputVector = new Point(subtractVectors(a, b));
        double result = Math.atan(inputVector.getY() / inputVector.getX());
        if (inputVector.getX() < 0) {
            result = result - (Math.PI);
        }
        return result;
    }

    /**
	 * Calculates the angle of a line with start point a and endpoint b 
	 * in degrees.
	 * <p>
	 * The output range is 180 to -180 degrees.
	 * 
	 * @param a Start point of the line.
	 * @param b Endpoint of the line.
	 * @return The angular orientation of the line in degrees.
	 */
    public static double angleOfLineDegrees(Point a, Point b) {
        return Math.toDegrees(angleOfLine(a, b));
    }

    /**
	 * Calculates the angle of a vector starting at the origin 
	 * and ending at point vec in radians.
	 * <p>
	 * The output range is PI to -PI radians.
	 * 
	 * @param vec Endpoint of the vector.
	 * @return The angular orientation of the vector in radians.
	 */
    public static double angleOfVector(Point vec) {
        return angleOfLine(new Point(0, 0), vec);
    }

    /**
	 * Calculates the angle of a vector with start point a and 
	 * endpoint b in radians.
	 * <p>
	 * The output range is PI to -PI radians. 
	 * 
	 * @param a Start point of the vector.
	 * @param b Endpoint of the vector.
	 * @return The angular orientation of the vector in radians.
	 */
    public static double angleOfVector(Point a, Point b) {
        return angleOfLine(a, b);
    }

    /**
	 * Calculates the angle of a vector starting at the origin
	 *  and ending at point vec in degrees.
	 * <p>
	 * The output range is 180 to -180 degrees.
	 * 
	 * @param vec Endpoint of the vector.
	 * @return The angular orientation of the vector in degrees.
	 */
    public static double angleOfVectorDegrees(Point vec) {
        return Math.toDegrees(angleOfVector(vec));
    }

    /**
	 * Calculates the angle of a vector with start point a and 
	 * endpoint b in degrees.
	 * <p>
	 * The output range is 180 to -180 degrees.
	 * 
	 * @param a Start point of the vector.
	 * @param b Endpoint of the vector.
	 * @return The angular orientation of the vector in degrees.
	 */
    public static double angleOfVectorDegrees(Point a, Point b) {
        return Math.toDegrees(angleOfLine(a, b));
    }

    /**
	 * Determines the point on an arc closest to the reference point p.
	 * 
	 * The arc is represented as a whole or segment of an ellipse 
	 * bounded by a rectangle orientated at startAngle radians with
	 * respect to the positive x-axis. The 
	 * bounding rectangle is a size sufficient to enclose the entire 
	 * ellipse, even when the arc is only a segment of the ellipse.
	 * The rotation by startAngle occurs at the point arcPos, which 
	 * is the top left corner of the ellipse's bounding rectangle. 
	 * startAngle has a range of -PI to PI radians. The ellipse segment 
	 * (arc) begins at 0 radians with respect to startAngle and 
	 * continues to arcAngle. The range of arcAngle is -2*PI to 2*PI.
	 * <p>
	 * Points on the arc are determined using the formula for an
	 * ellipse: (x^2/a^2) + (y^2/b^2) = 1 for the angular range
	 * arcAngle. The point on the arc with the shortest distance 
	 * to the reference point p calculated with the common distance 
	 * formula is the return value.
	 * <p>
	 * Since a circle is a special case of an ellipse, this method
	 * is suitable for determining the closest point on a circle.
	 * The bounding rectangle would have equal length and width.
	 * 
	 * @param p The reference point.
	 * @param arcPos Top left corner point of the bounding rectangle.
	 * @param aLen Length of the bounding rectangle.
	 * @param aHt Height of the bounding rectangle.
	 * @param startAngle Angular orientation with respect to the x-axis
	 * of the bounding rectangle in radians.
	 * @param arcAngle The angular range of the ellipse of interest in
	 * radians.
	 * @return The point on the arc closest to Point p.
	 */
    public static Point closestPointOnArc(Point p, Point arcPos, int aLen, int aHt, double startAngle, double arcAngle) {
        Point closest = arcPos;
        double shortestDist = Double.MAX_VALUE;
        int a = aLen / 2;
        int b = aHt / 2;
        Point center = addVectors(arcPos, rotateVector(new Point(a, b), startAngle));
        for (int x = -a; x <= a; ++x) {
            for (int y = -b; y <= b; ++y) {
                Point testP = new Point(x, y);
                if (isOnArc(testP, aLen, aHt, arcAngle)) {
                    testP = rotatePoint(testP, startAngle);
                    testP = addVectors(testP, center);
                    double curDist = distancePointToPoint(p, testP);
                    if (curDist < shortestDist) {
                        shortestDist = curDist;
                        closest = testP;
                    }
                }
            }
        }
        return closest;
    }

    /**
	 * Determines the point on a line closest to the reference point p.
	 * <p>
	 * The line L is defined by the points pL0 and pL1.
	 * The point on L closest to p is on a line containing p that is 
	 * perpendicular to L. L has infinite length.
	 * @param p The point of reference.
	 * @param pL0 A point on the line L.
	 * @param pL1 A point on the line L.
	 * @return The point on line L closest to the reference point p.
	 */
    public static Point closestPointOnLine(Point p, Point pL0, Point pL1) {
        Point vector1 = subtractVectors(pL0, pL1);
        UnitVector unitVector = getUnitVector(vector1);
        Point vector2 = new Point(subtractVectors(pL0, p));
        double compV2OnV1 = dotProduct(vector1, vector2) / distanceToPoint(vector1);
        int projX = (int) (compV2OnV1 * unitVector.getX());
        int projY = (int) (compV2OnV1 * unitVector.getY());
        Point projVector = new Point(projX, projY);
        return addVectors(pL0, projVector);
    }

    /**
	 * Determines the point on a rectangle closest to reference point p.
	 * <p> 
	 * The closest point forms
	 * a line with the reference point p perpendicular to the side of the 
	 * rectangle closest
	 * to the reference point p. If no such line exists, the closest point
	 * on the rectangle is the corner of the rectangle closest to the 
	 * reference point.
	 * @param p The point of reference.
	 * @param rPos The top left corner of the rectangle
	 * @param length The length of the rectangle.
	 * @param width The width of the rectangle.
	 * @return The point on the rectangle closest to the reference point
	 * p
	 */
    public static Point closestPointOnRectangle(Point p, Point rPos, int length, int width) {
        Point tLeft = rPos;
        Point tRight = new Point((int) tLeft.getX() + length, (int) tLeft.getY());
        Point bLeft = new Point((int) tLeft.getX(), (int) tLeft.getY() + width);
        Point bRight = new Point((int) tRight.getX(), (int) bLeft.getY());
        Point[] pt = { closestPointOnVector(p, tLeft, tRight), closestPointOnVector(p, tLeft, bLeft), closestPointOnVector(p, tRight, bRight), closestPointOnVector(p, bLeft, bRight) };
        double[] d = { distancePointToPoint(p, pt[0]), distancePointToPoint(p, pt[1]), distancePointToPoint(p, pt[2]), distancePointToPoint(p, pt[3]) };
        for (int i = 1; i < 4; i++) {
            if (d[i] < d[0]) {
                pt[0] = pt[i];
                d[0] = d[i];
            }
        }
        return pt[0];
    }

    /**
	 * Determines the point on a vector closest to the reference point p.
	 * <p>
	 * The vector is defined by the start point pL0 and the end point 
	 * pL1. If it exists, the point on the vector closest to p defines
	 * the line perpendicular to the vector. If it does not the closest
	 * point on the vector is the start or end point. A vector is treated
	 * as a line segment for this calculation.
	 * @param p The point of reference.
	 * @param pL0 Vector start point.
	 * @param pL1 Vector end point.
	 * @return The point on the vector closest to the reference point p.
	 */
    public static Point closestPointOnVector(Point p, Point pL0, Point pL1) {
        Point pt = closestPointOnLine(p, pL0, pL1);
        if (isOnVector(pt, pL0, pL1)) {
            return pt;
        }
        double d1 = distancePointToPoint(p, pL0);
        double d2 = distancePointToPoint(p, pL1);
        if (d1 < d2) {
            return pL0;
        }
        return pL1;
    }

    /**
	 * Calculates the cross product of two vectors.
	 * <p>
	 * The vectors start at the origin and end at points vecA and vecB.
	 * <p>
	 * The formula used to calculate the cross product:
	 * <p>
	 * vecA x vecB = (vecA.x*vecB.y)-(vecA.y*vecB.x)
	 * <p>
	 * Two vectors are paralel if the output is 0.
	 * @param vecA The end point of first vector starting at the origin.
	 * @param vecB The end point of second vector starting at the origin.
	 * @return The cross product of two vectors.
	 */
    public static double crossProduct(Point vecA, Point vecB) {
        return (vecA.getX() * vecB.getY()) - (vecA.getY() * vecB.getX());
    }

    /**
	 * Calculates the distance between the reference point p and the arc.
	 * <p>
	 * The method calculates the distance between the reference point p 
	 * and the point closest to it on the arc as determined by the
	 * {@link #closestPointOnArc(Point, Point, int, int, double, double) closestPointOnArc} 
	 * method.
	 * <p>
	 * The arc is defined as an ellipse or ellipse segment bounded by
	 * a rectangle.
	 * <p>
	 * *Note: The bounding rectangle arc is a size sufficient to enclose 
	 * the entire ellipse, even when the arc is only a segment of the 
	 * ellipse.
	 * @param p Point of reference
	 * @param arcPos Top left point of the bounding rectangle.
	 * @param aLen Length of the bounding rectangle.
	 * @param aHt Height of the bounding rectangle.
	 * @param startAngle Angular orientation with respect to the x-axis
	 * of the bounding rectangle in radians.
	 * @param arcAngle The angular range of the ellipse of interest in
	 * radians.
	 * @return The shortest distance between the arc and the reference
	 * point p, where the return value >= 0.
	 */
    public static double distancePointToArc(Point p, Point arcPos, int aLen, int aHt, double startAngle, double arcAngle) {
        return distancePointToPoint(p, closestPointOnArc(p, arcPos, aLen, aHt, startAngle, arcAngle));
    }

    /**
	 * Calculates the distance between the reference point p and a line.
	 * <p>
	 * The method calculates the distance between the reference point p
	 * and the closest point on the line L as determined by 
	 * {@link #closestPointOnLine(Point, Point, Point) closestPointOnLine} 
	 * <p>
	 * The line is assumed to have infinite length.
	 * @param p Point of reference.
	 * @param pL0 A point on the line L.
	 * @param pL1 A point on the line L.
	 * @return The distance between the reference point p and the line, 
	 * where the return value >= 0.
	 */
    public static double distancePointToLine(Point p, Point pL0, Point pL1) {
        Point pt = closestPointOnLine(p, pL0, pL1);
        return distancePointToPoint(p, pt);
    }

    /**
	 * Distance between points a and b.
	 * <p>
	 * @param a first point
	 * @param b second point
	 * @return The distance between a and b, where the return value >= 0.
	 */
    public static double distancePointToPoint(Point a, Point b) {
        return a.distance(b);
    }

    /**
	 * Calculates the distance between the reference point p and a rectangle.
	 * <p>
	 * The method calculates the distance between the reference point p
	 * and the closest point on the rectangle as determined by the
	 * {@link #closestPointOnRectangle(Point, Point, int, int) closestPointOnRectangle} 
	 * method.
	 * 
	 * @param p Point of reference.
	 * @param rPos Top left point of the rectangle.
	 * @param length Length of rectangle.
	 * @param width Width of the rectangle.
	 * @return The distance between the reference point p and the 
	 * rectangle, where the return value >= 0.
	 */
    public static double distancePointToRectangle(Point p, Point rPos, int length, int width) {
        return distancePointToPoint(p, closestPointOnRectangle(p, rPos, length, width));
    }

    /**
	 * Calculates the distance between a vector and a reference point p.
	 * <p>
	 * The method calculates the distance between the reference point p
	 * and the closest point on the vector with start point pV0 and end 
	 * point pV1. The closest point is determined by the method 
	 * {@link #closestPointOnVector(Point, Point, Point) closestPointOnVector}. 
	 * @param p Point of reference
	 * @param pV0 Start point of vector.
	 * @param pV1 End point of vector.
	 * @return The distance between the reference point p and the vector, 
	 * where the return value >= 0.
	 * 
	 */
    public static double distancePointToVector(Point p, Point pV0, Point pV1) {
        Point pt = closestPointOnVector(p, pV0, pV1);
        return distancePointToPoint(p, pt);
    }

    /**
	 * Distance between the origin and point a.
	 * <p>
	 * @param a Point of reference
	 * @return The distance between the origin and point a, where the 
	 * return value >= 0.
	 */
    public static double distanceToPoint(Point a) {
        return new Point(0, 0).distance(a);
    }

    /**
	 * Calculates the scalar (dot) product of two vectors.
	 * <p>
	 * The method calculates the scalar (dot) product of two vectors
	 * with start points at the origin and end points at vecA and vecB.
	 * <p>
	 * The formula used for the calculation:
	 * <p>
	 * vecA (dot) vecB = (vecA.x*vecB.x)+(vecA.y*vecB.y)
	 * <p>
	 * A return value of 0 indicates the vectors are perpendicular.
	 * @param vecA End point of first vector.
	 * @param vecB End point of second vector
	 * @return Scalar product: vecA (dot) vecB
	 */
    public static double dotProduct(Point vecA, Point vecB) {
        Point vecC = new Point(multiplyVectors(vecA, vecB));
        return vecC.getX() + vecC.getY();
    }

    private static UnitVector getUnitVector(Point p) {
        return UnitVector.getUnitVector(p);
    }

    /**
	 * Determines if the reference point p is on an ellipse
	 * <p>
	 * The ellipse is bounded
	 * by a rectangle located at ellpsPos and rotated by startAngle
	 * radians with respect to the x-axis. The point of rotation is
	 * ellpsPos.
	 * @param p Point of reference
	 * @param ellpsPos Upper left point of bounding rectangle
	 * @param aLen Length of bounding rectangle.
	 * @param aHt Height of bounding rectangle.
	 * @param startAngle Angle of rotation of the bounding rectangle
	 * in radians with respect to the x-axis.
	 * @return A boolean true if the reference point is on the ellipse 
	 * and boolean false otherwise.
	 */
    public static boolean isOnEllipse(Point p, Point ellpsPos, int aLen, int aHt, double startAngle) {
        Point pE = closestPointOnArc(p, ellpsPos, aLen, aHt, startAngle, 2 * Math.PI);
        Point cntr = new Point((int) (ellpsPos.getX() + (aLen / 2)), (int) (ellpsPos.getY() + (aHt / 2)));
        return (distancePointToPoint(cntr, p) <= distancePointToPoint(cntr, pE));
    }

    /**
	 * Determines if the reference point p is on a rectangle.
	 * <p>
	 * The upper left corner of the rectangle is located
	 * at point rPos with the dimensions length and width.
	 * @param p Point of reference
	 * @param rPos Point at the upper left corner of the rectangle.
	 * @param length Length of the rectangle.
	 * @param height Height of the rectangle.
	 * @return A boolean true if the reference point is on the rectangle
	 * and boolean false otherwise.
	 */
    public static boolean isOnRectangle(Point p, Point rPos, int length, int height) {
        return ((p.getX() >= rPos.getX()) && (p.getX() <= (rPos.getX() + length)) && (p.getY() >= rPos.getY()) && (p.getY() <= (rPos.getY() + height)));
    }

    private static boolean isOnArc(Point p, int len, int ht, double arcAngle) {
        boolean onArc = false;
        double pAngle = angleOfVector(p);
        double arcAng = arcAngle;
        double a = len / 2;
        double b = ht / 2;
        double aSqr = a * a;
        double bSqr = b * b;
        double xSqr = p.getX() * p.getX();
        double ySqr = p.getY() * p.getY();
        double result = (xSqr / aSqr) + (ySqr / bSqr);
        if ((pAngle < 0) && (arcAng < 0)) {
            pAngle = Math.abs(pAngle);
            arcAng = Math.abs(arcAng);
        } else if ((pAngle < 0) && (arcAng > 0)) {
            pAngle = pAngle + (2 * Math.PI);
        } else if ((pAngle > 0) && (arcAng < 0)) {
            pAngle = Math.abs(pAngle - (2 * Math.PI));
            arcAng = Math.abs(arcAng);
        }
        if ((pAngle <= arcAng) && (result > .9) && (result < 1.1)) {
            onArc = true;
        }
        return onArc;
    }

    private static boolean isOnVector(Point p, Point vecSt, Point vecEnd) {
        boolean result = true;
        Point vec = subtractVectors(vecSt, vecEnd);
        Point testPt = subtractVectors(vecSt, p);
        double angle = angleOfVector(vec);
        vec = rotateVector(vec, -angle);
        testPt = rotatePoint(testPt, -angle);
        if ((testPt.getX() < -1) || (testPt.getX() > (vec.getX() + 1))) {
            result = false;
        }
        if (Math.abs(testPt.getY()) > 3) {
            result = false;
        }
        return result;
    }

    /**
	 * Calculates the vector product of two vectors.
	 * <p>
	 * The two vectors start points occur on the origin and they have 
	 * end points at vecA and vecB.
	 * <p>
	 * The formula used to calculate the product:
	 * <p>
	 * vecA*vecB = (vecA.x*vecB.x, vecA.y*vecB.y)
	 * @param vecA End point of the first vector
	 * @param vecB End point of the second vector
	 * @return The end point of the vector product whose start point is
	 * the origin.
	 */
    public static Point multiplyVectors(Point vecA, Point vecB) {
        return new Point((int) (vecA.getX() * vecB.getX()), (int) (vecA.getY() * vecB.getY()));
    }

    /**
	 * Calculates the position of point a rotated about the origin by 
	 * angle radians.
	 * @param a The point to be rotated
	 * @param angle Angle of rotation in radians with range -2*PI to -2*PI.
	 * @return The point rotated about the origin by angle.
	 */
    public static Point rotatePoint(Point a, double angle) {
        double strtAng = angleOfLine(new Point(0, 0), a);
        int x = (int) (distanceToPoint(a) * Math.cos(strtAng + angle));
        int y = (int) (distanceToPoint(a) * Math.sin(strtAng + angle));
        return new Point(x, y);
    }

    /**
	 * Determines the position of a vector rotated about the origin by 
	 * angle radians.
	 * <p>
	 * The vector starts at the origin and ends at point vec. 
	 * @param vec End point of the vector to be rotated.
	 * @param angle Angle of rotation in radians with range -2*PI to -2*PI.
	 * @return The end point of the vector rotated by angle.
	 */
    public static Point rotateVector(Point vec, double angle) {
        double strtAng = angleOfVector(vec);
        int x = (int) (vectorLength(vec) * Math.cos(strtAng + angle));
        int y = (int) (vectorLength(vec) * Math.sin(strtAng + angle));
        return new Point(x, y);
    }

    /**
	 * Determines the endpoint of a vector rotated about its start point 
	 * by angle radians.
	 * <p>
	 * @param a Start point of the vector.
	 * @param b End point of the vector.
	 * @param angle Angle to rotate vector in radians with range 
	 * -2*PI to -2*PI.
	 * @return The endpoint of the vector rotated by angle radians. 
	 */
    public static Point rotateVector(Point a, Point b, double angle) {
        Point offset = rotateVector(subtractVectors(a, b), angle);
        return addVectors(a, offset);
    }

    public static Point scalarMultiply(double scalar, Point p) {
        return new Point((int) (scalar * p.getX()), (int) (scalar * p.getY()));
    }

    /**
	 * Calculates the resultant point from vector subtraction.
	 * <p>
	 * The vectors have start points at the origin and end
	 * points at vecA and vecB.
	 * <p>
	 * Vector subtraction formula used:
	 * <p>
	 * vecB - vecA = (vecB.x-vecA.x, vecB.y-vecA.y)
	 * @param vecA End point of the vector to subtract. 
	 * @param vecB End point of the vector to subtract from.
	 * @return The end point of the vector subtraction's result.
	 */
    public static Point subtractVectors(Point vecA, Point vecB) {
        return new Point((int) (vecB.getX() - vecA.getX()), (int) (vecB.getY() - vecA.getY()));
    }

    /**
	 * Returns the string representation of a point.
	 * <p>
	 * @param p The point to be represented as a string.
	 * @return String representation of the point in the form "(x, y)".
	 */
    public static String toString(Point p) {
        return "(" + Double.toString(p.getX()) + "," + Double.toString(p.getY()) + ")";
    }

    /**
	 * Determines the position of a point relocated by a length distance 
	 * in the direction of the angle direction in radians.
	 * @param a The point to be transposed.
	 * @param distance The real number distance greater than 0 that
	 * the vector is to be moved.
	 * @param direction Angle identifying the direction the point is moved.
	 * The range is -2*PI to 2*PI.
	 * @return The transposed point.
	 */
    public static Point transposePoint(Point a, double distance, double direction) {
        int x = (int) ((distance * Math.cos(direction)) + a.getX());
        int y = (int) ((distance * Math.sin(direction)) + a.getY());
        return new Point(x, y);
    }

    /**
	 * Determines the position of a vector relocated by a length distance 
	 * in the direction of the angle direction in radians.
	 * <p>
	 * @param a Start point of the vector to be transposed.
	 * @param b End point of the vector to be transposed.
	 * @param distance The real number distance greater than 0 that
	 * the vector is to be moved.
	 * @param direction Angle identifying the direction the vector is
	 * moved. The range is -2*PI to 2*PI.
	 * @return An array of points containing the start point at index
	 * 0 and the end point at index 1 of the transposed vector.
	 */
    public static Point[] transposeVector(Point a, Point b, double distance, double direction) {
        Point[] result = new Point[2];
        double vecAngle = angleOfVector(a, b);
        result[0] = transposePoint(a, distance, direction + vecAngle);
        result[1] = transposePoint(b, distance, direction + vecAngle);
        return result;
    }

    /**
	 * Calculates the length or magnitude of a vector starting at the
	 * origin.
	 * <p>
	 * @param vec End point of the vector.
	 * @return The real number distance or length greater than 0 of the
	 * vector.
	 */
    public static double vectorLength(Point vec) {
        return distanceToPoint(vec);
    }

    /**
	 * Calculates the length or magnitude of a vector with start point a
	 * and end point b.
	 * <p>
	 * @param a Start point of the vector.
	 * @param b End point of the vector.
	 * @return The real number distance or length greater than 0 of the
	 * vector.
	 */
    public static double vectorLength(Point a, Point b) {
        return distancePointToPoint(a, b);
    }
}
