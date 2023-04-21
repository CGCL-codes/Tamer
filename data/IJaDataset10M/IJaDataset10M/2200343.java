package org.nexopenframework.xml.binding.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.JdkVersion;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of {@link NamespaceHandlerSupport} for dealing with binding features in Spring
 * configuration files</p>
 * 
 * @see org.nexopenframework.xml.binding.config.XStreamBeanDefinitionParser
 * @see org.springframework.beans.factory.xml.NamespaceHandlerSupport
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class BindingNamespaceHandler extends NamespaceHandlerSupport {

    /**logging facility based in Apache Jakarta Commons Logging (Apache JCL)*/
    private static final Log logger = LogFactory.getLog(BindingNamespaceHandler.class);

    /**
	 * <p>Registering custom {@link BeanDefinitionParser} implementations for dealing with <code>binding</code> support
	 * in Spring configuration files</p>
	 * 
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
    public void init() {
        registerJava14DependentParser("marshaller", "org.nexopenframework.xml.binding.config.MarshallerBeanDefinitionParser");
        registerJava14DependentParser("xstream-components", "org.nexopenframework.xml.binding.config.XStreamBeanDefinitionParser");
    }

    /**
	 * <p>Register in <code>J2SE 1.4.x</code> and higher environment</p>
	 * 
	 * @param elementName
	 * @param parserClassName
	 */
    private void registerJava14DependentParser(final String elementName, final String parserClassName) {
        BeanDefinitionParser parser = null;
        if (JdkVersion.isAtLeastJava14()) {
            try {
                final Class parserClass = ClassUtils.forName(parserClassName, getClass().getClassLoader());
                parser = (BeanDefinitionParser) parserClass.newInstance();
            } catch (final Throwable e) {
                logger.error("Unexpected error registering XStream element", e);
                throw new IllegalStateException("Unable to create Java 1.5 dependent parser: " + parserClassName);
            }
        } else {
            parser = new BeanDefinitionParser() {

                public BeanDefinition parse(final Element element, final ParserContext parserContext) {
                    throw new IllegalStateException("Context namespace element '" + elementName + "' and its parser class [" + parserClassName + "] are only available on JDK 1.5 and higher");
                }
            };
        }
        registerBeanDefinitionParser(elementName, parser);
    }
}
