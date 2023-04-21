package org.impalaframework.spring.service.exporter;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

public class ServiceArrayRegistryExporterTest extends TestCase {

    private ServiceArrayRegistryExporter exporter;

    private DelegatingServiceRegistry registry;

    private DefaultListableBeanFactory beanFactory;

    private String service1 = "myservice1";

    private String service2 = "myservice2";

    private Class<?>[] classes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        classes = new Class[] { String.class };
        exporter = new ServiceArrayRegistryExporter();
        registry = new DelegatingServiceRegistry();
        beanFactory = createMock(DefaultListableBeanFactory.class);
        exporter.setBeanFactory(beanFactory);
        exporter.setModuleDefinition(new SimpleModuleDefinition("module1"));
        exporter.setServiceRegistry(registry);
        exporter.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }

    public void testGetBean() throws Exception {
        exporter.setBeanNames(new String[] { "myBean1", "myBean2" });
        expectService1();
        expectService2();
        replay(beanFactory);
        exporter.init();
        verify(beanFactory);
        assertSame(service1, registry.getService("myBean1", classes, false).getServiceBeanReference().getService());
        assertSame(service2, registry.getService("myBean2", classes, false).getServiceBeanReference().getService());
        exporter.destroy();
        assertNull(registry.getService("myBean1", classes, false));
        assertNull(registry.getService("myBean2", classes, false));
    }

    public void testGetBeanWithExportNames() throws Exception {
        exporter.setBeanNames(new String[] { "myBean1", "myBean2" });
        exporter.setExportNames(new String[] { "myExport1", "myExport2" });
        expectService1();
        expectService2();
        replay(beanFactory);
        exporter.init();
        verify(beanFactory);
        assertSame(service1, registry.getService("myExport1", classes, false).getServiceBeanReference().getService());
        assertSame(service2, registry.getService("myExport2", classes, false).getServiceBeanReference().getService());
        exporter.destroy();
        assertNull(registry.getService("myExport1", classes, false));
        assertNull(registry.getService("myExport2", classes, false));
    }

    private void expectService2() {
        expect(beanFactory.containsBean("&myBean2")).andReturn(false);
        expect(beanFactory.containsBeanDefinition("myBean2")).andReturn(false);
        expect(beanFactory.getBean("myBean2")).andReturn(service2);
    }

    private void expectService1() {
        expect(beanFactory.containsBean("&myBean1")).andReturn(false);
        expect(beanFactory.containsBeanDefinition("myBean1")).andReturn(false);
        expect(beanFactory.getBean("myBean1")).andReturn(service1);
    }
}
