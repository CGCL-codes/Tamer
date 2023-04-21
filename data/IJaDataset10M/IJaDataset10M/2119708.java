package eu.viitala.tiko;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class BasicEnGbTest extends BasicTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _locale = new Locale("en", "GB");
    }

    public void testAmPm() {
        try {
            final Date date = sdf.parse("2000-01-01 10:00");
            Tiko tiko = getTiko();
            assertEquals(sdf.parse("2000-01-01 11:00"), tiko.evaluate("h+1", date));
            assertEquals(sdf.parse("2000-01-01 22:00"), tiko.evaluate("a=PM", date));
            assertEquals(sdf.parse("2000-01-01 10:00"), tiko.evaluate("a=PM|a=AM", date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
