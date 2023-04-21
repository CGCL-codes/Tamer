package xmage.math.test;

import xmage.math.Point3d;
import xmage.math.Segment3d;
import huf.misc.tester.Tester;

public class Segment3dTest {

    public Segment3dTest(Tester t) {
        t.testClass(new Segment3d());
        constructor(t);
        nearest(t);
    }

    private void constructor(Tester t) {
        Segment3d s = null;
        s = new Segment3d();
        t.test("Segment3d() 01", s.a.getAsArray(), new double[] { 0.0, 0.0, 0.0 });
        t.test("Segment3d() 02", s.b.getAsArray(), new double[] { 0.0, 0.0, 1.0 });
        s = new Segment3d(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        t.test("Segment3d(6xdouble) 01", s.a.getAsArray(), new double[] { 1.0, 2.0, 3.0 });
        t.test("Segment3d(6xdouble) 02", s.b.getAsArray(), new double[] { 4.0, 5.0, 6.0 });
        s = new Segment3d(new Point3d(1.0, 2.0, 3.0), new Point3d(4.0, 5.0, 6.0));
        t.test("Segment3d(2xPoint3d) 01", s.a.getAsArray(), new double[] { 1.0, 2.0, 3.0 });
        t.test("Segment3d(2xPoint3d) 02", s.b.getAsArray(), new double[] { 4.0, 5.0, 6.0 });
    }

    private void nearest(Tester t) {
        Segment3d s = null;
        Point3d p = null;
        s = new Segment3d();
        p = s.nearest(0.0, 0.0, 0.0);
        t.test("nearest(3xdouble) 01", p.getAsArray(), new double[] { 0.0, 0.0, 0.0 });
        s = new Segment3d();
        p = s.nearest(0.0, 1.0, 0.0);
        t.test("nearest(3xdouble) 02", p.getAsArray(), new double[] { 0.0, 0.0, 0.0 });
        s = new Segment3d();
        p = s.nearest(0.0, 0.0, 1.0);
        t.test("nearest(3xdouble) 03", p.getAsArray(), new double[] { 0.0, 0.0, 1.0 });
        s = new Segment3d();
        p = s.nearest(0.0, 0.0, -1.0);
        t.test("nearest(3xdouble) 04", p.getAsArray(), new double[] { 0.0, 0.0, 0.0 });
        s = new Segment3d();
        p = s.nearest(0.0, 0.0, 2.0);
        t.test("nearest(3xdouble) 05", p.getAsArray(), new double[] { 0.0, 0.0, 1.0 });
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        new Segment3dTest(t);
        t.totals();
    }
}
