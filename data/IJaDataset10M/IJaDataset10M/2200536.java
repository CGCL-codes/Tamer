package com.googlecode.proxymatic.ioc.spring;

import com.googlecode.proxymatic.apps.factory.InstanceProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AutoregisterCapableBeanFactory extends DefaultListableBeanFactory {

    private List instanceProviders;

    public AutoregisterCapableBeanFactory(BeanFactory beanFactory) {
        super(beanFactory);
    }

    public void addInstanceProvider(InstanceProvider instanceProvider) {
        instanceProviders.add(instanceProvider);
    }

    protected Map findAutowireCandidates(String beanName, Class requiredType) {
        Map map = super.findAutowireCandidates(beanName, requiredType);
        return autoRegisterClasses(map, requiredType);
    }

    protected ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    public Map getBeansOfType(Class type) throws BeansException {
        Map map = super.getBeansOfType(type);
        return autoRegisterClasses(map, type);
    }

    public Object findInstance(Class realClass) {
        Map beansOfType = getBeansOfType(realClass);
        if (beansOfType.size() == 1) {
            return beansOfType.values().iterator().next();
        } else {
            return null;
        }
    }

    private Map autoRegisterClasses(Map map, Class type) {
        for (Iterator iterator = instanceProviders.iterator(); iterator.hasNext(); ) {
            InstanceProvider instanceProvider = (InstanceProvider) iterator.next();
            if (map.isEmpty()) {
                if (instanceProvider.containsInstance(type)) {
                    Object o = instanceProvider.getInstance(type);
                    registerBean(type, o, map);
                } else {
                    break;
                }
            }
        }
        return map;
    }

    private void registerBean(Class type, Object newBean, Map map) {
        registerSingleton(type.getName(), newBean);
        map.put(type.getName(), newBean);
    }

    private void lookForDynamicInterfacer(Class type, Map map) {
    }

    private void lookForDefaultImplementation(Class type, Map map) {
        String name = type.getName();
        String defaultClass = name.replaceFirst("^(.*\\.)", "$1Default");
        try {
            Class implClass = Class.forName(defaultClass, true, getClassLoader());
            Object newBean = autowire(implClass, RootBeanDefinition.AUTOWIRE_AUTODETECT, false);
            registerBean(implClass, newBean, map);
        } catch (ClassNotFoundException e) {
        }
    }
}
