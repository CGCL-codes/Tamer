package com.google.code.luar.type;

public class FunctionType extends AbstractType {

    public byte getType() {
        return FUNCTION;
    }

    public Object getValue() {
        return null;
    }

    protected boolean isLegalValue(Object value) {
        return false;
    }

    protected void doSetValue(Object value) {
    }
}
