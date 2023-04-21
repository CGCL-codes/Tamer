package alert;

import junit.framework.*;

public class AlertTestSuite extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite(AlertTestCases.class);
        return suite;
    }
}
