package edu.vt.middleware.gator;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests Spring application context wiring.
 *
 * @author  Middleware Services
 * @version  $Revision: 1421 $
 */
public class WiringTest {

    /** Tests Spring context wiring. */
    @Test
    public void testWiring() {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/WEB-INF/applicationContext.xml", "/WEB-INF/applicationContext-authz.xml", "/WEB-INF/securityContext.xml", "/WEB-INF/gator-servlet.xml" });
        Assert.assertTrue(context.getBeanDefinitionCount() > 0);
    }
}
