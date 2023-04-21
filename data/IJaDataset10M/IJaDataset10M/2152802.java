package ramon.connector.servlet;

import static org.junit.Assert.*;
import org.junit.Test;
import ramon.util.Pair;

public class TestHttpServletRequest {

    @Test
    public void testGetHttpVersion11() {
        Pair<Integer, Integer> version = ServletHttpRequest.getHttpVersion("HTTP/1.1");
        assertEquals(version.first, Integer.valueOf(1));
        assertEquals(version.second, Integer.valueOf(1));
    }

    @Test
    public void testGetHttpVersion12() {
        Pair<Integer, Integer> version = ServletHttpRequest.getHttpVersion("HTTP/1.2");
        assertEquals(version.first, Integer.valueOf(1));
        assertEquals(version.second, Integer.valueOf(2));
    }

    @Test(expected = RuntimeException.class)
    public void testGetHttpVersionFail() {
        ServletHttpRequest.getHttpVersion("1.1");
    }
}
