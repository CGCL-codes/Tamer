package com.phloc.holiday.parser.impl;

import junit.framework.Assert;
import org.junit.Test;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.datetime.PDTFactory;
import com.phloc.holiday.HolidayMap;
import com.phloc.holiday.config.Fixed;
import com.phloc.holiday.config.FixedWeekdayRelativeToFixed;
import com.phloc.holiday.config.Holidays;
import com.phloc.holiday.config.Month;
import com.phloc.holiday.config.Weekday;
import com.phloc.holiday.config.When;
import com.phloc.holiday.config.Which;

/**
 * @author Sven
 */
public final class FixedWeekdayRelativeToFixedParserTest {

    private static final FixedWeekdayRelativeToFixedParser s_aParser = FixedWeekdayRelativeToFixedParser.getInstance();

    @Test
    public void testEmpty() {
        final HolidayMap aHolidays = new HolidayMap();
        final Holidays config = new Holidays();
        s_aParser.parse(2011, aHolidays, config);
        Assert.assertTrue("Result is not empty.", aHolidays.isEmpty());
    }

    @Test
    public void testInvalid() {
        final HolidayMap aHolidays = new HolidayMap();
        final Holidays config = new Holidays();
        final FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
        rule.setWhich(Which.FIRST);
        rule.setWeekday(Weekday.MONDAY);
        rule.setWhen(When.BEFORE);
        final Fixed fixed = new Fixed();
        fixed.setDay(Integer.valueOf(29));
        fixed.setMonth(Month.JANUARY);
        rule.setDay(fixed);
        config.getFixedWeekdayRelativeToFixed().add(rule);
        rule.setValidTo(Integer.valueOf(2010));
        s_aParser.parse(2011, aHolidays, config);
        Assert.assertTrue("Result is not empty.", aHolidays.isEmpty());
    }

    @Test
    public void testParserFirstBefore() {
        final HolidayMap aHolidays = new HolidayMap();
        final Holidays config = new Holidays();
        final FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
        rule.setWhich(Which.FIRST);
        rule.setWeekday(Weekday.MONDAY);
        rule.setWhen(When.BEFORE);
        final Fixed fixed = new Fixed();
        fixed.setDay(Integer.valueOf(29));
        fixed.setMonth(Month.JANUARY);
        rule.setDay(fixed);
        config.getFixedWeekdayRelativeToFixed().add(rule);
        s_aParser.parse(2011, aHolidays, config);
        Assert.assertEquals("Wrong number of dates.", 1, aHolidays.size());
        Assert.assertEquals("Wrong date.", PDTFactory.createLocalDate(2011, 1, 24), ContainerHelper.getFirstElement(aHolidays.getAllDates()));
    }

    @Test
    public void testParserSecondBefore() {
        final HolidayMap aHolidays = new HolidayMap();
        final Holidays config = new Holidays();
        final FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
        rule.setWhich(Which.SECOND);
        rule.setWeekday(Weekday.MONDAY);
        rule.setWhen(When.BEFORE);
        final Fixed fixed = new Fixed();
        fixed.setDay(Integer.valueOf(29));
        fixed.setMonth(Month.JANUARY);
        rule.setDay(fixed);
        config.getFixedWeekdayRelativeToFixed().add(rule);
        s_aParser.parse(2011, aHolidays, config);
        Assert.assertEquals("Wrong number of dates.", 1, aHolidays.size());
        Assert.assertEquals("Wrong date.", PDTFactory.createLocalDate(2011, 1, 17), ContainerHelper.getFirstElement(aHolidays.getAllDates()));
    }

    @Test
    public void testParserThirdAfter() {
        final HolidayMap aHolidays = new HolidayMap();
        final Holidays config = new Holidays();
        final FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
        rule.setWhich(Which.THIRD);
        rule.setWeekday(Weekday.MONDAY);
        rule.setWhen(When.AFTER);
        final Fixed fixed = new Fixed();
        fixed.setDay(Integer.valueOf(29));
        fixed.setMonth(Month.JANUARY);
        rule.setDay(fixed);
        config.getFixedWeekdayRelativeToFixed().add(rule);
        s_aParser.parse(2011, aHolidays, config);
        Assert.assertEquals("Wrong number of dates.", 1, aHolidays.size());
        Assert.assertEquals("Wrong date.", PDTFactory.createLocalDate(2011, 2, 14), ContainerHelper.getFirstElement(aHolidays.getAllDates()));
    }

    @Test
    public void testParserFourthAfter() {
        final HolidayMap aHolidays = new HolidayMap();
        final Holidays config = new Holidays();
        final FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
        rule.setWhich(Which.FOURTH);
        rule.setWeekday(Weekday.TUESDAY);
        rule.setWhen(When.AFTER);
        final Fixed fixed = new Fixed();
        fixed.setDay(Integer.valueOf(15));
        fixed.setMonth(Month.MARCH);
        rule.setDay(fixed);
        config.getFixedWeekdayRelativeToFixed().add(rule);
        s_aParser.parse(2011, aHolidays, config);
        Assert.assertEquals("Wrong number of dates.", 1, aHolidays.size());
        Assert.assertEquals("Wrong date.", PDTFactory.createLocalDate(2011, 4, 12), ContainerHelper.getFirstElement(aHolidays.getAllDates()));
    }
}
