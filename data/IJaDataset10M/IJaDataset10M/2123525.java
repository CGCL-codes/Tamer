package com.ems.common.datatransformer.objectfactory.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;
import com.ems.common.datatransformer.objectfactory.IObjectFactory;

/**
 * @author Chiknin
 */
public class SpringObjectFactory implements IObjectFactory, BeanFactoryAware {

    private static BeanFactory beanFactory;

    public String getObjectFactoryName() {
        return OBJECT_FACTORY_SPRING;
    }

    public Object getObject(String name) {
        Assert.notNull(beanFactory, "The SpringbeanFactory not initialize");
        return beanFactory.getBean(name);
    }

    public void setBeanFactory(BeanFactory bf) throws BeansException {
        beanFactory = bf;
    }
}
