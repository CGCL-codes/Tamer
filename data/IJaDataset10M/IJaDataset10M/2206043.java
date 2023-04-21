package org.impalaframework.module.beanset;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import junit.framework.TestCase;
import org.impalaframework.module.beanset.BeanSetNode;
import org.impalaframework.module.beanset.RecordingImportingBeanDefinitionDocumentReader;
import org.impalaframework.module.beanset.impl.Bean1;
import org.impalaframework.module.beanset.impl.Bean2;
import org.impalaframework.module.beanset.impl.Bean3;
import org.impalaframework.module.beanset.impl.Bean4;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Test for <code>RecordingImportingBeanDefinitionDocumentReader</code>
 * @author Phil Zoio
 */
public class ImportingBeanSetTest extends TestCase {

    public void test() throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("beanset/beanset.properties"));
        GenericApplicationContext context = createContext(properties, "imported-context.xml");
        assertTrue(context.getBean("bean1") instanceof Bean1);
        assertTrue(context.getBean("bean2") instanceof Bean2);
        assertTrue(context.getBean("bean3") instanceof Bean3);
        properties.setProperty("bean2and3", "alternative-context.xml");
        context = createContext(properties, "alternative-context.xml");
        assertTrue(context.getBean("bean1") instanceof Bean1);
        assertTrue(context.getBean("bean2") instanceof Bean4);
        try {
            context.getBean("bean3");
            fail();
        } catch (NoSuchBeanDefinitionException e) {
        }
    }

    protected GenericApplicationContext createContext(final Properties properties, String expectedResource) {
        GenericApplicationContext context = new GenericApplicationContext();
        final RecordingImportingBeanDefinitionDocumentReader documentReader = new RecordingImportingBeanDefinitionDocumentReader(properties);
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context) {

            @Override
            protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
                return documentReader;
            }
        };
        xmlReader.loadBeanDefinitions(new ClassPathResource("beanset/beanset-context.xml"));
        List<BeanSetNode> nodes = documentReader.getTopLevelNodes();
        System.out.println(documentReader);
        assertEquals(1, nodes.size());
        final BeanSetNode topLevel = nodes.get(0);
        Set<BeanSetNode> children = topLevel.getChildren();
        assertEquals(1, children.size());
        BeanSetNode next = children.iterator().next();
        assertEquals(expectedResource, next.getResource());
        assertNotNull(topLevel.getChildNode(expectedResource));
        return context;
    }
}
