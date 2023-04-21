package nl.escay.monitio.utils;

import static org.junit.Assert.assertEquals;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import nl.escay.monitio.views.Category;
import nl.escay.monitio.views.ReminderView;
import org.junit.Test;

public class CalendarDateTest {

    @Test
    public void firstTest() {
        Date date = new Date(1);
        assertEquals(1, date.getTime());
        int gmtTimeZoneOffsetValue = 1;
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT");
        timeZoneGMT.setRawOffset(gmtTimeZoneOffsetValue * 3600000);
        Calendar today = Calendar.getInstance(timeZoneGMT);
        today.setTime(date);
        assertEquals(1, today.getTime().getTime());
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        assertEquals(-3599999, today.getTime().getTime());
    }

    @Test
    public void secondTest() throws ParseException {
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(df.format(new Date(306280800000l)));
        Date gwtClientDate = new Date(306284400000l);
        System.out.println(gwtClientDate);
        System.out.println("Year: " + gwtClientDate.getYear());
        System.out.println("Day: " + gwtClientDate.getDay());
        System.out.println("Hours: " + gwtClientDate.getHours());
        System.out.println("Minute: " + gwtClientDate.getMinutes());
        Date date = new Date(306280800000l);
        System.out.println(date);
        System.out.println(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");
        System.out.println(sdf.format(date));
        sdf.setTimeZone(timeZoneGMT);
        System.out.println(sdf.format(date));
        Calendar today = Calendar.getInstance(timeZoneGMT);
        today.setTimeInMillis(date.getTime());
        System.out.println(today.getTime());
        System.out.println(today.getTime().getTime());
        System.out.println("Hour of day: " + today.get(Calendar.HOUR_OF_DAY));
        Calendar today2 = Calendar.getInstance(timeZoneGMT);
        today2.setTimeInMillis(0);
        today2.set(1979, 8, 16, 0, 0, 0);
        System.out.println(today2.getTime());
        System.out.println(today2.getTime().getTime());
        System.out.println(sdf.format(today2.getTime().getTime()));
    }

    @Test
    public void importCSVTest() {
        String csvFile = "Name, day, month, year, category\r\n" + "J.R.R. Tolkien, 3, 1, 1892, Birthday\n" + "Marilyn Manson, 5, 1, 1969, Birthday\n\r" + "Rod Stewart, 10, 1, 1945, Birthday\n" + "ADSL Contract renewal, 8, 10, 0, Reminder";
        List<ReminderView> reminders = ConvertUtil.convertCSVFileToReminderViewList(csvFile);
        nl.escay.monitio.clientutils.CalendarDateTest.executeConvertCSVFileAsserts(reminders);
    }
}
