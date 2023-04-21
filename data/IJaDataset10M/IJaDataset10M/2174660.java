package xmage.math.test;

import xmage.math.Point2f;
import huf.misc.tester.Tester;

public class Point2fTest {

    public Point2fTest(Tester t) {
        t.testClass(new Point2f());
        constructor(t);
        set(t);
        add(t);
        sub(t);
        mul(t);
        div(t);
        rot(t);
        negate(t);
        length(t);
        distance(t);
        normalize(t);
        getAsArray(t);
        equalsMethod(t);
    }

    private void constructor(Tester t) {
        Point2f p = null;
        p = new Point2f();
        t.test("Point2f() 01", new float[] { 0.0f, 0.0f }, p.getAsArray());
        p = new Point2f(1.0f, 2.0f);
        t.test("Point2f(float, float) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
        p = new Point2f(new float[] { 1.0f, 2.0f });
        t.test("Point2f(float[]) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
        p = new Point2f(new Point2f(1.0f, 2.0f));
        t.test("Point2f(Point2f) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
    }

    private void set(Tester t) {
        Point2f p = null;
        p = new Point2f();
        p.set(1.0f, 2.0f);
        t.test("set(float, float) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
        p = new Point2f();
        p.set(new float[] { 1.0f, 2.0f });
        t.test("set(float[]) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
        p = new Point2f();
        p.set(new Point2f(1.0f, 2.0f));
        t.test("set(Point2f) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
    }

    private void add(Tester t) {
        Point2f p = null;
        p = new Point2f();
        p.add(1.0f, 2.0f);
        t.test("add(float, float) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
        p = new Point2f();
        p.add(new float[] { 1.0f, 2.0f });
        t.test("add(float[]) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
        p = new Point2f();
        p.add(new Point2f(1.0f, 2.0f));
        t.test("add(Point2f) 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
    }

    private void sub(Tester t) {
        Point2f p = null;
        p = new Point2f();
        p.sub(1.0f, 2.0f);
        t.test("sub(float, float) 01", new float[] { -1.0f, -2.0f }, p.getAsArray());
        p = new Point2f();
        p.sub(new float[] { 1.0f, 2.0f });
        t.test("sub(float[]) 01", new float[] { -1.0f, -2.0f }, p.getAsArray());
        p = new Point2f();
        p.sub(new Point2f(1.0f, 2.0f));
        t.test("sub(Point2f) 01", new float[] { -1.0f, -2.0f }, p.getAsArray());
    }

    private void mul(Tester t) {
        Point2f p = null;
        p = new Point2f(1.1f, 2.2f);
        p.mul(2.0f);
        t.test("mul(float) 01", new float[] { 2.2f, 4.4f }, p.getAsArray());
        p = new Point2f(1.1f, 2.2f);
        p.mul(2.0f, 5.0f);
        t.test("mul(float, float) 01", new float[] { 2.2f, 11.0f }, p.getAsArray());
        p = new Point2f(1.1f, 2.2f);
        p.mul(new float[] { 2.0f, 5.0f });
        t.test("mul(float[]) 01", new float[] { 2.2f, 11.0f }, p.getAsArray());
        p = new Point2f(1.1f, 2.2f);
        p.mul(new Point2f(2.0f, 5.0f));
        t.test("mul(Point2f) 01", new float[] { 2.2f, 11.0f }, p.getAsArray());
    }

    private void div(Tester t) {
        Point2f p = null;
        p = new Point2f(1.1f, 2.2f);
        p.div(2.0f);
        t.test("div(float) 01", new float[] { 0.55f, 1.1f }, p.getAsArray());
        p = new Point2f(1.1f, 2.2f);
        p.div(2.0f, 5.0f);
        t.test("div(float, float) 01", new float[] { 0.55f, 0.44f }, p.getAsArray());
        p = new Point2f(1.1f, 2.2f);
        p.div(new float[] { 2.0f, 5.0f });
        t.test("div(float[]) 01", new float[] { 0.55f, 0.44f }, p.getAsArray());
        p = new Point2f(1.1f, 2.2f);
        p.div(new Point2f(2.0f, 5.0f));
        t.test("div(Point2f) 01", new float[] { 0.55f, 0.44f }, p.getAsArray());
    }

    private void rot(Tester t) {
        Point2f p = null;
        p = new Point2f(1.0f, 0.0f);
        p.rot((float) Math.PI / 2.0f);
        t.test("rot 01", new float[] { 0.0f, 1.0f }, p.getAsArray());
        p.rot((float) Math.PI / 2.0f);
        t.test("rot 02", new float[] { -1.0f, 0.0f }, p.getAsArray());
        p.rot((float) Math.PI / 2.0f);
        t.test("rot 03", new float[] { 0.0f, -1.0f }, p.getAsArray());
        p.rot((float) Math.PI / 2.0f);
        t.test("rot 04", new float[] { 1.0f, 0.0f }, p.getAsArray());
        p.rot(-(float) Math.PI / 2.0f);
        t.test("rot 05", new float[] { 0.0f, -1.0f }, p.getAsArray());
    }

    private void negate(Tester t) {
        Point2f p = null;
        p = new Point2f(1.0f, 2.0f);
        p.negate();
        t.test("negate()", new float[] { -1.0f, -2.0f }, p.getAsArray());
    }

    private void length(Tester t) {
        Point2f p = null;
        p = new Point2f();
        t.test("length() 01", 0.0f, p.length());
        p = new Point2f(1.0f, 0.0f);
        t.test("length() 02", 1.0f, p.length());
        p = new Point2f(0.0f, -1.0f);
        t.test("length() 03", 1.0f, p.length());
        p = new Point2f(1.0f, 1.0f);
        t.test("length() 04", (float) Math.sqrt(2.0f), p.length());
    }

    private void distance(Tester t) {
        Point2f p = null;
        p = new Point2f();
        t.test("distance(float, float) 01", 0.0f, p.distance(0.0f, 0.0f));
        t.test("distance(float, float) 02", (float) Math.sqrt(2.0f), p.distance(1.0f, 1.0f));
        t.test("distance(float, float) 03", (float) Math.sqrt(5.0f), p.distance(1.0f, 2.0f));
        p = new Point2f(1.0f, 2.0f);
        t.test("distance(float, float) 04", 0.0f, p.distance(1.0f, 2.0f));
        t.test("distance(float, float) 05", (float) Math.sqrt(2.0f), p.distance(2.0f, 3.0f));
        t.test("distance(float, float) 06", (float) Math.sqrt(5.0f), p.distance(0.0f, 0.0f));
        p = new Point2f();
        t.test("distance(float[]) 01", 0.0f, p.distance(new float[] { 0.0f, 0.0f }));
        t.test("distance(float[]) 02", (float) Math.sqrt(2.0f), p.distance(new float[] { 1.0f, 1.0f }));
        t.test("distance(float[]) 03", (float) Math.sqrt(5.0f), p.distance(new float[] { 1.0f, 2.0f }));
        p = new Point2f(1.0f, 2.0f);
        t.test("distance(float[]) 04", 0.0f, p.distance(new float[] { 1.0f, 2.0f }));
        t.test("distance(float[]) 05", (float) Math.sqrt(2.0f), p.distance(new float[] { 2.0f, 3.0f }));
        t.test("distance(float[]) 06", (float) Math.sqrt(5.0f), p.distance(new float[] { 0.0f, 0.0f }));
        p = new Point2f();
        t.test("distance(Point2f) 01", 0.0f, p.distance(new Point2f(0.0f, 0.0f)));
        t.test("distance(Point2f) 02", (float) Math.sqrt(2.0f), p.distance(new Point2f(1.0f, 1.0f)));
        t.test("distance(Point2f) 03", (float) Math.sqrt(5.0f), p.distance(new Point2f(1.0f, 2.0f)));
        p = new Point2f(1.0f, 2.0f);
        t.test("distance(Point2f) 04", 0.0f, p.distance(new Point2f(1.0f, 2.0f)));
        t.test("distance(Point2f) 05", (float) Math.sqrt(2.0f), p.distance(new Point2f(2.0f, 3.0f)));
        t.test("distance(Point2f) 06", (float) Math.sqrt(5.0f), p.distance(new Point2f(0.0f, 0.0f)));
    }

    private void normalize(Tester t) {
        Point2f p = null;
        p = new Point2f(1.0f, 0.0f);
        p.normalize();
        t.test("normalize() 01", 1.0f, p.length());
        p = new Point2f(0.0f, 1.0f);
        p.normalize();
        t.test("normalize() 02", 1.0f, p.length());
        p = new Point2f(1.0f, 1.0f);
        p.normalize();
        t.test("normalize() 03", 1.0f, p.length());
    }

    private void getAsArray(Tester t) {
        Point2f p = null;
        p = new Point2f(1.0f, 2.0f);
        t.test("getAsArray01() 01", new float[] { 1.0f, 2.0f }, p.getAsArray());
    }

    private void equalsMethod(Tester t) {
        Point2f p = null;
        p = new Point2f();
        t.test("equals() 01", true, p.equals(new Point2f()));
        t.test("equals() 02", false, p.equals(new Point2f(1.0f, 2.0f)));
        t.test("equals() 03", false, p.equals(null));
        t.test("equals() 04", false, p.equals("foo"));
        p = new Point2f(1.0f, 2.0f);
        t.test("equals() 05", true, p.equals(new Point2f(1.0f, 2.0f)));
        t.test("equals() 06", false, p.equals(new Point2f(2.0f, 3.0f)));
        t.test("equals() 07", false, p.equals(null));
        t.test("equals() 08", false, p.equals("foo"));
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        new Point2fTest(t);
        t.totals();
    }
}
