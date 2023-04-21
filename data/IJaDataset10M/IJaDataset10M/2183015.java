package com.netx.bl.R1.core;

import com.netx.bl.R1.spi.DatabaseDriver;

public class FieldText extends FieldValidated {

    private final boolean _ignoreCase;

    private final int _minLength;

    private final long _maxLength;

    public FieldText(MetaData owner, String name, String columnName, Comparable<?> defaultValue, boolean mandatory, boolean readOnly, int minLength, long maxLength, boolean ignoreCase, ValidationExpr vExpr, Validator validator) {
        super(Field.TYPE.TEXT, owner, name, columnName, defaultValue, mandatory, readOnly, vExpr, validator);
        _minLength = minLength;
        _maxLength = maxLength;
        _ignoreCase = ignoreCase;
    }

    public int getMinLength() {
        return _minLength;
    }

    public long getMaxLength() {
        return _maxLength;
    }

    public boolean ignoreCase() {
        return _ignoreCase;
    }

    public Comparable<?> toObject(String value, DatabaseDriver driver) throws SizeExceededException {
        if (getMinLength() != 0 && value.length() < getMinLength()) {
            throw new SizeNotEnoughException(this, getMinLength(), value);
        }
        if (value.length() > getMaxLength()) {
            throw new SizeExceededException(this, getMaxLength(), value);
        }
        return value;
    }

    protected void checkType(Object value) {
        value = (String) value;
    }
}
