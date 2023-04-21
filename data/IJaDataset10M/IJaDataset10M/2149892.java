package org.exolab.jms.net.rmi.invoke;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.exolab.jms.net.invoke.DisconnectionTestCase;

/**
 * Tests ORB disconnection notification via the RMI connector.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/06/04 14:55:07 $
 */
public class RMIDisconnectionTest extends DisconnectionTestCase {

    /**
     * Construct a new <code>RMIDisconnectionTest</code>.
     *
     * @param name the name of test case
     */
    public RMIDisconnectionTest(String name) {
        super(name, "rmi://localhost:4096");
    }

    /**
     * Sets up the test suite.
     *
     * @return a test suite
     */
    public static Test suite() {
        return new TestSuite(RMIDisconnectionTest.class);
    }

    /**
     * The main line used to execute the test cases.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
