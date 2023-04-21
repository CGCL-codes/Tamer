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
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Shape;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link Polygon} - {@link Polygon} collision detection.
 * @author William Bittle
 * @version 1.1.0
 * @since 1.0.0
 */
public class PolygonPolygonTest extends AbstractTest {

    /** The test {@link Polygon} */
    private Polygon poly1;

    /** The test {@link Polygon} */
    private Polygon poly2;

    /**
	 * Sets up the test.
	 */
    @Before
    public void setup() {
        this.poly1 = Geometry.createUnitCirclePolygon(5, 1.0);
        this.poly2 = Geometry.createUnitCirclePolygon(6, 0.5);
    }

    /**
	 * Tests {@link Shape} AABB.
	 */
    @Test
    public void detectShapeAABB() {
        Transform t1 = new Transform();
        Transform t2 = new Transform();
        TestCase.assertTrue(this.aabb.detect(poly1, t1, poly2, t2));
        TestCase.assertTrue(this.aabb.detect(poly2, t2, poly1, t1));
        t1.translate(-1.0, 0.0);
        TestCase.assertTrue(this.aabb.detect(poly1, t1, poly2, t2));
        TestCase.assertTrue(this.aabb.detect(poly2, t2, poly1, t1));
        t2.translate(0.0, 1.1);
        TestCase.assertTrue(this.aabb.detect(poly1, t1, poly2, t2));
        TestCase.assertTrue(this.aabb.detect(poly2, t2, poly1, t1));
        t1.translate(-1.0, 0.0);
        TestCase.assertFalse(this.aabb.detect(poly1, t1, poly2, t2));
        TestCase.assertFalse(this.aabb.detect(poly2, t2, poly1, t1));
    }

    /**
	 * Tests {@link Collidable} AABB.
	 */
    @Test
    public void detectCollidableAABB() {
        CollidableTest ct1 = new CollidableTest(poly1);
        CollidableTest ct2 = new CollidableTest(poly2);
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        TestCase.assertTrue(this.aabb.detect(ct2, ct1));
        ct1.translate(-1.0, 0.0);
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        TestCase.assertTrue(this.aabb.detect(ct2, ct1));
        ct2.translate(0.0, 1.1);
        TestCase.assertTrue(this.aabb.detect(ct1, ct2));
        TestCase.assertTrue(this.aabb.detect(ct2, ct1));
        ct1.translate(-1.0, 0.0);
        TestCase.assertFalse(this.aabb.detect(ct1, ct2));
        TestCase.assertFalse(this.aabb.detect(ct2, ct1));
    }

    /**
	 * Tests {@link SapIncremental}.
	 */
    @Test
    public void detectSap() {
        List<BroadphasePair<CollidableTest>> pairs;
        CollidableTest ct1 = new CollidableTest(poly1);
        CollidableTest ct2 = new CollidableTest(poly2);
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
        ct1.translate(-1.0, 0.0);
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
        ct2.translate(0.0, 1.1);
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
        ct1.translate(-1.0, 0.0);
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
        TestCase.assertTrue(this.sat.detect(poly1, t1, poly2, t2, p));
        TestCase.assertTrue(this.sat.detect(poly1, t1, poly2, t2));
        n = p.getNormal();
        TestCase.assertEquals(1.265, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.809, n.x, 1.0e-3);
        TestCase.assertEquals(-0.587, n.y, 1.0e-3);
        TestCase.assertTrue(this.sat.detect(poly2, t2, poly1, t1, p));
        TestCase.assertTrue(this.sat.detect(poly2, t2, poly1, t1));
        n = p.getNormal();
        TestCase.assertEquals(1.265, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.809, n.x, 1.0e-3);
        TestCase.assertEquals(0.587, n.y, 1.0e-3);
        t1.translate(-1.0, 0.0);
        TestCase.assertTrue(this.sat.detect(poly1, t1, poly2, t2, p));
        TestCase.assertTrue(this.sat.detect(poly1, t1, poly2, t2));
        n = p.getNormal();
        TestCase.assertEquals(0.433, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.866, n.x, 1.0e-3);
        TestCase.assertEquals(-0.500, n.y, 1.0e-3);
        TestCase.assertTrue(this.sat.detect(poly2, t2, poly1, t1, p));
        TestCase.assertTrue(this.sat.detect(poly2, t2, poly1, t1));
        n = p.getNormal();
        TestCase.assertEquals(0.433, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.866, n.x, 1.0e-3);
        TestCase.assertEquals(0.500, n.y, 1.0e-3);
        t2.translate(0.0, 1.1);
        TestCase.assertFalse(this.sat.detect(poly1, t1, poly2, t2, p));
        TestCase.assertFalse(this.sat.detect(poly1, t1, poly2, t2));
        TestCase.assertFalse(this.sat.detect(poly2, t2, poly1, t1, p));
        TestCase.assertFalse(this.sat.detect(poly2, t2, poly1, t1));
        t1.translate(-1.0, 0.0);
        TestCase.assertFalse(this.sat.detect(poly1, t1, poly2, t2, p));
        TestCase.assertFalse(this.sat.detect(poly1, t1, poly2, t2));
        TestCase.assertFalse(this.sat.detect(poly2, t2, poly1, t1, p));
        TestCase.assertFalse(this.sat.detect(poly2, t2, poly1, t1));
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
        TestCase.assertTrue(this.gjk.detect(poly1, t1, poly2, t2, p));
        TestCase.assertTrue(this.gjk.detect(poly1, t1, poly2, t2));
        n = p.getNormal();
        TestCase.assertEquals(1.265, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.809, n.x, 1.0e-3);
        TestCase.assertEquals(-0.587, n.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.detect(poly2, t2, poly1, t1, p));
        TestCase.assertTrue(this.gjk.detect(poly2, t2, poly1, t1));
        n = p.getNormal();
        TestCase.assertEquals(1.265, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.809, n.x, 1.0e-3);
        TestCase.assertEquals(0.587, n.y, 1.0e-3);
        t1.translate(-1.0, 0.0);
        TestCase.assertTrue(this.gjk.detect(poly1, t1, poly2, t2, p));
        TestCase.assertTrue(this.gjk.detect(poly1, t1, poly2, t2));
        n = p.getNormal();
        TestCase.assertEquals(0.433, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(0.866, n.x, 1.0e-3);
        TestCase.assertEquals(-0.500, n.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.detect(poly2, t2, poly1, t1, p));
        TestCase.assertTrue(this.gjk.detect(poly2, t2, poly1, t1));
        n = p.getNormal();
        TestCase.assertEquals(0.433, p.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.866, n.x, 1.0e-3);
        TestCase.assertEquals(0.500, n.y, 1.0e-3);
        t2.translate(0.0, 1.1);
        TestCase.assertFalse(this.gjk.detect(poly1, t1, poly2, t2, p));
        TestCase.assertFalse(this.gjk.detect(poly1, t1, poly2, t2));
        TestCase.assertFalse(this.gjk.detect(poly2, t2, poly1, t1, p));
        TestCase.assertFalse(this.gjk.detect(poly2, t2, poly1, t1));
        t1.translate(-1.0, 0.0);
        TestCase.assertFalse(this.gjk.detect(poly1, t1, poly2, t2, p));
        TestCase.assertFalse(this.gjk.detect(poly1, t1, poly2, t2));
        TestCase.assertFalse(this.gjk.detect(poly2, t2, poly1, t1, p));
        TestCase.assertFalse(this.gjk.detect(poly2, t2, poly1, t1));
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
        TestCase.assertFalse(this.gjk.distance(poly1, t1, poly2, t2, s));
        TestCase.assertFalse(this.gjk.distance(poly2, t2, poly1, t1, s));
        t1.translate(-1.0, 0.0);
        TestCase.assertFalse(this.gjk.distance(poly1, t1, poly2, t2, s));
        TestCase.assertFalse(this.gjk.distance(poly2, t2, poly1, t1, s));
        t2.translate(0.0, 1.1);
        TestCase.assertTrue(this.gjk.distance(poly1, t1, poly2, t2, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.189, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(0.809, n.x, 1.0e-3);
        TestCase.assertEquals(0.587, n.y, 1.0e-3);
        TestCase.assertEquals(-0.403, p1.x, 1.0e-3);
        TestCase.assertEquals(0.555, p1.y, 1.0e-3);
        TestCase.assertEquals(-0.250, p2.x, 1.0e-3);
        TestCase.assertEquals(0.666, p2.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.distance(poly2, t2, poly1, t1, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.189, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(-0.809, n.x, 1.0e-3);
        TestCase.assertEquals(-0.587, n.y, 1.0e-3);
        TestCase.assertEquals(-0.250, p1.x, 1.0e-3);
        TestCase.assertEquals(0.666, p1.y, 1.0e-3);
        TestCase.assertEquals(-0.403, p2.x, 1.0e-3);
        TestCase.assertEquals(0.555, p2.y, 1.0e-3);
        t1.translate(-1.0, 0.0);
        TestCase.assertTrue(this.gjk.distance(poly1, t1, poly2, t2, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.998, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(0.809, n.x, 1.0e-3);
        TestCase.assertEquals(0.587, n.y, 1.0e-3);
        TestCase.assertEquals(-1.058, p1.x, 1.0e-3);
        TestCase.assertEquals(0.079, p1.y, 1.0e-3);
        TestCase.assertEquals(-0.250, p2.x, 1.0e-3);
        TestCase.assertEquals(0.666, p2.y, 1.0e-3);
        TestCase.assertTrue(this.gjk.distance(poly2, t2, poly1, t1, s));
        n = s.getNormal();
        p1 = s.getPoint1();
        p2 = s.getPoint2();
        TestCase.assertEquals(0.998, s.getDistance(), 1.0e-3);
        TestCase.assertEquals(-0.809, n.x, 1.0e-3);
        TestCase.assertEquals(-0.587, n.y, 1.0e-3);
        TestCase.assertEquals(-0.250, p1.x, 1.0e-3);
        TestCase.assertEquals(0.666, p1.y, 1.0e-3);
        TestCase.assertEquals(-1.058, p2.x, 1.0e-3);
        TestCase.assertEquals(0.079, p2.y, 1.0e-3);
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
        this.gjk.detect(poly1, t1, poly2, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, poly1, t1, poly2, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        TestCase.assertTrue(this.cmfs.getManifold(p, poly2, t2, poly1, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        this.sat.detect(poly1, t1, poly2, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, poly1, t1, poly2, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        TestCase.assertTrue(this.cmfs.getManifold(p, poly2, t2, poly1, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        t1.translate(-1.0, 0.0);
        this.gjk.detect(poly1, t1, poly2, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, poly1, t1, poly2, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(0.000, p1.y, 1.0e-3);
        TestCase.assertEquals(0.433, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.147, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.203, p2.y, 1.0e-3);
        TestCase.assertEquals(0.406, mp2.getDepth(), 1.0e-3);
        this.gjk.detect(poly2, t2, poly1, t1, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, poly2, t2, poly1, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(0.000, p1.y, 1.0e-3);
        TestCase.assertEquals(0.433, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.147, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.203, p2.y, 1.0e-3);
        TestCase.assertEquals(0.406, mp2.getDepth(), 1.0e-3);
        this.sat.detect(poly1, t1, poly2, t2, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, poly1, t1, poly2, t2, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(0.000, p1.y, 1.0e-3);
        TestCase.assertEquals(0.433, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.147, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.203, p2.y, 1.0e-3);
        TestCase.assertEquals(0.406, mp2.getDepth(), 1.0e-3);
        this.sat.detect(poly2, t2, poly1, t1, p);
        TestCase.assertTrue(this.cmfs.getManifold(p, poly2, t2, poly1, t1, m));
        TestCase.assertEquals(2, m.getPoints().size());
        mp1 = m.getPoints().get(0);
        mp2 = m.getPoints().get(1);
        p1 = mp1.getPoint();
        p2 = mp2.getPoint();
        TestCase.assertEquals(0.000, p1.x, 1.0e-3);
        TestCase.assertEquals(0.000, p1.y, 1.0e-3);
        TestCase.assertEquals(0.433, mp1.getDepth(), 1.0e-3);
        TestCase.assertEquals(-0.147, p2.x, 1.0e-3);
        TestCase.assertEquals(-0.203, p2.y, 1.0e-3);
        TestCase.assertEquals(0.406, mp2.getDepth(), 1.0e-3);
    }
}
