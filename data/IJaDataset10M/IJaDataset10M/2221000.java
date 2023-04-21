package gov.sns.apps.jeri.test;

import gov.sns.apps.jeri.apps.powersupplyfunctions.PowerSupplyFunctionsTester;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class PowerSupplyFunctionsTests {

    public static Test suite() {
        TestSuite suite;
        suite = new TestSuite("PowerSupplyFunctionsTests");
        suite.addTestSuite(PowerSupplyFunctionsTester.class);
        return suite;
    }

    public static void main(String args[]) {
        TestRunner.run(suite());
    }
}
