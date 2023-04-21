package pub.test;

import java.sql.*;
import junit.framework.*;
import java.util.*;
import pub.beans.GeneBean;
import pub.servlets.annotation_task.AnnotationStrategyI;
import pub.servlets.annotation_task.TairAnnotationStrategy;

public class AssignTasksTest extends TestCase {

    public AssignTasksTest(String name) {
        super(name);
    }

    public void setUp() {
        _strategy = new TairAnnotationStrategy();
    }

    public void tearDown() {
    }

    public void testTairStrategyZero() {
        assertEquals(_strategy.getCandidateGenes(0).size(), 0);
    }

    public void testSize() {
        List genes = _strategy.getCandidateGenes(1000);
        assertTrue(genes.size() <= 1000);
    }

    public void testObsoletionOfGenes() {
        List genes = _strategy.getCandidateGenes(1000);
        for (int i = 0; i < genes.size(); i++) {
            GeneBean bean = (GeneBean) genes.get(i);
            assertTrue(!bean.isObsolete());
        }
    }

    public void testDuplication() {
        List genes = _strategy.getCandidateGenes(1000);
        Set ids = new HashSet();
        for (int i = 0; i < genes.size(); i++) {
            GeneBean bean = (GeneBean) genes.get(i);
            assertTrue(!ids.contains(bean));
            ids.add(bean);
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new AssignTasksTest("testTairStrategyZero"));
        suite.addTest(new AssignTasksTest("testSize"));
        suite.addTest(new AssignTasksTest("testObsoletionOfGenes"));
        suite.addTest(new AssignTasksTest("testDuplication"));
        return suite;
    }

    private AnnotationStrategyI _strategy;
}
