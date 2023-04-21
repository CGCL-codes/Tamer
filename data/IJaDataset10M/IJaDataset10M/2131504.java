package org.chimaira.xion;

import junit.framework.Test;
import junit.framework.TestSuite;

public class XionTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for Xion");
        suite.addTestSuite(XionLexerCmpTest.class);
        suite.addTestSuite(XionParserCmpTest.class);
        suite.addTestSuite(XionParserTest.class);
        suite.addTestSuite(XionModelBuilderTest.class);
        suite.addTestSuite(XionLoaderTest.class);
        suite.addTestSuite(XionUtilTest.class);
        return suite;
    }
}
