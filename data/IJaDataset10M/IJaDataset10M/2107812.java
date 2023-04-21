package org.apache.harmony.sql.tests.javax.sql.rowset.serial;

import java.net.URL;
import javax.sql.rowset.serial.SerialDatalink;
import javax.sql.rowset.serial.SerialException;
import junit.framework.TestCase;

public class SerialDatalinkTest extends TestCase {

    public void testConstructor() throws Exception {
        URL url = new URL("http://www.someurl.com");
        new SerialDatalink(url);
        try {
            new SerialDatalink(null);
            fail("should throw SerialException");
        } catch (SerialException e) {
        }
    }

    public void testGetDatalink() throws Exception {
        URL url = new URL("http://www.someurl.com");
        SerialDatalink sdl = new SerialDatalink(url);
        URL dataLink = sdl.getDatalink();
        assertEquals(url, dataLink);
        assertNotSame(url, dataLink);
    }
}
