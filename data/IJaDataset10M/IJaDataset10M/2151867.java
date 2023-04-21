package org.kwantu.zakwantu;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ConfigurationTest {

    private static final Log LOG = LogFactory.getLog(ConfigurationTest.class);

    @Test
    public void simpleTest() throws ConfigurationException {
        ZakwantuConfiguration config = ZakwantuConfiguration.getInstance();
        assertTrue(!config.getM2DBProperties().isEmpty());
        assertTrue(!config.getAppRuntimeDBProperties().isEmpty());
        assertTrue(!config.getApplicationDBDefaultProperties().isEmpty());
        String url = config.getM2DBProperties().getProperty("hibernate.connection.url");
        LOG.info("url=" + url);
        assertTrue(!url.equals("x"));
        System.setProperty("m2.db.hibernate.connection.url", "x");
        url = config.getM2DBProperties().getProperty("hibernate.connection.url");
        assertTrue(url.equals("x"));
        System.clearProperty("m2.db.hibernate.connection.url");
        url = config.getM2DBProperties().getProperty("hibernate.connection.url");
        assertTrue(!url.equals("x"));
        System.clearProperty(ZakwantuApplication.PROPERTY_NAME_APPLICATION);
        System.clearProperty(ZakwantuApplication.PROPERTY_NAME_ARTIFACT_ID);
        assertNull(config.getInitialApplicationProperties());
        System.setProperty(ZakwantuApplication.PROPERTY_NAME_APPLICATION, "SampleApplication1");
        System.setProperty(ZakwantuApplication.PROPERTY_NAME_ARTIFACT_ID, "SampleApplication1");
        assertNotNull(config.getInitialApplicationProperties());
        assertNotNull(config.getInitialApplicationProperties().getName());
        LOG.info(ZakwantuConfiguration.InitialApplicationProperties.NAME + ": " + config.getInitialApplicationProperties().getName());
        System.setProperty(ZakwantuApplication.PROPERTY_NAME_APPLICATION, "AnotherApplication");
        assertEquals(config.getInitialApplicationProperties().getName(), "AnotherApplication");
        System.clearProperty(ZakwantuApplication.PROPERTY_NAME_APPLICATION);
        assertNull(config.getInitialApplicationProperties());
    }
}
