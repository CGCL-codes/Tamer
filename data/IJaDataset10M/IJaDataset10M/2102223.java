package org.jtools.tef;

import java.io.IOException;
import java.util.Properties;
import org.jtools.util.props.PropertyHelper;
import org.jtools.util.props.PropertySupport;
import org.jtools.util.props.SimplePropertySupport;

public class SimpleTEFModelConfiguration implements PropertySupport {

    private PropertySupport properties = new SimplePropertySupport();

    public Properties getProperties() {
        return properties.getProperties();
    }

    public void setProperties(Properties properties) {
        this.properties.setProperties(properties);
    }

    public String getPropertyDomain() {
        return properties.getPropertyDomain();
    }

    public void setPropertyDomain(String target) {
        this.properties.setPropertyDomain(target);
    }

    public void setDefaultPropertyDomain(String propertyDomain) {
        properties.setDefaultPropertyDomain(propertyDomain);
    }

    public void addConfiguredProperties(PropertyHelper p) throws IOException {
        properties.getProperties().putAll(p.load());
    }
}
