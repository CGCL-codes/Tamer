package com.loribel.commons.util;

import java.util.Currency;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * Test GB_NumberTools.
 *
 * @author Gr�gory Borelli
 */
public class GB_NumberToolsTest extends TestCase {

    public GB_NumberToolsTest(String a_name) {
        super(a_name);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_NumberTools#applyPattern(
     * double,java.lang.String) GB_NumberTools.applyPattern(double, String)}
     * method
     */
    public void test_applyPattern_double() {
        Locale.setDefault(Locale.ENGLISH);
        test_applyPattern_double(1, 12.2d, "#", "12");
        test_applyPattern_double(2, 12.36d, "000", "012");
        test_applyPattern_double(3, -12.36d, "#", "-12");
        test_applyPattern_double(4, -12345.36d, "#,###.##", "-12,345.36");
        test_applyPattern_double(5, -12345.36d, "####.##", "-12345.36");
        test_applyPattern_double(6, -12.36d, "#,###.000", "-12.360");
        test_applyPattern_double(7, .361d, "#%", "36%");
        test_applyPattern_double(7, .361d, "#.00%", "36.10%");
        Locale.setDefault(Locale.FRENCH);
        test_applyPattern_double(1, 12.2d, "#", "12");
        test_applyPattern_double(2, 12.36d, "000", "012");
        test_applyPattern_double(3, -12.36d, "#", "-12");
        test_applyPattern_double(4, -12345.36d, "#,###.##", "-12 345,36");
        test_applyPattern_double(5, -12345.36d, "####.##", "-12345,36");
        test_applyPattern_double(6, -12.36d, "#,###.000", "-12,360");
        test_applyPattern_double(7, .361d, "#.00%", "36,10%");
    }

    public void test_applyPattern_double(int a_index, double a_value, String a_patern, String a_expectedValue) {
        String l_result = GB_NumberTools.applyPattern(a_value, a_patern);
        assertEquals(a_index + " - Result is not as expected for pattern \"" + a_patern + "\"", a_expectedValue, l_result);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_NumberTools#applyPattern(
     * double,java.lang.String) GB_NumberTools.applyPattern(int, String)}
     * method
     */
    public void test_applyPattern_int() {
        Locale.setDefault(Locale.ENGLISH);
        test_applyPattern_int(1, 122, "#", "122");
        test_applyPattern_int(2, 12, "000", "012");
        test_applyPattern_int(3, 123456, "#", "123456");
        test_applyPattern_int(4, 123456, "#,###.##", "123,456");
        test_applyPattern_int(5, -123456, "#,###.000", "-123,456.000");
        Locale.setDefault(Locale.FRENCH);
        test_applyPattern_int(10, 122, "#", "122");
        test_applyPattern_int(11, 12, "000", "012");
        test_applyPattern_int(12, 123456, "#", "123456");
        test_applyPattern_int(13, 123456, "#,###.##", "123 456");
        test_applyPattern_int(14, -123456, "#,###.000", "-123 456,000");
    }

    public void test_applyPattern_int(int a_index, int a_value, String a_patern, String a_expectedValue) {
        String l_result = GB_NumberTools.applyPattern(a_value, a_patern);
        assertEquals(a_index + " - Result is not as expected for pattern \"" + a_patern + "\"", a_expectedValue, l_result);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_NumberTools#applyPattern(
     * java.lang.Number,java.lang.String) GB_NumberTools.applyPattern(
     * Number, String)} method
     */
    public void test_applyPattern_number() {
        Currency currency;
        Number number;
        String expectedResult;
        String pattern;
        String result;
        Locale.setDefault(Locale.ENGLISH);
        number = new Integer(12);
        pattern = "#";
        expectedResult = "12";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.01 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Double(12.36d);
        pattern = "000";
        expectedResult = "012";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.02 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Short((short) -12);
        pattern = "#";
        expectedResult = "-12";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.03 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Float(12.36f);
        pattern = "#,###.##";
        expectedResult = "12.36";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.04 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Integer(12);
        pattern = "##.000";
        expectedResult = "12.000";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.05 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Float(1.3f);
        pattern = "#%";
        expectedResult = "130%";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.06 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Double(12.36);
        pattern = "#.##¤";
        Locale.setDefault(Locale.US);
        currency = Currency.getInstance(Locale.getDefault());
        expectedResult = "12.36" + currency.getSymbol();
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.07 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
        number = new Long(5285);
        pattern = "#,###.##¤";
        Locale.setDefault(Locale.UK);
        currency = Currency.getInstance(Locale.getDefault());
        expectedResult = "5,285" + currency.getSymbol();
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.08 + " - Result is not as expected for pattern \"" + pattern + "\"", expectedResult, result);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_NumberTools#applyPatternDefault(
     * java.lang.Number,java.lang.String) GB_NumberTools.applyPatternDefault(
     * Number, String)} method
     */
    public void test_applyPatternDefault() {
        Number number;
        String expectedResult;
        String pattern;
        String result;
        Locale.setDefault(Locale.ENGLISH);
        number = new Integer(12);
        pattern = "";
        expectedResult = GB_NumberTools.applyPatternDefault(number);
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.01 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
        number = new Double(GB_NumberTools.LIMIT_FOR_BIG_VALUE);
        pattern = "";
        expectedResult = GB_NumberTools.applyPatternDefault(number);
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.02 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
        number = new Double(GB_NumberTools.LIMIT_FOR_SMALL_VALUE);
        pattern = "";
        expectedResult = GB_NumberTools.applyPatternDefault(number);
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.03 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
        number = new Integer(12);
        pattern = null;
        expectedResult = GB_NumberTools.applyPatternDefault(number);
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.04 + " - Result is not as expected for null pattern", expectedResult, result);
        number = new Double(GB_NumberTools.LIMIT_FOR_BIG_VALUE);
        pattern = null;
        expectedResult = GB_NumberTools.applyPatternDefault(number);
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.05 + " - Result is not as expected for null pattern", expectedResult, result);
        number = new Double(GB_NumberTools.LIMIT_FOR_SMALL_VALUE);
        pattern = null;
        expectedResult = GB_NumberTools.applyPatternDefault(number);
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.06 + " - Result is not as expected for null pattern", expectedResult, result);
        number = new Integer(1999999999);
        pattern = "#";
        expectedResult = "1999999999";
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.07 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
        number = new Double(0.000000000009);
        pattern = "#E00";
        expectedResult = "9E-12";
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.08 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
        number = new Double(0.0000001);
        pattern = "0.0E0";
        expectedResult = "1.0E-7";
        result = GB_NumberTools.applyPattern(number, pattern);
        assertEquals(1.09 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
        number = new Double(1999999999);
        pattern = "0.000000E0";
        expectedResult = "1.999999999E9";
        result = GB_NumberTools.applyPatternDefault(number, pattern);
        assertEquals(1.10 + " - Result is not as expected for pattern : \"" + pattern + "\"", expectedResult, result);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_NumberTools#applyPatternDefault(
     * java.lang.Number) GB_NumberTools.applyPatternDefault(Number)} method
     */
    public void test_applyPatternDefault_withoutPattern() {
        Number number;
        String expectedResult;
        String result;
        Locale.setDefault(Locale.ENGLISH);
        number = new Integer(12);
        expectedResult = "12";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.01 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Long(1000000000);
        expectedResult = "1E9";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.02 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Long(999999999);
        expectedResult = "999,999,999";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.03 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Integer(-1000000000);
        expectedResult = "-1E9";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.04 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Integer(-999999999);
        expectedResult = "-999,999,999";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.05 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Double(0.000002);
        expectedResult = "0.000002";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.06 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Double(0.0000009);
        expectedResult = "9E-7";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.07 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Double(-0.000002);
        expectedResult = "-0.000002";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.08 + " - Result is not as expected for default pattern", expectedResult, result);
        number = new Double(-0.0000009);
        expectedResult = "-9E-7";
        result = GB_NumberTools.applyPatternDefault(number);
        assertEquals(1.09 + " - Result is not as expected for default pattern", expectedResult, result);
    }

    /**
     * Test method : <tt>getTypesInString</tt>.
     */
    public void test_getTypesInString() {
        String[] l_types = GB_NumberTools.getTypesInString();
        assertEquals(1 + " - size", 5, l_types.length);
    }

    /**
     * Test method : <tt>getTypesSimple</tt>.
     */
    public void test_getTypesSimple() {
        String[] l_types = GB_NumberTools.getTypesSimple();
        assertEquals(1 + " - size", 5, l_types.length);
    }

    public void test_isEven() {
        assertTrue("1", GB_NumberTools.isEven(0));
        assertTrue("2", GB_NumberTools.isEven(2));
        assertTrue("3", GB_NumberTools.isEven(646476));
        assertFalse("4", GB_NumberTools.isEven(1));
        assertFalse("5", GB_NumberTools.isEven(12221));
        assertTrue("10", GB_NumberTools.isEven(-0));
        assertTrue("11", GB_NumberTools.isEven(-2));
        assertTrue("12", GB_NumberTools.isEven(-646476));
        assertFalse("13", GB_NumberTools.isEven(-1));
        assertFalse("14", GB_NumberTools.isEven(-12221));
    }

    /**
     * Test method : <tt>lettersToIndex</tt>.
     */
    public void test_lettersToIndex() {
        test_lettersToIndex(1, 1, "a");
        test_lettersToIndex(2, 2, "b");
        test_lettersToIndex(3, 3, "c");
        test_lettersToIndex(4, 4, "d");
        test_lettersToIndex(5, 5, "e");
        test_lettersToIndex(6, 26, "z");
        test_lettersToIndex(11, 1, "A");
        test_lettersToIndex(12, 2, "B");
        test_lettersToIndex(13, 3, "C");
        test_lettersToIndex(14, 4, "D");
        test_lettersToIndex(15, 5, "E");
        test_lettersToIndex(16, 26, "Z");
        test_lettersToIndex(21, -1, " ");
        test_lettersToIndex(22, -1, ".");
        test_lettersToIndex(23, -1, "5");
        test_lettersToIndex(31, 27, "aa");
        test_lettersToIndex(32, 28, "ab");
        test_lettersToIndex(33, 29, "ac");
        test_lettersToIndex(34, 30, "ad");
        test_lettersToIndex(35, 31, "ae");
        test_lettersToIndex(36, 26 + 26, "az");
        test_lettersToIndex(36, 26 + 26 + 1, "ba");
        test_lettersToIndex(41, 27, "aA");
        test_lettersToIndex(42, 28, "aB");
        test_lettersToIndex(43, 29, "AC");
    }

    /**
     * Test method : <tt>letterToIndex</tt>.
     */
    public void test_lettersToIndex(int a_index, int a_expectedValue, String a_letters) {
        int l_value;
        l_value = GB_NumberTools.lettersToIndex(a_letters);
        assertEquals(a_index + .1 + " - value", a_expectedValue, l_value);
        a_letters = GB_StringTransformTools.toCapitalize(a_letters);
        l_value = GB_NumberTools.lettersToIndex(a_letters);
        assertEquals(a_index + .2 + " - value", a_expectedValue, l_value);
        a_letters = a_letters.toUpperCase();
        l_value = GB_NumberTools.lettersToIndex(a_letters);
        assertEquals(a_index + .3 + " - value", a_expectedValue, l_value);
        a_letters = a_letters.toLowerCase();
        l_value = GB_NumberTools.lettersToIndex(a_letters);
        assertEquals(a_index + .4 + " - value", a_expectedValue, l_value);
    }

    /**
     * Test method : <tt>letterToIndex</tt>.
     */
    public void test_letterToIndex() {
        test_letterToIndex(1, 1, 'a');
        test_letterToIndex(2, 2, 'b');
        test_letterToIndex(3, 3, 'c');
        test_letterToIndex(4, 4, 'd');
        test_letterToIndex(5, 5, 'e');
        test_letterToIndex(6, 26, 'z');
        test_letterToIndex(11, 1, 'A');
        test_letterToIndex(12, 2, 'B');
        test_letterToIndex(13, 3, 'C');
        test_letterToIndex(14, 4, 'D');
        test_letterToIndex(15, 5, 'E');
        test_letterToIndex(16, 26, 'Z');
        test_letterToIndex(21, -1, ' ');
        test_letterToIndex(22, -1, '.');
        test_letterToIndex(23, -1, '5');
    }

    /**
     * Test method : <tt>letterToIndex</tt>.
     */
    public void test_letterToIndex(int a_index, int a_expectedValue, char a_letter) {
        int l_value;
        l_value = GB_NumberTools.letterToIndex(a_letter);
        assertEquals(a_index + .1 + " - value", a_expectedValue, l_value);
        l_value = GB_NumberTools.lettersToIndex("" + a_letter);
        assertEquals(a_index + .2 + " - value", a_expectedValue, l_value);
    }

    /**
     * Test method <tt>stringWithLocaleToNumber</tt>.
     */
    public void test_stringWithLocaleToNumber() {
        String s;
        Number n;
        Number result;
        Class type;
        Locale.setDefault(Locale.CANADA_FRENCH);
        s = "12,5";
        n = new Double(12.5);
        type = Double.class;
        result = GB_NumberTools.stringWithLocaleToNumber(s, type);
        assertEquals(1 + " - " + s, n, result);
        s = "12";
        n = new Integer(12);
        type = Integer.class;
        result = GB_NumberTools.stringWithLocaleToNumber(s, type);
        assertEquals(2 + " - " + s, n, result);
        s = "-555";
        n = new Integer(-555);
        type = Integer.class;
        result = GB_NumberTools.stringWithLocaleToNumber(s, type);
        assertEquals(3 + " - " + s, n, result);
        s = "-5,55";
        n = new Float(-5.55);
        type = Float.class;
        result = GB_NumberTools.stringWithLocaleToNumber(s, type);
        assertEquals(4 + " - " + s, n, result);
        s = "";
        n = null;
        type = Integer.class;
        result = GB_NumberTools.stringWithLocaleToNumber(s, type);
        assertEquals(5 + " - " + s, n, result);
        s = null;
        n = null;
        type = Float.class;
        result = GB_NumberTools.stringWithLocaleToNumber(s, type);
        assertEquals(6 + " - " + s, n, result);
        s = "99999999999";
        type = Integer.class;
        try {
            result = GB_NumberTools.stringWithLocaleToNumber(s, type);
            fail(s);
        } catch (NumberFormatException e) {
        }
    }

    public void test_toLongSafe() {
        test_toLongSafe(0, null, 0);
        test_toLongSafe(1, "", 0);
        test_toLongSafe(2, "  \n", 0);
        test_toLongSafe(3, "ajhdj;", 0);
        test_toLongSafe(4, "hsh1lkl2jhj", 12);
        test_toLongSafe(5, "env 1.000.000 US$", 1000000);
        test_toLongSafe(6, "1234", 1234);
    }

    private void test_toLongSafe(int a_index, String a_str, int a_expectedValue) {
        long l_value = GB_NumberTools.toLongSafe(a_str, false);
        assertEquals("" + a_index, a_expectedValue, l_value);
    }

    /**
     * M�thode de test pour tester la m�thode : <tt>toNumber</tt>.
     */
    public void test_toNumber() {
        test_toNumber(0, null, null, Double.class);
        test_toNumber(1, null, null, Float.class);
        test_toNumber(2, null, null, Integer.class);
        test_toNumber(3, null, null, Long.class);
        test_toNumber(4, null, null, Short.class);
        test_toNumber(5, new Double(5.215), new Double(5.215), Double.class);
        test_toNumber(6, new Double(5.215), new Float(5.215), Float.class);
        test_toNumber(7, new Double(5.215E-5), new Float(5.215E-5), Float.class);
        test_toNumber(8, new Double(5215), new Integer(5215), Integer.class);
        test_toNumber(9, new Double(5215), new Long(5215), Long.class);
        test_toNumber(10, new Double(5215), new Short((short) 5215), Short.class);
        test_toNumber(11, new Integer(5215), new Double(5215), Double.class);
        test_toNumber(11, new Integer(5215), new Float(5215), Float.class);
        test_toNumber(11, new Integer(5215), new Integer(5215), Integer.class);
        test_toNumber(11, new Integer(5215), new Long(5215), Long.class);
        test_toNumber(11, new Integer(5215), new Short((short) 5215), Short.class);
    }

    private void test_toNumber(int a_index, Number a_value, Number a_expectedValue, Class a_numberClass) {
        Number l_result = GB_NumberTools.toNumber(a_numberClass, a_value);
        if (a_value == null) {
            assertNull(a_index + .1 + " - Result not null", l_result);
        } else {
            assertTrue(a_index + .1 + " - result type is not valid", a_numberClass.isInstance(l_result));
            assertEquals(a_index + .2 + " - " + a_value, a_expectedValue, l_result);
        }
    }
}
