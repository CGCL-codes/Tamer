package com.ail.coretest.configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.UnknownNamespaceError;

/**
 * JUnit TestCase. 
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:28 $
 * @source $Source: /home/bob/CVSRepository/projects/core/test.jar/com/ail/coretest/configuration/TestWebServicesConfigurationLoader.java,v $
 */
public class TestWebServicesConfigurationLoader extends TestCase {

    private AbstractConfigurationLoader loader = null;

    private String TestNamespace = "TESTNAMESPACE";

    /** Constructs a test case with the given name. */
    public TestWebServicesConfigurationLoader(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestWebServicesConfigurationLoader.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /** utility method to delete the config table from the database.
     */
    protected void dropConfigTable() {
        try {
            loader.deleteConfigurationRepository();
        } catch (Throwable e) {
            if (e.getMessage().indexOf("Unknown table") == 0) {
                System.err.println("ignored: " + e);
            }
        }
    }

    /**
     * Utility method to remove all the test namespaces from the table.
     */
    private void clearNamespaces() {
        try {
            loader.purgeAllConfigurations();
        } catch (Throwable e) {
            if (e.getMessage().indexOf("Unknown table") == 0) {
                System.err.println("ignored: " + e);
            }
        }
    }

    /** Sets up the fixture (run before every test).
     * Get a loader, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        System.setProperty("com.ail.core.configure.loader", "com.ail.core.configure.WebServiceConfigurationLoader");
        System.setProperty("com.ail.core.configure.loaderParam.url", "http://localhost:8080/jboss-net/services/EJBLoader");
        if (loader == null) {
            loader = AbstractConfigurationLoader.loadLoader();
            loader.reset();
        }
        clearNamespaces();
    }

    /** Tears down the fixture, for example, close a network connection. This method is called after a test is executed. */
    protected void tearDown() {
    }

    /**
     * Try to load a configuration when the table does not exist.
     * The JDBC config loader should automatically create the config table
     * if it finds that is does not exist. This test ensures that this is
     * the case.
     */
    public void testLoadWithMissingTable() throws Exception {
        try {
            dropConfigTable();
            loader.loadConfiguration(TestNamespace, new VersionEffectiveDate());
            fail("Loaded an undefined namespace!!!");
        } catch (UnknownNamespaceError e) {
        }
    }

    /**
     * Attempt to load an undefined configuration.
     * loadConfiguration should throw an 'UnknownNamespaceError' if an attempt is
     * made to load the configuration of an undefined namespace.
     * <ul>
     * <li>Attempt to load the config for the namespace 'namespace-that-does-not-exist'.<li>
     * <li>Fail if no exception/error is thrown.</li>
     * <li>Fail if any exception/error other than UnknownNamespaceError is thrown.</li>
     * </ul>
     */
    public void testLoadUndefinedConfiguration() throws Exception {
        try {
            loader.loadConfiguration(TestNamespace, new VersionEffectiveDate());
            fail("Loaded an undefined namespace!!!");
        } catch (UnknownNamespaceError e) {
        }
    }

    /**
     * Test that a sample configuration can be saved.
     */
    public void testSaveConfiguration() throws Exception {
        Configuration config = new Configuration();
        config.setName("Peter");
        config.setVersion("1.0");
        loader.saveConfiguration(TestNamespace, config);
    }

    /**
     * Test that a sample configuration can be saved, and reloaded.
	 */
    public void testSaveAndLoadConfiguration() throws Exception {
        Configuration config;
        config = new Configuration();
        config.setName("Peter");
        config.setVersion("1.0");
        loader.saveConfiguration(TestNamespace, config);
        Thread.sleep(10);
        config = loader.loadConfiguration(TestNamespace, new VersionEffectiveDate());
        assertEquals(config.getName(), "Peter");
        assertEquals(config.getVersion(), "1.0");
    }

    /**
     * Test the history mechanism. Show that a configuration can be save and
     * loaded, then replaced with a new version, which can also be loaded, and
     * then that the old versions can also be reloaded.<p>
	 * <ul>
     * <li>Save a new configuration.</li>
     * <li>Load the configuration, and check that it is the same.</li>
	 * <li>Fail if the configuration was not the same.</li>
     * <li>wait 100 milliseconds (configurations work from date stamps. 1 millisecond should be enough, but we have time!).<li>
     * <li>Change the configuration and save it again (this should create a second persistent record).</li>
     * <li>Load the configuration, and check that the change just made is present.</li>
	 * <li>Fail if the change made is not present.</li>
     * <li>Load the original configuration by specifiying a timestamp before the 1 second delay.</li>
     * <li>Check the this configuration matches the original.</li>
     * <li>Fail if it does not match.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ul>
     */
    public void testSaveLoadSaveLoadLoadOldConfiguration() throws Exception {
        Configuration config = null;
        config = new Configuration();
        config.setName("Paul");
        config.setVersion("1.0");
        loader.saveConfiguration(TestNamespace, config);
        Thread.sleep(10);
        config = null;
        VersionEffectiveDate d = new VersionEffectiveDate();
        config = loader.loadConfiguration(TestNamespace, d);
        assertEquals(config.getName(), "Paul");
        assertEquals(config.getVersion(), "1.0");
        Thread.sleep(2000);
        config.setName("John");
        config.setVersion("2.0");
        loader.saveConfiguration(TestNamespace, config);
        Thread.sleep(10);
        config = null;
        config = loader.loadConfiguration(TestNamespace, new VersionEffectiveDate());
        assertEquals(config.getName(), "John");
        assertEquals(config.getVersion(), "2.0");
        config = null;
        config = loader.loadConfiguration(TestNamespace, d);
        assertEquals(config.getName(), "Paul");
        assertEquals(config.getVersion(), "1.0");
    }
}
