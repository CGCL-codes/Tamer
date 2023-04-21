package org.piuframework.persistence.config;

import java.util.Properties;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piuframework.context.ApplicationContext;
import org.piuframework.context.PersistenceContext;
import org.piuframework.context.impl.XMLApplicationContext;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class MinimalConfigTestCase extends TestCase {

    public static final String CONFIG_REPOSITORY_PATH = "/org/piuframework/persistence/config/piu-framework.cfg.xml";

    public static final String PERSISTENCE_CONFIG_NAME = "minimal-config-testcase";

    private static final Log log = LogFactory.getLog(MinimalConfigTestCase.class);

    private PersistenceContext context;

    public MinimalConfigTestCase(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Properties properties = new Properties();
        properties.put(XMLApplicationContext.PARAM_CONFIG_REPOSITORY_PATH, CONFIG_REPOSITORY_PATH);
        properties.put(PersistenceContext.PARAM_PERSISTENCE_CONFIG_NAME, PERSISTENCE_CONFIG_NAME);
        ApplicationContext applicationContext = new XMLApplicationContext(properties);
        context = (PersistenceContext) applicationContext.getSubContext(PersistenceContext.NAME);
    }

    public void test() throws Exception {
        log.debug("test invoked");
        PersistenceLayerConfig rootConfig = context.getPersistenceLayerConfig();
        assertNotNull(rootConfig);
        assertEquals(PERSISTENCE_CONFIG_NAME, rootConfig.getName());
        PersistenceLayerImplConfig persistenceLayerImplConfig = rootConfig.getPersistenceLayerImplConfig();
        assertNotNull(persistenceLayerImplConfig);
        assertEquals(0, persistenceLayerImplConfig.configNames().size());
        assertEquals("org.piuframework.persistence.hibernate.Hibernate2PersistenceLayer", persistenceLayerImplConfig.getClassName());
        assertNull(rootConfig.getDAOFactoryConfig());
        log.debug("test done");
    }
}
