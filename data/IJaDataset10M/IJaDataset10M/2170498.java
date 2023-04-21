package org.springframework.web.context.support;

import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.context.ServletContextAware;

/**
 * BeanPostProcessor implementation that passes the ServletContext to
 * beans that implement the ServletContextAware interface.
 *
 * <p>Web application contexts will automatically register this with their
 * underlying bean factory. Applications do not use this directly.
 *
 * @author Juergen Hoeller
 * @since 12.03.2004
 * @see org.springframework.web.context.ServletContextAware
 * @see org.springframework.web.context.support.XmlWebApplicationContext#postProcessBeanFactory
 */
public class ServletContextAwareProcessor implements BeanPostProcessor {

    protected final Log logger = LogFactory.getLog(getClass());

    private final ServletContext servletContext;

    /**
	 * Create a new ServletContextAwareProcessor for the given context.
	 */
    public ServletContextAwareProcessor(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ServletContextAware) {
            if (this.servletContext == null) {
                throw new IllegalStateException("Cannot satisfy ServletContextAware for bean '" + beanName + "' without ServletContext");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Invoking setServletContext on ServletContextAware bean '" + beanName + "'");
            }
            ((ServletContextAware) bean).setServletContext(this.servletContext);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
