package junit.extensions.jfunc.tests;

import junit.framework.*;
import junit.extensions.jfunc.*;

/**
 * A simple test class that demonstrating JFuncTestSuite
 **/
public class SweetSuite extends JFuncTestCase {

    boolean sharing = false;

    /**
     * Must have a default constructor in order for ProxyTestSuite to work
     **/
    public SweetSuite() {
        super("NONE");
    }

    public SweetSuite(String name) {
        super(name);
    }

    /**
     * This test simply passes
     **/
    public void testPassed() {
        assert("This will never fail", true);
    }

    /**
     * This test simply fails
     **/
    public void testFailed() {
        fail("This could never succeed");
    }

    /**
     * This test simply throws an error
     **/
    public void testErrored() {
        throw new RuntimeException("This is always error here");
    }

    /**
     * A test that can actually accept an argument.
     **/
    public void testArgs(String msg) {
        assert("Msg does not equal null", msg != null);
        //System.out.println("got argument " + msg);
    }

    /**
     * This test illustrates the differences when using the
     * oneTestInstancePerTest().
     **/
    public void testSharing() {
        assert("sharing should always start out false", sharing == false);
        sharing = true;
    }

    /**
     * TestTest.
     **/
    public static Test suite() {
        try {
            JFuncSuite suite = new JFuncSuite();
            suite.oneTest(true);
            SweetSuite realTestInstance = new SweetSuite();
            SweetSuite testProxy = 
                (SweetSuite) suite.getTestProxy(realTestInstance);
            testProxy.testPassed();
            testProxy.testFailed();
            testProxy.testErrored();
            testProxy.testArgs("testing 1, 2, 3");
            testProxy.testArgs(null);
            testProxy.testSharing();
            testProxy.testSharing();
            return suite;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
