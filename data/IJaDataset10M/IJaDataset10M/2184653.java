package net.sourceforge.poi.xml.hssf;

import net.sourceforge.poi.cocoon.serialization.elementprocessor.Attribute;
import junit.framework.*;
import java.util.*;
import java.io.*;

/**
 * Class to test EPDiagonal functionality.
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com)
 * @author Andrew C. Oliver (acoliver2@users.sourceforge.net)
 */
public class TestEPDiagonal extends TestCase {

    /**
     * Constructor TestEPDiagonal
     *
     * @param name
     */
    public TestEPDiagonal(String name) {
        super(name);
    }

    /**
     * test getShade
     *
     * @exception IOException
     */
    public void testGetStyle() throws IOException {
        for (int k = 1; k < 14; k++) {
            EPDiagonal ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            Attribute[] attrs = { new Attribute("Color", "0:ffff:1001"), new Attribute("Style", String.valueOf(k)) };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            assertEquals(k, ep.getStyle());
        }
        try {
            EPDiagonal ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            Attribute[] attrs = { new Attribute("Color", " 0:1:FFFF "), new Attribute("Style", "-1") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getStyle();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            EPDiagonal ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            Attribute[] attrs = { new Attribute("Color", " 0:1:FFFF "), new Attribute("Style", "14") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getStyle();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            EPDiagonal ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            Attribute[] attrs = { new Attribute("Style", "foo") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getStyle();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            ((EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class)).getStyle();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
    }

    /**
     * test getColor
     *
     * @exception IOException
     */
    public void testGetColor() throws IOException {
        EPDiagonal ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
        Attribute[] attrs = { new Attribute("Color", " 0:1:FFFF "), new Attribute("Style", "0") };
        ep.initialize(attrs, Helper.createEPStyleBorder());
        assertEquals(0, ep.getColor().getRed());
        assertEquals(1, ep.getColor().getGreen());
        assertEquals(65535, ep.getColor().getBlue());
        try {
            ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            attrs = new Attribute[] { new Attribute("Color", "0:1"), new Attribute("Style", "0") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getColor();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            attrs = new Attribute[] { new Attribute("Style", "0"), new Attribute("Color", "0:1:2:3") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getColor();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            attrs = new Attribute[] { new Attribute("Style", "0"), new Attribute("Color", "0:1:foo") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getColor();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            attrs = new Attribute[] { new Attribute("Style", "0"), new Attribute("Color", "0:1:foo") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getColor();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        try {
            ep = (EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class);
            attrs = new Attribute[] { new Attribute("Style", "0"), new Attribute("Color", "0:ffff:10001") };
            ep.initialize(attrs, Helper.createEPStyleBorder());
            ;
            ep.getColor();
            fail("Should have caught IOException");
        } catch (IOException ignored) {
        }
        assertNull(((EPDiagonal) Helper.makeElementProcessor(EPDiagonal.class)).getColor());
    }

    /**
     * main method to run the unit tests
     *
     * @param ignored_args
     */
    public static void main(String[] ignored_args) {
        System.out.println("Testing serialization.hssfhelpers.elementprocessors.EPDiagonal");
        junit.textui.TestRunner.run(TestEPDiagonal.class);
    }
}
