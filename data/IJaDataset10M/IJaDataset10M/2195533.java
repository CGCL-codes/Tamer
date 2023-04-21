package de.igdr;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.igdr.ims.datamodel.HelpFunctionsTest;
import de.igdr.ims.datamodel.MultiplicityTest;
import de.igdr.ims.datamodel.ObjectGroupingModelTest;

/**
 * Class for Testing the Test-Classes of these program
 * 
 * @author ebert
 * @since 1.5
 *
 */
public class AllTests {

    /**
	 * Function to test the Test-Classes.
	 * 
	 * @return <code>TestSuite</code>-Object as a <code>Test</code>-Object  @see junit.framework.TestSuite
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for de.igdr");
        suite.addTestSuite(MultiplicityTest.class);
        suite.addTestSuite(ObjectGroupingModelTest.class);
        suite.addTestSuite(HelpFunctionsTest.class);
        return suite;
    }
}
