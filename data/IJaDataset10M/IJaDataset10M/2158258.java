package jmud.config;

import jmud.engine.config.JMudConfig;
import jmud.engine.config.JMudConfigElement;
import junit.framework.TestCase;
import org.junit.Test;

public class JMudConfigTest extends TestCase {

    private JMudConfig config;

    private String testString = "KaiserSose";

    private String testFile = "testing.config";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.config = JMudConfig.getInstance();
    }

    @Test
    public void testContainsConfigElement() {
        this.config.putConfigElement(JMudConfigElement.dbName, this.testString);
        assertTrue(this.config.containsConfigElement(JMudConfigElement.dbName));
    }

    @Test
    public void testGetConfigElement() {
        this.config.putConfigElement(JMudConfigElement.dbName, this.testString);
        assertEquals(this.testString, this.config.getConfigElement(JMudConfigElement.dbName));
    }

    @Test
    public void testRemConfigElement() {
        this.config.putConfigElement(JMudConfigElement.dbName, this.testString);
        this.config.remConfigElement(JMudConfigElement.dbName);
        assertFalse(this.config.containsConfigElement(JMudConfigElement.dbName));
    }

    @Test
    public void testLoadConfig() {
        assertTrue(this.config.loadConfig());
        for (JMudConfigElement jmce : JMudConfigElement.values()) {
            assertTrue(this.config.containsConfigElement(jmce));
        }
    }

    @Test
    public void testLoadConfigString() {
        assertTrue(this.config.loadConfig(this.testFile));
        for (JMudConfigElement jmce : JMudConfigElement.values()) {
            assertTrue(this.config.containsConfigElement(jmce));
        }
        assertEquals(this.testFile, this.config.getUsingFile());
    }

    @Test
    public void testWriteConfigFile() {
        assertTrue(this.config.loadConfig(this.testFile));
        this.config.putConfigElement(JMudConfigElement.dbName, this.testString);
        assertTrue(this.config.writeConfigFile());
        this.config.clearAllElements();
        assertTrue(this.config.loadConfig(this.testFile));
        assertEquals(this.testString, JMudConfigElement.dbName.getCurrentValue());
    }

    @Test
    public void testPrintConfig() {
        fail("Not yet implemented");
    }
}
