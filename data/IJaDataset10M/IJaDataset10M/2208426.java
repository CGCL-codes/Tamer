package com.thesett.aima.search.util.uninformed;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.NDC;
import com.thesett.aima.search.impl.BaseQueueSearchTest;

/**
 * DepthBoundedSearchTst is a pure unit test class for the {@link DepthBoundedSearch} class.
 *
 * @author Rupert Smith
 */
public class DepthBoundedSearchTest extends TestCase {

    /** Default constructor that will result in the tests being run on a depth first search. */
    public DepthBoundedSearchTest(String testName) {
        super(testName);
    }

    /** Compile all the tests for the default test implementation of a search node into a test suite. */
    public static Test suite() {
        TestSuite suite = new TestSuite("DepthBoundedSearch Tests");
        suite.addTest(new BaseQueueSearchTest("testStartNodeEnqueued", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testRepeatedStateFilterReset", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testNoStartStateFails", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testNoGoalSearchReturnsNull", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testSearchReturnsGoal", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testSearchReturnsMultipleGoals", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testEncounteredNonGoalStatesExpanded", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testMaximumStepsReachedFails", new DepthBoundedSearch(5)));
        suite.addTest(new BaseQueueSearchTest("testMaximumStepsReachedSearchCompletedReturnsNull", new DepthBoundedSearch(5)));
        return suite;
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
