package org.dyn4j.collision;

import java.util.List;
import junit.framework.TestCase;
import org.dyn4j.collision.Collidable;
import org.dyn4j.collision.broadphase.BroadphasePair;
import org.dyn4j.collision.broadphase.SapIncremental;
import org.dyn4j.collision.manifold.ClippingManifoldSolver;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.manifold.ManifoldPoint;
import org.dyn4j.collision.narrowphase.Gjk;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.collision.narrowphase.Sat;
import org.dyn4j.collision.narrowphase.Separation;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Shape;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link Rectangle} - {@link Triangle} collision detection.
 * @author William Bittle
 * @version 1.1.0
 * @since 1.0.0
 */
public class RectangleTriangleTest extends AbstractTest {

    /** The test {@link Rectangle} */
    private Rectangle rect;

    /** The test {@link Triangle} */
    private Triangle tri;

    /**
	 * Sets up the test.
	 */
    @Before
    public void setup() {
        this.rect = new Rectangle(1.0, 1.0);
        this.tri = new Triangle(new Vector2(0.45, -0.12), new Vector2(-0.45, 0.38), new Vector2(-0.15, -0.22));
    }

    /**
	 * Tests {@link Shape} AABB.
	 */
    @Test
    public void detectShapeAABB() {
        Transform t1 = new Transform();
        Transform t2 = new Transform();
        TestCase.assertTrue(this.aabb.detect(rect, t1, tri, t2));
        TestCase.assertTrue(this.aabb.detect(tri, t2, rect, t1));
        t1.translate(-0.5, 0.0);
        TestCase.assertTrue(this.aabb.detect(rect, t1, tri, t2));
        TestCase.assertTrue(this.aabb.detect(tri, t2, rect, t1));
        t2.translate(0.34, 0.42);
        TestCase.assertTrue(this.aabb.detect(rect, t1, tri, t2));
        TestCase.assertTrue(this.aabb.detect(tri, t2, rect, t1));
        t2.translate(0.0, 0.58);
        TestCase.assertFalse(this.aabb.detect(rect, t1, tri, t2));
        TestCase.assertFalse(this.aabb.detect(tri, t2, rect, t1));
    }

    /**
	 * Tests {@link Collidable} AABB.
	 */
    @Test
    public void detectCollidableAABB() {
        CollidableTest ct1 = new CollidableTest(rect);
        CollidableTest ct2 = new CollidableTest(tri);
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        TestCase.assertTrue(this.aabb.detect(ct2, ct1));
        ct1.translate(-0.5, 0.0);
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        TestCase.assertTrue(this.aabb.detect(ct2, ct1));
        ct2.translate(0.34, 0.42);
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        ct2.translate(0.0, 0.58);
        TestCase.assertFalse(this.aabb.detect(ct1, ct2));
        TestCase.assertFalse(this.aabb.detect(ct2, ct1));
    }

    /**
	 * Tests {@link SapIncremental}.
	 */
    @Test
    public void detectSap() {
        List<BroadphasePair<CollidableTest>> pairs;
        CollidableTest ct1 = new CollidableTest(rect);
        CollidableTest ct2 = new CollidableTest(tri);
        this.sapI.add(ct1);
        this.sapI.add(ct2);
        this.sapBF.add(ct1);
        this.sapBF.add(ct2);
        this.sapT.add(ct1);
        this.sapT.add(ct2);
        this.dynT.add(ct1);
        this.dynT.add(ct2);
        pairs = this.sapI.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.sapBF.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.sapT.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.dynT.detect();
        TestCase.assertEquals(1, pairs.size());
        ct1.translate(-0.5, 0.0);
        this.sapI.update(ct1);
        this.sapBF.update(ct1);
        this.sapT.update(ct1);
        this.dynT.update(ct1);
        pairs = this.sapI.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.sapBF.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.sapT.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.dynT.detect();
        TestCase.assertEquals(1, pairs.size());
        ct2.translate(0.34, 0.42);
        this.sapI.update(ct2);
        this.sapBF.update(ct2);
        this.sapT.update(ct2);
        this.dynT.update(ct2);
        pairs = this.sapI.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.sapBF.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.sapT.detect();
        TestCase.assertEquals(1, pairs.size());
        pairs = this.dynT.detect();
        TestCase.assertEquals(1, pairs.size());
        ct1.translate(0.0, 1.6);
        this.sapI.update(ct1);
        this.sapBF.update(ct1);
        this.sapT.update(ct1);
        this.dynT.update(ct1);
        pairs = this.sapI.detect();
        TestCase.assertEquals(0, pairs.size());
        pairs = this.sapBF.detect();
        TestCase.assertEquals(0, pairs.size());
        pairs = this.sapT.detect();
        TestCase.assertEquals(0, pairs.size());
        pairs = this.dynT.detect();
        TestCase.assertEquals(0, pairs.size());
    }

    /**
	 * Tests {@link Sat}.
	 */
    @Test
    public void detectSat() {
        Penetration p = new Penetration();
        Transform t1 = new Transform();
        Transform t2 = new Transform();
        Vector2 n = null;
        TestCase.assertTrue(this.sat.detect(rect, t1, tri, t2, p));
        TestCase.assertTrue(this.sat.detect(rect, t1, tri, t2));
        n = p.getNormal();
        TestCase.assertEquals(0.720, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.000, n.x, 1.0e-3);
        TestCase.assertEquals(1.000, n.y, 1.0e-3);
        TestCase.assertTrue(this.sat.detect(tri, t2, rect, t1, p));
        TestCase.assertTrue(this.sat.detect(tri, t2, rect, t1));
        n = p.getNormal();
        TestCase.assertEquals(0.720, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.000, n.x, 1.0e-3);
        TestCase.assertEquals(-1.000, n.y, 1.0e-3);
        t1.translate(-0.5, 0.0);
        TestCase.assertTrue(this.sat.detect(rect, t1, tri, t2, p));
        TestCase.assertTrue(this.sat.detect(rect, t1, tri, t2));
        n = p.getNormal();
        TestCase.assertEquals(0.450, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(1.000, n.x, 1.0e-3);
        TestCase.assertEquals(0.000, n.y, 1.0e-3);
        TestCase.assertTrue(this.sat.detect(tri, t2, rect, t1, p));
        TestCase.assertTrue(this.sat.detect(tri, t2, rect, t1));
        n = p.getNormal();
        TestCase.assertEquals(0.450, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(-1.000, n.x, 1.0e-3);
        TestCase.assertEquals(0.000, n.y, 1.0e-3);
        t2.translate(0.34, 0.42);
        TestCase.assertFalse(this.sat.detect(rect, t1, tri, t2, p));
        TestCase.assertFalse(this.sat.detect(rect, t1, tri, t2));
        TestCase.assertFalse(this.sat.detect(tri, t2, rect, t1, p));
        TestCase.assertFalse(this.sat.detect(tri, t2, rect, t1));
        t2.translate(0.0, 0.58);
        TestCase.assertFalse(this.sat.detect(rect, t1, tri, t2, p));
        TestCase.assertFalse(this.sat.detect(rect, t1, tri, t2));
        TestCase.assertFalse(this.sat.detect(tri, t2, rect, t1, p));
        TestCase.assertFalse(this.sat.detect(tri, t2, rect, t1));
    }

    /**
	 * Tests {@link Gjk}.
	 */
    @Test
    public void detectGjk() {
        Penetration p = new Penetration();
        Transform t1 = new Transform();
        Transform t2 = new Transform();
        Vector2 n = null;
        TestCase.assertTrue(this.gjk.detect(rect, t1, tri, t2, p));
        TestCase.assertTrue(this.gjk.detect(rect, t1, tri, t2));
        n = p.getNormal();
        TestCase.assertEquals(0.720, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.000, n.x, 1.0e-3);
        TestCase.assertEquals(1.000, n.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.detect(tri, t2, rect, t1, p));
        TestCase.assertTrue(this.gjk.detect(tri, t2, rect, t1));
        n = p.getNormal();
        TestCase.assertEquals(0.720, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.000, n.x, 1.0e-3);
        TestCase.assertEquals(-1.000, n.y, 1.0e-3);
        t1.translate(-0.5, 0.0);
        TestCase.assertTrue(this.gjk.detect(rect, t1, tri, t2, p));
        TestCase.assertTrue(this.gjk.detect(rect, t1, tri, t2));
        n = p.getNormal();
        TestCase.assertEquals(0.450, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(1.000, n.x, 1.0e-3);
        TestCase.assertEquals(0.000, n.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.detect(tri, t2, rect, t1, p));
        TestCase.assertTrue(this.gjk.detect(tri, t2, rect, t1));
        n = p.getNormal();
        TestCase.assertEquals(0.450, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(-1.000, n.x, 1.0e-3);
        TestCase.assertEquals(0.000, n.y, 1.0e-3);
        t2.translate(0.34, 0.42);
        TestCase.assertFalse(this.gjk.detect(rect, t1, tri, t2, p));
        TestCase.assertFalse(this.gjk.detect(rect, t1, tri, t2));
        TestCase.assertFalse(this.gjk.detect(tri, t2, rect, t1, p));
        TestCase.assertFalse(this.gjk.detect(tri, t2, rect, t1));
        t2.translate(0.0, 0.58);
        TestCase.assertFalse(this.gjk.detect(rect, t1, tri, t2, p));
        TestCase.assertFalse(this.gjk.detect(rect, t1, tri, t2));
        TestCase.assertFalse(this.gjk.detect(tri, t2, rect, t1, p));
        TestCase.assertFalse(this.gjk.detect(tri, t2, rect, t1));
    }

    /**
	 * Tests the {@link Gjk} distance method.
	 */
    @Test
    public void gjkDistance() {
        Separation s = new Separation();
        Transform t1 = new Transform();
        Transform t2 = new Transform();
        Vector2 n, p1, p2;
        TestCase.assertFalse(this.gjk.distance(rect, t1, tri, t2, s));
        TestCase.assertFalse(this.gjk.distance(tri, t2, rect, t1, s));
        t1.translate(-0.5, 0.0);
        TestCase.assertFalse(this.gjk.distance(rect, t1, tri, t2, s));
        TestCase.assertFalse(this.gjk.distance(tri, t2, rect, t1, s));
        t2.translate(0.34, 0.42);
        TestCase.assertTrue(this.gjk.distance(rect, t1, tri, t2, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.035, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(0.894, n.x, 1.0e-3);
        TestCase.assertEquals(0.447, n.y, 1.0e-3);
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(0.500, p1.y, 1.0e-3);
        TestCase.assertEquals(0.032, p2.x, 1.0e-3);
        TestCase.assertEquals(0.516, p2.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.distance(tri, t2, rect, t1, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.035, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(-0.894, n.x, 1.0e-3);
        TestCase.assertEquals(-0.447, n.y, 1.0e-3);
        TestCase.assertEquals(0.032, p1.x, 1.0e-3);
        TestCase.assertEquals(0.516, p1.y, 1.0e-3);
        TestCase.assertEquals(0.000, p2.x, 1.0e-3);
        TestCase.assertEquals(0.500, p2.y, 1.0e-3);
        t2.translate(0.0, 0.58);
        TestCase.assertTrue(this.gjk.distance(rect, t1, tri, t2, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.338, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(0.561, n.x, 1.0e-3);
        TestCase.assertEquals(0.827, n.y, 1.0e-3);
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(0.500, p1.y, 1.0e-3);
        TestCase.assertEquals(0.190, p2.x, 1.0e-3);
        TestCase.assertEquals(0.780, p2.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.distance(tri, t2, rect, t1, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.338, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(-0.561, n.x, 1.0e-3);
        TestCase.assertEquals(-0.827, n.y, 1.0e-3);
        TestCase.assertEquals(0.190, p1.x, 1.0e-3);
        TestCase.assertEquals(0.780, p1.y, 1.0e-3);
        TestCase.assertEquals(0.000, p2.x, 1.0e-3);
        TestCase.assertEquals(0.500, p2.y, 1.0e-3);
    }

    /**
	 * Test the {@link ClippingManifoldSolver}.
	 */
    @Test
    public void getClipManifold() {
        Manifold m = new Manifold();
        Penetration p = new Penetration();
        Transform t1 = new Transform();
        Transform t2 = new Transform();
        ManifoldPoint mp1, mp2;
        Vector2 p1, p2;
        this.gjk.detect(rect, t1, tri, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, rect, t1, tri, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        TestCase.assertTrue(this.cmfs.getManifold(p, tri, t2, rect, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        this.sat.detect(rect, t1, tri, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, rect, t1, tri, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        TestCase.assertTrue(this.cmfs.getManifold(p, tri, t2, rect, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        t1.translate(-0.5, 0.0);
        this.gjk.detect(rect, t1, tri, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, rect, t1, tri, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(-0.450, p1.x, 1.0e-3);
        TestCase.assertEquals(0.380, p1.y, 1.0e-3);
        TestCase.assertEquals(0.450, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.150, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.220, p2.y, 1.0e-3);
        TestCase.assertEquals(0.150, mp2.getDepth(), 1.0e-3);
        this.gjk.detect(tri, t2, rect, t1, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, tri, t2, rect, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(-0.450, p1.x, 1.0e-3);
        TestCase.assertEquals(0.380, p1.y, 1.0e-3);
        TestCase.assertEquals(0.450, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.150, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.220, p2.y, 1.0e-3);
        TestCase.assertEquals(0.150, mp2.getDepth(), 1.0e-3);
        this.sat.detect(rect, t1, tri, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, rect, t1, tri, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(-0.450, p1.x, 1.0e-3);
        TestCase.assertEquals(0.380, p1.y, 1.0e-3);
        TestCase.assertEquals(0.450, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.150, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.220, p2.y, 1.0e-3);
        TestCase.assertEquals(0.150, mp2.getDepth(), 1.0e-3);
        this.sat.detect(tri, t2, rect, t1, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, tri, t2, rect, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(-0.450, p1.x, 1.0e-3);
        TestCase.assertEquals(0.380, p1.y, 1.0e-3);
        TestCase.assertEquals(0.450, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.150, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.220, p2.y, 1.0e-3);
        TestCase.assertEquals(0.150, mp2.getDepth(), 1.0e-3);
    }

    /**
	 * This failure case is used to test the fix implemented.
	 * <p>
	 * The shapes, positions, and orientations in this test
	 * caused the GJK implementation to oscillate between 
	 * two different simplexes until the maximum iteration 
	 * count was hit returning an incorrect separation.
	 */
    @Test
    public void distanceFail1() {
        Rectangle r = new Rectangle(10.0, 0.2);
        Triangle t = new Triangle(new Vector2(0.0, 0.5), new Vector2(-1.0, 0.0), new Vector2(1.0, 0.0));
        Transform txr = new Transform();
        txr.rotate(0.10866660409637111939);
        txr.translate(-0.010447620194083662, 1.107709398935564);
        Transform txt = new Transform();
        txt.translate(0.0, 0.5);
        Separation s = new Separation();
        this.gjk.distance(r, txr, t, txt, s);
        Vector2 n = s.getNormal();
        Vector2 p1 = s.getPoint1();
        Vector2 p2 = s.getPoint2();
        TestCase.assertEquals(0.008, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(0.108, n.x, 1.0e-3);
        TestCase.assertEquals(-0.994, n.y, 1.0e-3);
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(1.008, p1.y, 1.0e-3);
        TestCase.assertEquals(0.000, p2.x, 1.0e-3);
        TestCase.assertEquals(1.000, p2.y, 1.0e-3);
    }
}
