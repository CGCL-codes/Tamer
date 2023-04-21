package com.teg.tobe.db.command;

import com.teg.tobe.util.Converter;
import com.teg.tobe.util.NullStatus;

public class DefaultDbParameter implements DbParameter {

    private static final long serialVersionUID = 1L;

    public Object value;

    private String suffix;

    private String prefix;

    public DefaultDbParameter() {
    }

    public DefaultDbParameter(Object object) {
        this.value = object;
    }

    public Object getValue() {
        if (NullStatus.isNotNull(suffix)) {
            if (NullStatus.isNotNull(prefix)) {
                return prefix + Converter.asNotNullString(value) + suffix;
            } else {
                return Converter.asNotNullString(value) + suffix;
            }
        } else {
            if (NullStatus.isNotNull(prefix)) {
                return prefix + Converter.asNotNullString(value);
            }
        }
        return value;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
