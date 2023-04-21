package org.jete.util;

import junit.framework.TestCase;
import java.util.Date;

/**
 * @version $Id: DateOffsetTest.java,v 1.1 2006/03/05 00:17:13 tdeast Exp $
 */
public class DateOffsetTest extends TestCase {

    public void testDateOffset() {
        Date base = new Date();
        Date date = new DateOffset(base, 1000);
        assertTrue("Invalid offset", date.getTime() == (base.getTime() + 1000));
    }

    public void testNoOffset() {
        Date base = new Date();
        Date date = new DateOffset(base, 0);
        assertTrue("Invalid offset", date.getTime() == base.getTime());
    }

    public void testNoStartDate() {
        Date date = new DateOffset(1000);
        Date base = new Date();
        assertTrue("Invalid offset", date.getTime() > base.getTime());
    }
}
