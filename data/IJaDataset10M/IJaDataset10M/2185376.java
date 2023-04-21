package org.ddsteps.junit.behaviour;

import junit.framework.TestResult;
import org.ddsteps.spring.scope.TestMethodScope;

/**
 * This behaviour is the same as the usual behaviour of a JUnit TestCase.
 * <p>
 * It's stateless, so a singleton may be used.
 * 
 * @author adam
 * @version $Id: JUnitMethodBehaviour.java,v 1.1 2006/01/29 21:25:39 adamskogman
 *          Exp $
 */
public class JUnitMethodBehaviour implements DdBehaviour {

    /**
	 * Default constructor.
	 */
    public JUnitMethodBehaviour() {
    }

    /**
	 * @see org.ddsteps.junit.behaviour.DdBehaviour#countTestCases()
	 */
    public int countTestCases() {
        return 1;
    }

    /**
	 * Always return true, i.e. let the calling TestCase subclass call super.
	 * 
	 * @see org.ddsteps.junit.behaviour.DdBehaviour#run(junit.framework.TestResult,
	 *      org.ddsteps.junit.behaviour.DDStepsJUnitTestCaseHooks)
	 */
    public void run(TestResult result, DDStepsJUnitTestCaseHooks ddTestCase) {
        ddTestCase.superRun(result);
    }

    /**
	 * Delegate back to (@link DdBehaviourCallbackHandler#setUpMethod()).
	 * 
	 * @see org.ddsteps.junit.behaviour.DdBehaviour#setUp(org.ddsteps.junit.behaviour.DDStepsJUnitTestCaseHooks)
	 */
    public void setUp(DDStepsJUnitTestCaseHooks ddTestCase) throws Exception {
        ddTestCase.setUpMethod();
    }

    /**
	 * Delegate back to (@link DdBehaviourCallbackHandler#tearDownMethod()).
	 * 
	 * @see org.ddsteps.junit.behaviour.DdBehaviour#tearDown(org.ddsteps.junit.behaviour.DDStepsJUnitTestCaseHooks)
	 */
    public void tearDown(DDStepsJUnitTestCaseHooks ddTestCase) throws Exception {
        ddTestCase.tearDownMethod();
    }

    public void endTest(DDStepsJUnitTestCaseHooks testCase) {
        TestMethodScope.endTestMethod(testCase.getName());
    }

    public void startTest(DDStepsJUnitTestCaseHooks testCase) {
        TestMethodScope.beginTestMethod(testCase.getName());
    }
}
