package com.google.ical.values;

import com.google.ical.values.DateTimeValueImpl;
import com.google.ical.values.DateValueImpl;
import junit.framework.TestCase;
import java.util.TimeZone;

/**
 * @author mikesamuel+svn@gmail.com (Mike Samuel)
 */
public class RDateListTest extends TestCase {

    private static final TimeZone PST = TimeZone.getTimeZone("America/Los_Angeles");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertEquals(-8 * 60 * 60 * 1000, PST.getRawOffset());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRDateListParsing() throws Exception {
        RDateList rd1 = new RDateList("RDATE:20060412", PST);
        RDateList rd2 = new RDateList("RDATE:20060412T120000", PST);
        RDateList rd3 = new RDateList("RDATE:20060412T120000,20060413T153000Z", PST);
        assertEquals(1, rd1.getDatesUtc().length);
        assertEquals(new DateValueImpl(2006, 4, 12), rd1.getDatesUtc()[0]);
        assertEquals(1, rd2.getDatesUtc().length);
        assertEquals(new DateTimeValueImpl(2006, 4, 12, 19, 0, 0), rd2.getDatesUtc()[0]);
        assertEquals(2, rd3.getDatesUtc().length);
        assertEquals(new DateTimeValueImpl(2006, 4, 12, 19, 0, 0), rd3.getDatesUtc()[0]);
        assertEquals(new DateTimeValueImpl(2006, 4, 13, 15, 30, 0), rd3.getDatesUtc()[1]);
    }

    public void testRDateListParsingWithExplicitTzid() throws Exception {
        RDateList rd = new RDateList("RDATE;TZID=\"America/New_York\":20060412T120000", PST);
        assertEquals(1, rd.getDatesUtc().length);
        assertEquals(new DateTimeValueImpl(2006, 4, 12, 16, 0, 0), rd.getDatesUtc()[0]);
    }

    public void testRDateIcal() throws Exception {
        RDateList rd1 = new RDateList("RDATE:20060412", PST);
        RDateList rd2 = new RDateList("RDATE:20060412T120000", PST);
        RDateList rd3 = new RDateList("rdate:20060412T120000,20060413T153000", PST);
        RDateList rd4 = new RDateList("RDATE;TZID=\"America/New_York\":20060412T120000", PST);
        assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE:20060412", rd1.toIcal());
        assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE-TIME:" + "20060412T190000Z", rd2.toIcal());
        assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE-TIME" + ":20060412T190000Z,20060413T223000Z", rd3.toIcal());
        assertEquals("RDATE;TZID=\"America/New_York\";VALUE=DATE-TIME" + ":20060412T160000Z", rd4.toIcal());
    }

    public void testRDateIcalWithXParams() throws Exception {
        RDateList rd = new RDateList("RDATE;tzid=\"America/Los_Angeles\";X-FOO=BAR:20060412", PST);
        assertEquals("RDATE;TZID=\"America/Los_Angeles\";VALUE=DATE;X-FOO=BAR:20060412", rd.toIcal());
    }
}
