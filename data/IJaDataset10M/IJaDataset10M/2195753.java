package test.jts.junit.geom;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @version 1.7
 */
public class AreaLengthTest extends TestCase {

    private PrecisionModel precisionModel = new PrecisionModel();

    private GeometryFactory geometryFactory = new GeometryFactory(precisionModel, 0);

    WKTReader reader = new WKTReader(geometryFactory);

    private static final double TOLERANCE = 1E-5;

    public static void main(String args[]) {
        TestRunner.run(AreaLengthTest.class);
    }

    public AreaLengthTest(String name) {
        super(name);
    }

    public void testLength() throws Exception {
        checkLength("MULTIPOINT (220 140, 180 280)", 0.0);
        checkLength("LINESTRING (220 140, 180 280)", 145.6021977);
        checkLength("LINESTRING (0 0, 100 100)", 141.4213562373095);
        checkLength("POLYGON ((20 20, 40 20, 40 40, 20 40, 20 20))", 80.0);
        checkLength("POLYGON ((20 20, 40 20, 40 40, 20 40, 20 20), (25 35, 35 35, 35 25, 25 25, 25 35))", 120.0);
    }

    public void testArea() throws Exception {
        checkArea("MULTIPOINT (220 140, 180 280)", 0.0);
        checkArea("LINESTRING (220 140, 180 280)", 0.0);
        checkArea("POLYGON ((20 20, 40 20, 40 40, 20 40, 20 20))", 400.0);
        checkArea("POLYGON ((20 20, 40 20, 40 40, 20 40, 20 20), (25 35, 35 35, 35 25, 25 25, 25 35))", 300.0);
    }

    public void checkLength(String wkt, double expectedValue) throws Exception {
        Geometry g = reader.read(wkt);
        double len = g.getLength();
        assertEquals(expectedValue, len, TOLERANCE);
    }

    public void checkArea(String wkt, double expectedValue) throws Exception {
        Geometry g = reader.read(wkt);
        assertEquals(expectedValue, g.getArea(), TOLERANCE);
    }
}
