package junit.extensions.xtestsuite.abtests;

import junit.framework.Test;
import junit.extensions.TestSetup;

/**
 *
 * @author  daniel
 */
public class TestSetupA extends TestSetup {

    public TestSetupA(Test t) {
        super(t);
    }

    public void setUp() {
        TestSetupA.a = new A();
    }

    public void tearDown() {
        TestSetupA.a = null;
    }

    private static A a = null;

    public static final Object getTestData() {
        return TestSetupA.a;
    }
}
