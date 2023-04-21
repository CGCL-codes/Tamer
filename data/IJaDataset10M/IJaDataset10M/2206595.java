package net.sourceforge.poi.xml.hssf;

import net.sourceforge.poi.cocoon.serialization.elementprocessor.Attribute;
import junit.framework.*;
import java.io.*;

/**
 * Class to test EPCols functionality.
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com)
 */
public class TestEPCols extends TestCase {

    /**
     * Constructor TestEPCols
     *
     * @param name
     */
    public TestEPCols(String name) {
        super(name);
    }

    /**
     * Test getDefaultSizePts
     *
     * @exception IOException
     */
    public void testDefaultSizePts() throws IOException {
        EPCols ep = (EPCols) Helper.makeElementProcessor(EPCols.class);
        Attribute[] attrs = { new Attribute("DefaultSizePts", " 1.5 ") };
        ep.initialize(attrs, Helper.createEPSheet());
        assertEquals(1.5, ep.getDefaultSizePts(), 0.0);
        ep = (EPCols) Helper.makeElementProcessor(EPCols.class);
        attrs[0] = new Attribute("DefaultSizePts", "foo");
        try {
            ep.initialize(attrs, Helper.createEPSheet());
            ep.getDefaultSizePts();
            fail("Should have thrown exception on non-numeric attribute");
        } catch (IOException ignored) {
        }
        assertEquals(Double.parseDouble(EPCols.DEFAULT_SIZE_PTS), ((EPCols) Helper.makeElementProcessor(EPCols.class)).getDefaultSizePts(), 0.0);
    }

    /**
     * main method to run the unit tests
     *
     * @param ignored_args
     */
    public static void main(String[] ignored_args) {
        System.out.println("Testing serialization.hssfhelpers.elementprocessors.EPCols");
        junit.textui.TestRunner.run(TestEPCols.class);
    }
}
