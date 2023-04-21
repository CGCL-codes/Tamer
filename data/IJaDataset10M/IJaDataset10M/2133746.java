package fr.dz.swan.converters;

import java.awt.Toolkit;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 */
public class LengthConverterTest extends TestCase {

    private final int dpi = Toolkit.getDefaultToolkit().getScreenResolution();

    public LengthConverterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of a basic convertion
     */
    @Test
    public void testBasicConversion() {
        System.out.println("BasicConversion");
        String length = "180";
        int expResult = 180;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }

    @Test
    public void testSecondBasicConversion() {
        System.out.println("SecondBasicConversion");
        String length = "180px";
        int expResult = 180;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }

    @Test
    public void testInchConversion() {
        System.out.println("InchConversion");
        System.out.println("dpi : " + dpi);
        String length = "2in";
        int expResult = 2 * dpi;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }

    @Test
    public void testCmConversion() {
        System.out.println("CmConversion");
        System.out.println("dpi : " + dpi);
        String length = "50.8cm";
        int expResult = 20 * dpi;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }

    @Test
    public void testMmConversion() {
        System.out.println("MmConversion");
        System.out.println("dpi : " + dpi);
        String length = "508mm";
        int expResult = 20 * dpi;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }

    @Test
    public void testPtConversion() {
        System.out.println("PtConversion");
        System.out.println("dpi : " + dpi);
        String length = "144pt";
        int expResult = 2 * dpi;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }

    @Test
    public void testPcConversion() {
        System.out.println("PcConversion");
        System.out.println("dpi : " + dpi);
        String length = "12pc";
        int expResult = 2 * dpi;
        int result = LengthConverter.getPxFromLength(length);
        assertEquals(expResult, result);
    }
}
