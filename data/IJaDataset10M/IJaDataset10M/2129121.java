package org.apache.camel.example.spring.javaconfig;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.javaconfig.Main;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision: 893853 $
 */
public class IntegrationTest extends Assert {

    @Test
    public void testCamelRulesDeployCorrectlyInSpring() throws Exception {
        Main.main("-duration", "2s", "-cc", "org.apache.camel.example.spring.javaconfig.MyRouteConfig");
    }

    @Test
    public void testStartApplicationContext() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/camel-context.xml");
        String[] names = context.getBeanNamesForType(CamelContext.class);
        assertEquals("There should be a camel context ", 1, names.length);
        CamelContext camelContext = (CamelContext) context.getBean(names[0]);
        assertNotNull(camelContext);
        Thread.sleep(2000);
    }
}
