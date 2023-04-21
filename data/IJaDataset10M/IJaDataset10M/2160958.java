package com.goodcodeisbeautiful.archtea.config;

import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author hata
 *
 */
public class ArchteaConfigImplTest extends TestCase {

    public static Test suite() {
        return new TestSuite(ArchteaConfigImplTest.class);
    }

    ArchteaConfigImpl m_impl;

    protected void setUp() throws Exception {
        super.setUp();
        m_impl = new ArchteaConfigImpl();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testArchteaConfigImplArchteaConfig() {
        Properties props = new Properties();
        props.put("key", "value");
        ArchteaConfigImpl cfg = new ArchteaConfigImpl(props);
        cfg = new ArchteaConfigImpl(cfg);
        assertEquals("value", cfg.getProperty("key"));
        cfg.setProperty("key", "newValue");
        assertEquals("newValue", cfg.getProperty("key"));
    }

    public void testArchteaConfigImplPropertiesArchteaConfig() {
        Properties props = new Properties();
        props.put("key", "value");
        props.put("key2", "value2");
        ArchteaConfigImpl cfg = new ArchteaConfigImpl(props);
        props = new Properties();
        props.put("key", "defaultValue");
        cfg = new ArchteaConfigImpl(props, cfg);
        assertEquals("defaultValue", cfg.getProperty("key"));
        cfg.setProperty("key", "newValue");
        assertEquals("newValue", cfg.getProperty("key"));
        assertEquals("value2", cfg.getProperty("key2"));
        cfg.setProperty("key2", "newValue2");
        assertEquals("newValue2", cfg.getProperty("key2"));
    }

    public void testGetProperty() {
        assertNull(m_impl.getProperty("key"));
    }

    public void testSetProperty() {
        assertNull(m_impl.getProperty("key"));
        m_impl.setProperty("key", "value");
        assertEquals("value", m_impl.getProperty("key"));
    }

    public void testSetPropertyWithDefaultProp() {
        Properties props = new Properties();
        props.put("key", "defaultValue");
        m_impl = new ArchteaConfigImpl(props);
        assertEquals("defaultValue", m_impl.getProperty("key"));
        m_impl.setProperty("key", "value");
        assertEquals("value", m_impl.getProperty("key"));
    }

    public void testGetLong() {
        Properties props = new Properties();
        props.put("key", "1");
        m_impl = new ArchteaConfigImpl(props);
        assertEquals(1, m_impl.getLong("key", -1));
        m_impl.setProperty("key", "value");
        assertEquals(-1, m_impl.getLong("key", -1));
        m_impl.setProperty("key", "" + Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, m_impl.getLong("key", -1));
    }
}
