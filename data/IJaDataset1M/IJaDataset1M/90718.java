package uk.ac.warwick.dcs.cokefolk.server.operations;

import uk.ac.warwick.dcs.cokefolk.server.operations.types.*;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestAllOperations {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test all operations layer classes");
        suite.addTest(TestAllTypes.suite());
        suite.addTestSuite(TestEnvironment.class);
        return suite;
    }
}
