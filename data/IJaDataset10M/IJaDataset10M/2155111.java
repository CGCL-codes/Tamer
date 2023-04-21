package org.dozer.functional_tests.support;

import org.dozer.Mapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class ApplicationBeanFactory {

    private static BeanFactoryLocator bfl = ContextSingletonBeanFactoryLocator.getInstance();

    private static BeanFactory beanFactory = bfl.useBeanFactory("beanfactory").getFactory();

    private ApplicationBeanFactory() {
    }

    public static Object getBean(Class<Mapper> beanClass) {
        return beanFactory.getBean(beanClass.getName());
    }

    public static Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }
}
