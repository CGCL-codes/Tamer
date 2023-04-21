package orz.mikeneck.server.locrec.utils.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.junit.Test;
import orz.mikeneck.server.locrec.utils.DateUtils;

public class DateUtilsTest {

    private static String[] testTimeZone = { "UTC", "Asia/Tokyo", "hoge", null };

    @Test
    public void testGetTimeZone() {
        assertThat(DateUtils.getTimeZone(null), is("UTC"));
        String timeZone = "timezone";
        assertThat(DateUtils.getTimeZone(timeZone), is(timeZone));
        String[] availableIDs = TimeZone.getAvailableIDs();
        String dfTimeZone0 = availableIDs[0];
        assertThat(DateUtils.getTimeZone(dfTimeZone0), is(dfTimeZone0));
    }

    @Test
    public void testGetSimpleDateFormat() {
        List<String> list = Arrays.asList(testTimeZone);
        for (String item : list) {
            SimpleDateFormat format = DateUtils.getSimpleDateFormat(item);
            assertThat(format, is(instanceOf(SimpleDateFormat.class)));
            String timeZone = (item == null) ? "UTC" : item;
            assertThat(format.getTimeZone(), is(TimeZone.getTimeZone(timeZone)));
            assertThat(format.toPattern(), is(DateUtils.DAFULT_TIME_FORMAT));
        }
    }

    @Test
    public void testParseDate() {
        Calendar cal1 = Calendar.getInstance();
        String asiaTokyo = "Asia/Tokyo";
        cal1.setTimeZone(TimeZone.getTimeZone(asiaTokyo));
        cal1.set(2011, 6 - 1, 3, 17, 56, 0);
        Date case1 = cal1.getTime();
        try {
            assertThat(DateUtils.parseDate("2011/6/3-17:56", asiaTokyo).toString(), is(case1.toString()));
        } catch (ParseException e) {
            fail();
        }
        Calendar cal2 = Calendar.getInstance();
        TimeZone utcTime = TimeZone.getTimeZone("UTC");
        cal2.setTimeZone(utcTime);
        cal2.set(2011, 6 - 1, 3, 17, 56, 0);
        Date case2 = cal2.getTime();
        try {
            assertThat(DateUtils.parseDate("2011/6/3-17:56", utcTime).toString(), is(case2.toString()));
        } catch (ParseException e) {
            fail();
        }
        try {
            Date dt = DateUtils.parseDate("Fri Jun 03 17:56:00 JST 2011", "UTC");
            fail();
        } catch (ParseException e) {
            assertTrue(true);
        }
    }
}
