package de.bea.domingo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import de.bea.domingo.util.DateUtil;
import de.bea.domingo.util.GregorianDate;
import de.bea.domingo.util.GregorianDateTime;

/**
 * @author MarcusT
 */
public final class ItemProxyTest extends BaseItemProxyTest {

    /**
     * @param name the name of the test
     */
    public ItemProxyTest(String name) {
        super(name);
    }

    /**
     * @see de.bea.domingo.BaseItemProxyTest#createDBaseItem()
     * @return DBaseDocument a normal Document
     */
    protected DBaseItem createDBaseItem() {
        DDocument doc = getDatabase().createDocument();
        DBaseItem item = doc.appendItemValue(getItemName());
        return item;
    }

    /**
     * Creates a normal Document and appends d.
     * @param d double
     * @return DBaseDocument a normal Document
     */
    protected DBaseItem createDBaseItem(double d) {
        DDocument doc = getDatabase().createDocument();
        DBaseItem item = doc.appendItemValue(getItemName(), d);
        return item;
    }

    /**
     * Creates a normal document and adds i.
     * @param i  int
     * @return DBaseDocument a normal Document
     */
    protected DBaseItem createDBaseItem(int i) {
        DDocument doc = getDatabase().createDocument();
        DBaseItem item = doc.appendItemValue(getItemName(), i);
        return item;
    }

    /**
     * Creates a normal Document and adds s.
     * @param s string
     * @return DBaseDocument a normal Document
     */
    protected DBaseItem createDBaseItem(String s) {
        DDocument doc = getDatabase().createDocument();
        DBaseItem item = doc.appendItemValue(getItemName(), s);
        return item;
    }

    /**
     * Creates a normal Document.
     * @param d a Date
     * @return DBaseItem a normal Document
     */
    protected DBaseItem createDBaseItem(Calendar d) {
        DDocument doc = getDatabase().createDocument();
        DBaseItem item = doc.appendItemValue(getItemName(), d);
        return item;
    }

    /**
     * Creates a normal Document.
     * @param l a List
     * @return DBaseItem a normal Document
     */
    protected DBaseItem createDBaseItem(List l) {
        DDocument doc = getDatabase().createDocument();
        DBaseItem item = doc.appendItemValue(getItemName(), l);
        doc.save();
        return item;
    }

    /**
     * Tests appending of values to a text list.
     *
     */
    public void testAppendToTextList() {
        System.out.println("-> testAppendToTextList");
        List list = new ArrayList();
        DItem item = (DItem) createDBaseItem(list);
        List before = item.getValues();
        assertNotNull("Value list is null", before);
        assertEquals("size of value list", 1, before.size());
        assertEquals("first value", "", before.get(0));
        String secondValue = "second entry";
        item.appendToTextList(secondValue);
        List after = item.getValues();
        assertEquals("size of value list", 1, after.size());
        assertEquals("first value", secondValue, after.get(0));
        String thirdValue = "second entry";
        item.appendToTextList(thirdValue);
        after = item.getValues();
        assertEquals("size of value list", 2, after.size());
        assertEquals("first value", secondValue, after.get(0));
        assertEquals("second value", thirdValue, after.get(1));
    }

    /**
     * Tests getValueDateTime.
     *
     */
    public void testSetGetValueDateTime() {
        System.out.println("-> testSetGetValueDateTime");
        Calendar correct = new GregorianDate();
        DItem item = (DItem) createDBaseItem(correct);
        Calendar date = null;
        try {
            date = item.getValueDateTime();
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        System.out.println("date1 = " + DateUtil.getDateTimeString(correct));
        System.out.println("date2 = " + DateUtil.getDateTimeString(date));
        assertNotNull("Date value was a valid date, thus should be not null.", date);
        assertEquals("Both dates should be equal.", correct, date);
    }

    /**
     * Tests getValueDateTime.
     */
    public void testSetGetValueDateRange() {
        System.out.println("-> testSetGetValueDateRange");
        final Calendar calendar1 = new GregorianDate(2007, Calendar.MARCH, 3);
        final Calendar calendar2 = new GregorianDate();
        calendar2.add(Calendar.YEAR, 1);
        calendar2.add(Calendar.MONTH, 1);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        final DDocument doc = getDatabase().createDocument();
        doc.replaceItemValue("range", calendar1, calendar2);
        final DItem item = (DItem) doc.getFirstItem("range");
        DDateRange range = item.getValueDateRange();
        final Calendar c1 = range.getFrom();
        final Calendar c2 = range.getTo();
        System.out.println(calendar1);
        System.out.println(c1);
        assertEquals(calendar1, c1);
        assertEquals(calendar2, c2);
    }

    /**
     * Tests getValueInteger.
     */
    public void testSetGetValueInteger() {
        System.out.println("-> testSetGetValueInteger");
        int expected = 12;
        DItem item = (DItem) createDBaseItem(expected);
        int value = item.getValueInteger().intValue();
        assertEquals("Returned value should be " + expected + " and not " + value + ".", expected, value);
    }

    /**
     * Tests getValueDouble.
     */
    public void testSetGetValueDouble() {
        System.out.println("-> testSetGetValueDouble");
        double expected = 12.3;
        DItem item = (DItem) createDBaseItem(expected);
        double value = item.getValueDouble().doubleValue();
        assertEquals("Unexpected returned value", expected, value, 0.001);
    }

    /**
     * Tests getValueString.
     */
    public void testSetGetValueString() {
        System.out.println("-> testSetGetValueString");
        String expected = "someValueXXX";
        DItem item = (DItem) createDBaseItem(expected);
        String value = item.getValueString();
        assertEquals("Unexpected returned value", expected, value);
    }

    /**
     * Tests getValues.
     */
    public void testSetGetValues() {
        System.out.println("-> testSetGetValues");
        {
            String exp1 = "someValue1";
            String exp2 = "someValue2";
            List expList = new ArrayList();
            expList.add(exp1);
            expList.add(exp2);
            DItem item = (DItem) createDBaseItem(expList);
            List values = item.getValues();
            assertEquals("Item should contain the list " + expList + " but contains: " + values, expList, values);
        }
        {
            List dateList = new ArrayList();
            Calendar calendar;
            calendar = new GregorianCalendar();
            calendar = new GregorianDateTime(calendar);
            dateList.add(calendar);
            dateList.add(calendar);
            DItem item = (DItem) createDBaseItem(dateList);
            List values = item.getValues();
            Calendar result1 = (Calendar) values.get(0);
            Calendar result2 = (Calendar) values.get(1);
            result1.setTimeZone(calendar.getTimeZone());
            result2.setTimeZone(calendar.getTimeZone());
            System.out.println("dateRef = " + DateUtil.getDateTimeString(calendar) + " zone: " + calendar.getTimeZone().getID());
            System.out.println("date[0] = " + DateUtil.getDateTimeString(result1) + " zone: " + result1.getTimeZone().getID());
            System.out.println("date[1] = " + DateUtil.getDateTimeString(result2) + " zone: " + result2.getTimeZone().getID());
            assertEquals("first date", calendar, result1);
            assertEquals("second date", calendar, result2);
        }
    }

    /**
     * Tests the method isSummary.
     *
     */
    public void testIsSummary() {
        DBaseItem item = createDBaseItem();
        if (item instanceof DItem) {
            assertTrue("The summary flag of this item should be true.", ((DItem) item).isSummary());
        } else {
            assertTrue("The summary flag of this item should be false.", !((DItem) item).isSummary());
        }
    }

    /**
     * Tests the method setSummary.
     */
    public void testSetSummary() {
        DBaseItem item = createDBaseItem();
        if (item instanceof DItem) {
            assertTrue("The summary flag of this item should indicate true.", ((DItem) item).isSummary());
        } else {
            assertTrue("The summary flag of this item should indicate false.", !((DItem) item).isSummary());
        }
        ((DItem) item).setSummary(false);
        assertTrue("The summary flag of this item should be false.", !((DItem) item).isSummary());
        ((DItem) item).setSummary(true);
        assertTrue("The summary flag of this item should again be true.", ((DItem) item).isSummary());
    }

    /**
     * Tests the method setAuthors.
     */
    public void testSetReaders() {
        DBaseItem item = createDBaseItem();
        if (item instanceof DItem) {
            assertTrue("The summary flag of this item should indicate true.", !((DItem) item).isReaders());
            ((DItem) item).setReaders(true);
            assertTrue("The summary flag of this item should be false.", ((DItem) item).isReaders());
            ((DItem) item).setReaders(false);
            assertTrue("The summary flag of this item should again be true.", !((DItem) item).isReaders());
        }
    }

    /**
     * Tests the method setAuthors.
     */
    public void testSetAuthors() {
        DBaseItem item = createDBaseItem();
        if (item instanceof DItem) {
            assertTrue("The summary flag of this item should indicate true.", !((DItem) item).isAuthors());
            ((DItem) item).setAuthors(true);
            assertTrue("The summary flag of this item should be false.", ((DItem) item).isAuthors());
            ((DItem) item).setAuthors(false);
            assertTrue("The summary flag of this item should again be true.", !((DItem) item).isAuthors());
        }
    }

    /**
     * Tests the method setAuthors.
     */
    public void testSetTimezone() {
        DItem item = (DItem) createDBaseItem();
        TimeZone timezone = new SimpleTimeZone(+1, "CET");
        item.setValueDateTime(timezone);
        assertTrue("The summary flag of this item should indicate true.", !((DItem) item).isAuthors());
        ((DItem) item).setAuthors(true);
        assertTrue("The summary flag of this item should be false.", ((DItem) item).isAuthors());
        ((DItem) item).setAuthors(false);
        assertTrue("The summary flag of this item should again be true.", !((DItem) item).isAuthors());
    }
}
