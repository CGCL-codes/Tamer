package test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author peter
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EOTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("All EthOntos Tests");
        suite.addTest(new TestSuite(EOTestCase.class));
        return suite;
    }
}
