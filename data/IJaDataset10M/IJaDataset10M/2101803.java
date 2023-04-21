package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.ObjectPool;
import gov.nasa.jpf.delayed.test.util.TestUtils;
import gov.nasa.jpf.jvm.RawTest;

/**
 * Test for the IF_ACMPNE instruction.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestCPIF_ACMPNE extends RawTest {

    public static void main(String[] args) {
        TestCPIF_ACMPNE test = new TestCPIF_ACMPNE();
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
    public void testGetAnyInfiniteIF_ACMPNE() {
        TestUtils.reset();
        ObjectPool<Node> op = new ObjectPool<Node>(Node.class, false);
        Node n1 = op.getAny();
        Node n2 = op.getAny();
        assert (TestUtils.getCounter() == 0);
        if (n1.id != n2.id) {
            assert (TestUtils.getCounter() == 1 && n1.id == 0 && n2.id == 1);
        } else {
            assert (TestUtils.getCounter() == 0 && n1.id == 0 && n2.id == 0);
        }
        TestUtils.incCounter();
    }
}
