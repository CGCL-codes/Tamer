package org.isakiev.wic.core.filter;

import java.util.ArrayList;
import java.util.List;
import org.isakiev.wic.core.math.CoordinateVector;
import org.isakiev.wic.core.math.CoordinateMatrix;

/**
 * Gets representatives of the contiguity classes
 * 
 * @author Ruslan Isakiev
 */
public class RepresentativeCreator {

    private static final CoordinateVector vector1 = new CoordinateVector(0, 1);

    private static final CoordinateVector vector2 = new CoordinateVector(1, 0);

    private static final CoordinateVector vector3 = new CoordinateVector(1, 1);

    public static List<CoordinateVector> getNotNullRepresentatives(CoordinateMatrix matrix) {
        CoordinateVector v1 = matrix.multiply(vector1);
        CoordinateVector v2 = matrix.multiply(vector2);
        CoordinateVector v3 = matrix.multiply(vector3);
        int minX = Math.min(Math.min(0, v1.getIntX()), Math.min(v2.getIntX(), v3.getIntX()));
        int minY = Math.min(Math.min(0, v1.getIntY()), Math.min(v2.getIntY(), v3.getIntY()));
        int maxX = Math.max(Math.max(0, v1.getIntX()), Math.max(v2.getIntX(), v3.getIntX()));
        int maxY = Math.max(Math.max(0, v1.getIntY()), Math.max(v2.getIntY(), v3.getIntY()));
        CoordinateMatrix inverseMatrix = matrix.invert();
        List<CoordinateVector> representatives = new ArrayList<CoordinateVector>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (x != 0 || y != 0) {
                    CoordinateVector source = new CoordinateVector(x, y);
                    CoordinateVector result = inverseMatrix.multiply(source);
                    if ((0 <= result.getX()) && (result.getX() < 1) && (0 <= result.getY()) && (result.getY() < 1)) {
                        representatives.add(source);
                    }
                }
            }
        }
        return representatives;
    }
}
