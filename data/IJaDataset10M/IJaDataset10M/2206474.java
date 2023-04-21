package org.piuframework.service;

import java.util.Properties;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piuframework.context.ApplicationContext;
import org.piuframework.context.ServiceContext;
import org.piuframework.context.impl.XMLApplicationContext;
import org.piuframework.service.test.InterceptorInvocationEvent;
import org.piuframework.service.test.TestService;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class InterceptorPropertiesTestCase extends TestCase {

    public static final String CONFIG_REPOSITORY_PATH = "/org/piuframework/service/piu-framework.cfg.xml";

    public static final String SERVICE_CONFIG_NAME = "interceptor-properties-testcase";

    private static final Log log = LogFactory.getLog(InterceptorPropertiesTestCase.class);

    private ServiceFactory serviceFactory;

    public InterceptorPropertiesTestCase(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Properties properties = new Properties();
        properties.put(XMLApplicationContext.PARAM_CONFIG_REPOSITORY_PATH, CONFIG_REPOSITORY_PATH);
        properties.put(ServiceContext.PARAM_SERVICE_CONFIG_NAME, SERVICE_CONFIG_NAME);
        ApplicationContext context = new XMLApplicationContext(properties);
        ServiceContext serviceContext = (ServiceContext) context.getSubContext(ServiceContext.NAME);
        serviceFactory = serviceContext.getServiceFactory();
    }

    public void test() throws Exception {
        log.debug("test invoked");
        TestService service = (TestService) serviceFactory.getService(TestService.class);
        try {
            service.setDummyString("test");
            assertTrue(false);
        } catch (InterceptorInvocationEvent e) {
            assertEquals("defaultValueProp1", e.getConfigProperties().get("test-prop1"));
            assertEquals("ValueProp2", e.getConfigProperties().get("test-prop2"));
        }
        log.debug("test done");
    }
}
