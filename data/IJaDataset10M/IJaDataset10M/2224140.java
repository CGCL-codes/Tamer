package test.jts.junit.geom.prep;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.prep.*;
import com.vividsolutions.jts.geom.util.*;
import com.vividsolutions.jts.io.*;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import com.vividsolutions.jts.util.Stopwatch;

/**
 * Stress tests fast intersector to confirm it find intersections correctly.
 * 
 * @author Owner
 *
 */
public class PreparedPolygonIntersectsStressTest extends TestCase {

    static final int MAX_ITER = 10000;

    static PrecisionModel pm = new PrecisionModel();

    static GeometryFactory fact = new GeometryFactory(pm, 0);

    static WKTReader wktRdr = new WKTReader(fact);

    static WKTWriter wktWriter = new WKTWriter();

    public static void main(String args[]) {
        TestRunner.run(PreparedPolygonIntersectsStressTest.class);
    }

    boolean testFailed = false;

    public PreparedPolygonIntersectsStressTest(String name) {
        super(name);
    }

    public void test() {
        run(1000);
    }

    public void run(int nPts) {
        Geometry poly = createSineStar(new Coordinate(0, 0), 100, nPts);
        System.out.println(poly);
        System.out.println();
        test(poly);
    }

    Geometry createCircle(Coordinate origin, double size, int nPts) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(origin);
        gsf.setSize(size);
        gsf.setNumPoints(nPts);
        Geometry circle = gsf.createCircle();
        return circle;
    }

    Geometry createSineStar(Coordinate origin, double size, int nPts) {
        SineStarFactory gsf = new SineStarFactory();
        gsf.setCentre(origin);
        gsf.setSize(size);
        gsf.setNumPoints(nPts);
        gsf.setArmLengthRatio(0.1);
        gsf.setNumArms(20);
        Geometry poly = gsf.createSineStar();
        return poly;
    }

    LineString createTestLine(Envelope env, double size, int nPts) {
        double width = env.getWidth();
        double xOffset = width * Math.random();
        double yOffset = env.getHeight() * Math.random();
        Coordinate basePt = new Coordinate(env.getMinX() + xOffset, env.getMinY() + yOffset);
        LineString line = createTestLine(basePt, size, nPts);
        return line;
    }

    LineString createTestLine(Coordinate base, double size, int nPts) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(base);
        gsf.setSize(size);
        gsf.setNumPoints(nPts);
        Geometry circle = gsf.createCircle();
        return (LineString) circle.getBoundary();
    }

    public void test(Geometry g) {
        int count = 0;
        while (count < MAX_ITER) {
            count++;
            LineString line = createTestLine(g.getEnvelopeInternal(), 10, 20);
            testResultsEqual(g, line);
        }
    }

    public void testResultsEqual(Geometry g, LineString line) {
        boolean slowIntersects = g.intersects(line);
        PreparedGeometryFactory pgFact = new PreparedGeometryFactory();
        PreparedGeometry prepGeom = pgFact.create(g);
        boolean fastIntersects = prepGeom.intersects(line);
        if (slowIntersects != fastIntersects) {
            System.out.println(line);
            System.out.println("Slow = " + slowIntersects + ", Fast = " + fastIntersects);
            throw new RuntimeException("Different results found for intersects() !");
        }
    }
}
