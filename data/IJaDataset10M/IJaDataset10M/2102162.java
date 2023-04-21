package br.com.linkcom.neo.util;

import java.util.Map;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author rogelgarcia
 * @since 22/01/2006
 * @version 1.1
 */
public class BeanFacotryUtils {

    public boolean containsBeanOfClass(ConfigurableListableBeanFactory beanFactory, Class<?> beanClass) {
        Map<?, ?> map = beanFactory.getBeansOfType(beanClass);
        return !map.isEmpty();
    }

    /**
	 * Registra um bean na fabrica. O bean ser� configurado com AUTOWIRE BY TYPE.
	 * O nome do bean ser� o nome da classe
	 * @param beanFactory
	 * @param beanClass
	 */
    public void registerBean(ConfigurableListableBeanFactory beanFactory, Class beanClass) {
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass, mutablePropertyValues);
        beanDefinition.setAutowireMode(DefaultListableBeanFactory.AUTOWIRE_BY_TYPE);
        String beanName = Util.strings.uncaptalize(beanClass.getSimpleName());
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(beanName, beanDefinition);
    }

    public void registerBean(ConfigurableListableBeanFactory beanFactory, Class beanClass, String name) {
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass, mutablePropertyValues);
        beanDefinition.setAutowireMode(DefaultListableBeanFactory.AUTOWIRE_BY_TYPE);
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(name, beanDefinition);
    }

    public void registerBean(ConfigurableListableBeanFactory beanFactory, Class beanClass, String name, MutablePropertyValues mutablePropertyValues) {
        boolean autowire = true;
        registerBean(beanFactory, beanClass, name, mutablePropertyValues, autowire);
    }

    public void registerBean(ConfigurableListableBeanFactory beanFactory, Class beanClass, String name, MutablePropertyValues mutablePropertyValues, boolean autowire) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass, mutablePropertyValues);
        if (autowire) {
            beanDefinition.setAutowireMode(DefaultListableBeanFactory.AUTOWIRE_BY_TYPE);
        }
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(name, beanDefinition);
    }
}
