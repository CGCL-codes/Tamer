package net.sourceforge.poi.xml.hssf;

import net.sourceforge.poi.cocoon.serialization.elementprocessor.Attribute;
import junit.framework.*;
import java.io.*;

/**
 * Class to test EP_Bottom functionality.
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com)
 */
public class TestEP_Bottom extends TestCase {

    /**
     * Constructor TestEP_Bottom
     *
     * @param name
     */
    public TestEP_Bottom(String name) {
        super(name);
    }

    /**
     * Test getPoints, getPrefUnit
     *
     * @exception IOException
     */
    public void testGetters() throws IOException {
        EP_Bottom ep = (EP_Bottom) Helper.makeElementProcessor(EP_Bottom.class);
        Attribute[] attrs = { new Attribute("Points", " 12.6 "), new Attribute("PrefUnit", " cm ") };
        ep.initialize(attrs, null);
        assertEquals(12.6, ep.getPoints(), 0.0);
        assertEquals(PrintUnits.PRINT_UNITS_CM, ep.getPrefUnit());
        ep = (EP_Bottom) Helper.makeElementProcessor(EP_Bottom.class);
        attrs[0] = new Attribute("Points", "foo");
        ep.initialize(attrs, null);
        try {
            ep.getPoints();
            fail("Should have thrown exception on non-numeric points");
        } catch (IOException ignored) {
        }
        ep = (EP_Bottom) Helper.makeElementProcessor(EP_Bottom.class);
        attrs[0] = new Attribute("Points", "");
        ep.initialize(attrs, null);
        try {
            ep.getPoints();
            fail("Should have thrown exception on empty points");
        } catch (IOException ignored) {
        }
        ep = (EP_Bottom) Helper.makeElementProcessor(EP_Bottom.class);
        attrs[1] = new Attribute("PrefUnit", "junk");
        ep.initialize(attrs, null);
        try {
            ep.getPrefUnit();
            fail("Should have thrown exception on nonsense print units");
        } catch (IOException ignored) {
        }
        ep = (EP_Bottom) Helper.makeElementProcessor(EP_Bottom.class);
        attrs[1] = new Attribute("PrefUnit", "");
        ep.initialize(attrs, null);
        try {
            ep.getPrefUnit();
            fail("Should have thrown exception on empty print units");
        } catch (IOException ignored) {
        }
        ep = (EP_Bottom) Helper.makeElementProcessor(EP_Bottom.class);
        try {
            ep.getPoints();
            fail("Should have thrown exception on non-existent points");
        } catch (IOException ignored) {
        }
        try {
            ep.getPrefUnit();
            fail("Should have thrown exception on non-existent print units");
        } catch (IOException ignored) {
        }
    }

    /**
     * main method to run the unit tests
     *
     * @param ignored_args
     */
    public static void main(String[] ignored_args) {
        System.out.println("Testing serialization.hssfhelpers.EP_Bottom");
        junit.textui.TestRunner.run(TestEP_Bottom.class);
    }
}
