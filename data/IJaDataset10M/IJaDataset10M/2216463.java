package org.unitilsnew.core.config;

import org.junit.Before;
import org.junit.Test;
import java.util.Properties;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Ducheyne
 */
public class ConfigurationContainsPropertyTest {

    private Configuration configuration;

    @Before
    public void initialize() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        configuration = new Configuration(properties);
    }

    @Test
    public void found() {
        boolean result = configuration.containsProperty("property");
        assertTrue(result);
    }

    @Test
    public void notFound() {
        boolean result = configuration.containsProperty("xxx");
        assertFalse(result);
    }
}
