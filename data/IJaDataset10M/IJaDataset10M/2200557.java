package org.nexopenframework.test.context.junit4;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.nexopenframework.test.context.support.JndiTestExecutionListener;
import org.nexopenframework.test.context.support.PerformanceTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * <p>NexOpen Framework</p>
 *
 * <p>Example of a Simple TestCase where we extend <code>Spring</code> functionally adding
 * suitable <code>TestExecutionListener</code> implementations.</p>
 *
 * @see org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-06-20 18:18:44 +0100 $ 
 * @since 2.0.0.GA
 */
@ContextConfiguration(locations = "/openfrwk-modules-test.xml")
@TestExecutionListeners({ JndiTestExecutionListener.class, PerformanceTestExecutionListener.class })
public class SimpleTestExecutionListenersTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    SimpleService service;

    @Test
    public void simple() {
        assertNotNull(service);
    }
}
