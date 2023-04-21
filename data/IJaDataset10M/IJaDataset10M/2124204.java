package up2p.repository.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import up2p.core.WebAdapter;

/**
 * Runs all JUnit tests in the up2p.repository package.
 * 
 * @author Neal Arthorne
 * @version 1.0
 */
public class AllRepositoryTests {

    static {
        System.setProperty(WebAdapter.UP2P_HOME, System.getProperty("user.dir"));
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("All repository Package Tests");
        suite.addTestSuite(TestDefaultRepository.class);
        suite.addTestSuite(TestFedoraRepository.class);
        return suite;
    }
}
