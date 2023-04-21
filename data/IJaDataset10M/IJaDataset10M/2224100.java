package org.jsofa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import junit.framework.TestCase;
import org.jsofa.exception.ExpressionParseException;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class TimePointsJUnitTest extends TestCase {

    public TimePointsJUnitTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTimePoints() throws ExpressionParseException {
        TimePoints tp = new TimePoints();
        tp.buildExpression("2007-01-01 00:00:00,2007-2-1 00:00:00,2007-3-1 00:00:00,2007-4-1 00:00:00");
    }

    public void testGetNextDate() throws ExpressionParseException, ParseException {
        TimePoints tp = new TimePoints();
        tp.buildExpression("2007-01-01 00:00:00,2007-2-1 00:00:00,2007-3-1 00:00:00,2007-4-1 00:00:00");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date orgDate = df.parse("2007-2-25 00:00:00");
        Date nextDate = tp.getNextValidDatetime(orgDate);
        System.out.println(nextDate);
        Date expectedDate = df.parse("2007-3-1 00:00:00");
        super.assertEquals(nextDate, expectedDate);
    }
}
