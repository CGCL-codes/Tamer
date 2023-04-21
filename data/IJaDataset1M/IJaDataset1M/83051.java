package org.xmatthew.spy2servers.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author Matthew Xie
 *
 */
public abstract class ComponentParser extends AbstractSingleBeanDefinitionParser {

    private static final String NAME_ELEMENT = "name";

    protected boolean shouldGenerateId() {
        return false;
    }

    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        if (element.hasAttribute(NAME_ELEMENT)) {
            builder.addPropertyValue(NAME_ELEMENT, element.getAttribute(NAME_ELEMENT));
        }
    }
}
