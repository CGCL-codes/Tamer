package cn.myapps;

import junit.framework.Test;
import junit.framework.TestSuite;
import cn.myapps.core.security.action.LoginActionTest;

public class AllTests {

    public static Test suite() {
        System.setProperty("cactus.contextURL", "http://localhost:8080/test");
        TestSuite suite = new TestSuite("All tests with Jetty");
        suite.addTestSuite(LoginActionTest.class);
        return null;
    }
}
