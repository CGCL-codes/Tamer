package se.issi.magnolia.module.blossom.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class BlossomNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("configuration", new BlossomConfigurationBeanDefinitionParser());
    }
}
