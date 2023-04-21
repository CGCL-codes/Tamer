package org.opennms.netmgt.provision.detector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.UnknownHostException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.provision.ServiceDetector;
import org.opennms.netmgt.provision.detector.simple.LdapDetector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Donald Desloge
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/detectors.xml" })
public class LdapDetectorTest implements ApplicationContextAware {

    private ApplicationContext m_applicationContext;

    @Test
    public void testMyDetector() throws UnknownHostException {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        m_applicationContext = applicationContext;
    }

    @SuppressWarnings("unused")
    private LdapDetector getDetector(Class<? extends ServiceDetector> detectorClass) {
        Object bean = m_applicationContext.getBean(detectorClass.getName());
        assertNotNull(bean);
        assertTrue(detectorClass.isInstance(bean));
        return (LdapDetector) bean;
    }
}
