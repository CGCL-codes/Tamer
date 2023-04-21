package architecture.ee.component.core;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.MethodInvoker;
import org.springframework.web.context.ContextLoader;
import architecture.common.event.api.EventListener;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.Component;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;
import architecture.common.lifecycle.event.ApplicationPropertyChangeEvent;
import architecture.common.lifecycle.event.StateChangeEvent;
import architecture.common.lifecycle.service.AdminService;
import architecture.common.lifecycle.service.PluginService;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.spring.lifecycle.SpringAdminService;
import architecture.ee.spring.lifecycle.support.SpringLifecycleSupport;

/**
 * @author  donghyuck
 */
public class AdminServiceImpl extends SpringLifecycleSupport implements SpringAdminService {

    /**
	 */
    private ContextLoader contextLoader;

    /**
	 */
    private ServletContext servletContext;

    /**
	 */
    private ConfigurableApplicationContext applicationContext;

    /**
	 */
    private Version version;

    /**
	 */
    private ConfigService configService;

    public AdminServiceImpl() {
        super();
        setName("AdminService");
        this.contextLoader = null;
        this.configService = null;
        this.servletContext = null;
        this.applicationContext = null;
        this.version = new Version(2, 0, 0, Version.ReleaseStatus.Release_Candidate, 1);
    }

    /**
	 * @return
	 */
    public ConfigService getConfigService() {
        return configService;
    }

    protected ConfigurableApplicationContext getBootstrapApplicationContext() {
        return architecture.common.lifecycle.bootstrap.Bootstrap.getBootstrapApplicationContext();
    }

    protected <T> T getBootstrapComponent(Class<T> requiredType) {
        return architecture.common.lifecycle.bootstrap.Bootstrap.getBootstrapComponent(requiredType);
    }

    /**
	 * @param configService
	 */
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public boolean isSetConfigService() {
        if (configService != null) return true;
        return false;
    }

    public boolean isSetApplicationContext() {
        if (applicationContext != null) {
            return applicationContext.isActive();
        }
        return false;
    }

    /**
	 * @param contextLoader
	 */
    public void setContextLoader(ContextLoader contextLoader) {
        this.contextLoader = contextLoader;
    }

    /**
	 * @return
	 */
    public ContextLoader getContextLoader() {
        return contextLoader;
    }

    public boolean isSetContextLoader() {
        if (contextLoader != null) return true;
        return false;
    }

    /**
	 * @return
	 */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
	 * @param servletContext
	 */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        if (getRepository().getState() != State.INITIALIZED) {
            ((RepositoryImpl) getRepository()).setServletContext(servletContext);
        }
    }

    public boolean isSetServletContext() {
        if (servletContext != null) return true;
        return false;
    }

    @Override
    protected void doInitialize() {
        addStateChangeListener(this);
    }

    @Override
    protected void doStart() {
        Thread currentThread = Thread.currentThread();
        ClassLoader oldLoader = currentThread.getContextClassLoader();
        if (AdminHelper.isSetupComplete()) {
            PluginService pluginService = getBootstrapComponent(PluginService.class);
            pluginService.prepare();
        }
        if (isSetServletContext() && isSetContextLoader()) {
            try {
                this.applicationContext = (ConfigurableApplicationContext) getContextLoader().initWebApplicationContext(getServletContext());
                this.applicationContext.start();
            } finally {
                if (oldLoader != null) currentThread.setContextClassLoader(oldLoader);
            }
        }
    }

    @Override
    protected void doStop() {
        if (isSetApplicationContext()) {
            this.applicationContext.stop();
            if (isSetServletContext()) {
                contextLoader.closeWebApplicationContext(getServletContext());
            } else {
                if (applicationContext instanceof org.springframework.context.support.AbstractApplicationContext) ((org.springframework.context.support.AbstractApplicationContext) applicationContext).close();
            }
        }
    }

    @Override
    public void destroy() {
        if (isSetApplicationContext()) {
            if (isSetServletContext()) {
                contextLoader.closeWebApplicationContext(getServletContext());
            } else {
                if (applicationContext instanceof org.springframework.context.support.AbstractApplicationContext) ((org.springframework.context.support.AbstractApplicationContext) applicationContext).close();
            }
        }
    }

    /**
	 * @return
	 */
    public ConfigurableApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public void autowireComponent(Object obj) {
        if (isSetApplicationContext()) {
            getApplicationContext().getAutowireCapableBeanFactory().autowireBeanProperties(obj, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
        }
    }

    public <T> T getComponent(Class<T> requiredType) {
        if (!isSetApplicationContext()) {
            throw new IllegalStateException("");
        }
        if (requiredType == null) {
            throw new ComponentNotFoundException("");
        }
        try {
            return getApplicationContext().getBean(requiredType);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ComponentNotFoundException(e);
        }
    }

    public void refresh() {
        if (!isSetApplicationContext()) {
            throw new IllegalStateException();
        }
        getApplicationContext().refresh();
    }

    /**
	 * @return
	 */
    public Version getVersion() {
        return this.version;
    }

    @EventListener
    public void onEvent(StateChangeEvent event) {
        Object source = event.getSource();
        if (source instanceof Component) {
            if (source instanceof AdminService) {
            } else {
            }
            log.debug(String.format("[%s] %s > %s", ((Component) source).getName(), event.getOldState().toString(), event.getNewState().toString()));
        }
    }

    @EventListener
    public void onEvent(ApplicationPropertyChangeEvent event) {
        log.debug("property changed " + event.getOldValue() + ">" + event.getNewValue());
    }

    public boolean isReady() {
        if (isSetApplicationContext()) {
            return true;
        }
        return false;
    }
}
