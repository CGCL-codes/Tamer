package com.germinus.xpression.cms.educative;

public class Purpose {

    private String sourceKey;

    private String valueKey;

    public Purpose() {
    }

    public Purpose(String sourceKey, String valueKey) {
        super();
        this.sourceKey = sourceKey;
        this.valueKey = valueKey;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }
}
