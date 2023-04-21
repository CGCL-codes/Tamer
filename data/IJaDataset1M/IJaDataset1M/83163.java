package org.springframework.webflow.engine.builder.xml;

import java.util.Properties;
import junit.framework.TestCase;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;

public class XmlFlowRegistryFactoryBeanTests extends TestCase {

    private XmlFlowRegistryFactoryBean factoryBean = new XmlFlowRegistryFactoryBean();

    public void testCreateFromLocations() throws Exception {
        ClassPathResource[] locations = new ClassPathResource[] { new ClassPathResource("flow.xml", getClass()) };
        factoryBean.setFlowLocations(locations);
        factoryBean.setBeanFactory(new StaticListableBeanFactory());
        factoryBean.afterPropertiesSet();
        FlowDefinitionRegistry registry = (FlowDefinitionRegistry) factoryBean.getObject();
        assertEquals(1, registry.getFlowDefinitionCount());
        assertEquals("flow", registry.getFlowDefinitions()[0].getId());
    }

    public void testCreateFromDefinitions() throws Exception {
        Properties properties = new Properties();
        properties.put("foo", "classpath:/org/springframework/webflow/engine/builder/xml/flow.xml");
        factoryBean.setFlowDefinitions(properties);
        factoryBean.setBeanFactory(new StaticListableBeanFactory());
        factoryBean.afterPropertiesSet();
        FlowDefinitionRegistry registry = (FlowDefinitionRegistry) factoryBean.getObject();
        assertEquals(1, registry.getFlowDefinitionCount());
        assertEquals("foo", registry.getFlowDefinitions()[0].getId());
    }
}
