package de.mindmatters.faces.spring.factory.xml;

import javax.faces.el.PropertyResolver;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * implementation for parsing '<code>property-resolver</code>' tags and
 * creating {@link PropertyResolver} definitions.
 * 
 * @author Andreas Kuhrwahl
 * @deprecated
 * 
 */
final class PropertyResolverBeanDefinitionParser extends AbstractApplicationPluginBeanDefinitionParser {

    /**
     * {@inheritDoc}
     */
    protected Object createConstructorArgumentValue(final AbstractBeanDefinition bd, final ParserContext parserContext) {
        return "#{facesContext.application.propertyResolver}";
    }

    /**
     * {@inheritDoc}
     */
    protected Class pluginClass() {
        return PropertyResolver.class;
    }
}
