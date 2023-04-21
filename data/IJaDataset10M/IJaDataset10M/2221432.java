package architecture.ee.spring.context.impl;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationHelper;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.event.StateChangeEvent;
import architecture.common.lifecycle.service.AdminService;
import architecture.ee.spring.lifecycle.SpringAdminService;

/**
 * @author  donghyuck
 */
public class ApplicationHelperImpl implements ApplicationHelper, ApplicationListener<ApplicationEvent> {

    private Log log = LogFactory.getLog(getClass());

    private ConfigurableApplicationContext applicationContext;

    /**
	 */
    private State state = State.INITIALIZED;

    /**
	 */
    private SpringAdminService adminService;

    protected ApplicationHelperImpl(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.adminService = this.applicationContext.getBean(SpringAdminService.class);
    }

    public ConfigurableApplicationContext getConfigurableApplicationContext() {
        if (adminService.isSetApplicationContext()) return adminService.getApplicationContext(); else return this.applicationContext;
    }

    protected ConfigurableListableBeanFactory getBeanFactory() {
        if (getConfigurableApplicationContext() == null) {
            throw new IllegalStateException("");
        }
        return getConfigurableApplicationContext().getBeanFactory();
    }

    @SuppressWarnings("rawtypes")
    protected void addComponent(String name, Class clazz) {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(clazz);
        getBeanFactory().registerSingleton(name, bd);
    }

    public ConfigService getConfigService() {
        return getAdminService().getConfigService();
    }

    public Repository getRepository() {
        return applicationContext.getBean(Repository.class);
    }

    public boolean isReady() {
        return getAdminService().isReady();
    }

    /**
	 * @return
	 */
    public AdminService getAdminService() {
        return adminService;
    }

    public void autowireComponent(Object obj) {
        if (getConfigurableApplicationContext() != null) getConfigurableApplicationContext().getAutowireCapableBeanFactory().autowireBeanProperties(obj, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
    }

    @SuppressWarnings("unchecked")
    public <T> T createComponent(Class<T> requiredType) {
        if (getConfigurableApplicationContext() != null) return (T) getConfigurableApplicationContext().getAutowireCapableBeanFactory().createBean(requiredType, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
        throw new IllegalStateException("");
    }

    public <T> T getComponent(Class<T> requiredType) {
        if (requiredType == null) {
            throw new ComponentNotFoundException("");
        }
        try {
            return getConfigurableApplicationContext().getBean(requiredType);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ComponentNotFoundException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    public Object getComponent(Object obj) throws ComponentNotFoundException {
        if (getConfigurableApplicationContext() == null) {
            throw new IllegalStateException("");
        }
        if (obj == null) {
            throw new ComponentNotFoundException("");
        }
        if (obj instanceof Class) {
            Class requiredType = (Class) obj;
            String names[] = getConfigurableApplicationContext().getBeanNamesForType(requiredType);
            if (names == null || names.length == 0) {
                throw new ComponentNotFoundException("");
            } else if (names.length > 1) {
                return new ArrayList(getConfigurableApplicationContext().getBeansOfType(requiredType).values());
            }
            obj = names[0];
        }
        try {
            return getConfigurableApplicationContext().getBean(obj.toString());
        } catch (BeansException e) {
            throw new ComponentNotFoundException("", e);
        }
    }

    @SuppressWarnings("rawtypes")
    public Object getInstance(Object obj) {
        if (getConfigurableApplicationContext() == null) {
            throw new IllegalStateException();
        }
        if (obj instanceof String) {
            try {
                Class clazz = getConfigurableApplicationContext().getClassLoader().loadClass((String) obj);
                return createComponent(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void refresh() {
        if (getConfigurableApplicationContext() == null) {
            throw new IllegalStateException();
        }
        getConfigurableApplicationContext().refresh();
    }

    public void onEvent(StateChangeEvent event) {
        Object source = event.getSource();
        if (source instanceof AdminService) {
            this.state = event.getNewState();
            if (event.getNewState() == State.STARTED) {
            }
        }
        log.debug("[Server] " + event.getOldState().toString() + " > " + event.getNewState().toString());
    }

    public void onApplicationEvent(ApplicationEvent event) {
        log.debug("###" + event);
    }
}
