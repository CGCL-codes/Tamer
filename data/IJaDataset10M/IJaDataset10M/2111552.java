package com.netx.bl.R1.core;

class ValidatedArgument extends Argument {

    public ValidatedArgument(Field field, Comparable<?> value) throws ValidationException {
        super(field, value);
        field.validate(getValue());
        if (field.isReadOnly()) {
            throw new ReadOnlyFieldException(field);
        }
    }
}
