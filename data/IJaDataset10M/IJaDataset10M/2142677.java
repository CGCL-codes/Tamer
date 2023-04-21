package de.mindmatters.faces.spring.factory.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * implementation for parsing '<code>phase</code>' tags and creating
 * {@link de.mindmatters.faces.lifecycle.Phase} definitions.
 * 
 * @author Andreas Kuhrwahl
 * 
 */
final class PhaseBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * {@inheritDoc}
     */
    public BeanDefinition parse(final Element element, final ParserContext parserContext) {
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        BeanDefinitionHolder phaseListener = delegate.parseBeanDefinitionElement(element);
        if (phaseListener != null) {
            delegate.decorateBeanDefinitionIfRequired(element, phaseListener);
            BeanDefinitionReaderUtils.registerBeanDefinition(phaseListener, parserContext.getRegistry());
            parserContext.getReaderContext().fireComponentRegistered(new BeanComponentDefinition(phaseListener));
        }
        return null;
    }
}
