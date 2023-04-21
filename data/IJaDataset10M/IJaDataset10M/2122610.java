package jimo.osgi.modules.testbundle.junit;

import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import jimo.osgi.modules.testbundle.TestBundleActivator;
import junit.framework.TestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;

public class ManagedServiceFactoryTestCases extends TestCase {

    private static final String FACTORY_PID = "jimo.osgi.cm.tests.factoryPid";

    protected static final String FACORY_NAME = "JIMO TestBundle managed service factory";

    private static final String TESTPROPERTY = "jimo.cm.testProperty";

    private static final Object TESTPROPERTYVALUE = "TEST_PROPERTY_VALUE";

    private static final long DELAY = 15000;

    private static final String TESTPLUGINPROPERTY = "jimo.cm.testPluginProperty";

    private static final Object TESTPLUGINPROPERTYVALUE = "TEST_PLUGIN_PROPERTY_VALUE";

    private ServiceRegistration registration;

    private ServiceTracker configurationAdminTracker;

    private Map mapUpdates = Collections.synchronizedMap(new HashMap());

    private ManagedServiceFactory serviceFactory;

    BundleContext bundleContext;

    Configuration configuration;

    ServiceRegistration pluginRegistration;

    protected void setUp() throws Exception {
        serviceFactory = new ManagedServiceFactory() {

            public void deleted(String pid) {
            }

            public String getName() {
                return FACORY_NAME;
            }

            public synchronized void updated(String pid, Dictionary properties) throws ConfigurationException {
                if (properties == null) return;
                System.out.println("ManagedServiceFactory updated");
                mapUpdates.put(pid, properties);
                this.notifyAll();
            }
        };
        Dictionary properties = new Hashtable();
        properties.put(Constants.SERVICE_PID, FACTORY_PID);
        bundleContext = TestBundleActivator.getBundleContext();
        registration = bundleContext.registerService(ManagedServiceFactory.class.getName(), serviceFactory, properties);
        configurationAdminTracker = new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), null);
        configurationAdminTracker.open();
        mapUpdates.clear();
    }

    protected void tearDown() throws Exception {
        configurationAdminTracker.close();
        if (configuration != null) configuration.delete();
        configuration = null;
        if (pluginRegistration != null) pluginRegistration.unregister();
        if (registration != null) registration.unregister();
    }

    public void testManagedServiceFactory() throws Exception {
        ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) configurationAdminTracker.getService();
        assertNotNull(configurationAdmin);
        configuration = configurationAdmin.createFactoryConfiguration(FACTORY_PID);
        String pid = configuration.getPid();
        assertEquals(FACTORY_PID, configuration.getFactoryPid());
        Dictionary properties = new Hashtable();
        properties.put(TESTPROPERTY, TESTPROPERTYVALUE);
        synchronized (serviceFactory) {
            System.out.println("Updating ManagedServiceFactory");
            configuration.update(properties);
            serviceFactory.wait(DELAY);
        }
        Object value = configuration.getProperties().get(TESTPROPERTY);
        assertEquals(TESTPROPERTYVALUE, value);
        Dictionary updatedProperties = (Dictionary) mapUpdates.get(configuration.getPid());
        assertNotNull(updatedProperties);
        value = updatedProperties.get(TESTPROPERTY);
        assertEquals(TESTPROPERTYVALUE, value);
        Configuration[] configurations = configurationAdmin.listConfigurations("(" + Constants.SERVICE_PID + "=" + pid + ")");
        assertEquals(1, configurations.length);
        configuration.delete();
        configuration = null;
        configurations = configurationAdmin.listConfigurations("(" + Constants.SERVICE_PID + "=" + pid + ")");
        assertEquals(0, configurations.length);
    }

    public void testConfigurationPlugin() throws IOException {
        ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) configurationAdminTracker.getService();
        assertNotNull(configurationAdmin);
        configuration = configurationAdmin.createFactoryConfiguration(FACTORY_PID);
        String pid = configuration.getPid();
        ConfigurationPlugin plugin0 = new ConfigurationPlugin() {

            public void modifyConfiguration(ServiceReference reference, Dictionary properties) {
                properties.put(TESTPLUGINPROPERTY, TESTPLUGINPROPERTYVALUE);
            }
        };
        Dictionary properties = new Hashtable();
        properties.put(ConfigurationPlugin.CM_TARGET, pid);
        pluginRegistration = bundleContext.registerService(new String[] { ConfigurationPlugin.class.getName() }, plugin0, properties);
        assertEquals(FACTORY_PID, configuration.getFactoryPid());
        properties = new Hashtable();
        configuration.update(properties);
        Object value = configuration.getProperties().get(TESTPLUGINPROPERTY);
        assertEquals(TESTPLUGINPROPERTYVALUE, value);
    }
}
