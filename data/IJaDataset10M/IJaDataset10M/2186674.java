package net.walend.jmsbridge.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
A supersuite of tests of jmsbridge.

 @author @dwalend@
*/
public class JMSBridgeTests extends TestCase {

    public JMSBridgeTests(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(QueueTest.suite());
        suite.addTest(TopicTest.suite());
        return suite;
    }
}
