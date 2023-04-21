package eu.viitala.tiko;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BasicFiFiTest extends BasicTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _locale = new Locale("fi", "FI");
    }

    /**
	 * Test setting the time fields to earliest or latest point in time.
	 * @throws Exception
	 */
    public void testMinMaxSetting() throws Exception {
        final Date date = sdf.parse("2000-01-01 10:10");
        Tiko tiko = getTiko();
        assertEquals(sdf.parse("2000-01-01 00:10"), tiko.evaluate("k<", date));
        assertEquals(sdf.parse("2000-01-01 23:10"), tiko.evaluate("k>", date));
        assertEquals(sdf.parse("2000-01-01 10:00"), tiko.evaluate("m<", date));
        assertEquals(sdf.parse("2000-01-01 10:59"), tiko.evaluate("m>", date));
        assertEquals(sdf.parse("0001-01-01 10:10"), tiko.evaluate("y<", date));
        assertEquals(sdf.parse("292278994-01-01 10:10"), tiko.evaluate("y>", date));
        List<String> timeFields = Tiko.getTimeFields();
        for (String field : timeFields) {
            if (!"W".equals(field)) {
                Date temp = tiko.evaluate(field + "<", date);
                assertEquals("Error on field: " + field, temp, tiko.evaluate(field + "<", tiko.evaluate(field + ">", temp)));
            }
        }
    }
}
