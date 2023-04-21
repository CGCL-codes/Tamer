package edu.mobbuzz.web.facade;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseFacade {

    private BeanFactory factory;

    public BeanFactory getFactory() {
        if (factory == null) {
            factory = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        return factory;
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }
}
