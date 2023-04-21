package com.mtgi.analytics.aop.config.v11;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.mtgi.analytics.aop.BehaviorTrackingAdvice;

public class BtAdviceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return BehaviorTrackingAdvice.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        if (element.hasAttribute(BtNamespaceConstants.ATT_TRACKING_MANAGER)) {
            builder.addPropertyReference(BtNamespaceConstants.PROP_TRACKING_MANAGER, element.getAttribute(BtNamespaceConstants.ATT_TRACKING_MANAGER));
            return;
        } else {
            builder.addPropertyReference(BtNamespaceConstants.PROP_TRACKING_MANAGER, "defaultTrackingManager");
        }
    }
}
