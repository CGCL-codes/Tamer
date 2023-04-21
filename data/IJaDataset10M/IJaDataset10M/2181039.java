package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.ObjectPool;
import gov.nasa.jpf.delayed.test.util.TestUtils;
import gov.nasa.jpf.jvm.RawTest;

/**
 * Test for the GETFIELD instruction
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestCPGETFIELD extends RawTest {

    public static void main(String[] args) {
        TestCPGETFIELD test = new TestCPGETFIELD();
        if (!runSelectedTest(args, test)) {
            runAllTests(args, test);
        }
    }

    private static class Node {

        static int ids = 0;

        int id = ids++;
    }

    /**
   * DFSearch is required
   */
    public void testGetAnyInfiniteGETFIELD() {
        TestUtils.reset();
        ObjectPool<Node> op = new ObjectPool<Node>(Node.class, false);
        Node n1 = op.getAny();
        Node n2 = op.getAny();
        assert (TestUtils.getCounter() == 0);
        Node tmp1 = n1;
        Node tmp2 = n2;
        assert (TestUtils.getCounter() == 0);
        int id1 = tmp1.id;
        int id2 = tmp2.id;
        if (TestUtils.getCounter() == 0) assert (id1 == 0 && id2 == 0); else if (TestUtils.getCounter() == 1) assert (id1 == 0 && id2 == 1);
        TestUtils.incCounter();
    }

    /**
   * DFSearch is required
   */
    public void testGetNewFiniteGETFIELD() {
        TestUtils.reset();
        ObjectPool<Node> op = new ObjectPool<Node>(Node.class, 3, false);
        Node any0 = op.getAny();
        Node new0 = op.getNew();
        Node any1 = op.getAny();
        assert (TestUtils.getCounter() == 0);
        int any0Id = any0.id;
        int newId = new0.id;
        int any1Id = any1.id;
        if (TestUtils.getCounter() == 0) assert (any0Id == 0 && newId == 1 && any1Id == 0); else if (TestUtils.getCounter() == 1) assert (any0Id == 0 && newId == 1 && any1Id == 1); else if (TestUtils.getCounter() == 2) assert (any0Id == 0 && newId == 1 && any1Id == 2); else assert (false);
        TestUtils.incCounter();
    }

    private static class C {

        private Node n;

        void setNode(Node v) {
            n = v;
        }

        Node getNode() {
            return n;
        }
    }

    public void testMethodGetAnyFiniteGETFIELD() {
        TestUtils.reset();
        ObjectPool<Node> op = new ObjectPool<Node>(Node.class, 3, false);
        C c = new C();
        c.setNode(op.getAny());
        Node res = c.getNode();
        assert (TestUtils.getCounter() == 0);
        int id = res.id;
        assert (id == 0 && TestUtils.getCounter() == 0);
        TestUtils.incCounter();
    }

    private static class O {

        static int ids = 0;

        int id = ids++;

        /**
     * Returns a + b + id
     */
        int calculate(int a, int b) {
            assert (TestUtils.getCounter() == 0);
            return a + b + id;
        }
    }

    public void testMethodCallUninitializedObjectGETFIELD() {
        TestUtils.reset();
        ObjectPool<O> op = new ObjectPool<O>(O.class, 3, false);
        O any = op.getAny();
        O o = op.getAny();
        assert (TestUtils.getCounter() == 0);
        int res = o.calculate(any.id, 3);
        if (TestUtils.getCounter() == 0) assert (res == 3); else if (TestUtils.getCounter() == 1) assert (res == 4); else assert (false);
        TestUtils.incCounter();
    }
}
