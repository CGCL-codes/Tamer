package com.nybbleware.tag4j;

/**
 *
 * @author NACHIKET PATEL
 */
public class SimpleTagKey implements TagKey {

    private String keyId;

    private Class valueType;

    public SimpleTagKey(String keyId, Class valueType) {
        this.keyId = keyId;
        this.valueType = valueType;
    }

    public String getKeyId() {
        return keyId;
    }

    public Class getValueType() {
        return valueType;
    }
}
