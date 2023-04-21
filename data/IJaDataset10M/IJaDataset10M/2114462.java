package com.thesett.aima.state.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.NDC;
import com.thesett.aima.state.StateTestBase;
import com.thesett.aima.state.TestBeanImpl;

/**
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class WrappedBeanStateTest extends TestCase {

    /** Creates a new ExtendableBeanStateTest object. */
    public WrappedBeanStateTest(String testName) {
        super(testName);
    }

    /**
     * Compile all the tests for the default test for test states into a test suite plus any tests defined in this test
     * class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("WrappedBeanState Tests");
        suite.addTest(new StateTestBase("testGetPropertiesOk", new WrappedBeanState(new TestBeanImpl())));
        suite.addTest(new StateTestBase("testGetNonExistantPropertyFails", new WrappedBeanState(new TestBeanImpl())));
        suite.addTest(new StateTestBase("testGetterMethodExceptionsCausesFailure", new WrappedBeanState(new TestBeanImpl())));
        suite.addTest(new StateTestBase("testSetPropertiesOk", new WrappedBeanState(new TestBeanImpl())));
        suite.addTest(new StateTestBase("testSetNonExistantPropertiesFails", new WrappedBeanState(new TestBeanImpl())));
        suite.addTest(new StateTestBase("testSetterMethodExceptionCausesFailure", new WrappedBeanState(new TestBeanImpl())));
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
