package org.programmerplanet.intracollab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;

/**
 * Comment goes here...
 * 
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2009 Joseph Fifield
 */
public class DateRangeTest extends TestCase {

    public void testDateRange_Nulls() {
        try {
            DateRange dateRange = new DateRange(null, new Date());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            DateRange dateRange = new DateRange(new Date(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testDateRange_StartAfterEnd() throws Exception {
        Date start = date("11/28/2009 09:00:00");
        Date end = date("11/28/2009 07:00:00");
        try {
            DateRange dateRange = new DateRange(start, end);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testDateRange() throws Exception {
        Date start = date("11/28/2009 07:00:00");
        Date end = date("11/28/2009 09:00:00");
        DateRange dateRange = new DateRange(start, end);
        assertSame("dateRange.start", start, dateRange.getStart());
        assertSame("dateRange.end", end, dateRange.getEnd());
    }

    private Date date(String str) throws ParseException {
        return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(str);
    }
}
