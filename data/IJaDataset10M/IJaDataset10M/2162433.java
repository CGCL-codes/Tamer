package org.nexopenframework.tasks.config;

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
 * <p>Comment here</p>
 * 
 * @see org.springframework.beans.factory.xml.NamespaceHandlerSupport
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2008-10-18 16:49:24 +0100 $ 
 * @since 0.5.0.GA
 */
public class TaskNamespaceHandler extends NamespaceHandlerSupport {

    /**
	 * <p>Registering the {@link TaskBeanDefinitionParser} into Spring IoC</p>
	 * 
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
    public void init() {
        registerJava14DependentParser("executor", "org.nexopenframework.tasks.config.TaskBeanDefinitionParser");
    }

    /**
	 * <p>Register in <code>J2SE 1.4.x</code> and higher environment</p>
	 * 
	 * @see #registerBeanDefinitionParser(String, BeanDefinitionParser)
	 * @param elementName
	 * @param parserClassName
	 */
    private void registerJava14DependentParser(final String elementName, final String parserClassName) {
        BeanDefinitionParser parser = null;
        if (JdkVersion.isAtLeastJava14()) {
            try {
                final Class parserClass = ClassUtils.forName(parserClassName, getClass().getClassLoader());
                parser = (BeanDefinitionParser) parserClass.newInstance();
            } catch (final Throwable ex) {
                throw new IllegalStateException("Unable to create Java 1.4 dependent parser: " + parserClassName);
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
