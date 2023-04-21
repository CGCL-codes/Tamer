package org.exolab.jms.net.http.invoke;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.exolab.jms.net.invoke.ExceptionTestCase;
import org.exolab.jms.net.util.SSLUtil;

/**
 * Tests exception handling, via the HTTPS connector.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.3 $ $Date: 2005/05/03 13:46:00 $
 */
public class HTTPSExceptionTest extends ExceptionTestCase {

    /**
     * Construct an instance of this class for a specific test case.
     *
     * @param name the name of test case
     * @throws Exception for any error
     */
    public HTTPSExceptionTest(String name) throws Exception {
        super(name, "https-server://localhost:3030", "https://localhost:8443/openjms-tunnel/tunnel", SSLUtil.getHTTPSProperties("test.keystore", "secret"), null);
    }

    /**
     * Sets up the test suite.
     *
     * @return a test suite
     */
    public static Test suite() {
        return new TestSuite(HTTPSExceptionTest.class);
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
