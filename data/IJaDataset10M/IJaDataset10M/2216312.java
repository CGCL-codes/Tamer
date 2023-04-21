package com.koylu.caffein.model.caffein;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class CaffeinConfigProperty {

    private String clazz;

    private String name;

    private String value;

    private List<CaffeinConfigProperty> properties;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<CaffeinConfigProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<CaffeinConfigProperty> properties) {
        this.properties = properties;
    }

    public void addChild(CaffeinConfigProperty config) {
        if (properties == null) {
            properties = new ArrayList<CaffeinConfigProperty>();
        }
        properties.add(config);
    }

    public Object toObject() throws Exception {
        if (getClazz() != null) {
            Object object = Class.forName(getClazz()).newInstance();
            if (properties != null) {
                for (CaffeinConfigProperty property : properties) {
                    PropertyUtils.setProperty(object, property.getName(), ConvertUtils.convert(property.toObject(), PropertyUtils.getPropertyType(object, property.getName())));
                }
            }
            return object;
        }
        return value;
    }

    public String toString() {
        return clazz + "-" + name + "-" + value + "-" + properties;
    }
}
