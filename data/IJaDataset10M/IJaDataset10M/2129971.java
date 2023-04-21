package org.xaware.server.common;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ServerCommonTestSuite extends TestSuite {

    public ServerCommonTestSuite(final String name) {
        super(name);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("org.xaware.server.common Unit Tests");
        suite.addTestSuite(XAwareUtilTest.class);
        return suite;
    }
}
