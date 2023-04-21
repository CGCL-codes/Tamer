package be.gnx.dukono.projection;

import junit.framework.TestCase;
import be.gnx.dukono.coordinate.Geographic2DCoordinate;
import be.gnx.dukono.coordinate.Projected2DCoordinate;

public class MercatorTest extends TestCase {

    private static double defaultDelta = 0.000000000000001;

    public void testMercator() {
        Geographic2DCoordinate geographic2dCoordinate = new Geographic2DCoordinate();
        geographic2dCoordinate.setCoordinates(Math.PI / 4, 1.1);
        Mercator mercator = new Mercator();
        assertEquals(1.1, geographic2dCoordinate.toProjected2DCoordinate(mercator).getEasting(), defaultDelta);
        assertEquals(0.8813735870195429, geographic2dCoordinate.toProjected2DCoordinate(mercator).getNorthing(), defaultDelta);
    }

    public void testInverseMercator() {
        Mercator mercator = new Mercator();
        Projected2DCoordinate projected2dCoordinate = new Projected2DCoordinate(mercator);
        projected2dCoordinate.setCoordinates(1.1, 0.8813735870195429, null);
        assertEquals(1.1, projected2dCoordinate.toGeographic2DCoordinate().getLongitude(), defaultDelta);
        assertEquals(Math.PI / 4, projected2dCoordinate.toGeographic2DCoordinate().getLatitude(), defaultDelta);
    }

    public void testMercatorWithOrigin() {
        Geographic2DCoordinate geographic2dCoordinate = new Geographic2DCoordinate();
        geographic2dCoordinate.setCoordinates(Math.PI / 4, 1.1);
        Geographic2DCoordinate mercatorOrigin = new Geographic2DCoordinate();
        mercatorOrigin.setCoordinates(1.1, 1.1);
        Mercator mercator = new Mercator();
        mercator.setOrigin(mercatorOrigin);
        assertEquals(0.0, geographic2dCoordinate.toProjected2DCoordinate(mercator).getEasting(), defaultDelta);
        assertEquals(0.8813735870195429, geographic2dCoordinate.toProjected2DCoordinate(mercator).getNorthing(), defaultDelta);
    }

    public void testNegativeInfinity() {
        Geographic2DCoordinate geographic2dCoordinate = new Geographic2DCoordinate();
        geographic2dCoordinate.setCoordinates(-(Math.PI / 2), 0.0);
        Mercator mercator = new Mercator();
        assertEquals(Double.NEGATIVE_INFINITY, geographic2dCoordinate.toProjected2DCoordinate(mercator).getNorthing(), defaultDelta);
    }
}
