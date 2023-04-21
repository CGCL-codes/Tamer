package org.pustefixframework.webservices.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 
 * @author mleidig
 *
 */
public class WebServiceNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("webservice", new WebServiceBeanDefinitionParser());
    }
}
