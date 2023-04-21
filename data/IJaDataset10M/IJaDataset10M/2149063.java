package org.springframework.aop.framework.adapter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * BeanPostProcessor implementation that "registers" instances of any
 * non-default AdvisorAdapters with GlobalAdvisorAdapterRegistry.
 *
 * <p>The only requirement for it to work is that it needs to be defined
 * in application context along with any arbitrary "non-native" Spring
 * AdvisorAdapters that need to be "recognized" by Spring's AOP framework.
 * 
 * @author Dmitriy Kopylenko
 * @since 27.02.2004
 * @see AdvisorAdapter
 * @see AdvisorAdapterRegistry
 * @see GlobalAdvisorAdapterRegistry
 */
public class AdvisorAdapterRegistrationManager implements BeanPostProcessor {

    private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();

    /**
	 * Specify the AdvisorAdapterRegistry to use.
	 * Default is the global AdvisorAdapterRegistry.
	 * @see org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry
	 */
    public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
        this.advisorAdapterRegistry = advisorAdapterRegistry;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AdvisorAdapter) {
            this.advisorAdapterRegistry.registerAdvisorAdapter((AdvisorAdapter) bean);
        }
        return bean;
    }
}
