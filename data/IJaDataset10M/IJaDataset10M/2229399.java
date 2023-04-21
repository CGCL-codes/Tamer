package gnu.testlet.java2.util.Calendar;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Locale;

public class setTimeZone implements Testlet {

    public void test(TestHarness harness) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        cal.setTime(new Date());
        int hour1 = cal.get(Calendar.HOUR_OF_DAY);
        cal.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        int hour2 = cal.get(Calendar.HOUR_OF_DAY);
        int delta = (hour1 - hour2 + 24) % 24;
        harness.check(delta, 5);
    }
}
