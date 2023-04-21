package org.xmldb.core.configuration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xmldb.core.exceptions.XmlDBRuntimeException;
import static org.junit.Assert.*;
import org.xmldb.core.sessionfactory.SessionFactory;

/**
 *
 * @author Giacomo Stefano Gabriele
 */
public class ConfigurationTest {

    public ConfigurationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Inizio Test --> ConfigurationTest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Fine Test --> ConfigurationTest");
    }

    /**
     * Test of getSessionFactory method, of class Configuration.
     */
    @Test(expected = XmlDBRuntimeException.class)
    public void testGetSessionFactory() {
        System.out.println("test --> getSessionFactory");
        Configuration instance = new Configuration();
        SessionFactory result = instance.getSessionFactory();
        System.out.println("Ottenuto -->" + result);
        assertNull(result);
    }

    /**
     * Test of buildConfiguration method, of class Configuration.
     */
    @Test
    public void testBuildConfiguration() {
        System.out.println("test --> testBuildConfiguration");
        Configuration instance = new Configuration().buildConfiguration();
        SessionFactory result = instance.getSessionFactory();
        System.out.println("Ottenuto -->" + result);
        assertNotNull(result);
    }
}
