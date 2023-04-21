package org.gvsig.gpe.containers;

import junit.framework.TestCase;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GeometryAsserts {

    public static void bbox(Bbox bbox, double[] x, double[] y, double[] z) {
        TestCase.assertNotNull(bbox);
        if (bbox != null) {
            TestCase.assertEquals(new Double(bbox.getMinX()), new Double(x[0]));
            TestCase.assertEquals(new Double(bbox.getMaxX()), new Double(x[1]));
            TestCase.assertEquals(new Double(bbox.getMinY()), new Double(y[0]));
            TestCase.assertEquals(new Double(bbox.getMaxY()), new Double(y[1]));
            TestCase.assertEquals(new Double(bbox.getMinZ()), new Double(z[0]));
            TestCase.assertEquals(new Double(bbox.getMaxZ()), new Double(z[1]));
        }
    }

    public static void point(Point point, double x, double y, double z) {
        TestCase.assertNotNull(point);
        if (point != null) {
            TestCase.assertEquals(new Double(point.getX()), new Double(x));
            TestCase.assertEquals(new Double(point.getY()), new Double(y));
            TestCase.assertEquals(new Double(point.getZ()), new Double(z));
        }
    }

    public static void lineString(LineString lineString, double[] x, double[] y, double[] z) {
        TestCase.assertNotNull(lineString);
        if (lineString != null) {
            for (int i = 0; i < lineString.getCoordinatesNumber(); i++) {
                TestCase.assertEquals(new Double(lineString.geCoordinateAt(i, 0)), new Double(x[i]));
                TestCase.assertEquals(new Double(lineString.geCoordinateAt(i, 1)), new Double(y[i]));
                TestCase.assertEquals(new Double(lineString.geCoordinateAt(i, 2)), new Double(z[i]));
            }
        }
    }

    public static void linearRing(LinearRing linearRing, double[] x, double[] y, double[] z) {
        TestCase.assertNotNull(linearRing);
        if (linearRing != null) {
            for (int i = 0; i < linearRing.getCoordinatesNumber(); i++) {
                TestCase.assertEquals(new Double(linearRing.geCoordinateAt(i, 0)), new Double(x[i]));
                TestCase.assertEquals(new Double(linearRing.geCoordinateAt(i, 1)), new Double(y[i]));
                TestCase.assertEquals(new Double(linearRing.geCoordinateAt(i, 2)), new Double(z[i]));
            }
        }
    }

    public static void polygon(Polygon polygon, double[] x, double[] y, double[] z) {
        TestCase.assertNotNull(polygon);
        if (polygon != null) {
            for (int i = 0; i < polygon.getCoordinatesNumber(); i++) {
                TestCase.assertEquals(new Double(polygon.geCoordinateAt(i, 0)), new Double(x[i]));
                TestCase.assertEquals(new Double(polygon.geCoordinateAt(i, 1)), new Double(y[i]));
                TestCase.assertEquals(new Double(polygon.geCoordinateAt(i, 2)), new Double(z[i]));
            }
        }
    }

    private static void assertArray(double[] x, double[] y) {
        TestCase.assertEquals(x.length, y.length);
        for (int i = 0; i < x.length; i++) {
            TestCase.assertEquals(new Double(x[i]), new Double(y[i]));
        }
    }
}
