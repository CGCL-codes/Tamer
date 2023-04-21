package org.exolab.jms.net.vm.invoke;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.exolab.jms.net.invoke.ExceptionTestCase;

/**
 * Tests exception handling, via the VM connector.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/05/03 13:46:03 $
 */
public class VMExceptionTest extends ExceptionTestCase {

    /**
     * Construct an instance of this class for a specific test case.
     *
     * @param name the name of test case
     */
    public VMExceptionTest(String name) {
        super(name, "vm://");
    }

    /**
     * Sets up the test suite.
     *
     * @return a test suite
     */
    public static Test suite() {
        return new TestSuite(VMExceptionTest.class);
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
