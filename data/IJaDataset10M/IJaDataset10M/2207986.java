package com.xpresso.utils.unitTests.dates;

import java.util.Date;
import com.xpresso.utils.date.DateUtils;
import junit.framework.TestCase;

public class DateUtilsTest extends TestCase {

    public void testDateUtils() {
        Date now = new Date();
        System.out.println(DateUtils.formatDateToMysql(now));
    }
}
