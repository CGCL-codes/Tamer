package test.org.mandarax.sql;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for the mandarax sql package.
 * @author <a href="mailto:jochen.hiller@bauer-partner.com">Jochen Hiller</a>
 * @version 3.4 <7 March 05>
 * @since 2.2
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.org.mandarax.sql");
        suite.addTest(new TestSuite(TestDefaultConnectionManager.class));
        return suite;
    }
}
