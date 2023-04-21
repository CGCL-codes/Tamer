package com.genia.toolbox.web.servlet;

import java.io.File;
import javax.servlet.ServletContextEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.genia.toolbox.spring.initializer.ApplicationContextHolder;
import com.genia.toolbox.spring.initializer.ProcessInitializer;
import com.genia.toolbox.web.io.MutableServletContext;

/**
 * A custom spring <code>ContextLoaderListener</code> that generate its config
 * file from the jar in the classpath.
 */
public class CustomContextLoaderListener extends ContextLoaderListener {

    /**
   * the <code>Log</code> variabe.
   */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomContextLoaderListener.class);

    /**
   * keeping the information if a thread is currently initializing a
   * {@link org.springframework.web.context.WebApplicationContext}.
   */
    private static final ThreadLocal<Boolean> WEB_APPLICATION_CONTEXT_IS_INITIALIZING = new ThreadLocal<Boolean>();

    /**
   * Notification that the web application initialization process is starting.
   * All ServletContextListeners are notified of context initialization before
   * any filter or servlet in the web application is initialized.
   * 
   * @param servletContextEvent
   *          the event that triggers this method.
   */
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        File springConfigFile = null;
        try {
            WEB_APPLICATION_CONTEXT_IS_INITIALIZING.set(Boolean.TRUE);
            springConfigFile = File.createTempFile("spring-config", ".xml");
            ProcessInitializer.init();
            springConfigFile.deleteOnExit();
            ProcessInitializer.init();
            ProcessInitializer.saveConfigFile(springConfigFile);
            ProcessInitializer.freeResources();
            final MutableServletContext mutableServletContext = new MutableServletContext(servletContextEvent.getServletContext());
            mutableServletContext.addResource("/WEB-INF/applicationContext.xml", springConfigFile);
            super.contextInitialized(new ServletContextEvent(mutableServletContext));
            ApplicationContextHolder.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(mutableServletContext));
            springConfigFile.delete();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            WEB_APPLICATION_CONTEXT_IS_INITIALIZING.remove();
        }
    }

    /**
   * returns whether a {@link org.springframework.web.context.WebApplicationContext} is currently initialized by
   * the current thread.
   * 
   * @return whether a {@link org.springframework.web.context.WebApplicationContext} is currently initialized by
   *         the current thread
   */
    public static Boolean isWebApplicationContextInitializing() {
        Boolean res = WEB_APPLICATION_CONTEXT_IS_INITIALIZING.get();
        if (res == null) {
            return Boolean.FALSE;
        }
        return res;
    }
}
