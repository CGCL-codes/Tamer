package org.broadleafcommerce.util;

import org.springframework.beans.factory.FactoryBean;

public class EnvironmentFactoryBean implements FactoryBean {

    private String className;

    public EnvironmentFactoryBean(String className) {
        this.className = className;
    }

    public Object getObject() throws Exception {
        return Class.forName(className).newInstance();
    }

    @SuppressWarnings("unchecked")
    public Class getObjectType() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSingleton() {
        return false;
    }
}
