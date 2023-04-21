package atlantik.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

public class Atlantik_UI_TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for atlantik.ui");
        suite.addTestSuite(Test_Main.class);
        suite.addTestSuite(Test_remote_AltantikClient_connect.class);
        suite.addTestSuite(Test_Simple_AI.class);
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
