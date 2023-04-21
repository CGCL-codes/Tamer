package us.k5n.ical;

import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test cases for Utils.
 * 
 * @author Craig Knudsen, craig@k5n.us
 * @version $Id: UtilsTest.java,v 1.3 2007/12/18 16:03:44 cknudsen Exp $
 * 
 */
public class UtilsTest extends TestCase implements Constants {

    public void setUp() {
    }

    public void testDayOfWeek() {
        int[] wdays = { Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY };
        Calendar c = Calendar.getInstance();
        c.setLenient(true);
        c.set(Calendar.YEAR, 1700);
        while (true) {
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH) + 1;
            int d = c.get(Calendar.DAY_OF_MONTH);
            int javaWeekday = c.get(Calendar.DAY_OF_WEEK);
            int wday = Utils.getDayOfWeek(y, m, d);
            assertTrue("Weekday mismatch for " + m + "/" + d + "/" + y + ": " + "java weekday=" + javaWeekday + ", Utils weekday=" + wday, javaWeekday == wdays[wday]);
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
            if (y >= 2201) break;
        }
    }

    public void testFileExtensionToMime() {
        String mime = Utils.getMimeTypeForExtension("picture.jpg");
        assertTrue("Wrong mime type for jpg: " + mime, mime.equals("image/jpeg"));
        mime = Utils.getMimeTypeForExtension("picture.jpeg");
        assertTrue("Wrong mime type for jpeg: " + mime, mime.equals("image/jpeg"));
        mime = Utils.getMimeTypeForExtension("file.doc");
        assertTrue("Wrong mime type for doc: " + mime, mime.equals("application/msword"));
        mime = Utils.getMimeTypeForExtension("file.xls");
        assertTrue("Wrong mime type for xls: " + mime, mime.equals("application/excel"));
        mime = Utils.getMimeTypeForExtension("file.html");
        assertTrue("Wrong mime type for xls: " + mime, mime.equals("text/html"));
    }

    public static Test suite() {
        return new TestSuite(UtilsTest.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(UtilsTest.class);
    }
}
