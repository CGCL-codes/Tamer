package org.goet.datamodel.impl;

import org.goet.datamodel.*;

public class TypedValueImpl {

    protected Type type;

    protected Object value;

    public TypedValueImpl(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
