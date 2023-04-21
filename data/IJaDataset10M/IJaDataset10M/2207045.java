package com.liferay.portal.spring.annotation;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ReflectionUtils;

/**
 * <a href="BeanReferenceAnnotationBeanPostProcessor.java.html"><b><i>View
 * Source</i></b></a>
 *
 * @author Michael Young
 *
 */
public class BeanReferenceAnnotationBeanPostProcessor implements BeanFactoryAware, InstantiationAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        InjectionMetadata injectionMetadata = findResourceMetadata(bean.getClass());
        try {
            injectionMetadata.injectFields(bean, beanName);
        } catch (Throwable t) {
            throw new BeanCreationException(beanName, "Injection of BeanReference fields failed", t);
        }
        return true;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
        return null;
    }

    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class beanType, String beanName) {
        if (beanType == null) {
            return;
        }
        InjectionMetadata injectionMetadata = findResourceMetadata(beanType);
        injectionMetadata.checkConfigMembers(beanDefinition);
    }

    public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptors, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = findResourceMetadata(bean.getClass());
        try {
            metadata.injectMethods(bean, beanName, propertyValues);
        } catch (Throwable t) {
            throw new BeanCreationException(beanName, "Injection of BeanReference methods failed", t);
        }
        return propertyValues;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        _beanFactory = beanFactory;
    }

    protected InjectionMetadata findResourceMetadata(Class clazz) {
        InjectionMetadata injectionMetadata = _injectionMetadataMap.get(clazz);
        if (injectionMetadata != null) {
            return injectionMetadata;
        }
        synchronized (_injectionMetadataMap) {
            injectionMetadata = _injectionMetadataMap.get(clazz);
            if (injectionMetadata == null) {
                injectionMetadata = new InjectionMetadata(clazz);
                BeanReferenceCallback callback = new BeanReferenceCallback(_beanFactory, injectionMetadata, clazz);
                ReflectionUtils.doWithFields(clazz, callback);
                ReflectionUtils.doWithMethods(clazz, callback);
                _injectionMetadataMap.put(clazz, injectionMetadata);
            }
        }
        return injectionMetadata;
    }

    private BeanFactory _beanFactory;

    private Map<Class<?>, InjectionMetadata> _injectionMetadataMap = new ConcurrentHashMap<Class<?>, InjectionMetadata>();
}
