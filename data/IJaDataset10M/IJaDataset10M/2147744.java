package com.rccloud.inproxy;

import java.lang.reflect.Field;
import com.rccloud.inproxy.converter.ProxyConverter;

/**
 * Proxy info for class field
 */
class FieldProxyInfo {

    private Field proxyField;

    private Class<?> targetClass;

    private String targetFieldName;

    private Class<? extends ProxyConverter> converter;

    public Field getProxyField() {
        return proxyField;
    }

    public void setProxyField(Field proxyField) {
        this.proxyField = proxyField;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }

    public Class<? extends ProxyConverter> getConverter() {
        return converter;
    }

    public void setConverter(Class<? extends ProxyConverter> converter) {
        this.converter = converter;
    }
}
