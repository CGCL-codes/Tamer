package org.jgap.event;

import junit.framework.*;
import org.jgap.*;

/**
 * Tests the GeneticEvent class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GeneticEventTest extends JGAPTestCase {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.8 $";

    public static Test suite() {
        TestSuite suite = new TestSuite(GeneticEventTest.class);
        return suite;
    }

    /**
   * @author Klaus Meffert
   * @since 1.1
   */
    public void testConstruct_0() {
        try {
            new GeneticEvent("testEventName", null);
            fail();
        } catch (IllegalArgumentException illex) {
            ;
        }
    }

    /**
   * @author Klaus Meffert
   * @since 1.1
   */
    public void testConstruct_1() {
        GeneticEvent event = new GeneticEvent(null, this);
        assertNull(event.getEventName());
    }

    /**
   * @author Klaus Meffert
   * @since 3.0
   */
    public void testConstruct_2() {
        GeneticEvent event = new GeneticEvent("testName", this);
        assertEquals("testName", event.getEventName());
    }

    /**
   * @author Klaus Meffert
   * @since 3.0
   */
    public void testConstruct_3() {
        GeneticEvent event = new GeneticEvent("testName", this, "aValue");
        assertEquals("testName", event.getEventName());
        assertEquals("aValue", event.getValue());
    }

    /**
   * @author Klaus Meffert
   * @since 1.1
   */
    public void testGetEventName_0() {
        GeneticEvent event = new GeneticEvent("testEventName", this);
        assertEquals("testEventName", event.getEventName());
    }

    /**
   * @author Klaus Meffert
   * @since 1.1
   */
    public void testGENOTYPE_EVOLVED_EVENT_0() {
        assertTrue(GeneticEvent.GENOTYPE_EVOLVED_EVENT != null);
        assertTrue(GeneticEvent.GENOTYPE_EVOLVED_EVENT.length() > 0);
    }
}
