package com.jmex.model.collada.types;

public interface SchemaTypeBinary extends SchemaType {

    public final int BINARY_VALUE_UNDEFINED = -1;

    public final int BINARY_VALUE_BASE64 = 0;

    public final int BINARY_VALUE_HEX = 1;

    public int binaryType();
}
