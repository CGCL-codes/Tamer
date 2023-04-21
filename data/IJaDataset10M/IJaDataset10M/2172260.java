package geovista.treemap;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The TestAllTM class launch the whole
 * set of JUnit test for the treemap package.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * 
 */
public class TestAllTM {

    /**
     * Suite of the whole set of tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("All treemap Tests");
        suite.addTest(TestTMUpdater.suite());
        return suite;
    }
}
