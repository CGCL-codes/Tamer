package net.sf.compositor.util;

import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.compositor.util.StackProbe;

public class ConfigTest extends TestCase {

    private Config config;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(StackProbe.getMyClass());
    }

    public void setUp() {
        final Properties defaults = new Config();
        config = new Config(defaults);
        config.setProperty("int.0", "0");
        defaults.setProperty("int.d.0", "0");
        config.setProperty("int.-1", "-1");
        defaults.setProperty("int.d.-1", "-1");
        config.setProperty("int.999999999", "999999999");
        defaults.setProperty("int.d.999999999", "999999999");
        config.setProperty("bad.int.a", "1.23");
        defaults.setProperty("bad.int.a", "1.23");
        config.setProperty("bad.int.b", "1.23");
        defaults.setProperty("bad.int.b", "123");
        config.setProperty("double.0", "0");
        defaults.setProperty("double.d.0", "0");
        config.setProperty("double.-0.5", "-0.5");
        defaults.setProperty("double.d.-0.5", "-0.5");
        config.setProperty("double.9999.9999", "9999.9999");
        defaults.setProperty("double.d.9999.9999", "9999.9999");
        config.setProperty("bad.double.a", "a");
        defaults.setProperty("bad.double.a", "0.0");
        defaults.setProperty("bad.double.d.a", "0.0");
        config.setProperty("bad.double.b", "b");
        defaults.setProperty("bad.double.d.d", "b");
        config.setProperty("bad.float.a", "a");
        defaults.setProperty("bad.float.a", "0.0f");
        defaults.setProperty("bad.float.d.a", "0.0f");
        config.setProperty("bad.float.b", "b");
        defaults.setProperty("bad.float.d.d", "b");
        config.setProperty("bool.a", "true");
        config.setProperty("bool.b", "false");
    }

    public void testGetDoubleProperty() {
        assertEquals("double.0 not 0.", 0d, config.getDoubleProperty("double.0"), 0d);
        assertEquals("double.0 not 0.", 0d, config.getDoubleProperty("double.0", 0d), 0d);
        assertEquals("double.x not 0.5", 0.5d, config.getDoubleProperty("double.x", 0.5d), 0d);
        assertEquals("double.d.0 not 0.", 0d, config.getDoubleProperty("double.d.0"), 0d);
        assertEquals("double.d.0 not 0.", 0d, config.getDoubleProperty("double.d.0", 0.5d), 0d);
        assertEquals("double.-0.5 not -0.5.", -0.5d, config.getDoubleProperty("double.-0.5"), 0d);
        assertEquals("double.-0.5 not -0.5.", -0.5d, config.getDoubleProperty("double.-0.5", 0.5d), 0d);
        assertEquals("double.d.-0.5 not -0.5.", -0.5d, config.getDoubleProperty("double.d.-0.5"), 0d);
        assertEquals("double.d.-0.5 not -0.5.", -0.5d, config.getDoubleProperty("double.d.-0.5", 0.5d), 0d);
        assertEquals("double.9999.9999 not 9999.9999.", 9999.9999d, config.getDoubleProperty("double.9999.9999"), 0d);
        assertEquals("double.9999.9999 not 9999.9999.", 9999.9999d, config.getDoubleProperty("double.9999.9999", 0.5d), 0d);
        assertEquals("double.d.9999.9999 not 9999.9999.", 9999.9999d, config.getDoubleProperty("double.d.9999.9999"), 0d);
        assertEquals("double.d.9999.9999 not 9999.9999.", 9999.9999d, config.getDoubleProperty("double.d.9999.9999", 0.5d), 0d);
    }

    public void testGetFloatProperty() {
        assertEquals("double.0 not 0.", 0f, config.getFloatProperty("double.0"), 0f);
        assertEquals("double.0 not 0.", 0f, config.getFloatProperty("double.0", 0f), 0f);
        assertEquals("double.x not 0.5", 0.5f, config.getFloatProperty("double.x", 0.5f), 0f);
        assertEquals("double.d.0 not 0.", 0f, config.getFloatProperty("double.d.0"), 0f);
        assertEquals("double.d.0 not 0.", 0f, config.getFloatProperty("double.d.0", 0.5f), 0f);
        assertEquals("double.-0.5 not -0.5.", -0.5f, config.getFloatProperty("double.-0.5"), 0f);
        assertEquals("double.-0.5 not -0.5.", -0.5f, config.getFloatProperty("double.-0.5", 0.5f), 0f);
        assertEquals("double.d.-0.5 not -0.5.", -0.5f, config.getFloatProperty("double.d.-0.5"), 0f);
        assertEquals("double.d.-0.5 not -0.5.", -0.5f, config.getFloatProperty("double.d.-0.5", 0.5f), 0f);
        assertEquals("double.9999.9999 not 9999.9999.", 9999.9999f, config.getFloatProperty("double.9999.9999"), 0f);
        assertEquals("double.9999.9999 not 9999.9999.", 9999.9999f, config.getFloatProperty("double.9999.9999", 0.5f), 0f);
        assertEquals("double.d.9999.9999 not 9999.9999.", 9999.9999f, config.getFloatProperty("double.d.9999.9999"), 0f);
        assertEquals("double.d.9999.9999 not 9999.9999.", 9999.9999f, config.getFloatProperty("double.d.9999.9999", 0.5f), 0f);
    }

    public void testGetIntProperty() {
        assertEquals("int.0 not 0.", 0, config.getIntProperty("int.0"));
        assertEquals("int.0 not 0.", 0, config.getIntProperty("int.0", 99));
        assertEquals("int.x not 99.", 99, config.getIntProperty("int.x", 99, Config.SILENT));
        assertEquals("int.d.0 not 0.", 0, config.getIntProperty("int.d.0"));
        assertEquals("int.d.0 not 0.", 0, config.getIntProperty("int.d.0", 99));
        assertEquals("int.-1 not -1.", -1, config.getIntProperty("int.-1"));
        assertEquals("int.-1 not -1.", -1, config.getIntProperty("int.-1", 99));
        assertEquals("int.d.-1 not -1.", -1, config.getIntProperty("int.d.-1"));
        assertEquals("int.d.-1 not -1.", -1, config.getIntProperty("int.d.-1", 99));
        assertEquals("int.999999999 not 999999999.", 999999999, config.getIntProperty("int.999999999"));
        assertEquals("int.999999999 not 999999999.", 999999999, config.getIntProperty("int.999999999", 99));
        assertEquals("int.d.999999999 not 999999999.", 999999999, config.getIntProperty("int.d.999999999"));
        assertEquals("int.d.999999999 not 999999999.", 999999999, config.getIntProperty("int.d.999999999", 99));
        try {
            config.getIntProperty("double.9999.9999", Config.SILENT);
            fail("Shouldn't have been able to get an int from a double property.");
        } catch (final NumberFormatException e) {
        }
        assertEquals("double.9999.9999 not 99.", 99, config.getIntProperty("double.9999.9999", 99, Config.SILENT));
        try {
            config.getIntProperty("bad.int.a", Config.SILENT);
            fail("Shouldn't have been able to get bad int.");
        } catch (final NumberFormatException e) {
        }
        assertEquals("Bad int A not 99.", 99, config.getIntProperty("bad.int.a", 99, Config.SILENT));
        assertEquals("Bad int B not 123.", 123, config.getIntProperty("bad.int.b", Config.SILENT));
        assertEquals("Bad int B not 123.", 123, config.getIntProperty("bad.int.b", 99, Config.SILENT));
    }

    public void testSetIntProperty() {
        assertNull("Unexpected value for 'sausage'.", config.getProperty("sausage"));
        config.setIntProperty("sausage", 57);
        assertEquals("Wrong value for 'sausage'.", 57, config.getIntProperty("sausage", Config.SILENT));
    }

    public void testSetDoubleProperty() {
        assertNull("Unexpected value for 'sausage'.", config.getProperty("sausage"));
        config.setDoubleProperty("sausage", 57.5d);
        assertEquals("Wrong value for 'sausage'.", 57.5d, config.getDoubleProperty("sausage"), 0d);
    }

    public void testSetFloatProperty() {
        assertNull("Unexpected value for 'sausage'.", config.getProperty("sausage"));
        config.setFloatProperty("sausage", 57.5f);
        assertEquals("Wrong value for 'sausage'.", 57.5f, config.getFloatProperty("sausage"), 0d);
    }

    public void testBadDoubleProperty() {
        assertEquals("Wrong value for 'bad.double.a'.", 0, config.getDoubleProperty("bad.double.a"), 0d);
        assertEquals("Wrong value for 'bad.double.d.a'.", 0, config.getDoubleProperty("bad.double.d.a"), 0d);
        try {
            config.getDoubleProperty("bad.double.b");
            fail("Unexpected success for bad.double.b.");
        } catch (final NumberFormatException x) {
        }
        try {
            config.getDoubleProperty("bad.double.d.b");
            fail("Unexpected success for bad.double.d.b.");
        } catch (final NumberFormatException x) {
        }
    }

    public void testBadFloatProperty() {
        assertEquals("Wrong value for 'bad.float.a'.", 0, config.getFloatProperty("bad.float.a"), 0d);
        assertEquals("Wrong value for 'bad.float.d.a'.", 0, config.getFloatProperty("bad.float.d.a"), 0d);
        try {
            config.getFloatProperty("bad.float.b");
            fail("Unexpected success for bad.float.b.");
        } catch (final NumberFormatException x) {
        }
        try {
            config.getFloatProperty("bad.float.d.b");
            fail("Unexpected success for bad.float.d.b.");
        } catch (final NumberFormatException x) {
        }
    }

    public void testGetBooleanProperty() {
        assertEquals("Wrong bool (1).", true, config.getBooleanProperty("bool.a", false));
        assertEquals("Wrong bool (2).", false, config.getBooleanProperty("bool.b", true));
        assertEquals("Wrong bool (3).", true, config.getBooleanProperty("bool.wtf", true));
        assertEquals("Wrong bool (4).", false, config.getBooleanProperty("bool.wtf", false));
    }
}
