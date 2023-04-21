package com.jacum.cms.source.metadata;

import java.util.Map;
import java.util.HashMap;

/**
 */
public class ContentItemPropertyDefinition {

    public static final String TYPE_BOOLEAN = "BOOLEAN";

    public static final String TYPE_STRING = "STRING";

    public static final String TYPE_DATE = "DATE";

    public static final String TYPE_BINARY = "BINARY";

    public static final String TYPE_DOUBLE = "DOUBLE";

    public static final String TYPE_LONG = "LONG";

    public static final String TYPE_LONG_TIMESTAMP = "LONG_TIMESTAMP";

    public static final String TYPE_BOOLEAN_ARRAY = "BOOLEAN_ARRAY";

    public static final String TYPE_STRING_ARRAY = "STRING_ARRAY";

    public static final String TYPE_DATE_ARRAY = "DATE_ARRAY";

    public static final String TYPE_BINARY_ARRAY = "BINARY_ARRAY";

    public static final String TYPE_DOUBLE_ARRAY = "DOUBLE_ARRAY";

    public static final String TYPE_LONG_ARRAY = "LONG_ARRAY";

    public static final String TYPE_LONG_TIMESTAMP_ARRAY = "LONG_TIMESTAMP_ARRAY";

    public static final String TYPE_REFERENCE_ARRAY = "REFERENCE_ARRAY";

    private String name;

    private String type;

    private String[] valueConstraints;

    public ContentItemPropertyDefinition() {
    }

    public ContentItemPropertyDefinition(String name, String type) {
        this.name = name;
        this.type = type;
        this.valueConstraints = new String[] {};
    }

    public ContentItemPropertyDefinition(String name, String type, String[] valueConstraints) {
        this.name = name;
        this.type = type;
        this.valueConstraints = valueConstraints;
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

    public String[] getValueConstraints() {
        return valueConstraints;
    }

    public void setValueConstraints(String[] valueConstraints) {
        this.valueConstraints = valueConstraints;
    }
}
