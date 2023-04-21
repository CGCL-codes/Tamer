package org.exolab.jms.net.http.registry;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.exolab.jms.net.registry.RegistryTestCase;
import org.exolab.jms.net.registry.Registry;
import org.exolab.jms.net.util.SSLUtil;

/**
 * Tests the behaviour of the {@link Registry}, using the HTTP connector.
 *
 * @version     $Revision: 1.2 $ $Date: 2005/04/17 14:15:26 $
 * @author      <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 */
public class HTTPSRegistryTest extends RegistryTestCase {

    /**
     * Construct an instance of this class for a specific test case.
     *
     * @param name the name of test case
     * @throws Exception for any error
     */
    public HTTPSRegistryTest(String name) throws Exception {
        super(name, "https-server://localhost:3030", "https://localhost:8443/openjms-tunnel/tunnel", SSLUtil.getHTTPSProperties("test.keystore", "secret"));
    }

    /**
     * Sets up the test suite.
     *
     * @return a test suite
     */
    public static Test suite() {
        return new TestSuite(HTTPSRegistryTest.class);
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
