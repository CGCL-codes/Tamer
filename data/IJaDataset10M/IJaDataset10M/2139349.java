package com.avaje.ebean.server.deploy;

public class RawSqlColumnInfo {

    final String name;

    final String label;

    final String propertyName;

    final boolean scalarProperty;

    public RawSqlColumnInfo(String name, String label, String propertyName, boolean scalarProperty) {
        this.name = name;
        this.label = label;
        this.propertyName = propertyName;
        this.scalarProperty = scalarProperty;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isScalarProperty() {
        return scalarProperty;
    }

    public String toString() {
        return "name:" + name + " label:" + label + " prop:" + propertyName;
    }
}
