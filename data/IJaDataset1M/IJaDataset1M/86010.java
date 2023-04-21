package net.sourceforge.jtombstone.test.dead;

import net.sourceforge.jtombstone.CompareXMLToSavedTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 *
 * @author Bill Alexander
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sourceforge.jtombstone.test.dead");
        suite.addTest(new CompareXMLToSavedTest("dead"));
        suite.addTest(new CompareXMLToSavedTest("dead", "warnings", "warnings.xml", "warningsOutput.xml", false));
        suite.addTest(new CompareXMLToSavedTest("dead", "allMains", "allMains.xml", "allMainsOutput.xml", false));
        return suite;
    }

    public static void main(String[] args) {
        CompareXMLToSavedTest.setDoOutput(true);
        junit.textui.TestRunner.run(suite());
    }
}
