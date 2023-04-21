package magoffin.matt.ieat.dao;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all DAO tests.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 4 $ $Date: 2009-04-30 23:27:53 -0400 (Thu, 30 Apr 2009) $
 */
public class AllTests {

    /**
	 * @return test
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for magoffin.matt.ieat.dao");
        suite.addTestSuite(MealDaoTest.class);
        suite.addTestSuite(RecipeDaoTest.class);
        suite.addTestSuite(UserDaoTest.class);
        return suite;
    }
}
