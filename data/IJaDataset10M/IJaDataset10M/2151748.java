package org.nexopenframework.deployment.config;

import junit.framework.TestCase;
import org.nexopenframework.deployment.config.ServiceConfigLocator;
import org.nexopenframework.deployment.config.ServiceConfigProcessor;
import org.nexopenframework.services.Service;
import org.nexopenframework.services.ServiceSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ServiceConfigProcessorTest extends TestCase {

    private ApplicationContext context;

    public void testServiceConfigAware() {
        MyService service = (MyService) context.getBean(MyService.class.getName());
        assertEquals("openfrwk.test", service.getDomain());
        assertEquals(MyService.class.getName(), service.getName());
        assertNotNull(service.getObjectName());
        MyService2 service2 = (MyService2) context.getBean(MyService2.class.getName());
        assertEquals("openfrwk.custom", service2.getDomain());
    }

    protected void setUp() {
        GenericApplicationContext gac = new GenericApplicationContext();
        ServiceConfigProcessor processor = new ServiceConfigProcessor(gac.getBeanFactory());
        gac.getBeanFactory().registerSingleton("processor", processor);
        {
            BeanDefinition myService = BeanDefinitionBuilder.rootBeanDefinition(MyService.class).getBeanDefinition();
            BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(myService, MyService.class.getName()), gac);
            BeanDefinition myService2 = BeanDefinitionBuilder.rootBeanDefinition(MyService2.class).getBeanDefinition();
            BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(myService2, MyService2.class.getName()), gac);
            BeanDefinition mockDomainLocator = BeanDefinitionBuilder.rootBeanDefinition(MockDomainLocator.class).getBeanDefinition();
            BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(mockDomainLocator, "MockDomainLocator"), gac);
        }
        gac.refresh();
        this.context = gac;
    }

    public static class MockDomainLocator implements ServiceConfigLocator {

        public String lookupDomain(Service service) {
            return "openfrwk.test";
        }

        public String lookupName(Service service) {
            return service.getClass().getName();
        }
    }

    public static class MyService extends ServiceSupport {
    }

    public static class MyService2 extends ServiceSupport {

        public MyService2() {
            setDomain("openfrwk.custom");
        }
    }
}
