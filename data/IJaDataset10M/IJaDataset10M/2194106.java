package org.piuframework.j2ee.jndi.config;

import java.util.Properties;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piuframework.context.ApplicationContext;
import org.piuframework.context.J2EEContext;
import org.piuframework.context.impl.XMLApplicationContext;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class ImplicitDefaultsTestCase extends TestCase {

    public static final String CONFIG_REPOSITORY_PATH = "/org/piuframework/j2ee/jndi/config/piu-framework.cfg.xml";

    public static final String JNDI_CONFIG_NAME = "implicit-defaults-testcase";

    private static final Log log = LogFactory.getLog(ImplicitDefaultsTestCase.class);

    private J2EEContext context;

    public ImplicitDefaultsTestCase(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Properties properties = new Properties();
        properties.put(XMLApplicationContext.PARAM_CONFIG_REPOSITORY_PATH, CONFIG_REPOSITORY_PATH);
        properties.put(J2EEContext.PARAM_JNDI_CONFIG_NAME, JNDI_CONFIG_NAME);
        ApplicationContext applicationContext = new XMLApplicationContext(properties);
        context = (J2EEContext) applicationContext.getSubContext(J2EEContext.NAME);
    }

    public void test() throws Exception {
        log.debug("test invoked");
        ServiceLocatorConfig rootConfig = context.getServiceLocatorConfig();
        assertNotNull(rootConfig);
        assertEquals(JNDI_CONFIG_NAME, rootConfig.getName());
        InitialContextConfig defaultInitialContextConfig = rootConfig.getDefaultInitialContextConfig();
        assertNotNull(defaultInitialContextConfig);
        assertEquals("test-server", rootConfig.getDefaultInitialContextName());
        assertEquals("test-server", defaultInitialContextConfig.getName());
        assertEquals(InitialContextConfig.DEFAULT_CREATE_STRATEGY, defaultInitialContextConfig.getCreateStrategy());
        log.debug("test done");
    }
}
