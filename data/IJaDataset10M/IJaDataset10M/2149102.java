package it.newinstance.test.watchdog.security;

import junit.framework.Test;
import junit.framework.TestSuite;
import it.newinstance.test.watchdog.security.context.TestDefaultContext;
import it.newinstance.test.watchdog.security.listeners.TestPatternListener;
import it.newinstance.test.watchdog.security.monitors.TestServerSocketMonitor;
import it.newinstance.test.watchdog.security.monitors.TestTailMonitor;
import it.newinstance.test.watchdog.security.xml.TestParser;

/**
 * @author Luigi R. Viggiano
 * @version $Revision: 36 $
 * @since 27-nov-2005
 */
public class AllTests {

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("Test for it.newinstance.whatchdog.test");
        suite.addTestSuite(TestTailMonitor.class);
        suite.addTestSuite(TestParser.class);
        suite.addTest(TestPatternListener.suite());
        suite.addTestSuite(TestDefaultContext.class);
        suite.addTestSuite(TestServerSocketMonitor.class);
        return suite;
    }
}
