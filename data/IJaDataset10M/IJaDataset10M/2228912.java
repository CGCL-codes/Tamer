package org.josef.test.science.math;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.science.math.RomanNumbers;
import org.junit.Test;

/**
 * JUnit test class for class {@link RomanNumbers}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3598 $
 */
@Status(stage = PRODUCTION)
@Review(by = "Kees Schotanus", at = "2009-09-28")
public final class RomanNumbersTest {

    /**
     * Tests {@link RomanNumbers#toRoman(int)}.
     */
    @Test
    public void testToRoman() {
        assertEquals("Arabic number 0", "", RomanNumbers.toRoman(0));
        assertEquals("Arabic number 1", "I", RomanNumbers.toRoman(1));
        assertEquals("Arabic number 2", "II", RomanNumbers.toRoman(2));
        assertEquals("Arabic number 3", "III", RomanNumbers.toRoman(3));
        assertEquals("Arabic number 4", "IV", RomanNumbers.toRoman(4));
        assertEquals("Arabic number 5", "V", RomanNumbers.toRoman(5));
        assertEquals("Arabic number 6", "VI", RomanNumbers.toRoman(6));
        assertEquals("Arabic number 7", "VII", RomanNumbers.toRoman(7));
        assertEquals("Arabic number 8", "VIII", RomanNumbers.toRoman(8));
        assertEquals("Arabic number 9", "IX", RomanNumbers.toRoman(9));
        assertEquals("Arabic number 10", "X", RomanNumbers.toRoman(10));
    }

    /**
     * Tests {@link RomanNumbers#toArabic(String)}.
     */
    @Test
    public void testToArabic() {
        assertEquals("Simple Roman numeral", 1, RomanNumbers.toArabic("I"));
        assertEquals("Simple Roman number", 2, RomanNumbers.toArabic("II"));
        assertEquals("Simple Roman number", 3, RomanNumbers.toArabic("III"));
        assertEquals("Simple Roman numberl", 4, RomanNumbers.toArabic("IV"));
        assertEquals("Simple Roman numeral", 5, RomanNumbers.toArabic("V"));
        assertEquals("Simple Roman numeral", 10, RomanNumbers.toArabic("X"));
        assertEquals("Simple Roman numeral", 50, RomanNumbers.toArabic("L"));
        assertEquals("Simple Roman numeral", 100, RomanNumbers.toArabic("C"));
        assertEquals("Simple Roman numeral", 500, RomanNumbers.toArabic("D"));
        assertEquals("Simple Roman numeral", 1000, RomanNumbers.toArabic("M"));
        assertEquals("Simple Roman numeral", 334, RomanNumbers.toArabic("IVXLCDM"));
        try {
            RomanNumbers.toArabic("Not a Roman number");
            fail("Unexpectedly parsed an illegal Roman number");
        } catch (final IllegalArgumentException exception) {
            assert true;
        }
    }

    /**
     * Tests {@link RomanNumbers#isValid(String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue("V is a valid Roman number", RomanNumbers.isValid("V"));
        assertFalse("VD is not a valid Roman number", RomanNumbers.isValid("VD"));
    }

    /**
     * Tests {@link RomanNumbers#toArabic(String)} and
     * {@link RomanNumbers#toRoman(int)}.
     * <br>Converts all arabic numbers from 0 to
     * {@link RomanNumbers#LARGEST_ROMAN_NUMBER}, to a Roman number, converts
     * the Roman number to an Arabic number and checks for equality.
     */
    @Test
    public void conversions() {
        for (int i = 0; i <= RomanNumbers.LARGEST_ROMAN_NUMBER; i++) {
            final String roman = RomanNumbers.toRoman(i);
            final int arabic = RomanNumbers.toArabic(roman);
            assertEquals(i + ":" + roman, i, arabic);
        }
    }
}
