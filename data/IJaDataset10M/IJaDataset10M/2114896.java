package com.google.ical.values;

import junit.framework.TestCase;

/**
 * @author mikesamuel+svn@gmail.com (Mike Samuel)
 */
public class VcalRewriterTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRewriteRule() throws Exception {
        assertEquals("RRULE:FREQ=DAILY;UNTIL=20021231T000000Z", VcalRewriter.rewriteRule("RRULE:D1 20021231T000000"));
        assertEquals("RRULE:FREQ=DAILY;INTERVAL=10", VcalRewriter.rewriteRule("RRULE:D10 #0"));
        assertEquals("RRULE:FREQ=WEEKLY;INTERVAL=2;BYDAY=SU", VcalRewriter.rewriteRule("RRULE:W2 SU #0"));
        assertEquals("RRULE:FREQ=WEEKLY;BYDAY=TU,TH;UNTIL=20021031T000000Z", VcalRewriter.rewriteRule("RRULE:W1 TU TH 20021031T000000"));
        assertEquals("RRULE:FREQ=MONTHLY;BYDAY=1TU,3TU", VcalRewriter.rewriteRule("RRULE:MP1 1+ 3+ TU #0"));
        assertEquals("RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=-2FR", VcalRewriter.rewriteRule("RRULE:MP2 2- FR #0"));
        assertEquals("RRULE:FREQ=MONTHLY;BYDAY=FR", VcalRewriter.rewriteRule("RRULE:MP FR #0"));
        assertEquals("RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TH,FR", VcalRewriter.rewriteRule("RRULE:MP2 TH FR"));
        assertEquals("RRULE:FREQ=MONTHLY;INTERVAL=4;BYMONTHDAY=1", VcalRewriter.rewriteRule("RRULE:MD4 1 #0"));
        assertEquals("RRULE:FREQ=MONTHLY;BYMONTHDAY=15,-1", VcalRewriter.rewriteRule("RRULE:MD1 15 1- #0"));
        assertEquals("RRULE:FREQ=MONTHLY;BYMONTHDAY=15,-1", VcalRewriter.rewriteRule("RRULE:MD1 15 LD #0"));
        assertEquals("RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=2;COUNT=10", VcalRewriter.rewriteRule("RRULE:YM4 2 #10"));
        assertEquals("RRULE:FREQ=YEARLY;BYMONTH=6,7,8;COUNT=10", VcalRewriter.rewriteRule("RRULE:YM1 6 7 8 #10"));
        assertEquals("RRULE:FREQ=YEARLY;BYYEARDAY=100", VcalRewriter.rewriteRule("RRULE:YD1 100 #0"));
        assertEquals("RRULE:FREQ=YEARLY;INTERVAL=2;BYYEARDAY=243", VcalRewriter.rewriteRule("RRULE:YD2 243 #0"));
    }

    public void testRewriteDoesntInterfere() throws Exception {
        assertEquals("RRULE:FREQ=YEARLY;INTERVAL=2;BYYEARDAY=243", VcalRewriter.rewriteRule("RRULE:FREQ=YEARLY;INTERVAL=2;BYYEARDAY=243"));
    }
}
