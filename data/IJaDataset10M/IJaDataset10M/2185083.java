package com.aptana.ide.editor.jscomment.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Kevin Lindsey
 */
public final class AllTests {

    /**
	 * Private constructor for utility classes
	 */
    private AllTests() {
    }

    /**
	 * @return Returns all unit tests to be run in this suite.
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for com.aptana.ide.editor.jscomment");
        suite.addTestSuite(TestDelimiterTokens.class);
        suite.addTestSuite(TestLiteralTokens.class);
        suite.addTestSuite(TestWhitespaceTokens.class);
        return suite;
    }
}
