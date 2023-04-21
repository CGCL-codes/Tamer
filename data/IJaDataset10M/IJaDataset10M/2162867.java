package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.ObjectPool;
import gov.nasa.jpf.delayed.test.util.TestUtils;
import gov.nasa.jpf.jvm.RawTest;

/**
 * Test for the AALOAD instruction.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestNCPAALOAD extends RawTest {

    public static void main(String[] args) {
        TestNCPAALOAD test = new TestNCPAALOAD();
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
    public void testFiniteNullAALOAD() {
        TestUtils.reset();
        ObjectPool<Node> op = new ObjectPool<Node>(Node.class, 3, true);
        Node[] nodes = new Node[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = op.getAny();
        }
        assert (TestUtils.getCounter() == 0);
        Node[] tmps = new Node[3];
        for (int i = 0; i < nodes.length; i++) {
            tmps[i] = nodes[i];
        }
        if (TestUtils.getCounter() == 0) assert (tmps[0] == null && tmps[1] == null && tmps[2] == null); else if (TestUtils.getCounter() == 1) assert (nodes[0] == null && nodes[1] == null && nodes[2].id == 0); else if (TestUtils.getCounter() == 2) assert (nodes[0] == null && nodes[1].id == 0 && nodes[2] == null); else if (TestUtils.getCounter() == 3) assert (nodes[0] == null && nodes[1].id == 0 && nodes[2].id == 0); else if (TestUtils.getCounter() == 4) assert (nodes[0] == null && nodes[1].id == 0 && nodes[2].id == 1); else if (TestUtils.getCounter() == 5) assert (tmps[0].id == 0 && tmps[1] == null && tmps[2] == null); else if (TestUtils.getCounter() == 6) assert (nodes[0].id == 0 && nodes[1] == null && nodes[2].id == 0); else if (TestUtils.getCounter() == 7) assert (nodes[0].id == 0 && nodes[1] == null && nodes[2].id == 1); else if (TestUtils.getCounter() == 8) assert (nodes[0].id == 0 && nodes[1].id == 0 && nodes[2] == null); else if (TestUtils.getCounter() == 9) assert (nodes[0].id == 0 && nodes[1].id == 0 && nodes[2].id == 0); else if (TestUtils.getCounter() == 10) assert (nodes[0].id == 0 && nodes[1].id == 0 && nodes[2].id == 1); else if (TestUtils.getCounter() == 11) assert (nodes[0].id == 0 && nodes[1].id == 1 && nodes[2] == null); else if (TestUtils.getCounter() == 12) assert (nodes[0].id == 0 && nodes[1].id == 1 && nodes[2].id == 0); else if (TestUtils.getCounter() == 13) assert (nodes[0].id == 0 && nodes[1].id == 1 && nodes[2].id == 1); else if (TestUtils.getCounter() == 14) assert (nodes[0].id == 0 && nodes[1].id == 1 && nodes[2].id == 2); else assert (false);
        TestUtils.incCounter();
    }

    /**
   * DFSearch is required
   */
    public void testFiniteAALOAD() {
        TestUtils.reset();
        ObjectPool<Node> op = new ObjectPool<Node>(Node.class, 3, false);
        Node[] nodes = new Node[3];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = op.getAny();
        }
        assert (TestUtils.getCounter() == 0);
        Node[] tmps = new Node[3];
        for (int i = 0; i < nodes.length; i++) {
            tmps[i] = nodes[i];
        }
        if (TestUtils.getCounter() == 0) assert (tmps[0].id == 0 && tmps[1].id == 0 && tmps[2].id == 0); else if (TestUtils.getCounter() == 1) assert (tmps[0].id == 0 && tmps[1].id == 0 && tmps[2].id == 1); else if (TestUtils.getCounter() == 2) assert (tmps[0].id == 0 && tmps[1].id == 1 && tmps[2].id == 0); else if (TestUtils.getCounter() == 3) assert (tmps[0].id == 0 && tmps[1].id == 1 && tmps[2].id == 1); else if (TestUtils.getCounter() == 4) assert (tmps[0].id == 0 && tmps[1].id == 1 && tmps[2].id == 2); else assert (false);
        TestUtils.incCounter();
    }
}
