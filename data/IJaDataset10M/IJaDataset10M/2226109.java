package com.thesett.aima.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.NDC;
import com.thesett.aima.search.util.OperatorImpl;

/**
 * SearchNodeTest is a pure unit test class for the {@link SearchNode} class.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Check start state node always has no parent, no applied operation, depth zero, path cost zero.
 * <tr><td> Check expand successors creates exactly one node for each successor state.
 * <tr><td> Check make node creates a node of the same class as the original.
 * <tr><td> Check make node throws SearchNotExhaustiveException when the search node class has a private constructor.
 * <tr><td> Check make node throws SearchNotExhaustiveException when the search node class has no no arguemtn constructor.
 * <tr><td> Check make node creates a node with the right state, parent, applied operation, depth and path cost.
 * <tr><td> Check make node creates a node the repeated state filter copied.
 * <tr><td> Check expand successors only creates successors allowed through the repeated state filter.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class SearchNodeTest extends TestCase {

    /** The {@link SearchNode} object to test. */
    SearchNode testSearchNode;

    /**
     * The {@link SearchNode} object to use to test that make node creates nodes of the same class as the original. In
     * the default version of this test class the constructor sets a node which is a sub-class of {@link SearchNode}
     * here in order to make the test an effective one.
     */
    SearchNode testMakeNode;

    /**
     * Default constructor that will result in the tests being run on the {@link SearchNode} class. This is used to
     * check the sanity of the tests being run and the default implementation of the search node class.
     */
    public SearchNodeTest(String testName) {
        super(testName);
        testSearchNode = new SearchNode(new TestTraversableState());
        testMakeNode = new SearchNodeSubClass();
    }

    /**
     * Builds the tests to be run on a supplied search node implementation that extends the default one. This allows the
     * default test methods to be applied to arbitrary implementations of search node in sub-classes of this test class.
     */
    public SearchNodeTest(String testName, SearchNode testSearchNode) {
        super(testName);
        this.testSearchNode = testSearchNode;
        this.testMakeNode = testSearchNode;
    }

    /** Compile all the tests for the default test implementation of a search node into a test suite. */
    public static Test suite() {
        TestSuite suite = new TestSuite("SearchNode Tests");
        suite.addTestSuite(SearchNodeTest.class);
        return suite;
    }

    /** Check expand successors creates exactly one node for each successor state. */
    public void testExpandSuccessorsCreatesOneNodeForEachSuccessor() throws Exception {
        String errorMessage = "";
        Map<Operator, SearchNode> testSuccessors = new HashMap<Operator, SearchNode>();
        Queue<SearchNode> successors = new LinkedList<SearchNode>();
        testSearchNode.expandSuccessors(successors, false);
        for (SearchNode next : successors) {
            testSuccessors.put(next.getAppliedOp(), next);
        }
        for (Iterator<Successor> j = testSearchNode.getState().successors(false); j.hasNext(); ) {
            Successor nextSuccessor = j.next();
            if (!testSuccessors.containsKey(nextSuccessor.getOperator())) {
                errorMessage += "No successor node was found for the operation " + nextSuccessor.getOperator() + " although the original state generated a successor state for this operation.\n";
            }
            {
                testSuccessors.remove(nextSuccessor.getOperator());
            }
        }
        if (!testSuccessors.isEmpty()) {
            errorMessage += "There are successor nodes for which the original state does not generate successor states:\n";
            for (SearchNode next : testSuccessors.values()) {
                errorMessage += "Successor node with state " + next.getState() + " and applied op " + next.getAppliedOp();
            }
        }
        assertTrue(errorMessage, "".equals(errorMessage));
    }

    /** Check make node creates a node the repeated state filter copied. */
    public void testMakeNodeCopiesRepeatedStateFilter() throws Exception {
        String errorMessage = "";
        Successor successor = new Successor(new TestTraversableState(), new OperatorImpl<String>("testOp"), 1.0f);
        RepeatedStateFilter filter = new BlankFilter();
        testSearchNode.setRepeatedStateFilter(filter);
        SearchNode successorNode = testSearchNode.makeNode(successor);
        if (successorNode.getRepeatedStateFilter() != filter) {
            errorMessage += "The successor node does not have the same repeated state filter as the successor.\n";
        }
        assertTrue(errorMessage, "".equals(errorMessage));
    }

    /** Check make node creates a node of the same class as the original. */
    public void testMakeNodeCreatesNodesOfOriginalClass() throws Exception {
        String errorMessage = "";
        String nodeClassName = testMakeNode.getClass().getName();
        if ("com.thesett.aima.search.SearchNode".equals(nodeClassName)) {
            errorMessage += "The class to be tested must not be com.thesett.aima.search.SearchNode but must be a sub-class of this.\n";
        }
        SearchNode newNode = testMakeNode.makeNode(new Successor(new TestTraversableState(), new OperatorImpl<String>(""), 0.0f));
        String newNodeClassName = newNode.getClass().getName();
        if (!nodeClassName.equals(newNodeClassName)) {
            errorMessage += "The class created by the newNode method on class " + nodeClassName + " does not match this class but is a SearchNode of class " + newNodeClassName;
        }
        assertTrue(errorMessage, "".equals(errorMessage));
    }

    /**
     * Check make node throws SearchNotExhaustiveException when the search node class has no no argument constructor.
     */
    public void testMakeNodeFailsOnNoNoArgConstructorSearchNode() throws Exception {
        boolean testPassed = false;
        SearchNode testNode = new SearchNodeSubClassNoNoArgConstructor(new Object());
        try {
            testNode.makeNode(new Successor(new TestTraversableState(), new OperatorImpl<String>(""), 0.0f));
        } catch (RuntimeException e) {
            testPassed = true;
        }
        if (!testPassed) {
            fail("Calling makeNode on a SearchNode with no no arg constructor " + "should have raised a RuntimeException.\n");
        }
    }

    /** Check make node throws SearchNotExhaustiveException when the search node class has a private constructor. */
    public void testMakeNodeFailsPrivateConstructor() throws Exception {
        boolean testPassed = false;
        SearchNode testNode = SearchNodeSubClassPrivateConstructor.getInstance();
        try {
            testNode.makeNode(new Successor(new TestTraversableState(), new OperatorImpl<String>(""), 0.0f));
        } catch (RuntimeException e) {
            testPassed = true;
        }
        if (!testPassed) {
            fail("Calling makeNode on a SearchNode with no public constructor " + "should have raised a RuntimeException.\n");
        }
    }

    /** Check make node creates a node with the right state, parent, applied operation, depth and path cost. */
    public void testMakeNodePropertiesOk() throws Exception {
        String errorMessage = "";
        Successor successor = new Successor(new TestTraversableState(), new OperatorImpl<String>("testOp"), 1.0f);
        SearchNode successorNode = testSearchNode.makeNode(successor);
        if (!successorNode.getState().equals(successor.getState())) {
            errorMessage += "The successor node does not have the same state as the successor.\n";
        }
        if (!successorNode.getParent().equals(testSearchNode)) {
            errorMessage += "The successor node does not have parent equal to its generating search node.\n";
        }
        if (!successorNode.getAppliedOp().equals(successor.getOperator())) {
            errorMessage += "The sucessor node does not have the same appliled operation as the successor.\n";
        }
        if (successorNode.getDepth() != (testSearchNode.getDepth() + 1)) {
            errorMessage += "The successor node does not have depth equal to its generating node plus one.\n";
        }
        if (successorNode.getPathCost() != (testSearchNode.getPathCost() + successor.getCost())) {
            errorMessage += "The successor node does not have path cost equal to the parent path cost " + "plus the successor path cost.\n";
        }
        assertTrue(errorMessage, "".equals(errorMessage));
    }

    /** Check expand successors only creates successors allowed through the repeated state filter. */
    public void testRepeatedStateFilterIsApplied() throws Exception {
        String errorMessage = "";
        int count = 0;
        for (Iterator<Successor> i = testSearchNode.getState().successors(false); i.hasNext(); i.next()) {
            count++;
        }
        if (count == 0) {
            return;
        }
        Queue<SearchNode> blankFilteredNodes = new LinkedList<SearchNode>();
        testSearchNode.setRepeatedStateFilter(new BlankFilter());
        testSearchNode.expandSuccessors(blankFilteredNodes, false);
        if (blankFilteredNodes.size() != count) {
            errorMessage += "Some successors were filtered out by a blank filter.";
        }
        Queue<SearchNode> blockingFilteredNodes = new LinkedList<SearchNode>();
        testSearchNode.setRepeatedStateFilter(new BlockingFilter());
        testSearchNode.expandSuccessors(blockingFilteredNodes, false);
        if (blockingFilteredNodes.size() != 0) {
            errorMessage += "Some successors made it through a blocking filter.";
        }
        assertTrue(errorMessage, "".equals(errorMessage));
    }

    /** Check start state node always has no parent, no applied operation, depth zero and path cost zero. */
    public void testStartStatePropertiesOk() throws Exception {
        String errorMessage = "";
        SearchNode testNode = new SearchNode(new TestTraversableState());
        if (testNode.getParent() != null) {
            errorMessage += "Start node does not have a null parent.\n";
        }
        if (testNode.getAppliedOp() != null) {
            errorMessage += "Start node does not have a null applied operation.\n";
        }
        if (testNode.getDepth() != 0) {
            errorMessage += "Start node does not have zero depth.\n";
        }
        if (testNode.getPathCost() != 0) {
            errorMessage += "Start node does not have path cost zero.\n";
        }
        assertTrue(errorMessage, "".equals(errorMessage));
    }

    /** @throws Exception */
    protected void setUp() throws Exception {
        NDC.push(getName());
    }

    /** @throws Exception */
    protected void tearDown() throws Exception {
        NDC.pop();
    }
}
