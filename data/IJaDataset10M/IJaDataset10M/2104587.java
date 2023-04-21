package org.echarts.edt.sip.internal.pattern;

public class TestCondition extends Condition {

    private boolean supportsIC = false;

    private boolean ignoreCase = false;

    private boolean valueRequired = true;

    private String var = null;

    private String value = null;

    public TestCondition(String tag, boolean supportsIC) {
        super(tag);
        this.supportsIC = supportsIC;
    }

    public TestCondition(String tag, boolean supportsIC, boolean valueRequired) {
        super(tag);
        this.supportsIC = supportsIC;
        this.valueRequired = valueRequired;
    }

    public boolean isValid() {
        return var != null && value != null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        if (supportsIC) this.ignoreCase = ignoreCase;
    }

    public boolean hasAttribute() {
        return supportsIC && ignoreCase;
    }

    public String getAttribute() {
        return hasAttribute() ? " ignore-case=\"true\"" : "";
    }

    public boolean supportsIgnoreCase() {
        return supportsIC;
    }

    public String toString() {
        if (isValid()) {
            return var + " " + getTag() + getAttribute() + " " + value;
        }
        return getTag() + getAttribute();
    }

    public boolean isValueRequired() {
        return valueRequired;
    }

    public void setValueRequired(boolean requiresValue) {
        this.valueRequired = requiresValue;
    }
}
