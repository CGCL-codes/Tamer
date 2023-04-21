package xmage.math.test;

import xmage.math.Point2f;
import xmage.math.Quaternionf;
import huf.misc.tester.Tester;

public class QuaternionfTest {

    public QuaternionfTest(Tester t) {
        t.testClass(new Point2f());
        matrixConstr(t);
    }

    private void matrixConstr(Tester t) {
        Quaternionf q = new Quaternionf(new xmage.math.Matrix3f(new float[] { 0.9396926274441205f, -0.34202012503238066f, 0.0f, 0.34202012503238066f, 0.9396926274441205f, 0.0f, 0.0f, 0.0f, 1.0f }));
        t.test("test01", new float[] { 0.0f, 0.0f, 0.1736f, 0.9848f }, new float[] { q.x, q.y, q.z, q.w });
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        new QuaternionfTest(t);
        t.totals();
    }
}
