package math.linearalgebra;

import math.real.RealRing;

public class GetPointVectorTest extends TestBase {

    public void test3() {
        Space<Double> s1 = makeSpace(makeVec(0, 0, 0), makeVec(1, 1, 1), makeVec(1, 1, -1));
        Vector<Double> pt = new GetPointVector<Double>(RealRing.instance, s1, makeVec(1, 2));
        assertEquals(pt.get(0), 3.0);
        assertEquals(pt.get(1), 3.0);
        assertEquals(pt.get(2), -1.0);
    }

    public void test4() {
        Space<Double> s1 = makeSpace(makeVec(0, 2, 1));
        Vector<Double> pt = new GetPointVector<Double>(RealRing.instance, s1, makeVec(new double[0]));
        assertEquals(pt.get(0), 0.0);
        assertEquals(pt.get(1), 2.0);
        assertEquals(pt.get(2), 1.0);
    }
}
