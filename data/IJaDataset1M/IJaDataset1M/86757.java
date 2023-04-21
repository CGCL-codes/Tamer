package org.jcompany.jdoc.config;

import java.io.Serializable;

public class PlcHibernateProperty implements Serializable {

    private String propertyName;

    private String propertyValue;

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String newPropertyValue) {
        propertyValue = newPropertyValue;
    }

    public PlcHibernateProperty() {
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String newPropertyName) {
        propertyName = newPropertyName;
    }
}
