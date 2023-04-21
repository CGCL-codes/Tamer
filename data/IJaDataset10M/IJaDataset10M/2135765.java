package javax.time.calendar;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import java.io.Serializable;
import java.util.Locale;
import javax.time.calendar.format.DateTimeFormatterBuilder.TextStyle;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test QuarterOfYear.
 *
 * @author Michael Nascimento Santos
 * @author Stephen Colebourne
 */
@Test
public class TestQuarterOfYear {

    @BeforeMethod
    public void setUp() {
    }

    public void test_interfaces() {
        assertTrue(Enum.class.isAssignableFrom(QuarterOfYear.class));
        assertTrue(Serializable.class.isAssignableFrom(QuarterOfYear.class));
        assertTrue(Comparable.class.isAssignableFrom(QuarterOfYear.class));
    }

    public void test_factory_int_singleton() {
        for (int i = 1; i <= 4; i++) {
            QuarterOfYear test = QuarterOfYear.of(i);
            assertEquals(test.getValue(), i);
            assertSame(QuarterOfYear.of(i), test);
        }
    }

    @Test(expectedExceptions = IllegalCalendarFieldValueException.class)
    public void test_factory_int_valueTooLow() {
        QuarterOfYear.of(0);
    }

    @Test(expectedExceptions = IllegalCalendarFieldValueException.class)
    public void test_factory_int_valueTooHigh() {
        QuarterOfYear.of(5);
    }

    public void test_getText() {
        assertEquals(QuarterOfYear.Q1.getText(TextStyle.SHORT, Locale.US), "Q1");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_getText_nullStyle() {
        QuarterOfYear.Q1.getText(null, Locale.US);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test_getText_nullLocale() {
        QuarterOfYear.Q1.getText(TextStyle.FULL, null);
    }

    public void test_next() {
        assertEquals(QuarterOfYear.Q1.next(), QuarterOfYear.Q2);
        assertEquals(QuarterOfYear.Q2.next(), QuarterOfYear.Q3);
        assertEquals(QuarterOfYear.Q3.next(), QuarterOfYear.Q4);
        assertEquals(QuarterOfYear.Q4.next(), QuarterOfYear.Q1);
    }

    public void test_previous() {
        assertEquals(QuarterOfYear.Q1.previous(), QuarterOfYear.Q4);
        assertEquals(QuarterOfYear.Q2.previous(), QuarterOfYear.Q1);
        assertEquals(QuarterOfYear.Q3.previous(), QuarterOfYear.Q2);
        assertEquals(QuarterOfYear.Q4.previous(), QuarterOfYear.Q3);
    }

    public void test_roll() {
        assertEquals(QuarterOfYear.Q1.roll(-4), QuarterOfYear.Q1);
        assertEquals(QuarterOfYear.Q1.roll(-3), QuarterOfYear.Q2);
        assertEquals(QuarterOfYear.Q1.roll(-2), QuarterOfYear.Q3);
        assertEquals(QuarterOfYear.Q1.roll(-1), QuarterOfYear.Q4);
        assertEquals(QuarterOfYear.Q1.roll(0), QuarterOfYear.Q1);
        assertEquals(QuarterOfYear.Q1.roll(1), QuarterOfYear.Q2);
        assertEquals(QuarterOfYear.Q1.roll(2), QuarterOfYear.Q3);
        assertEquals(QuarterOfYear.Q1.roll(3), QuarterOfYear.Q4);
        assertEquals(QuarterOfYear.Q1.roll(4), QuarterOfYear.Q1);
    }

    public void test_getFirstMonthOfQuarter() {
        assertEquals(QuarterOfYear.Q1.getFirstMonthOfQuarter(), MonthOfYear.JANUARY);
        assertEquals(QuarterOfYear.Q2.getFirstMonthOfQuarter(), MonthOfYear.APRIL);
        assertEquals(QuarterOfYear.Q3.getFirstMonthOfQuarter(), MonthOfYear.JULY);
        assertEquals(QuarterOfYear.Q4.getFirstMonthOfQuarter(), MonthOfYear.OCTOBER);
    }

    public void test_toString() {
        assertEquals(QuarterOfYear.Q1.toString(), "Q1");
        assertEquals(QuarterOfYear.Q2.toString(), "Q2");
        assertEquals(QuarterOfYear.Q3.toString(), "Q3");
        assertEquals(QuarterOfYear.Q4.toString(), "Q4");
    }

    public void test_enum() {
        assertEquals(QuarterOfYear.valueOf("Q4"), QuarterOfYear.Q4);
        assertEquals(QuarterOfYear.values()[0], QuarterOfYear.Q1);
    }
}
