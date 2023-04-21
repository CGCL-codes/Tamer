package com.global360.sketchpadbpmn.propertystore;

public class PropertyItem {

    private String groupName;

    private String name;

    private Object value;

    public PropertyItem(String groupName, String propertyName, Object propertyValue) {
        this.setGroupName(groupName);
        this.name = propertyName;
        this.value = propertyValue;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getName() {
        return name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public Boolean getBooleanValue() {
        Boolean result = null;
        if (value instanceof Boolean) {
            result = (Boolean) value;
        }
        return result;
    }

    public Boolean getValue(Boolean defaultValue) {
        return defaultValue;
    }
}
