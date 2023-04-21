package net.walend.digraph.test;

import java.util.Iterator;
import junit.framework.TestSuite;
import junit.framework.Test;
import net.walend.toolkit.junit.TestCase;
import net.walend.digraph.GEDigraph;
import net.walend.digraph.HashGEDigraph;
import net.walend.digraph.MutableGEDigraph;
import net.walend.digraph.MutableHashGEDigraph;
import net.walend.digraph.NodeMissingException;
import net.walend.digraph.EdgeMissingException;
import net.walend.digraph.EdgeNotUniqueException;
import net.walend.digraph.DigraphException;
import net.walend.digraph.MutableHashCEDigraph;
import net.walend.digraph.MutableHashUEDigraph;

/**



 @author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
 @since 20010812
*/
public class MutableHashGEDigraphTest extends MutableGEDigraphTest {

    public MutableHashGEDigraphTest(String testName) {
        super(testName);
    }

    protected GEDigraph getEmptyTestGEDigraph() {
        return getEmptyTestMutableGEDigraph();
    }

    protected GEDigraph getTestGEDigraph() {
        return getTestMutableGEDigraph();
    }

    protected MutableGEDigraph getTestMutableGEDigraph() {
        MutableGEDigraph result = getEmptyTestMutableGEDigraph();
        TestGEDigraphFactory.fillTestGEDigraph(result);
        return result;
    }

    protected MutableGEDigraph getEmptyTestMutableGEDigraph() {
        return new MutableHashGEDigraph(7, 7);
    }

    public void testEmptyConstructor() {
        MutableHashGEDigraph victem = new MutableHashGEDigraph();
        testIsEmpty(victem, true);
    }

    public void testIntConstructor() {
        MutableHashGEDigraph victem = new MutableHashGEDigraph(5, 11);
        testIsEmpty(victem, true);
    }

    public void testConstructors() {
        MutableHashCEDigraph guts1 = new MutableHashCEDigraph(7, 7);
        TestCEDigraphFactory.fillTestCEDigraph(guts1);
        MutableGEDigraph test1 = new MutableHashGEDigraph(guts1);
        MutableHashUEDigraph guts2 = new MutableHashUEDigraph(7, 7);
        TestUEDigraphFactory.fillTestUEDigraph(guts2);
        MutableGEDigraph test2 = new MutableHashGEDigraph(guts2);
        MutableHashGEDigraph guts3 = new MutableHashGEDigraph(7, 7);
        TestGEDigraphFactory.fillTestGEDigraph(guts3);
        MutableGEDigraph test3 = new MutableHashGEDigraph(guts3);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new MutableHashGEDigraphTest("testEmptyConstructor"));
        suite.addTest(new MutableHashGEDigraphTest("testCopyConstructor"));
        suite.addTest(new MutableHashGEDigraphTest("testIntConstructor"));
        suite.addTest(new MutableHashGEDigraphTest("testConstructors"));
        suite.addTest(new MutableHashGEDigraphTest("testQueriesOnEmpty"));
        suite.addTest(new MutableHashGEDigraphTest("testQueriesOnTestGEDigraph"));
        suite.addTest(new MutableHashGEDigraphTest("testAddNode"));
        suite.addTest(new MutableHashGEDigraphTest("testAddEdge"));
        suite.addTest(new MutableHashGEDigraphTest("testRemoveNode"));
        suite.addTest(new MutableHashGEDigraphTest("testRemoveEdgeByNodes"));
        suite.addTest(new MutableHashGEDigraphTest("testAddNodes"));
        suite.addTest(new MutableHashGEDigraphTest("testRemoveNodes"));
        suite.addTest(new MutableHashGEDigraphTest("testRemoveGEDigraph"));
        suite.addTest(new MutableHashGEDigraphTest("testRetainNodes"));
        suite.addTest(new MutableHashGEDigraphTest("testClear"));
        suite.addTest(new MutableHashGEDigraphTest("testClearEdges"));
        suite.addTest(new MutableHashGEDigraphTest("testGEDigraphAlgebraContainsGEDigraph"));
        suite.addTest(new MutableHashGEDigraphTest("testGEDigraphAlgebraSameGEDigraphs"));
        suite.addTest(new MutableHashGEDigraphTest("testGEDigraphAlgebraIntersectGEDigraphs"));
        suite.addTest(new MutableHashGEDigraphTest("testGEDigraphAlgebraUnionGEDigraphs"));
        return suite;
    }
}
