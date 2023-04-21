package org.salamandra.o2.type.impl;

public class StringTypeImpl<T> extends ClassTypeImpl<T> {

    public StringTypeImpl(T o, String fieldName) {
        super(o, fieldName);
    }

    public String getStringValue() {
        return (getO() == null ? new String() : String.valueOf(getO()));
    }
}
