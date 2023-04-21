package org.nexopenframework.spring;

import java.util.Iterator;
import java.util.Map;
import org.nexopenframework.core.locator.ServiceLocator;
import org.nexopenframework.core.locator.ServiceLocatorException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Easy implementation of retrieving Services or Business Objects from Spring
 *    Context</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @see ListableBeanFactory
 * @version 1.0
 * @since 1.0
 */
public class SpringServiceLocator implements ServiceLocator {

    /**Spring's Bean Factory*/
    private ListableBeanFactory beanFactory;

    /**
	 * <p></p>
	 */
    public SpringServiceLocator(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
	 * <p>Retrieve Service from Spring Context</p>
	 * 
	 * @see ServiceLocator#locateService(java.lang.Class)
	 * @throws ServiceLocatorException if there are problems in service location in Spring's context
	 * #see {@link BeanFactoryUtils#beanOfTypeIncludingAncestors(ListableBeanFactory, Class, boolean, boolean)}
	 */
    public Object locateService(Class serviceClass) throws ServiceLocatorException {
        try {
            Object obj = BeanFactoryUtils.beanOfTypeIncludingAncestors(beanFactory, serviceClass, false, false);
            return obj;
        } catch (NoSuchBeanDefinitionException e) {
            return handleMultipleBeans(serviceClass, e);
        } catch (BeansException e) {
            throw new ServiceLocatorException("Internal Spring error", e);
        }
    }

    /**
	 * @param serviceClass
	 * @param e
	 * @return
	 */
    private Object handleMultipleBeans(Class serviceClass, NoSuchBeanDefinitionException e) {
        Map beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, serviceClass, false, false);
        Iterator it_services = beans.values().iterator();
        while (it_services.hasNext()) {
            Object obj = it_services.next();
            Class clazz = obj.getClass();
            if (serviceClass.isAssignableFrom(clazz) && !AopUtils.isAopProxy(obj)) {
                return obj;
            }
        }
        throw (beans.size() > 0) ? new ServiceLocatorException("No suitable beans found for " + serviceClass) : new ServiceLocatorException("No beans defined " + serviceClass, e);
    }
}
