package com.netx.bl.R1.core;

public class FieldDouble extends FieldNumber {

    public FieldDouble(MetaData owner, String name, String columnName, Comparable<?> defaultValue, boolean mandatory, boolean readOnly, ValidationExpr vExpr, Validator validator) {
        super(Field.TYPE.DOUBLE, owner, name, columnName, defaultValue, mandatory, readOnly, vExpr, validator);
    }

    protected void checkType(Object value) {
        value = (Double) value;
    }
}
