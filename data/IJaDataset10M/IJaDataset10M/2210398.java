package com.blogspot.ostas.flex.memory.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import flex.messaging.FactoryInstance;
import flex.messaging.FlexFactory;
import flex.messaging.config.ConfigMap;

public class SpringFactory implements FlexFactory {

    private static final String SOURCE = "source";

    private static final class SpringFactoryInstance extends FactoryInstance {

        SpringFactoryInstance(SpringFactory factory, String id, ConfigMap properties) {
            super(factory, id, properties);
        }

        public String toString() {
            return "SpringFactory instance for id=" + getId() + " source=" + getSource() + " scope=" + getScope();
        }

        public Object lookup() {
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(flex.messaging.FlexContext.getServletConfig().getServletContext());
            String beanName = getSource();
            return appContext.getBean(beanName);
        }
    }

    public SpringFactory() {
    }

    public void initialize(String id, ConfigMap configMap) {
    }

    public FactoryInstance createFactoryInstance(String id, ConfigMap properties) {
        SpringFactoryInstance instance = new SpringFactoryInstance(this, id, properties);
        instance.setSource(properties.getPropertyAsString(SOURCE, instance.getId()));
        return instance;
    }

    public Object lookup(FactoryInstance inst) {
        SpringFactoryInstance factoryInstance = (SpringFactoryInstance) inst;
        return factoryInstance.lookup();
    }
}
