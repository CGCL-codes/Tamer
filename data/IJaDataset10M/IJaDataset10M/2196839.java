package onepoint.project.modules.project.components.test;

import junit.framework.TestCase;
import onepoint.express.XComponent;
import onepoint.express.XValidationException;
import onepoint.express.server.XFormSchema;
import onepoint.project.modules.project.components.OpActivityGraphFactory;
import onepoint.project.modules.project.components.OpGanttValidator;
import onepoint.project.modules.project.components.OpGraph;
import onepoint.xml.XDocumentHandler;
import onepoint.xml.XLoader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai.costin
 *
 */
public class OpActivityGraphMultiplierTest extends TestCase {

    /**
    * The name of the xml file that contains the test data.
    */
    private static final String TEST_DATA_FILENAME = "activityGraphTestData.xml";

    /**
    * Data set used for testing.
    */
    private XComponent dataSet;

    /**
    * Activity Graph that will help in determining if we have any cycles in the data set.
    */
    private OpGraph graph;

    /**
    * Used to verify the obtained result.
    */
    private boolean result;

    /**
    * validator used in OpGanttValidator part of the tests.
    */
    private OpGanttValidator validator;

    /**
    * @see junit.framework.TestCase#setUp()
    */
    public void setUp() throws Exception {
        super.setUp();
        XLoader xmlLoader = new XLoader(new XDocumentHandler(new XFormSchema()));
        InputStream testDataInputStream = this.getClass().getResourceAsStream(TEST_DATA_FILENAME);
        XComponent testForm = (XComponent) xmlLoader.loadObject(testDataInputStream, null);
        dataSet = (XComponent) testForm.getChild(0);
        validator = new OpGanttValidator();
        validator.setDataSet(dataSet);
    }

    /**
    * Creates a <code>List</code> out of a data set.
    * <p/>
    * Helper method.
    *
    * @param dataSet -data set to be used inthe List generation.
    * @return a <code>List</code> with the children of the dataSet.
    */
    private List dataSetToList(XComponent dataSet) {
        List result = new ArrayList();
        for (int i = 0; i < dataSet.getChildCount(); i++) {
            result.add(dataSet.getChild(i));
        }
        return result;
    }

    /**
    * Make a link between the 2 given activities.
    * source->target (target is a successor for source and
    * source is a predecessors for target).
    * Any othet link between this activities is erased.
    * Using this method only one link can be make between 2 activities.
    * <p/>
    * Helper Method.
    *
    * @param source - source point of link (index in data set)
    * @param target - target point of link (index in data set)
    */
    private void makeLink(int source, int target) {
        ArrayList pred = new ArrayList();
        pred.add(new Integer(source));
        OpGanttValidator.setPredecessors((XComponent) dataSet.getChild(target), pred);
        ArrayList succ = new ArrayList();
        succ.add(new Integer(target));
        OpGanttValidator.setSuccessors((XComponent) dataSet.getChild(source), succ);
    }

    /**
    * Tests the creation of a new Activity Graph from a given dataSet.
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testCreateGraph() throws Exception {
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertFalse("The graph has no cycles ", result);
    }

    /**
    * Tests the detection of cycles in a data set created by adding only successors, using an activity graph.
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testIncompleteSuccessorsCycle() throws Exception {
        ArrayList succ = new ArrayList();
        succ.add(new Integer(0));
        OpGanttValidator.setSuccessors((XComponent) dataSet.getChild(0), succ);
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertTrue("1 cycle should have been detected ", result);
        succ = new ArrayList();
        succ.add(new Integer(1));
        OpGanttValidator.setSuccessors((XComponent) dataSet.getChild(0), succ);
        succ = new ArrayList();
        succ.add(new Integer(2));
        OpGanttValidator.setSuccessors((XComponent) dataSet.getChild(1), succ);
        succ = new ArrayList();
        succ.add(new Integer(0));
        OpGanttValidator.setSuccessors((XComponent) dataSet.getChild(2), succ);
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertTrue("1 cycle should have been detected ", result);
    }

    /**
    * Tests the detection of cycles in a data set created by adding only predecessors, using an activity graph.
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testPredecessorsCycle() throws Exception {
        ArrayList pred = new ArrayList();
        pred.add(new Integer(0));
        OpGanttValidator.setPredecessors((XComponent) dataSet.getChild(0), pred);
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertTrue("1 cycle should have been detected ", result);
        pred = new ArrayList();
        pred.add(new Integer(1));
        OpGanttValidator.setPredecessors((XComponent) dataSet.getChild(0), pred);
        pred = new ArrayList();
        pred.add(new Integer(2));
        OpGanttValidator.setPredecessors((XComponent) dataSet.getChild(1), pred);
        pred = new ArrayList();
        pred.add(new Integer(0));
        OpGanttValidator.setPredecessors((XComponent) dataSet.getChild(2), pred);
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertTrue("1 cycle should have been detected ", result);
    }

    /**
    * Tests the detection of cycles for 2 collections in a data set, using an activity graph.
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testCollectionCollectionLoop() throws Exception {
        makeLink(6, 2);
        makeLink(3, 5);
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertTrue("cycles should have been detected ", result);
    }

    /**
    * Tests the detection of a loop, using an activity graph.
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testLoop() throws Exception {
        makeLink(0, 1);
        makeLink(1, 2);
        makeLink(3, 4);
        makeLink(4, 5);
        makeLink(5, 7);
        makeLink(6, 8);
        makeLink(6, 0);
        graph = OpActivityGraphFactory.createGraph(dataSet);
        result = graph.hasCycles();
        assertTrue("cycles should have been detected ", result);
    }

    /**
    * Tests the detection of Loops using detectLoops method from OpGanttValidator.
    *
    * @throws Exception - if anything fails.
    */
    public void testDetectLoops() throws Exception {
        makeLink(0, 1);
        makeLink(1, 2);
        makeLink(3, 4);
        makeLink(4, 5);
        makeLink(5, 7);
        makeLink(6, 8);
        makeLink(6, 0);
        result = validator.detectLoops();
        assertTrue("cycles should have been detected ", result);
    }

    /**
    * Tests the detection of Loops using detectLoops method from OpGanttValidator.
    * - collection to collection loop
    *
    * @throws Exception - if anything fails.
    */
    public void testDetectLoopsCollectionToCollection() throws Exception {
        makeLink(6, 2);
        makeLink(3, 5);
        result = validator.detectLoops();
        assertTrue("cycles should have been detected ", result);
    }

    /**
    * Tests the detection of Loops using detectLoops method from OpGanttValidator.
    * - collection to collection loop
    *
    * @throws Exception - if anything fails.
    */
    public void testDetectLoopsInnerCollection() throws Exception {
        makeLink(8, 9);
        makeLink(9, 10);
        makeLink(10, 8);
        result = validator.detectLoops();
        assertTrue("cycles should have been detected ", result);
    }

    /**
    * Tests the detection of Loops using detectLoops method from OpGanttValidator.
    * - no loops
    *
    * @throws Exception - if anything fails.
    */
    public void testNoLoopDetectLoops() throws Exception {
        makeLink(0, 1);
        makeLink(1, 3);
        makeLink(4, 5);
        makeLink(5, 8);
        result = validator.detectLoops();
        assertFalse("NO cycles should have been detected ", result);
    }

    /**
    * Tests the loop detection feature using linksLoop method from OpGanttValidator.
    * It tests loop 0->1->2->0 and 0->0.
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testLinksLoop() throws Exception {
        ArrayList succ;
        makeLink(0, 1);
        makeLink(1, 2);
        succ = new ArrayList();
        succ.add(new Integer(0));
        result = false;
        try {
            validator.linksLoop(succ, new Integer(0), OpGanttValidator.SUCCESSORS_COLUMN_INDEX);
        } catch (XValidationException e) {
            result = true;
        }
        assertTrue("Loop of activity linked to itself was not found ", result);
        succ.add(new Integer(0));
        result = false;
        try {
            validator.linksLoop(succ, new Integer(2), OpGanttValidator.SUCCESSORS_COLUMN_INDEX);
        } catch (XValidationException e) {
            result = true;
        }
        assertTrue("Loop of activity linked 0->1->2->0 was not found ", result);
        ArrayList pred = new ArrayList();
        pred.add(new Integer(0));
        result = false;
        try {
            validator.linksLoop(pred, new Integer(0), OpGanttValidator.PREDECESSORS_COLUMN_INDEX);
        } catch (XValidationException e) {
            result = true;
        }
        assertTrue("Loop of activity linked to itself was not found ", result);
        pred = new ArrayList();
        pred.add(new Integer(2));
        result = false;
        try {
            validator.linksLoop(pred, new Integer(2), OpGanttValidator.PREDECESSORS_COLUMN_INDEX);
        } catch (XValidationException e) {
            result = true;
        }
        assertTrue("Loop of activity linked 0->1->2->0 was not found ", result);
    }

    /**
    * Tests the loop detection feature using linksLoop method from OpGanttValidator.
    * It tests loop 7->8
    *
    * @throws Exception - Exception if anything fails.
    */
    public void testLinksLoopCollectionToChild() throws Exception {
        ArrayList succ;
        succ = new ArrayList();
        succ.add(new Integer(8));
        result = false;
        try {
            validator.linksLoop(succ, new Integer(7), OpGanttValidator.SUCCESSORS_COLUMN_INDEX);
        } catch (XValidationException e) {
            result = true;
        }
        assertTrue("Loop of activity linked to itself was not found ", result);
        ArrayList pred = new ArrayList();
        pred.add(new Integer(8));
        result = false;
        try {
            validator.linksLoop(pred, new Integer(7), OpGanttValidator.PREDECESSORS_COLUMN_INDEX);
        } catch (XValidationException e) {
            result = true;
        }
        assertTrue("Loop of activity linked to itself was not found ", result);
    }

    /**
    * @see junit.framework.TestCase#tearDown()
    */
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
