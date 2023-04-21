package net.sf.derquinsej;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import org.testng.annotations.Test;

/**
 * Tests for CaseIgnoringString
 * @author Andres Rodriguez
 */
public class CaseIgnoringStringTest {

    private static final String VALUE0 = "VaLue0";

    private static final String VALUE1 = "VaLuE1";

    /**
	 * Test equality.
	 */
    @Test
    public void same() {
        CaseIgnoringString cis1 = CaseIgnoringString.valueOf(VALUE0);
        CaseIgnoringString cis2 = CaseIgnoringString.valueOf(VALUE0.toLowerCase());
        CaseIgnoringString cis3 = CaseIgnoringString.valueOf(VALUE0.toUpperCase());
        assertEquals(cis1, cis1);
        assertEquals(cis1, cis2);
        assertEquals(cis2, cis1);
        assertEquals(cis1, cis3);
        assertEquals(cis3, cis1);
        assertEquals(cis2, cis3);
        assertEquals(cis3, cis2);
        assertEquals(cis1.hashCode(), cis2.hashCode());
        assertEquals(cis1.hashCode(), cis3.hashCode());
        assertEquals(cis2.hashCode(), cis3.hashCode());
    }

    /**
	 * Test non-equality.
	 */
    @Test
    public void distinct() {
        CaseIgnoringString cis0 = CaseIgnoringString.valueOf(VALUE0);
        CaseIgnoringString cis1 = CaseIgnoringString.valueOf(VALUE1);
        assertNotSame(cis0, cis1);
        assertNotSame(cis1, cis0);
    }
}
