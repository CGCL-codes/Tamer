package net.sf.statcvs;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * JUnit Test suite containing all tests for StatCvs.
 * 
 * @author Manuel Schulze
 * @version $Id: AllTests.java,v 1.13 2002/08/23 14:49:29 cyganiak Exp $
 */
public class AllTests {

    /**
     * Method suite.
     * @return Test suite
     */
    public static Test suite() {
        Logger.getLogger("net.sf.statcvs").setLevel(Level.OFF);
        final TestSuite suite = new TestSuite("Test for net.sf.statcvs");
        suite.addTest(net.sf.statcvs.input.AllTests.suite());
        suite.addTest(net.sf.statcvs.model.AllTests.suite());
        suite.addTest(net.sf.statcvs.util.AllTests.suite());
        suite.addTest(net.sf.statcvs.pages.AllTests.suite());
        suite.addTest(net.sf.statcvs.renderer.AllTests.suite());
        suite.addTest(net.sf.statcvs.reportmodel.AllTests.suite());
        suite.addTest(net.sf.statcvs.output.AllTests.suite());
        return suite;
    }

    /**
     * Runs all StatCvs unit tests with the text TestRunner
     * @param args Ignored.
     */
    public static void main(final String[] args) {
        TestRunner.run(suite());
    }
}
