package gov.lanl.ockham.service;

import gov.lanl.util.properties.PropertiesUtil;
import junit.framework.TestCase;

public class ServiceRegistryDBTest extends TestCase {

    String pfile = null;

    ServiceRegistryDB registry = null;

    String identifier = "123";

    String text = "<something/>";

    public static final String DB_CONFFILE = "ockham.properties";

    public void setUp() throws Exception {
        registry = new ServiceRegistryDB(PropertiesUtil.loadConfigByCP(DB_CONFFILE));
    }

    public void testPut() throws Exception {
        registry.putRecord(identifier, text, "Collection");
        String result = registry.getRecord(identifier);
        assertEquals(text, result);
        result = registry.getRecord(identifier);
        assertEquals(text, result);
    }

    public void testDelete() throws Exception {
        testPut();
        registry.deleteRecord(identifier);
        try {
            registry.getRecord(identifier);
            assertTrue(false);
        } catch (IdDoesNotExistException ex) {
            assertTrue(true);
        }
    }

    public void testWrongPut() throws Exception {
        try {
            registry.putRecord(identifier, text, "collections");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    public void tearDown() throws Exception {
        registry.deleteRecord(identifier);
    }
}
