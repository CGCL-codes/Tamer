package org.dasein.cloud.services.rdbms;

import java.util.Map;

public class ConfigurationParameter implements Map.Entry<String, ConfigurationParameter> {

    private boolean applyImmediately;

    private String dataType;

    private String description;

    private String key;

    private boolean modifiable;

    private Object parameter;

    private String validation;

    public ConfigurationParameter() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public ConfigurationParameter getValue() {
        return this;
    }

    @Override
    public ConfigurationParameter setValue(ConfigurationParameter value) {
        return this;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setApplyImmediately(boolean applyImmediately) {
        this.applyImmediately = applyImmediately;
    }

    public boolean isApplyImmediately() {
        return applyImmediately;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getValidation() {
        return validation;
    }
}
