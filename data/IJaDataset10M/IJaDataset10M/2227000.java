package test.net.sourceforge.pmd.rules.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * tests for the net.sourceforge.pmd.rules.junit package
 *
 * @author Boris Gruschko ( boris at gruschko.org )
 * @version $Id: JUnitRulesTests.java 2282 2003-09-29 14:32:33Z tomcopeland $
 */
public class JUnitRulesTests {

    /**
   * test suite
   *
   * @return test suite
   */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.net.sourceforge.pmd.rules.junit");
        suite.addTestSuite(JUnitAssertionsShouldIncludeMessageRuleTest.class);
        suite.addTestSuite(JUnitSpellingRuleTest.class);
        suite.addTestSuite(JUnitStaticSuiteRuleTest.class);
        return suite;
    }
}
