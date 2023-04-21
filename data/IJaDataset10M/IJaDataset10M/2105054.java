package de.etqw.openranked.helper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class LinxuConfigReaderTest {

    class LinuxConfigReaderMock extends LinuxConfigReader {

        public LinuxConfigReaderMock(String _p) {
            super(_p);
        }

        public void readLine(String _line, Map<String, String> _m) {
            super.readLine(_line, _m);
        }
    }

    @Test
    public void readLine() {
        LinuxConfigReaderMock mock = new LinuxConfigReaderMock("");
        Map<String, String> map = new HashMap<String, String>();
        mock.readLine("", map);
        assertEquals(0, map.size());
        mock.readLine("#", map);
        assertEquals(0, map.size());
        mock.readLine(" #", map);
        assertEquals(0, map.size());
        mock.readLine(" = val", map);
        assertEquals(0, map.size());
        mock.readLine("= val", map);
        assertEquals(0, map.size());
        mock.readLine("key=val", map);
        assertTrue(map.containsKey("key"));
        assertEquals("val", map.get("key"));
        map.clear();
        mock.readLine("key   =    val", map);
        assertTrue(map.containsKey("key"));
        assertEquals("val", map.get("key"));
        mock.readLine("key=val = asd", map);
        assertTrue(map.containsKey("key"));
        assertEquals("val = asd", map.get("key"));
    }
}
