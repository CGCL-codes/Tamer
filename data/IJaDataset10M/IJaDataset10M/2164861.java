package snipsnap.test.plugin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import snipsnap.test.plugin.servlet.TestLoadPlugins;

/**
 * @author Matthias L. Jugel <matthias.jugel@first.fraunhofer.de>
 * @version $Id$
 */
public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(TestLoadPlugins.class);
        return testSuite;
    }
}
