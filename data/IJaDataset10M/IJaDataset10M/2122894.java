package org.arpenteur.photogrammetry.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.media.jai.PerspectiveTransform;
import junit.framework.JUnit4TestAdapter;
import org.arpenteur.common.math.geometry.Geometry;
import org.arpenteur.common.math.geometry.point.IPoint2D;

/**
 * BestFitUnitTest: test class for CalculHomography and Affinity
 * 
 */
public class BestFit2DUnitTest {

    /** Basic tests + check that the PerspectiveTransform is correctly built */
    @org.junit.Test
    public void computeTestHomography() throws Exception {
        ArrayList<IPoint2D> pRef = new ArrayList<IPoint2D>();
        pRef.add(Geometry.newPoint2D(0, 0));
        pRef.add(Geometry.newPoint2D(3008, 0));
        pRef.add(Geometry.newPoint2D(3008, 2000));
        pRef.add(Geometry.newPoint2D(0, 2000));
        ArrayList<IPoint2D> pMes = new ArrayList<IPoint2D>();
        pMes.add(Geometry.newPoint2D(-26.629, 17.263));
        pMes.add(Geometry.newPoint2D(26.287, 17.263));
        pMes.add(Geometry.newPoint2D(26.288, -17.929));
        pMes.add(Geometry.newPoint2D(-26.630, -17.930));
        ComputeHomography ch = new ComputeHomography(pRef, pMes);
        assertTrue(ch.compute(true, false));
        for (int i = 0; i < 4; i++) {
            assertEquals(pRef.get(i), ch.transform(pMes.get(i)));
            assertEquals(pMes.get(i), ch.transformInverse(pRef.get(i)));
        }
        PerspectiveTransform pt = ch.getPerspectiveTransform();
        for (int i = 0; i < 4; i++) {
            assertTrue(new Point2D.Double(pRef.get(i).getX(), pRef.get(i).getY()).distance(pt.transform(new Point2D.Double(pMes.get(i).getX(), pMes.get(i).getY()), null)) < 0.0001);
            assertTrue(new Point2D.Double(pMes.get(i).getX(), pMes.get(i).getY()).distance(pt.inverseTransform(new Point2D.Double(pRef.get(i).getX(), pRef.get(i).getY()), null)) < 0.0001);
        }
    }

    /** Basic tests with same value than for Homography */
    @org.junit.Test
    public void computeTestAffinity() throws Exception {
        ArrayList<IPoint2D> pRef = new ArrayList<IPoint2D>();
        pRef.add(Geometry.newPoint2D(0, 0));
        pRef.add(Geometry.newPoint2D(3008, 0));
        pRef.add(Geometry.newPoint2D(3008, 2000));
        pRef.add(Geometry.newPoint2D(0, 2000));
        ArrayList<IPoint2D> pMes = new ArrayList<IPoint2D>();
        pMes.add(Geometry.newPoint2D(-26.629, 17.263));
        pMes.add(Geometry.newPoint2D(26.287, 17.263));
        pMes.add(Geometry.newPoint2D(26.288, -17.929));
        pMes.add(Geometry.newPoint2D(-26.630, -17.930));
        ComputeAffinity aff = new ComputeAffinity(pRef, pMes);
        assertTrue(aff.compute(true, false));
        for (int i = 0; i < 4; i++) {
            assertTrue(pRef.get(i).dist(aff.transform(pMes.get(i))) < 0.04);
            assertTrue(pMes.get(i).dist(aff.transformInverse(pRef.get(i))) < 0.04);
        }
    }

    /**
	 * Compatibility with Ant 1.6.5 and JUnit 3.8.x
	 * 
	 * @return Test
	 */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BestFit2DUnitTest.class);
    }

    public static void main(String[] p_Args) {
        BestFit2DUnitTest test = new BestFit2DUnitTest();
        try {
            test.computeTestHomography();
            test.computeTestAffinity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
