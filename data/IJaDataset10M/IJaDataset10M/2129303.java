package org.rob.confjsflistener.core.properties;

import org.junit.Test;
import org.rob.confjsflistener.BaseTest;
import org.rob.confjsflistener.core.exception.PropertiesConfigurationException;
import org.rob.confjsflistener.multiphaselistener.config.ListenerConfiguration;
import org.rob.confjsflistener.multiphaselistener.config.ListenerConfigurationImpl;
import org.rob.confjsflistener.simplelistener.configuration.SimpleListenerConfiguration;
import org.rob.confjsflistener.simplelistener.configuration.SimpleListenerConfigurationImpl;

/**
 * @author Roberto
 *
 */
public class PropertiesReaderTest extends BaseTest {

    private PropertiesReader<SimpleListenerConfiguration> simpleConfigurationReader = new PropertiesReaderImpl<SimpleListenerConfiguration>();

    private PropertiesReader<ListenerConfiguration> configurationReader = new PropertiesReaderImpl<ListenerConfiguration>();

    @Test
    public void testPropertiesSimpleConfiguration() throws PropertiesConfigurationException {
        SimpleListenerConfiguration simpleListenerConfiguration = new SimpleListenerConfigurationImpl();
        simpleConfigurationReader.loadConfiguration(TEST_BUNDLE_NAME, simpleListenerConfiguration, PropertiesConstants.PROPERTIES_PREFIX, SIMPLE_TEST_1);
        checkSimpleTest1Configuration(simpleListenerConfiguration);
    }

    @Test
    public void testPropertiesExtendedSimpleConfiguration() throws PropertiesConfigurationException {
        TestSimpleConfiguration simpleListenerConfiguration = new TestSimpleConfiguration();
        simpleConfigurationReader.loadConfiguration(TEST_BUNDLE_NAME, simpleListenerConfiguration, PropertiesConstants.PROPERTIES_PREFIX, SIMPLE_TEST_2);
        checkSimpleTest2Configuration(simpleListenerConfiguration);
    }

    @Test
    public void testPropertiesMultipleConfiguration() throws PropertiesConfigurationException {
        ListenerConfiguration listenerConfiguration = new ListenerConfigurationImpl();
        configurationReader.loadConfiguration(TEST_BUNDLE_NAME, listenerConfiguration, PropertiesConstants.PROPERTIES_PREFIX, MULTI_TEST_1);
        checkMultiTest1Configuration(listenerConfiguration);
    }
}
