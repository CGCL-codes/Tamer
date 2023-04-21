package org.oclc.da.ndiipp.relationship.pvt.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class is base for all test cases in this package.
 * 
 * @author stanesca
 * Created on Jan 12, 2005
 */
public class AllTests {

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (junit.textui.TestRunner.run(suite()).wasSuccessful()) System.exit(0); else System.exit(5);
    }

    /**
     * Suite.
     * 
     * @return the test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.oclc.da.ndiipp.relationship.pvt.test");
        suite.addTestSuite(RelationshipManagerPrePostTest.class);
        suite.addTestSuite(RelationshipManagerQueryTest.class);
        return suite;
    }
}
