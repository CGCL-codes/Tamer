package org.doxla.spring.automock2.xml.namespace;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Register a {@link BasicAutoMockBeanDefinitionParser} for each of the Spring AutoMock elements
 * defined in the automock.xsd schema.
 * 
 * @author danoxlade
 */
public class AutoMockNameSpaceHandler extends NamespaceHandlerSupport {

    /**
	 * Register each {@link BasicAutoMockBeanDefinitionParser} with Spring
	 */
    public void init() {
        for (NamespaceElement element : NamespaceElement.values()) {
            registerBeanDefinitionParser(element.name(), new BasicAutoMockBeanDefinitionParser(element));
        }
    }
}
