package math.linearalgebra;

import java.util.ArrayList;
import java.util.List;
import math.real.RealRing;

public class CrossProductVectorTest extends TestBase {

    public void test(int n, Vector... vectors) {
        List<Vector<Double>> l = new ArrayList<Vector<Double>>();
        for (Vector<Double> v : vectors) {
            l.add(v);
        }
        Vector<Double> cross = new CrossProductVector<Double>(RealRing.instance, new VectorRowMatrix<Double>(l, n));
        assertTrue(LinearMath.lengthSquared(RealRing.instance, cross) > .000001);
        for (Vector<Double> v : l) {
            assertTrue(Math.abs(LinearMath.dotProduct(RealRing.instance, cross, v)) < .000001);
        }
    }

    public void test() {
        test(3, makeVec(1, 2, 3), makeVec(6, 5, 4));
    }

    public void test2() {
        test(4, makeVec(1, 2, 3, 7), makeVec(6, 5, 4, 3.1), makeVec(1.5, 4.1, 4, 3.1));
    }

    public void test3() {
        test(2, makeVec(1, 2));
    }
}
