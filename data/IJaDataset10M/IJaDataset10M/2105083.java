package org.htmlparser.tests;

import junit.framework.TestSuite;
import org.htmlparser.tests.ParserTestCase;

public class AllTests extends ParserTestCase {

    static {
        System.setProperty("org.htmlparser.tests.AllTests", "AllTests");
    }

    public AllTests(String name) {
        super(name);
    }

    public static TestSuite suite() {
        TestSuite suite;
        TestSuite sub;
        suite = new TestSuite("HTMLParser Tests");
        sub = new TestSuite("Basic Tests");
        sub.addTestSuite(ParserTest.class);
        sub.addTestSuite(AssertXmlEqualsTest.class);
        sub.addTestSuite(FunctionalTests.class);
        sub.addTestSuite(LineNumberAssignedByNodeReaderTest.class);
        suite.addTest(sub);
        suite.addTest(org.htmlparser.tests.lexerTests.AllTests.suite());
        suite.addTest(org.htmlparser.tests.scannersTests.AllTests.suite());
        suite.addTest(org.htmlparser.tests.utilTests.AllTests.suite());
        suite.addTest(org.htmlparser.tests.tagTests.AllTests.suite());
        suite.addTest(org.htmlparser.tests.visitorsTests.AllTests.suite());
        suite.addTest(org.htmlparser.tests.parserHelperTests.AllTests.suite());
        suite.addTestSuite(org.htmlparser.tests.filterTests.FilterTest.class);
        return (suite);
    }
}
