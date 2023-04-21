package test.jts.perf.geom.prep;

import java.util.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.prep.*;
import com.vividsolutions.jts.geom.util.*;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import com.vividsolutions.jts.util.Stopwatch;

public class PreparedPolygonIntersectsPerfTest {

    static final int MAX_ITER = 1;

    static final int NUM_AOI_PTS = 2000;

    static final int NUM_LINES = 5000;

    static final int NUM_LINE_PTS = 1000;

    static PrecisionModel pm = new PrecisionModel();

    static GeometryFactory fact = new GeometryFactory(pm, 0);

    static WKTReader wktRdr = new WKTReader(fact);

    static WKTWriter wktWriter = new WKTWriter();

    Stopwatch sw = new Stopwatch();

    public static void main(String[] args) {
        PreparedPolygonIntersectsPerfTest test = new PreparedPolygonIntersectsPerfTest();
        test.test();
    }

    boolean testFailed = false;

    public PreparedPolygonIntersectsPerfTest() {
    }

    public void test() {
        test(5);
        test(10);
        test(500);
        test(1000);
        test(2000);
    }

    public void test(int nPts) {
        Geometry sinePoly = createSineStar(new Coordinate(0, 0), 100, nPts);
        Geometry target = sinePoly;
        List lines = createLines(target.getEnvelopeInternal(), NUM_LINES, 1.0, NUM_LINE_PTS);
        System.out.println();
        test(target, lines);
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

    List createLines(Envelope env, int nItems, double size, int nPts) {
        int nCells = (int) Math.sqrt(nItems);
        List geoms = new ArrayList();
        double width = env.getWidth();
        double xInc = width / nCells;
        double yInc = width / nCells;
        for (int i = 0; i < nCells; i++) {
            for (int j = 0; j < nCells; j++) {
                Coordinate base = new Coordinate(env.getMinX() + i * xInc, env.getMinY() + j * yInc);
                Geometry line = createLine(base, size, nPts);
                geoms.add(line);
            }
        }
        return geoms;
    }

    Geometry createLine(Coordinate base, double size, int nPts) {
        SineStarFactory gsf = new SineStarFactory();
        gsf.setCentre(base);
        gsf.setSize(size);
        gsf.setNumPoints(nPts);
        Geometry circle = gsf.createSineStar();
        return circle.getBoundary();
    }

    public void test(Geometry g, List lines) {
        System.out.println("AOI # pts: " + g.getNumPoints() + "      # lines: " + lines.size() + "   # pts in line: " + NUM_LINE_PTS);
        int maxCount = MAX_ITER;
        Stopwatch sw = new Stopwatch();
        int count = 0;
        for (int i = 0; i < MAX_ITER; i++) {
            count = testPrepGeomCached(g, lines);
        }
        System.out.println("Count of intersections = " + count);
        System.out.println("Finished in " + sw.getTimeString());
    }

    public int testOriginal(Geometry g, List lines) {
        System.out.println("Using orginal JTS algorithm");
        int count = 0;
        for (Iterator i = lines.iterator(); i.hasNext(); ) {
            LineString line = (LineString) i.next();
            if (g.intersects(line)) count++;
        }
        return count;
    }

    public int testPrepGeomCached(Geometry g, List lines) {
        System.out.println("Using cached Prepared Geometry");
        PreparedGeometryFactory pgFact = new PreparedGeometryFactory();
        PreparedGeometry prepGeom = pgFact.create(g);
        int count = 0;
        for (Iterator i = lines.iterator(); i.hasNext(); ) {
            LineString line = (LineString) i.next();
            if (prepGeom.intersects(line)) count++;
        }
        return count;
    }

    /**
   * Tests using PreparedGeometry, but creating a new
   * PreparedGeometry object each time.
   * This tests whether there is a penalty for using 
   * the PG algorithm as a complete replacement for 
   * the original algorithm.
   *  
   * @param g
   * @param lines
   * @return
   */
    public int testPrepGeomNotCached(Geometry g, List lines) {
        System.out.println("Using NON-CACHED Prepared Geometry");
        PreparedGeometryFactory pgFact = new PreparedGeometryFactory();
        int count = 0;
        for (Iterator i = lines.iterator(); i.hasNext(); ) {
            LineString line = (LineString) i.next();
            PreparedGeometry prepGeom = pgFact.create(g);
            if (prepGeom.intersects(line)) count++;
        }
        return count;
    }
}
