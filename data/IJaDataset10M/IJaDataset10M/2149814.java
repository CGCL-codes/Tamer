package org.nexopenframework.management.spring.monitor.annotations;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.management.monitor.spi.EventNotifier;
import org.nexopenframework.management.spring.monitor.interceptor.MonitorInterceptor;
import org.nexopenframework.management.spring.monitor.strategy.CompositeMonitorStrategy;
import org.nexopenframework.management.spring.monitor.strategy.MonitorStrategy;
import org.nexopenframework.management.spring.support.AnnotationsUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>TODO Comment here</p>
 * 
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see org.nexopenframework.management.spring.monitor.strategy.MonitorStrategy
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m1
 */
public class MonitorBeanPostProcessor implements BeanPostProcessor, BeanClassLoaderAware {

    /**Logging Facility based in Jakarta Commons Logging (JCL hereafter)*/
    private static final Log logger = LogFactory.getLog(MonitorBeanPostProcessor.class);

    /**The AOP Interceptor*/
    private final MonitorInterceptor interceptor = new MonitorInterceptor();

    /**The current classloader*/
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /***/
    private CompositeMonitorStrategy strategy = new CompositeMonitorStrategy();

    public void setEventNotifier(final EventNotifier notifier) {
        this.interceptor.setEventNotifier(notifier);
    }

    /**
	 * @param strategy
	 */
    public void setMonitorStrategy(final MonitorStrategy strategy) {
        this.strategy.addMonitorStrategy(strategy);
    }

    /**
	 * @param strategies
	 */
    public void setMonitorStrategies(final List<MonitorStrategy> strategies) {
        this.strategy.setMonitorStrategies(strategies);
    }

    /**
	 * <p></p>
	 * 
	 * @see org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang.ClassLoader)
	 */
    public void setBeanClassLoader(final ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    /**
	 * <p></p>
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        if (strategy.isMonitorable(bean)) {
            if (bean instanceof Advised) {
                final Advised advised = (Advised) bean;
                if (!advised.isFrozen()) {
                    advised.addAdvice(this.interceptor);
                    return bean;
                } else {
                    logger.warn("Bean [" + beanName + "] is an AOP Proxy that is frozen");
                    logger.warn("Creating a proxy of proxy for monitorable features");
                }
            }
            final ProxyFactory pf = new ProxyFactory(bean);
            final Class<?> clazz = AopUtils.isAopProxy(bean) ? AnnotationsUtils.findClassFromProxy(bean) : bean.getClass();
            final Class<?>[] interfaces = clazz.getInterfaces();
            pf.setInterfaces(interfaces);
            pf.addAdvice(this.interceptor);
            return pf.getProxy(this.beanClassLoader);
        }
        return bean;
    }

    /**
	 * <p></p>
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }
}
