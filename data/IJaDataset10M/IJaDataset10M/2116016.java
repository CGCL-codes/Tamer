package org.impalaframework.web.spring.config;

import javax.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * Class which can be used to export the current application context to the {@link ServletContext}
 * using a specified key, as defined using {@link #contextAttribute}.
 * 
 * @author Phil Zoio
 */
public class ApplicationContextExporter implements ServletContextAware, ApplicationContextAware, InitializingBean, DisposableBean {

    private String contextAttribute;

    private ServletContext servletContext;

    private ApplicationContext applicationContext;

    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(contextAttribute, "contextAttribute cannot be null");
        Assert.notNull(applicationContext, "applicationContext cannot be null");
        Assert.notNull(servletContext, "servletContext cannot be null");
        servletContext.setAttribute(contextAttribute, applicationContext);
    }

    public void destroy() throws Exception {
        if (contextAttribute != null) {
            servletContext.removeAttribute(contextAttribute);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
