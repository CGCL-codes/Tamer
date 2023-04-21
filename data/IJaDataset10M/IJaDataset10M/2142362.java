package org.nexopenframework.deployment.enhacer.impl;

import org.aopalliance.aop.Advice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nexopenframework.business.BusinessService;
import org.nexopenframework.core.ServiceComponent;
import org.nexopenframework.deployment.enhacer.EnhacerException;
import org.nexopenframework.deployment.enhacer.ServiceComponentEnhacer;
import org.nexopenframework.deployment.enhacer.addition.CompositeInterceptorAdditionLocator;
import org.nexopenframework.deployment.enhacer.addition.InterceptorAdditionLocator;
import org.nexopenframework.deployment.enhacer.support.FacadeStrategy;
import org.nexopenframework.engine.impl.BusinessServiceEngine;
import org.nexopenframework.engine.support.ServiceComponentEngineInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of the {@link ServiceComponentEnhacer} for dealing with Business Services</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class InterceptorBusinessEnhacer implements ServiceComponentEnhacer, ApplicationContextAware {

    /** {@link org.apache.commons.logging} logging facility  */
    protected final Log logger = LogFactory.getLog(getClass());

    /**AOP interceptor. It is Thread-safe and could be shared with all the Business Services*/
    private ServiceComponentEngineInterceptor interceptor;

    /**the spring application context*/
    private ApplicationContext applicationContext;

    /**the interceptor addition locator*/
    private InterceptorAdditionLocator locator;

    /**the order in which this ServiceComponentEnhacer should be loaded */
    private int order = Ordered.LOWEST_PRECEDENCE;

    /**Facade strategy for dealing with facade support*/
    private FacadeStrategy facadeStrategy;

    /**
	 * @return
	 */
    public int getOrder() {
        return order;
    }

    /**
	 * @param order
	 */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
	 * @param facadeStrategy
	 */
    public void setFacadeStrategy(FacadeStrategy facadeStrategy) {
        this.facadeStrategy = facadeStrategy;
    }

    /**
	 * <p>Performs composite location of interceptors using several implementations</p>
	 * 
	 * @param locators
	 * @see CompositeInterceptorAdditionLocator
	 */
    public void setInterceptorAdditionLocators(InterceptorAdditionLocator[] locators) {
        this.locator = new CompositeInterceptorAdditionLocator(locators);
    }

    /**
	 * @param locator
	 */
    public void setInterceptorAdditionLocator(InterceptorAdditionLocator locator) {
        this.locator = locator;
    }

    /**
	 * <p>Auto-discovery from {@link BusinessServiceEngine} in spring context</p>
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BusinessServiceEngine engine = (BusinessServiceEngine) BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, BusinessServiceEngine.class, false, false);
        interceptor = new ServiceComponentEngineInterceptor();
        interceptor.setServiceEngine(engine);
        interceptor.setBeanFactory(applicationContext);
        this.applicationContext = applicationContext;
    }

    /**
	 * 
	 * @see org.nexopenframework.deployment.enhacer.ServiceComponentEnhacer#enhace(org.nexopenframework.core.ServiceComponent)
	 */
    public ServiceComponent enhace(ServiceComponent bean) throws EnhacerException {
        if (bean instanceof BusinessService) {
            try {
                Assert.notNull(this.facadeStrategy, "Facade Strategy could not be null");
                BusinessService service = (BusinessService) bean;
                ProxyFactory proxyFactory = new ProxyFactory();
                Class[] interfaces = bean.getClass().getInterfaces();
                proxyFactory.setInterfaces(interfaces);
                if (!this.facadeStrategy.isFacade(service)) {
                    proxyFactory.setProxyTargetClass(true);
                }
                TargetSource targetSource = createTargetSource(service);
                proxyFactory.setTargetSource(targetSource);
                proxyFactory.addAdvice(interceptor);
                Class[] interceptors = locator.locateInterceptors(service);
                if (interceptors != null) {
                    for (int k = 0; k < interceptors.length; k++) {
                        Class beanClass = interceptors[k];
                        Object beanInterceptor = null;
                        try {
                            beanInterceptor = BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, beanClass);
                        } catch (BeansException e) {
                            if (logger.isInfoEnabled()) {
                                logger.info("No bean class in the Spring Application Context for bean interceptor " + beanClass.getName());
                                logger.info("Create a new instance of this bean interceptor");
                            }
                            beanInterceptor = beanClass.newInstance();
                        }
                        if (beanInterceptor instanceof Advice) {
                            if (logger.isInfoEnabled()) {
                                logger.info("Added a extra interceptor " + beanClass.getName() + " to service " + bean);
                            }
                            Advice advice = (Advice) beanInterceptor;
                            proxyFactory.addAdvice(advice);
                        } else {
                            if (logger.isErrorEnabled()) {
                                logger.error("Not a Interceptor :: " + beanInterceptor + " added to service " + bean);
                            }
                        }
                    }
                }
                proxyFactory.setFrozen(true);
                Object beanProxy = proxyFactory.getProxy();
                BusinessService mbean = (BusinessService) beanProxy;
                return mbean;
            } catch (Exception e) {
                throw new EnhacerException("In " + bean + " Exception " + e.getClass().getName(), e);
            }
        }
        return bean;
    }

    /**
	 * Set the target or TargetSource.
	 * @param target target. If this is an implementation of TargetSource it is
	 * used as our TargetSource; otherwise it is wrapped in a SingletonTargetSource.
	 * @return a TargetSource for this object
	 */
    protected TargetSource createTargetSource(Object target) {
        if (target instanceof TargetSource) {
            return (TargetSource) target;
        }
        return new SingletonTargetSource(target);
    }
}
