package test.jts.junit.operation;

import com.vividsolutions.jts.algorithm.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.*;
import com.vividsolutions.jts.operation.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Tests {@link IsSimpleOp} with different {@link BoundaryNodeRule}s.
 *
 * @author Martin Davis
 * @version 1.7
 */
public class IsSimpleTest extends TestCase {

    private static final double TOLERANCE = 0.00005;

    public static void main(String args[]) {
        TestRunner.run(IsSimpleTest.class);
    }

    private GeometryFactory fact = new GeometryFactory();

    private WKTReader rdr = new WKTReader(fact);

    public IsSimpleTest(String name) {
        super(name);
    }

    /**
   * 2 LineStrings touching at an endpoint
   * @throws Exception
   */
    public void test2TouchAtEndpoint() throws Exception {
        String a = "MULTILINESTRING((0 1, 1 1, 2 1), (0 0, 1 0, 2 1))";
        runIsSimpleTest(a, BoundaryNodeRule.MOD2_BOUNDARY_RULE, true, new Coordinate(2, 1));
        runIsSimpleTest(a, BoundaryNodeRule.ENDPOINT_BOUNDARY_RULE, true, new Coordinate(2, 1));
    }

    /**
   * 3 LineStrings touching at an endpoint.
   * 
   * @throws Exception
   */
    public void test3TouchAtEndpoint() throws Exception {
        String a = "MULTILINESTRING ((0 1, 1 1, 2 1),   (0 0, 1 0, 2 1),  (0 2, 1 2, 2 1))";
        runIsSimpleTest(a, BoundaryNodeRule.MOD2_BOUNDARY_RULE, true, new Coordinate(2, 1));
        runIsSimpleTest(a, BoundaryNodeRule.ENDPOINT_BOUNDARY_RULE, true, new Coordinate(2, 1));
    }

    public void testCross() throws Exception {
        String a = "MULTILINESTRING ((20 120, 120 20), (20 20, 120 120))";
        runIsSimpleTest(a, BoundaryNodeRule.MOD2_BOUNDARY_RULE, false, new Coordinate(70, 70));
        runIsSimpleTest(a, BoundaryNodeRule.ENDPOINT_BOUNDARY_RULE, false, new Coordinate(70, 70));
    }

    public void testMultiLineStringWithRingTouchAtEndpoint() throws Exception {
        String a = "MULTILINESTRING ((100 100, 20 20, 200 20, 100 100), (100 200, 100 100))";
        runIsSimpleTest(a, BoundaryNodeRule.MOD2_BOUNDARY_RULE, false, new Coordinate(100, 100));
        runIsSimpleTest(a, BoundaryNodeRule.ENDPOINT_BOUNDARY_RULE, true);
    }

    public void testRing() throws Exception {
        String a = "LINESTRING (100 100, 20 20, 200 20, 100 100)";
        runIsSimpleTest(a, BoundaryNodeRule.MOD2_BOUNDARY_RULE, true);
        runIsSimpleTest(a, BoundaryNodeRule.ENDPOINT_BOUNDARY_RULE, true);
    }

    private void runIsSimpleTest(String wkt, BoundaryNodeRule bnRule, boolean expectedResult) throws ParseException {
        runIsSimpleTest(wkt, bnRule, expectedResult, null);
    }

    private void runIsSimpleTest(String wkt, BoundaryNodeRule bnRule, boolean expectedResult, Coordinate expectedLocation) throws ParseException {
        Geometry g = rdr.read(wkt);
        IsSimpleOp op = new IsSimpleOp(g, bnRule);
        boolean isSimple = false;
        isSimple = op.isSimple();
        Coordinate nonSimpleLoc = op.getNonSimpleLocation();
        assertTrue(isSimple || nonSimpleLoc != null);
        assertTrue(expectedResult == isSimple);
        if (!isSimple && expectedLocation != null) {
            assertTrue(expectedLocation.distance(nonSimpleLoc) < TOLERANCE);
        }
    }
}
