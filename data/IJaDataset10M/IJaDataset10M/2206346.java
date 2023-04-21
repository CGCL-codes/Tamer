package org.impalaframework.spring.config;

import java.util.Properties;
import org.impalaframework.config.PropertySource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * {@link FactoryBean} which exposes a particular named value from a wired in
 * {@link PropertySource} instance.
 * 
 * @author Phil Zoio
 */
public class PropertySourceValueFactoryBean implements FactoryBean {

    private String name;

    private String defaultValue;

    private PropertySource propertySource;

    /**
     * Returns {@link Properties} instance held by {@link Properties} holder.
     * Otherwise, returns empty {@link Properties} instance.
     */
    public Object getObject() throws Exception {
        Assert.notNull(propertySource, "propertySource cannot be null");
        Assert.notNull(name, "name cannot be null");
        String value = propertySource.getValue(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public Class<?> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPropertySource(PropertySource propertySource) {
        this.propertySource = propertySource;
    }
}
