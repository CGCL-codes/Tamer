package org.impalaframework.config;

import java.util.Collection;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of {@link PropertySource} which will attempt to retrieve a
 * non-null value from a succession of wired in {@link PropertySource}
 * instances. If any of these return a non-null value from a
 * {@link #getValue(String)} call, then {@link #getValue(String)} returns with
 * this returned value.
 * 
 * Implements Gang of Four Composite pattern.
 * 
 * @author Phil Zoio
 */
public class CompositePropertySource implements PropertySource {

    private final Collection<PropertySource> propertySources;

    public CompositePropertySource(Collection<PropertySource> propertySources) {
        super();
        Assert.notNull(propertySources, "propertySources cannot be null");
        Assert.notEmpty(propertySources, "propertySources cannot be empty");
        this.propertySources = propertySources;
    }

    public String getValue(String name) {
        Assert.notNull(name, "name cannot be null");
        for (PropertySource propertySource : propertySources) {
            String value = getValue(propertySource, name);
            if (value != null) return value;
        }
        return null;
    }

    protected String getValue(PropertySource propertySource, String name) {
        final String value = propertySource.getValue(name);
        if (value != null) {
            return value;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ObjectUtils.identityToString(this));
        buffer.append(" - propertySources: ");
        buffer.append(propertySources);
        return buffer.toString();
    }
}
