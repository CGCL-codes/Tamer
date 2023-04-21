package org.ujac.util.text;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.ujac.util.StopWatch;
import org.ujac.util.text.SmartDecimalFormat;
import junit.framework.TestCase;

/**
 * Name: SmartDecimalFormatTest<br>
 * Description: Test cases for the SmartDecimalFormat class.
 * 
 * @author lauerc
 */
public class SmartDecimalFormatTest extends TestCase {

    /** The german floating point format to test. */
    private NumberFormat decNumberFormatDE = null;

    /** The german integer format to test. */
    private NumberFormat intNumberFormatDE = null;

    /** The US floating point format to test. */
    private NumberFormat decNumberFormatUS = null;

    /** The US integer format to test. */
    private NumberFormat intNumberFormatUS = null;

    /**
   * @see junit.framework.TestCase#setUp()
   */
    protected void setUp() throws Exception {
        this.decNumberFormatDE = new SmartDecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.GERMANY), BigDecimal.ROUND_HALF_UP);
        this.intNumberFormatDE = new SmartDecimalFormat("###,###", new DecimalFormatSymbols(Locale.GERMANY), BigDecimal.ROUND_HALF_UP);
        this.decNumberFormatUS = new SmartDecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US), BigDecimal.ROUND_HALF_UP);
        this.intNumberFormatUS = new SmartDecimalFormat("###,###", new DecimalFormatSymbols(Locale.US), BigDecimal.ROUND_HALF_UP);
    }

    /**
   * Tests parsing of numbers.
   * @throws ParseException In case the parsing failed.
   */
    public void testParse() throws ParseException {
        Number number = decNumberFormatUS.parse("2500");
        assertEquals(number, new Long(2500));
        number = decNumberFormatDE.parse("2500");
        assertEquals(number, new Long(2500));
        number = decNumberFormatUS.parse("2500.121");
        assertEquals(number, new Double(2500.121));
        number = decNumberFormatDE.parse("2500,121");
        assertEquals(number, new Double(2500.121));
    }

    /**
   * Tests formatting of numbers.
   */
    public void testFormat() {
        String strNumber = decNumberFormatUS.format(2500);
        assertEquals(strNumber, "2,500.00");
        strNumber = decNumberFormatDE.format(2500);
        assertEquals(strNumber, "2.500,00");
        strNumber = decNumberFormatUS.format(2500.121);
        assertEquals(strNumber, "2,500.12");
        strNumber = decNumberFormatDE.format(2500.125);
        assertEquals(strNumber, "2.500,13");
    }

    /**
   * Tests correct rounding of numbers.
   */
    public void testRound() {
        double number = 2500.0 / 1000.0;
        String strNumber = decNumberFormatUS.format(number);
        assertEquals(strNumber, "2.50");
        strNumber = intNumberFormatUS.format(2500.0 / 1000.0);
        assertEquals(strNumber, "3");
        strNumber = intNumberFormatDE.format(2500.10 / 1000.0);
        assertEquals(strNumber, "3");
        strNumber = intNumberFormatDE.format(2499.99 / 1000.0);
        assertEquals(strNumber, "2");
    }

    /**
   * Tests correct rounding of numbers.
   */
    public void testRoundUp() {
        NumberFormat fmt = new SmartDecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US), BigDecimal.ROUND_UP);
        String strNumber = fmt.format(100.021);
        assertEquals("100.03", strNumber);
        strNumber = fmt.format(100.03);
        assertEquals("100.03", strNumber);
        strNumber = fmt.format(-100.023);
        assertEquals("-100.03", strNumber);
        strNumber = fmt.format(-100.030);
        assertEquals("-100.03", strNumber);
    }

    /**
   * Tests correct rounding of numbers.
   */
    public void testRoundDown() {
        NumberFormat fmt = new SmartDecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US), BigDecimal.ROUND_DOWN);
        String strNumber = fmt.format(100.021);
        assertEquals("100.02", strNumber);
        strNumber = fmt.format(-100.020);
        assertEquals("-100.02", strNumber);
        strNumber = fmt.format(-100.023);
        assertEquals("-100.02", strNumber);
        strNumber = fmt.format(-100.030);
        assertEquals("-100.03", strNumber);
    }

    /**
   * Tests correct rounding of numbers.
   */
    public void testRoundHalfUp() {
        NumberFormat fmt = new SmartDecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US), BigDecimal.ROUND_HALF_UP);
        String strNumber = fmt.format(100.024);
        assertEquals("100.02", strNumber);
        strNumber = fmt.format(-100.025);
        assertEquals("-100.03", strNumber);
        strNumber = fmt.format(100.025);
        assertEquals("100.03", strNumber);
        strNumber = fmt.format(100.036);
        assertEquals("100.04", strNumber);
        StopWatch sw = new StopWatch();
        for (int i = 0; i < 100000; i++) {
            String result = fmt.format(1123.125);
            result = fmt.format(-1123.125);
            result = fmt.format(1123.124);
            result = fmt.format(-1123.124);
        }
        System.out.println("took " + sw.stop() + "ms for 100000 loops");
    }

    /**
   * Tests correct rounding of numbers.
   */
    public void testRoundHalfDown() {
        NumberFormat fmt = new SmartDecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US), BigDecimal.ROUND_HALF_DOWN);
        String strNumber = fmt.format(100.025);
        assertEquals("100.02", strNumber);
        strNumber = fmt.format(-100.026);
        assertEquals("-100.03", strNumber);
        strNumber = fmt.format(-100.025);
        assertEquals("-100.02", strNumber);
        strNumber = fmt.format(100.036);
        assertEquals("100.04", strNumber);
    }

    /**
   * Tests output of invalid numbers.
   */
    public void testNaN() {
        double number = Double.NaN;
        String strNumber = decNumberFormatUS.format(number);
        System.out.println(number + " --> '" + strNumber + "'");
        number = Double.POSITIVE_INFINITY;
        strNumber = decNumberFormatUS.format(number);
        System.out.println(number + " --> '" + strNumber + "'");
    }
}
