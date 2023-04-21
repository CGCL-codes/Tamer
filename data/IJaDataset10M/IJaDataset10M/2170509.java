package org.nakedobjects.applib.valueholder;

public class DateTimeTest extends ValueTestCase {

    private DateTime actual;

    protected void setUp() throws Exception {
        super.setUp();
        actual = new DateTime(2000, 2, 1, 10, 59, 30);
    }

    public void testGetDay() {
        assertEquals(1, actual.getDay());
    }

    public void testGetMonth() {
        assertEquals(2, actual.getMonth());
    }

    public void testGetYear() {
        assertEquals(2000, actual.getYear());
    }

    public void testGetMinute() {
        assertEquals(59, actual.getMinute());
    }

    public void testGetHour() {
        assertEquals(10, actual.getHour());
    }

    public void testSaveRestore() throws Exception {
        DateTime timeStamp1 = new DateTime();
        timeStamp1.parseUserEntry("2003-1-4 10:45");
        assertFalse(timeStamp1.isEmpty());
        DateTime timeStamp2 = new DateTime();
        timeStamp2.restoreFromEncodedString(timeStamp1.asEncodedString());
        assertEquals(timeStamp1.longValue(), timeStamp2.longValue());
        assertFalse(timeStamp2.isEmpty());
    }

    public void testSaveRestorOfNull() throws Exception {
        DateTime timeStamp1 = new DateTime();
        timeStamp1.clear();
        assertTrue("DateTime isEmpty", timeStamp1.isEmpty());
        DateTime timeStamp2 = new DateTime();
        timeStamp2.restoreFromEncodedString(timeStamp1.asEncodedString());
        assertTrue(timeStamp2.isEmpty());
    }

    public void testNew() {
        DateTime expected = new DateTime(2003, 8, 17, 21, 30, 25);
        DateTime actual = new DateTime();
        assertEquals(expected, actual);
    }

    public void testNow() {
        DateTime expected = new DateTime(2003, 8, 17, 21, 30, 25);
        DateTime actual = new DateTime();
        actual.reset();
        assertEquals(expected, actual);
    }
}
