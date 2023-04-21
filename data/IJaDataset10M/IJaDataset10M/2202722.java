package oss.jthinker.widgets;

import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Point;
import java.util.Random;
import javax.swing.JLabel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit-tests for GeometryUtils class.
 * 
 * @author iappel
 */
public class GeometryUtilsTest {

    public GeometryUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of computeCenterPoint method, of class GeometryUtils.
     */
    @Test
    public void computeCenterPoint() {
        System.out.println("computeCenterPoint");
        Component c = new JLabel("foobar");
        c.setBounds(100, 100, 150, 150);
        Point expect = new Point(175, 175);
        assertEquals(expect, GeometryUtils.computeCenterPoint(c));
    }

    /**
     * Test of setCenterPoint method, of class GeometryUtils.
     */
    @Test
    public void setCenterPoint() {
        System.out.println("setCenterPoint");
        Component c = new JLabel("foobar");
        c.setSize(150, 100);
        Point expect = new Point(75, 100);
        GeometryUtils.setCenterPoint(c, new Point(150, 150));
        assertEquals(expect, c.getLocation());
    }

    /**
     * Test of adjustCenterPoint method, of class GeometryUtils.
     */
    @Test
    public void adjustCenterPoint() {
        System.out.println("adjustCenterPoint");
        Component c = new JLabel("foobar");
        c.setBounds(100, 100, 100, 100);
        GeometryUtils.adjustCenterPoint(c, new Point(60, 70));
        assertEquals(new Point(110, 120), c.getLocation());
    }

    /**
     * Test of distanceToLine method, of class GeometryUtils.
     */
    @Test
    public void distanceToLine() {
        System.out.println("distanceToLine");
        Point a = new Point(0, 0);
        Point b = new Point(10, 10);
        Point c = new Point(5, 5);
        double d = GeometryUtils.distanceToLine(c, a, b);
        assertEquals(0.0, d);
        b.x = 0;
        d = GeometryUtils.distanceToLine(c, a, b);
        assertTrue(Math.abs(d - 5) < 0.001);
    }

    /**
     * Test of hypotenuse method, of class GeometryUtils.
     */
    @Test
    public void hypotenuse() {
        System.out.println("hypotenuse");
        double result = GeometryUtils.hypotenuse(3, 4);
        assertEquals(5.0, result);
        result = GeometryUtils.hypotenuse(5, 12);
        assertEquals(13.0, result);
    }

    /**
     * Test of triangleSin method, of class GeometryUtils.
     */
    @Test
    public void triangleSin() {
        System.out.println("triangleSin");
        double result1 = GeometryUtils.triangleSin(10, 20);
        double result2 = GeometryUtils.triangleSin(20, 40);
        assertEquals(0.0, result1 - result2);
    }

    /**
     * Test of triangleCos method, of class GeometryUtils.
     */
    @Test
    public void triangleCos() {
        System.out.println("triangleCos");
        double result1 = GeometryUtils.triangleCos(10, 20);
        double result2 = GeometryUtils.triangleCos(20, 40);
        assertEquals(0.0, result1 - result2);
    }

    @Test
    public void mainRule() {
        System.out.println("mainRule");
        Random rng = new Random();
        double a = rng.nextInt(100);
        double b = rng.nextInt(100);
        double sn = GeometryUtils.triangleSin(a, b);
        double cs = GeometryUtils.triangleCos(a, b);
        double smm = GeometryUtils.hypotenuse(sn, cs);
        double diff = Math.abs(1.0 - smm);
        diff *= 10000000;
        int iDiff = (int) diff;
        assertEquals(0, iDiff);
    }

    /**
     * Test of rectangleIntersection method, of class GeometryUtils.
     */
    @Test
    public void rectangleIntersection() {
        System.out.println("rectangleIntersection");
        Rectangle area = new Rectangle(-1, -1, 2, 2);
        Point outPoint = new Point(0, 42);
        Point awaitResult = new Point(0, 1);
        assertEquals(awaitResult, GeometryUtils.rectangleIntersection(area, outPoint));
        outPoint = new Point(50, 50);
        awaitResult = new Point(1, 1);
        assertEquals(awaitResult, GeometryUtils.rectangleIntersection(area, outPoint));
        area = new Rectangle(-20, -20, 40, 40);
        outPoint = new Point(100, 50);
        awaitResult = new Point(20, 10);
        assertEquals(awaitResult, GeometryUtils.rectangleIntersection(area, outPoint));
    }

    /**
     * Regression test of intersection between two ellipses.
     */
    @Test
    public void twoEllipsesRegression() {
        System.out.println("twoEllipsesRegression");
        Rectangle area = new Rectangle(100, 100, 56, 48);
        Point outPoint = new Point(528, 124);
        Point isectPoint = new Point(156, 124);
        assertEquals(isectPoint, GeometryUtils.ellipseIntersection(area, outPoint));
    }

    /**
     * Regression test on intersection between ellipse and rectangle.
     */
    @Test
    public void ellipseAndRectangleRegression() {
        System.out.println("ellipseAndRectangleRegression");
        Rectangle area = new Rectangle(500, 110, 49, 28);
        Point outPoint = new Point(128, 124);
        Point isectPoint = new Point(500, 124);
        assertEquals(isectPoint, GeometryUtils.rectangleIntersection(area, outPoint));
    }

    /**
     * Regression test on intersection with rounded rectangle.
     */
    @Test
    public void roundRectTest() {
        System.out.println("roundRectTest");
        Rectangle area = new Rectangle(90, 90, 20, 20);
        Point outPoint = new Point(200, 100);
        Point isectPoint = new Point(110, 100);
        assertEquals(isectPoint, GeometryUtils.roundRectangleIntersection(area, outPoint));
    }

    /**
     * Test on intersection with a hexagon.
     */
    @Test
    public void hexagonTest() {
        System.out.println("hexagonTest");
        Rectangle area = new Rectangle(200, 200, 100, 100);
        Point outPoint = new Point(250, 100);
        Point isectPoint = new Point(250, 200);
        assertEquals(isectPoint, GeometryUtils.hexagonIntersection(area, outPoint));
        outPoint = new Point(400, 250);
        isectPoint = new Point(300, 250);
        assertEquals(isectPoint, GeometryUtils.hexagonIntersection(area, outPoint));
        outPoint = new Point(200, 200);
        isectPoint = new Point(211, 211);
        assertEquals(isectPoint, GeometryUtils.hexagonIntersection(area, outPoint));
        outPoint = new Point(100, 100);
        isectPoint = new Point(211, 211);
        assertEquals(isectPoint, GeometryUtils.hexagonIntersection(area, outPoint));
        outPoint = new Point(300, 300);
        isectPoint = new Point(288, 288);
        assertEquals(isectPoint, GeometryUtils.hexagonIntersection(area, outPoint));
        outPoint = new Point(300, 200);
        isectPoint = new Point(288, 211);
        assertEquals(isectPoint, GeometryUtils.hexagonIntersection(area, outPoint));
    }

    /**
     * Test on intersection of two lines.
     */
    @Test
    public void lineIntersectionTest() {
        System.out.println("lineIntersectionTest");
        Point p1 = new Point(100, 100);
        Point p2 = new Point(100, 200);
        Point q1 = new Point(200, 100);
        Point q2 = new Point(200, 200);
        assertNull(GeometryUtils.lineIntersection(p1, p2, q1, q2));
        Point i = new Point(150, 150);
        assertEquals(i, GeometryUtils.lineIntersection(p1, q2, q1, p2));
        q1 = new Point(50, 150);
        q2 = new Point(150, 150);
        i = new Point(100, 150);
        assertEquals(i, GeometryUtils.lineIntersection(p1, p2, q1, q2));
        assertEquals(i, GeometryUtils.lineIntersection(q1, q2, p1, p2));
        q1 = new Point(50, 140);
        q2 = new Point(150, 160);
        assertEquals(i, GeometryUtils.lineIntersection(p1, p2, q1, q2));
        assertEquals(i, GeometryUtils.lineIntersection(q1, q2, p1, p2));
    }
}
