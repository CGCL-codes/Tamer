package com.neusoft.wenjoy.core;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
*
* @author vvenLi@gmail.com
*/
public class ExtNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("bean", new ExtBeanBeanDefinitionParser());
    }
}
