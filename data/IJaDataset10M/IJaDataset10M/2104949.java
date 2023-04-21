package kite.config.xml;

import kite.throttle.ThrottleTemplate;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * <p>
 * Parses <code>&lt;kite:throttle&gt;</code> elements in Spring application
 * context configuration files.
 * </p>
 * 
 * @version $Id$
 * @author Willie Wheeler
 * @since 1.0
 */
class ThrottleParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element elem) {
        return ThrottleTemplate.class;
    }

    @Override
    protected void doParse(Element elem, BeanDefinitionBuilder builder) {
        builder.addConstructorArgValue(elem.getAttribute("limit"));
    }
}
