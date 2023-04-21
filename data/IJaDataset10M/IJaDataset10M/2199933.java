package edu.kit.pse.ass.entity;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sebastian
 * 
 */
public class WorkplaceTest {

    /** The user. */
    Workplace workplace;

    /**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Before
    public void setUp() throws Exception {
        workplace = new Workplace();
    }

    /**
	 * Test set parent facility.
	 */
    @Test
    public void testSetParentFacility() {
        workplace.setParentFacility(null);
        assertNull(workplace.getParentFacility());
    }

    /**
	 * Test set parent facility.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testSetParentFacility2() {
        workplace.setParentFacility(new Workplace());
    }

    /**
	 * Test set parent facility.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testSetParentFacility3() {
        workplace.setParentFacility(new Building());
    }

    /**
	 * Test set parent facility.
	 */
    @Test
    public void testSetParentFacility4() {
        workplace.setParentFacility(new Room());
        assertTrue(workplace.getParentFacility() != null);
    }

    /**
	 * Test add contained facility
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testAddContainedFacility() {
        workplace.addContainedFacility(null);
    }
}
