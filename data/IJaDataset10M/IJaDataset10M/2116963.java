package org.cheetah.spring.xml;

import org.cheetah.activities.Log;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class LogDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return Log.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String name = element.getAttribute("name");
        String level = element.getAttribute("level");
        bean.addPropertyValue("name", name);
        bean.addPropertyValue("level", level);
    }
}
