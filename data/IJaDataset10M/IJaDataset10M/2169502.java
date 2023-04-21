package bgo.unittests;

import bgo.utils.IDUtil;
import junit.framework.TestCase;

/**
 * test if IDUtil is working correctly
 * 
 * @author Sebastian Duevel
 * 
 */
public class IDUtilTest extends TestCase {

    /**
	 * setUp-Function
	 * 
	 * @throws Exception
	 *             is thrown, if tests fail
	 */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * just calls super.tearDown
	 * 
	 * @throws Exception
	 *             Exception taken from super.tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * tests if the generated IDs are unique
	 * 
	 * this test is not 100% save... as the IDUtil class itself isn't, either
	 * 
	 */
    public final void testgenerateID() {
        assertNotSame(IDUtil.generateID(), IDUtil.generateID());
    }
}
