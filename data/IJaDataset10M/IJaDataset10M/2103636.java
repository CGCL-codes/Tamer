package test.longhall.core;

import java.util.*;
import junit.framework.*;
import longhall.core.*;

public class ConfigurationTests extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ConfigurationTests.class);
    }

    public ConfigurationTests(String s) {
        super(s);
    }

    public void testIterating() throws Exception {
        Configuration config = new Configuration(null);
        Iterator i = config.assignments();
        assertNotNull(i);
        assertTrue("should be empty", !i.hasNext());
        config.putLiteral("prop1", "stringValue");
        config.putLiteral("prop2", "12345");
        config.putLiteral("prop3", "\n");
        i = config.assignments();
        assertNotNull(i);
        int count = 0;
        while (i.hasNext()) {
            i.next();
            count++;
        }
        assertEquals(3, count);
        for (i = config.assignments(); i.hasNext(); ) {
            Assigner a = (Assigner) i.next();
            assertNotNull(a);
            assertTrue(!a.isKnown());
            if ("prop1".equals(a.getProperty())) {
                assertEquals("stringValue", ((LiteralAssigner) a).getValue());
            } else if ("prop2".equals(a.getProperty())) {
                assertEquals("12345", ((LiteralAssigner) a).getValue());
            } else if ("prop3".equals(a.getProperty())) {
                assertEquals("\n", ((LiteralAssigner) a).getValue());
            } else {
                assertTrue("Unknown property name.  Where did that come from?", false);
            }
        }
    }

    public void testLayering() throws Exception {
        Configuration baseConfig = new Configuration(null);
        baseConfig.putLiteral("prop1", "stringValue");
        baseConfig.putLiteral("prop2", "12345");
        baseConfig.putLiteral("prop3", "\n");
        Configuration layer2 = new Configuration(null, baseConfig);
        layer2.putLiteral("prop2", "abcdefg");
        layer2.putLiteral("prop4", "99.999");
        Configuration layer3 = new Configuration(null, layer2);
        layer3.putLiteral("prop1", "stringOverride");
        layer3.putLiteral("prop5", "\t");
        Iterator i = layer3.assignments();
        assertNotNull(i);
        int count = 0;
        while (i.hasNext()) {
            i.next();
            count++;
        }
        assertEquals(5, count);
        for (i = layer3.assignments(); i.hasNext(); ) {
            Assigner a = (Assigner) i.next();
            assertNotNull(a);
            assertTrue(!a.isKnown());
            if ("prop1".equals(a.getProperty())) {
                assertEquals("stringOverride", ((LiteralAssigner) a).getValue());
            } else if ("prop2".equals(a.getProperty())) {
                assertEquals("abcdefg", ((LiteralAssigner) a).getValue());
            } else if ("prop3".equals(a.getProperty())) {
                assertEquals("\n", ((LiteralAssigner) a).getValue());
            } else if ("prop4".equals(a.getProperty())) {
                assertEquals("99.999", ((LiteralAssigner) a).getValue());
            } else if ("prop5".equals(a.getProperty())) {
                assertEquals("\t", ((LiteralAssigner) a).getValue());
            } else {
                assertTrue("Unknown property name.  Where did that come from?", false);
            }
        }
    }
}
