package com.buschmais.maexo.test.mbeans.osgi.compendium;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerNotification;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;
import com.buschmais.maexo.framework.commons.mbean.objectname.ObjectNameFactoryHelper;
import com.buschmais.maexo.mbeans.osgi.compendium.ConfigurationAdminMBean;
import com.buschmais.maexo.mbeans.osgi.compendium.ConfigurationMBean;
import com.buschmais.maexo.mbeans.osgi.compendium.ConfigurationMBeanConstants;
import com.buschmais.maexo.test.Constants;
import com.buschmais.maexo.test.MBeanNotificationListener;
import com.buschmais.maexo.test.MaexoTests;
import com.buschmais.maexo.test.MBeanNotificationListener.NotificationEvent;
import com.buschmais.maexo.test.common.mbeans.MaexoMBeanTests;

/**
 * This class tests <code>ConfigurationAdminMBean</code> functionality.
 *
 * @see MaexoTests
 */
public class ConfigurationAdminMBeanTest extends MaexoMBeanTests {

    private class DictionaryItem {

        private String name;

        private Object mbeanValue;

        private Object dictionaryValue;

        public DictionaryItem(String name, Object value) {
            this(name, value, value);
        }

        public DictionaryItem(String name, Object value, Object expectedValue) {
            this.name = name;
            this.mbeanValue = value;
            this.dictionaryValue = expectedValue;
        }

        /**
		 * @return the name
		 */
        public String getName() {
            return name;
        }

        /**
		 * @return the mbeanValue
		 */
        public Object getMBeanValue() {
            return mbeanValue;
        }

        /**
		 * @return the dictionaryValue
		 */
        public Object getDictionaryValue() {
            return dictionaryValue;
        }
    }

    private static final String PID = "com.buschmais.maexo.test.managedservice";

    private static final String FACTORY_PID = "com.buschmais.maexo.test.managedservicefactory";

    private static final int TIMEOUT = 5;

    private ServiceReference configAdminServiceReference = null;

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        this.configAdminServiceReference = this.bundleContext.getServiceReference(ConfigurationAdmin.class.getName());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void onTearDown() throws Exception {
        super.bundleContext.ungetService(this.configAdminServiceReference);
        super.onTearDown();
    }

    /**
	 * Returns a configuration admin from OSGi container for testing of general
	 * configuration admin functionality.
	 *
	 * @return The configuration admin.
	 */
    private ConfigurationAdmin getConfigurationAdmin() {
        ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) this.bundleContext.getService(this.configAdminServiceReference);
        return configurationAdmin;
    }

    /**
	 * Returns a configuration admin MBean for the given configuration admin.
	 *
	 * @param configurationAdmin
	 *            The configuration admin.
	 * @return The configuration admin MBean.
	 */
    private ConfigurationAdminMBean getConfigurationAdminMBean(ConfigurationAdmin configurationAdmin) {
        ServiceRegistration serviceRegistrationConfigurationAdmin = bundleContext.registerService(ConfigurationAdmin.class.getName(), configurationAdmin, null);
        Object serviceId = serviceRegistrationConfigurationAdmin.getReference().getProperty(org.osgi.framework.Constants.SERVICE_ID);
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(org.osgi.framework.Constants.SERVICE_ID, serviceId);
        ObjectNameFactoryHelper objectNameFactoryHelper = new ObjectNameFactoryHelper(this.bundleContext);
        ObjectName objectName = objectNameFactoryHelper.getObjectName(configurationAdmin, ConfigurationAdmin.class, properties);
        final ConfigurationAdminMBean configurationAdminMBean = (ConfigurationAdminMBean) getMBean(objectName, ConfigurationAdminMBean.class);
        return configurationAdminMBean;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected String[] getTestBundlesNames() {
        return new String[] { Constants.ARTIFACT_SWITCHBOARD, Constants.ARTIFACT_PLATFORM_MBEAN_SERVER, Constants.ARTIFACT_COMMONS_MBEAN, Constants.ARTIFACT_OSGI_COMPENDIUM_MBEAN, Constants.ARTIFACT_OSGI_COMPENDIUM, Constants.ARTIFACT_FELIX_CONFIGADMIN };
    }

    /**
	 * Creates a list of dictionary items for test purposes.
	 *
	 * @return The list of dictionary items.
	 */
    private List<DictionaryItem> createDictionaryItems() {
        List<DictionaryItem> dictionaryItems = new LinkedList<DictionaryItem>();
        dictionaryItems.add(new DictionaryItem("Boolean", new Boolean(true)));
        dictionaryItems.add(new DictionaryItem("ArrayOfBoolean", new Boolean[] { new Boolean(true), new Boolean(false) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveBoolean", new Boolean[] { new Boolean(true), new Boolean(false) }, new boolean[] { true, false }));
        dictionaryItems.add(new DictionaryItem("Byte", Byte.valueOf(Byte.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfByte", new Byte[] { Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveByte", new Byte[] { Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE) }, new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("Character", Character.valueOf(Character.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfCharacter", new Character[] { Character.valueOf(Character.MIN_VALUE), Character.valueOf(Character.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveCharacter", new Character[] { Character.valueOf(Character.MIN_VALUE), Character.valueOf(Character.MAX_VALUE) }, new char[] { Character.MIN_VALUE, Character.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("Double", new Double(Double.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfDouble", new Double[] { new Double(Double.MIN_VALUE), new Double(Double.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveDouble", new Double[] { new Double(Double.MIN_VALUE), new Double(Double.MAX_VALUE) }, new double[] { Double.MIN_VALUE, Double.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("Float", new Float(Float.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfFloat", new Float[] { new Float(Float.MIN_VALUE), new Float(Float.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfFloat", new Float[] { new Float(Float.MIN_VALUE), new Float(Float.MAX_VALUE) }, new float[] { Float.MIN_VALUE, Float.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("Integer", new Integer(Integer.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfInteger", new Integer[] { new Integer(Integer.MIN_VALUE), new Integer(Integer.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveInteger", new Integer[] { new Integer(Integer.MIN_VALUE), new Integer(Integer.MAX_VALUE) }, new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("Long", new Long(Long.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfLong", new Long[] { new Long(Long.MIN_VALUE), new Long(Long.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveLong", new Long[] { new Long(Long.MIN_VALUE), new Long(Long.MAX_VALUE) }, new long[] { Long.MIN_VALUE, Long.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("Short", new Short(Short.MAX_VALUE)));
        dictionaryItems.add(new DictionaryItem("ArrayOfShort", new Short[] { new Short(Short.MIN_VALUE), new Short(Short.MAX_VALUE) }));
        dictionaryItems.add(new DictionaryItem("ArrayOfPrimitiveShort", new Short[] { new Short(Short.MIN_VALUE), new Short(Short.MAX_VALUE) }, new short[] { Short.MIN_VALUE, Short.MAX_VALUE }));
        dictionaryItems.add(new DictionaryItem("String", "String"));
        dictionaryItems.add(new DictionaryItem("ArrayOfString", new String[] { "StringValue 1", "StringValue2" }));
        return dictionaryItems;
    }

    /**
	 * Stores the values from the dictionary items into the MBean identified by
	 * the {@link ObjectName}.
	 *
	 * @param objectName
	 *            The {@link ObjectName}.
	 * @param dictionaryItems
	 *            The dictionary items.
	 * @throws Exception
	 */
    private void setProperties(ObjectName objectName, List<DictionaryItem> dictionaryItems) throws Exception {
        MBeanServerConnection connection = super.getMBeanServerConnection();
        for (DictionaryItem item : dictionaryItems) {
            String name = item.getName();
            Object value = item.getMBeanValue();
            Class<?> valueType = value.getClass();
            connection.invoke(objectName, "set" + name, new Object[] { name, value }, new String[] { String.class.getName(), valueType.getName() });
        }
    }

    /**
	 * Checks if the given values are equal, arrays are handled.
	 *
	 * @param name
	 *            The name of the property.
	 * @param expectedValue
	 *            The expected value.
	 * @param value
	 *            The value to check.
	 */
    @SuppressWarnings("unchecked")
    private void checkPropertyValue(String name, Object expectedValue, Object value) {
        if (expectedValue.getClass().isArray()) {
            int expectedLength = Array.getLength(expectedValue);
            assertEquals(expectedLength, Array.getLength(value));
            for (int i = 0; i < expectedLength; i++) {
                assertEquals("comparism of property " + name + " failed.", Array.get(expectedValue, i), Array.get(value, i));
            }
            expectedValue = new ArrayList(Arrays.asList(expectedValue));
            value = new ArrayList(Arrays.asList(value));
        } else {
            assertEquals("comparism of property " + name + " failed.", expectedValue, value);
        }
    }

    /**
	 * Checks property values obtained from a {@link ConfigurationMBean} against
	 * the given {@link DictionaryItem}s.
	 *
	 * @param objectName
	 *            The {@link ObjectName} of the MBean.
	 * @param dictionaryItems
	 *            The dictionary items.
	 * @throws Exception
	 */
    private void checkMBeanProperties(ObjectName objectName, List<DictionaryItem> dictionaryItems) throws Exception {
        MBeanServerConnection connection = super.getMBeanServerConnection();
        for (DictionaryItem item : dictionaryItems) {
            String name = item.getName();
            Object expectedMBeanValue = item.getMBeanValue();
            Object value = connection.invoke(objectName, "get" + name, new Object[] { name }, new String[] { String.class.getName() });
            this.checkPropertyValue(name, expectedMBeanValue, value);
        }
    }

    /**
	 * Checks property values obtained from a {@link Configuration} against the
	 * given {@link DictionaryItem}s.
	 *
	 * @param properties
	 *            The properties.
	 * @param items
	 *            The dictionary items.
	 */
    private void checkDictionaryProperties(Dictionary<String, Object> properties, List<DictionaryItem> items) {
        for (DictionaryItem item : items) {
            Object expectedValue = item.getDictionaryValue();
            Object value = properties.get(item.getName());
            this.checkPropertyValue(item.getName(), expectedValue, value);
        }
    }

    /**
	 * Does a basic check of the string representations for
	 * {@link ConfigurationMBean#getProperties()}.
	 *
	 * @param configurationMBean
	 *            The MBean.
	 * @param items
	 *            The expected items.
	 */
    private void checkCompositeProperties(ConfigurationMBean configurationMBean, List<DictionaryItem> items) {
        TabularData tabularData = configurationMBean.getProperties();
        for (DictionaryItem item : items) {
            CompositeData compositeData = tabularData.get(new Object[] { item.getName() });
            assertNotNull(compositeData);
            String name = (String) compositeData.get(ConfigurationMBeanConstants.PROPERTY_ITEM_NAME);
            assertEquals(item.getName(), name);
            String typeName = (String) compositeData.get(ConfigurationMBeanConstants.PROPERTY_ITEM_TYPE);
            assertNotNull(typeName);
            String value = (String) compositeData.get(ConfigurationMBeanConstants.PROPERTY_ITEM_VALUE);
            assertNotNull(value);
        }
    }

    /**
	 * Registers a managed service.
	 *
	 * @param queue
	 *            The {@link Queue} which must be used to store received
	 *            {@link ConfigurationEvent}s.
	 * @return The {@link ServiceRegistration} of the managed service.
	 */
    private ServiceRegistration registerManagedService(BlockingQueue<ConfigurationEvent> queue) {
        ManagedService managedService = new ManagedServiceImpl(queue);
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(org.osgi.framework.Constants.SERVICE_PID, PID);
        ServiceRegistration serviceRegistration = super.bundleContext.registerService(ManagedService.class.getName(), managedService, properties);
        return serviceRegistration;
    }

    /**
	 * Tests the life cycle of a configuration for a {@link ManagedService}.
	 *
	 * @param update
	 *            If <code>true</code> the configuration will be updated with
	 *            the values from the dictionary items and the changes will be
	 *            checked.
	 * @throws Exception
	 */
    private void createUpdateDeleteManagedServiceConfiguration(boolean update) throws Exception {
        BlockingQueue<ConfigurationEvent> queue = new LinkedBlockingQueue<ConfigurationEvent>();
        ServiceRegistration serviceRegistration = this.registerManagedService(queue);
        ConfigurationAdminMBean configurationAdminMBean = this.getConfigurationAdminMBean(this.getConfigurationAdmin());
        ObjectName configurationObjectName = configurationAdminMBean.getConfiguration(PID);
        ConfigurationMBean configurationMBean = (ConfigurationMBean) super.getMBean(configurationObjectName, ConfigurationMBean.class);
        ConfigurationEvent configurationEvent = queue.poll(TIMEOUT, TimeUnit.SECONDS);
        assertNotNull(configurationEvent);
        assertNull(configurationEvent.getDictionary());
        if (update) {
            List<DictionaryItem> dictionaryItems = this.createDictionaryItems();
            this.setProperties(configurationObjectName, dictionaryItems);
            configurationMBean.update();
            configurationEvent = queue.poll(TIMEOUT, TimeUnit.SECONDS);
            assertNotNull(configurationEvent);
            assertNotNull(configurationEvent.getDictionary());
            this.checkDictionaryProperties(configurationEvent.getDictionary(), dictionaryItems);
            this.checkMBeanProperties(configurationObjectName, dictionaryItems);
            this.checkCompositeProperties(configurationMBean, dictionaryItems);
        }
        configurationMBean.delete();
        configurationEvent = queue.poll(TIMEOUT, TimeUnit.SECONDS);
        assertNotNull(configurationEvent);
        assertNull(configurationEvent.getDictionary());
        serviceRegistration.unregister();
    }

    /**
	 * Tests creating, updating and deleting a configuration using MBeans.
	 */
    public void test_managedService_createUpdateDelete() throws Exception {
        this.createUpdateDeleteManagedServiceConfiguration(true);
    }

    /**
	 * Tests creating and deleting a configuration using MBeans.
	 */
    public void test_managedService_createDelete() throws Exception {
        this.createUpdateDeleteManagedServiceConfiguration(false);
    }

    /**
	 * Registers a managed service factory.
	 *
	 * @param queues
	 *            The queue which must be used to store received
	 *            {@link ConfigurationEvent}s.
	 * @return The {@link ServiceRegistration} of the managed service.
	 */
    private ServiceRegistration registerManagedServiceFactory(BlockingQueue<ConfigurationEvent> queue) {
        ManagedServiceFactory managedServiceFactory = new ManagedServiceFactoryImpl(queue);
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put(org.osgi.framework.Constants.SERVICE_PID, FACTORY_PID);
        ServiceRegistration serviceRegistration = super.bundleContext.registerService(ManagedServiceFactory.class.getName(), managedServiceFactory, properties);
        return serviceRegistration;
    }

    /**
	 * Tests the life cycle of a configuration for a
	 * {@link ManagedServiceFactory}.
	 *
	 * @param update
	 *            If <code>true</code> the configuration will be updated with
	 *            the values from the dictionary items and the changes will be
	 *            checked.
	 * @throws Exception
	 */
    private void createUpdateDeleteManagedServiceFactoryConfiguration(boolean update) throws Exception {
        BlockingQueue<ConfigurationEvent> queue = new LinkedBlockingQueue<ConfigurationEvent>();
        ServiceRegistration serviceRegistration = this.registerManagedServiceFactory(queue);
        try {
            ConfigurationAdminMBean configurationAdminMBean = this.getConfigurationAdminMBean(this.getConfigurationAdmin());
            ObjectName configurationObjectName = configurationAdminMBean.createFactoryConfiguration(FACTORY_PID);
            ConfigurationMBean configurationMBean = (ConfigurationMBean) super.getMBean(configurationObjectName, ConfigurationMBean.class);
            String pid = configurationMBean.getPid();
            ConfigurationEvent configurationEvent = null;
            if (update) {
                List<DictionaryItem> dictionaryItems = this.createDictionaryItems();
                this.setProperties(configurationObjectName, dictionaryItems);
                configurationMBean.update();
                configurationEvent = queue.poll(TIMEOUT, TimeUnit.SECONDS);
                assertNotNull(configurationEvent);
                assertEquals(pid, configurationEvent.getName());
                assertNotNull(configurationEvent.getDictionary());
                this.checkDictionaryProperties(configurationEvent.getDictionary(), dictionaryItems);
                this.checkMBeanProperties(configurationObjectName, dictionaryItems);
                this.checkCompositeProperties(configurationMBean, dictionaryItems);
            }
            configurationMBean.delete();
            configurationEvent = queue.poll(TIMEOUT, TimeUnit.SECONDS);
            assertNotNull(configurationEvent);
            assertNull(configurationEvent.getDictionary());
        } finally {
            serviceRegistration.unregister();
        }
    }

    /**
	 * Tests creating, updating and deleting a configuration using MBeans.
	 */
    public void test_managedServiceFactory_createUpdateDelete() throws Exception {
        this.createUpdateDeleteManagedServiceFactoryConfiguration(true);
    }

    /**
	 * Tests creating and deleting a configuration using MBeans.
	 */
    public void test_managedServiceFactory_createDelete() throws Exception {
        this.createUpdateDeleteManagedServiceFactoryConfiguration(false);
    }

    /**
	 * Checks monitoring the life cycle of a configuration which is not
	 * controlled by the {@link ConfigurationAdminMBean}.
	 * <p>
	 * The test creates, updates and deletes a configuration and test, whether
	 * the corresponding {@link ConfigurationMBean}s are registered and
	 * unregistered.
	 *
	 * @param useManagedServiceFactoryConfiguration
	 *            if <code>true</code> use a managed service factory
	 *            configuration, otherwise a managed service configuration.
	 * @throws Exception
	 *             If an error occurs.
	 */
    private void configurationLifeCycleMonitoring(boolean useManagedServiceFactoryConfiguration) throws Exception {
        MBeanNotificationListener<MBeanServerNotification> notificationListener = new MBeanNotificationListener<MBeanServerNotification>();
        super.registerNotificationListener(notificationListener, new ObjectName(MaexoTests.MBEANSERVERDELEGATE_OBJECTNAME));
        Configuration configuration = null;
        if (useManagedServiceFactoryConfiguration) {
            configuration = this.getConfigurationAdmin().createFactoryConfiguration(FACTORY_PID);
        } else {
            configuration = this.getConfigurationAdmin().getConfiguration(PID);
        }
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(org.osgi.framework.Constants.SERVICE_ID, this.configAdminServiceReference.getProperty(org.osgi.framework.Constants.SERVICE_ID));
        props.put(org.osgi.framework.Constants.SERVICE_PID, configuration.getPid());
        ObjectName expectedObjectName = super.getObjectName(configuration, Configuration.class, props);
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("key", "value");
        configuration.update(properties);
        NotificationEvent<MBeanServerNotification> event = notificationListener.getNotificationEvents().poll(TIMEOUT, TimeUnit.SECONDS);
        assertEquals(MBeanServerNotification.REGISTRATION_NOTIFICATION, event.getNotification().getType());
        assertEquals(expectedObjectName, event.getNotification().getMBeanName());
        configuration.delete();
        event = notificationListener.getNotificationEvents().poll(TIMEOUT, TimeUnit.SECONDS);
        assertNotNull(event);
        assertEquals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION, event.getNotification().getType());
        assertEquals(expectedObjectName, event.getNotification().getMBeanName());
    }

    /**
	 * Checks monitoring the life cycle of a managed service configuration which
	 * is not controlled by the {@link ConfigurationAdminMBean}.
	 */
    public void test_managedServiceConfigurationLifeCycleMonitoring() throws Exception {
        this.configurationLifeCycleMonitoring(false);
    }

    /**
	 * Checks monitoring the life cycle of a managed service factory
	 * configuration which is not controlled by the
	 * {@link ConfigurationAdminMBean}.
	 */
    public void test_managedServiceFactoryConfigurationLifeCycleMonitoring() throws Exception {
        this.configurationLifeCycleMonitoring(true);
    }
}
