package fetzu.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TimeZoneTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetSystemTimeZoneId() {
        String systemTimeZoneId = TimeZoneManager.getJavaTimeZoneId();
        assertTrue(systemTimeZoneId.length() > 0);
    }

    @Test
    public void testSetSystemTimeZone() throws InterruptedException {
        String tz = "America/Los_Angeles";
        TimeZoneManager.setSystemTimeZone(tz);
        String rtz = TimeZoneManager.getJavaTimeZoneId();
        assertEquals(tz, rtz);
    }
}
