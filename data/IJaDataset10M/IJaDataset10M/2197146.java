package com.nokia.ats4.appmodel.plugin;

import com.nokia.ats4.appmodel.MainApplication;
import com.nokia.ats4.appmodel.plugin.testengine.DefaultPlugin;
import com.nokia.ats4.appmodel.plugin.testengine.TestEngineAdapter;
import com.nokia.ats4.appmodel.util.Settings;
import java.io.File;
import java.util.Map;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tuukkki
 */
public class PluginManagerTest {

    private String DEFAULT_PLUGIN_NAME = "Default Plugin";

    private String DEFAULT_PLUGIN_CLASS = "com.nokia.ats4.appmodel.plugin.testengine.DefaultPlugin";

    public PluginManagerTest() {
    }

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configure("./resources/logging.properties");
        Settings.load(MainApplication.FILE_PROPERTIES);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTestEngineAdapter method, of class PluginManager.
     */
    @Test
    public void testLoadClass() throws Exception {
        System.out.println("testLoadClass");
        PluginManager instance = PluginManager.getInstance();
        TestEngineAdapter result = instance.loadClass(TestEngineAdapter.class, DEFAULT_PLUGIN_CLASS);
        assertNotNull(result);
        assertEquals(DEFAULT_PLUGIN_CLASS, result.getClass().getName());
        assertTrue(result instanceof DefaultPlugin);
    }

    @Test
    public void testListPlugins() {
        System.out.println("testListPlugins");
        PluginManager instance = PluginManager.getInstance();
        Map<String, String> plugins = instance.listPlugins();
        assertTrue(plugins.size() == 1);
        String name = plugins.get(DEFAULT_PLUGIN_CLASS);
        assertNotNull(name);
        assertEquals(DEFAULT_PLUGIN_NAME, name);
    }
}
