package unittest.misc;

import static org.junit.Assert.*;
import org.junit.Test;
import enduro.racer.configuration.ConfigParser;

public class ConfigParserTest {

    @Test
    public void testSetUpWithoutParameter() {
        ConfigParser.delete();
        assertTrue(ConfigParser.getInstance() != null);
    }

    @Test
    public void testFileNotFound() {
        ConfigParser c = ConfigParser.getInstance("asd");
        assertTrue(c.fileNotFound());
    }

    @Test
    public void testGuiConfig() {
        String[] s = ConfigParser.getInstance("config.conf").getClientSetup();
        assertEquals("false", s[0]);
        assertEquals("127.0.0.1", s[1]);
        assertEquals("44444", s[2]);
        assertEquals("Start", s[3]);
    }
}
