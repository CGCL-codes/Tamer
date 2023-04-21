package net.sf.irunninglog.servlet.formbean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import net.sf.irunninglog.util.ConstantValues;
import net.sf.irunninglog.util.ConversionException;

public class TestRunDataByMonthFormBean extends FormBeanTestCase {

    RunDataByMonthFormBean bean;

    Calendar cal;

    public TestRunDataByMonthFormBean(String name) {
        super(name);
    }

    public void setUp() {
        super.setUp();
        bean = new RunDataByMonthFormBean();
        cal = GregorianCalendar.getInstance();
    }

    public void testNoArgConstructor() {
        assertEquals(cal.get(Calendar.MONTH), bean.getMonth());
        assertEquals(cal.get(Calendar.YEAR), bean.getYear());
    }

    public void testTwoArgConstructor() {
        bean = new RunDataByMonthFormBean(Calendar.JANUARY, 2004);
        assertEquals(Calendar.JANUARY, bean.getMonth());
        assertEquals(2004, bean.getYear());
    }

    public void testGetSetMonth() {
        bean.setMonth(Calendar.DECEMBER);
        assertEquals(Calendar.DECEMBER, bean.getMonth());
        bean.setMonth(Calendar.JULY);
        assertEquals(Calendar.JULY, bean.getMonth());
        bean.setMonth(-1);
        assertEquals(11, bean.getMonth());
    }

    public void testIncrementMonth() {
        for (int i = 0; i < 100; i++) {
            assertEquals(cal.get(Calendar.MONTH), bean.getMonth());
            assertEquals(cal.get(Calendar.YEAR), bean.getYear());
            bean.incrementMonth();
            cal.add(Calendar.MONTH, 1);
        }
    }

    public void testDecrementMonth() {
        for (int i = 0; i < 100; i++) {
            assertEquals(cal.get(Calendar.MONTH), bean.getMonth());
            assertEquals(cal.get(Calendar.YEAR), bean.getYear());
            bean.decrementMonth();
            cal.add(Calendar.MONTH, -1);
        }
    }

    public void testGetSetYear() {
        bean.setYear(2004);
        assertEquals(2004, bean.getYear());
        bean.setYear(1066);
        assertEquals(1066, bean.getYear());
        bean.setYear(1);
        assertEquals(1, bean.getYear());
        bean.setYear(0);
        assertEquals(1, bean.getYear());
        bean.setYear(-2);
        assertEquals(3, bean.getYear());
    }

    public void testPopulate() throws ConversionException {
        bean.setMonth(Calendar.MAY);
        bean.setYear(2005);
        assertNotNull(bean.getWeeks());
        assertEquals(0, bean.getWeeks().size());
        bean.populate(null);
        assertNotNull(bean.getWeeks());
        assertEquals(0, bean.getWeeks().size());
        List days = new ArrayList();
        RunDataFormBean data = new RunDataFormBean();
        days.add(data);
        try {
            bean.populate(days);
            failTest();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
        data.setDate("5/1/2005");
        bean.populate(days);
        assertNotNull(bean.getWeeks());
        assertEquals(5, bean.getWeeks().size());
        WeekFormBean week = (WeekFormBean) ((List) bean.getWeeks()).get(0);
        assertNotNull(week);
        assertNotNull(week.getSunday().getRunData());
        assertNull(week.getMonday().getRunData());
        RunDataFormBean data2 = new RunDataFormBean();
        data2.setDate("5/27/2005");
        days.add(data2);
        bean.populate(days);
        assertNotNull(bean.getWeeks());
        assertEquals(5, bean.getWeeks().size());
        week = (WeekFormBean) ((List) bean.getWeeks()).get(0);
        assertNotNull(week);
        assertNotNull(week.getSunday().getRunData());
        assertNull(week.getMonday().getRunData());
        WeekFormBean week4 = (WeekFormBean) ((List) bean.getWeeks()).get(3);
        assertNotNull(week4);
        assertNotNull(week4.getFriday().getRunData());
        assertNull(week4.getWednesday().getRunData());
        RunDataFormBean data3 = new RunDataFormBean();
        data3.setDate("5/1/2004");
        days.add(data3);
        try {
            bean.populate(days);
            failTest();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    public void testGetDateString() {
        assertNotNull(bean.getDateString());
    }

    public void testGetNextParameters() {
        bean.setMonth(Calendar.JANUARY);
        bean.setYear(2003);
        Map parameters = bean.getNextParameters();
        assertEquals(3, parameters.size());
        assertTrue(parameters.containsKey(ConstantValues.STRING_ACTION));
        assertEquals(ConstantValues.STRING_NEXT, parameters.get(ConstantValues.STRING_ACTION));
        assertTrue(parameters.containsKey(ConstantValues.STRING_MONTH));
        assertEquals(String.valueOf(Calendar.JANUARY), parameters.get(ConstantValues.STRING_MONTH));
        assertTrue(parameters.containsKey(ConstantValues.STRING_YEAR));
        assertEquals(String.valueOf(2003), parameters.get(ConstantValues.STRING_YEAR));
    }

    public void testGetPreviousParameters() {
        bean.setMonth(Calendar.DECEMBER);
        bean.setYear(2001);
        Map parameters = bean.getPreviousParameters();
        assertEquals(3, parameters.size());
        assertTrue(parameters.containsKey(ConstantValues.STRING_ACTION));
        assertEquals(ConstantValues.STRING_PREVIOUS, parameters.get(ConstantValues.STRING_ACTION));
        assertTrue(parameters.containsKey(ConstantValues.STRING_MONTH));
        assertEquals(String.valueOf(Calendar.DECEMBER), parameters.get(ConstantValues.STRING_MONTH));
        assertTrue(parameters.containsKey(ConstantValues.STRING_YEAR));
        assertEquals(String.valueOf(2001), parameters.get(ConstantValues.STRING_YEAR));
    }

    public void testGetWeeks() throws ConversionException {
        List days = new ArrayList();
        bean.setMonth(Calendar.MAY);
        bean.setYear(2005);
        bean.populate(days);
        assertNotNull(bean.getWeeks());
        assertEquals(5, bean.getWeeks().size());
        bean.setMonth(Calendar.JULY);
        bean.setYear(2005);
        bean.populate(days);
        assertNotNull(bean.getWeeks());
        assertEquals(6, bean.getWeeks().size());
        bean.setMonth(Calendar.FEBRUARY);
        bean.setYear(1998);
        bean.populate(days);
        assertNotNull(bean.getWeeks());
        assertEquals(4, bean.getWeeks().size());
    }

    public void testGetNumberOfDaysInMonth() throws Exception {
        Method method = RunDataByMonthFormBean.class.getDeclaredMethod("getNumberOfDaysInMonth", null);
        method.setAccessible(true);
        bean.setMonth(Calendar.JANUARY);
        assertEquals(new Integer(31), method.invoke(bean, null));
        bean.setMonth(Calendar.FEBRUARY);
        bean.setYear(1998);
        assertEquals(new Integer(28), method.invoke(bean, null));
        bean.setYear(1996);
        assertEquals(new Integer(29), method.invoke(bean, null));
        bean.setMonth(Calendar.MARCH);
        assertEquals(new Integer(31), method.invoke(bean, null));
        bean.setMonth(Calendar.APRIL);
        assertEquals(new Integer(30), method.invoke(bean, null));
        bean.setMonth(Calendar.MAY);
        assertEquals(new Integer(31), method.invoke(bean, null));
        bean.setMonth(Calendar.JUNE);
        assertEquals(new Integer(30), method.invoke(bean, null));
        bean.setMonth(Calendar.JULY);
        assertEquals(new Integer(31), method.invoke(bean, null));
        bean.setMonth(Calendar.AUGUST);
        assertEquals(new Integer(31), method.invoke(bean, null));
        bean.setMonth(Calendar.SEPTEMBER);
        assertEquals(new Integer(30), method.invoke(bean, null));
        bean.setMonth(Calendar.OCTOBER);
        assertEquals(new Integer(31), method.invoke(bean, null));
        bean.setMonth(Calendar.NOVEMBER);
        assertEquals(new Integer(30), method.invoke(bean, null));
        bean.setMonth(Calendar.DECEMBER);
        assertEquals(new Integer(31), method.invoke(bean, null));
    }
}
