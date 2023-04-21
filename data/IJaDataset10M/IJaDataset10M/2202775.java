package org.jlib.core.number;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.jlib.core.number.NumberUtility.isEven;
import static org.jlib.core.number.NumberUtility.isOdd;
import static org.jlib.core.number.NumberUtility.parseHexDigit;

public class NumberUtilityTest {

    @Test
    public void testParseHexDigit0() {
        assertEquals(0, parseHexDigit('0'));
    }

    @Test
    public void testParseHexDigit1() {
        assertEquals(1, parseHexDigit('1'));
    }

    @Test
    public void testParseHexDigit2() {
        assertEquals(2, parseHexDigit('2'));
    }

    @Test
    public void testParseHexDigit3() {
        assertEquals(3, parseHexDigit('3'));
    }

    @Test
    public void testParseHexDigit4() {
        assertEquals(4, parseHexDigit('4'));
    }

    @Test
    public void testParseHexDigit5() {
        assertEquals(5, parseHexDigit('5'));
    }

    @Test
    public void testParseHexDigit6() {
        assertEquals(6, parseHexDigit('6'));
    }

    @Test
    public void testParseHexDigit7() {
        assertEquals(7, parseHexDigit('7'));
    }

    @Test
    public void testParseHexDigit8() {
        assertEquals(8, parseHexDigit('8'));
    }

    @Test
    public void testParseHexDigit9() {
        assertEquals(9, parseHexDigit('9'));
    }

    @Test
    public void testParseHexDigitA() {
        assertEquals(10, parseHexDigit('A'));
    }

    @Test
    public void testParseHexDigitB() {
        assertEquals(11, parseHexDigit('B'));
    }

    @Test
    public void testParseHexDigitC() {
        assertEquals(12, parseHexDigit('C'));
    }

    @Test
    public void testParseHexDigitD() {
        assertEquals(13, parseHexDigit('D'));
    }

    @Test
    public void testParseHexDigitE() {
        assertEquals(14, parseHexDigit('E'));
    }

    @Test
    public void testParseHexDigitF() {
        assertEquals(15, parseHexDigit('F'));
    }

    @Test
    public void testParseHexDigitSmallA() {
        assertEquals(10, parseHexDigit('a'));
    }

    @Test
    public void testParseHexDigitSmallB() {
        assertEquals(11, parseHexDigit('b'));
    }

    @Test
    public void testParseHexDigitSmallC() {
        assertEquals(12, parseHexDigit('c'));
    }

    @Test
    public void testParseHexDigitSmallD() {
        assertEquals(13, parseHexDigit('d'));
    }

    @Test
    public void testParseHexDigitSmallE() {
        assertEquals(14, parseHexDigit('e'));
    }

    @Test
    public void testParseHexDigitSmallF() {
        assertEquals(15, parseHexDigit('f'));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseHexDigitG() {
        parseHexDigit('G');
    }

    @Test(expected = NumberFormatException.class)
    public void testParseHexDigitSmallG() {
        parseHexDigit('g');
    }

    @Test(expected = NumberFormatException.class)
    public void testParseHexDigitPlus() {
        parseHexDigit('+');
    }

    @Test(expected = NumberFormatException.class)
    public void testParseHexDigitMinus() {
        parseHexDigit('-');
    }

    @Test
    public final void testIsEven0() {
        assertTrue(isEven(0));
    }

    @Test
    public final void testIsEven1() {
        assertFalse(isEven(1));
    }

    @Test
    public final void testIsEven2() {
        assertTrue(isEven(2));
    }

    @Test
    public final void testIsEvenMinus1() {
        assertFalse(isEven(-1));
    }

    @Test
    public final void testIsEvenMinus2() {
        assertTrue(isEven(-2));
    }

    @Test
    public final void testIsOdd() {
        assertFalse(isOdd(0));
    }

    @Test
    public final void testIsOdd1() {
        assertTrue(isOdd(1));
    }

    @Test
    public final void testIsOdd2() {
        assertFalse(isOdd(2));
    }

    @Test
    public final void testIsOddMinus1() {
        assertTrue(isOdd(-1));
    }

    @Test
    public final void testIsOddMinus2() {
        assertFalse(isOdd(-2));
    }
}
