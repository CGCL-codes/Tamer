package universe.tests.server;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.swingui.TestRunner;

/**
 * All the server tests.
 *
 * @version $Id: ServerTests.java,v 1.2 2002/03/10 20:40:43 jjweston Exp $
 */
public class ServerTests extends TestCase {

    public ServerTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ServerTest.class);
        return suite;
    }

    /**
     * Permits this test class to be run by itself.
     *
     * @param args   standard java command-line arguments; ignored in this case
     */
    public static void main(String[] args) {
        String[] test = { "universe.tests.server.ServerTests" };
        TestRunner.main(test);
    }
}
