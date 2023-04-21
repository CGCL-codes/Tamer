package net.woodstock.rockapi.pojo.converter.token;

import net.woodstock.rockapi.util.FieldInfo;

public interface TokenAttributeConverter<T> {

    public T fromText(String text, FieldInfo fieldInfo);

    public String toText(T o, FieldInfo fieldInfo);
}
