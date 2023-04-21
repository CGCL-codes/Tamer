package com.curlap.orb.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * FieldProperty
 */
public class FieldProperty implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String modifier;

    public FieldProperty() {
    }

    public FieldProperty(Field field) {
        name = field.getName();
        type = field.getType().getName();
        modifier = Modifier.toString(field.getModifiers());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
