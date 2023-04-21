package javax.time.calendar;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Test ISO DayOfMonth rule.
 *
 * @author Stephen Colebourne
 */
@Test
public class TestISODayOfMonthRule extends AbstractTestDateTimeRule {

    public TestISODayOfMonthRule() {
        super(LocalDate.of(2009, 12, 26), 26);
    }

    @Override
    protected DateTimeRule rule() {
        return ISODateTimeRule.DAY_OF_MONTH;
    }

    public void test_basics() throws Exception {
        DateTimeRule rule = ISODateTimeRule.DAY_OF_MONTH;
        assertEquals(rule.getType(), DateTimeField.class);
        assertEquals(rule.getName(), "DayOfMonth");
        assertEquals(rule.getValueRange(), DateTimeRuleRange.of(1, 28, 31));
        assertEquals(rule.getPeriodUnit(), ISOPeriodUnit.DAYS);
        assertEquals(rule.getPeriodRange(), ISOPeriodUnit.MONTHS);
    }

    public void test_getMaximumValue_Calendrical_june() {
        Calendrical cal = YearMonth.of(2007, MonthOfYear.JUNE);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 30));
    }

    public void test_getMaximumValue_Calendrical_july() {
        Calendrical cal = YearMonth.of(2007, MonthOfYear.JULY);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 31));
    }

    public void test_getMaximumValue_Calendrical_febLeap() {
        Calendrical cal = YearMonth.of(2008, MonthOfYear.FEBRUARY);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 29));
    }

    public void test_getMaximumValue_Calendrical_febNonLeap() {
        Calendrical cal = YearMonth.of(2007, MonthOfYear.FEBRUARY);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 28));
    }

    public void test_getMaximumValue_Calendrical_juneNoYear() {
        Calendrical cal = DateTimeField.of(ISODateTimeRule.MONTH_OF_YEAR, 6);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 30));
    }

    public void test_getMaximumValue_Calendrical_julyNoYear() {
        Calendrical cal = DateTimeField.of(ISODateTimeRule.MONTH_OF_YEAR, 7);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 31));
    }

    public void test_getMaximumValue_Calendrical_febNoYear() {
        Calendrical cal = DateTimeField.of(ISODateTimeRule.MONTH_OF_YEAR, 2);
        assertEquals(rule().getValueRange(cal), DateTimeRuleRange.of(1, 28, 29));
    }

    public void test_getValue_Calendrical_date() {
        Calendrical cal = LocalDate.of(2007, 6, 20);
        assertEquals(rule().getValue(cal), rule().field(20));
    }

    public void test_getValue_Calendrical_dateTime() {
        Calendrical cal = LocalDateTime.of(2007, 6, 20, 12, 30);
        assertEquals(rule().getValue(cal), rule().field(20));
    }

    public void test_getValue_Calendrical_monthDay() {
        Calendrical cal = MonthDay.of(6, 20);
        assertEquals(rule().getValue(cal), rule().field(20));
    }

    public void test_getValue_Calendrical_dateTimeFields() {
        Calendrical cal = DateTimeFields.of(rule(), 20);
        assertEquals(rule().getValue(cal), rule().field(20));
    }
}
