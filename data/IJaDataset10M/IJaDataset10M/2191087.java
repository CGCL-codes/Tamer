package org.apache.harmony.beans.tests.java.beans.beancontext;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test for java.beans.beancontext.
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Suite for org.apache.harmony.beans.tests.java.beans.beancontext");
        suite.addTestSuite(BeanContextChildSupportTest.class);
        suite.addTestSuite(BeanContextEventTest.class);
        suite.addTestSuite(BeanContextMembershipEventTest.class);
        suite.addTestSuite(BeanContextServiceAvailableEventTest.class);
        suite.addTestSuite(BeanContextServiceRevokedEventTest.class);
        suite.addTestSuite(BeanContextServicesSupportTest.class);
        suite.addTestSuite(BeanContextSupportTest.class);
        return suite;
    }
}
