package test.jts.junit.geom.prep;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.util.SineStarFactory;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public abstract class StressTestHarness {

    static final int MAX_ITER = 10000;

    static PrecisionModel pm = new PrecisionModel();

    static GeometryFactory fact = new GeometryFactory(pm, 0);

    static WKTReader wktRdr = new WKTReader(fact);

    static WKTWriter wktWriter = new WKTWriter();

    private int numTargetPts = 1000;

    public StressTestHarness() {
    }

    public void setTargetSize(int nPts) {
        numTargetPts = nPts;
    }

    public void run(int nIter) {
        System.out.println("Running " + nIter + " tests");
        Geometry poly = createSineStar(new Coordinate(0, 0), 100, numTargetPts);
        System.out.println(poly);
        System.out.println();
        run(nIter, poly);
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

    Geometry createRandomTestGeometry(Envelope env, double size, int nPts) {
        double width = env.getWidth();
        double xOffset = width * Math.random();
        double yOffset = env.getHeight() * Math.random();
        Coordinate basePt = new Coordinate(env.getMinX() + xOffset, env.getMinY() + yOffset);
        Geometry test = createTestCircle(basePt, size, nPts);
        if (test instanceof Polygon && Math.random() > 0.5) {
            test = test.getBoundary();
        }
        return test;
    }

    Geometry createTestCircle(Coordinate base, double size, int nPts) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setCentre(base);
        gsf.setSize(size);
        gsf.setNumPoints(nPts);
        Geometry circle = gsf.createCircle();
        return circle;
    }

    public void run(int nIter, Geometry target) {
        int count = 0;
        while (count < nIter) {
            count++;
            Geometry test = createRandomTestGeometry(target.getEnvelopeInternal(), 10, 20);
            System.out.println("Test[" + count + "] " + target.getClass() + "/" + test.getClass());
            boolean isResultCorrect = checkResult(target, test);
            if (!isResultCorrect) {
                throw new RuntimeException("Invalid result found");
            }
        }
    }

    public abstract boolean checkResult(Geometry target, Geometry test);
}
