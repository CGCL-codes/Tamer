package com.jmex.model.collada.types;

public class TypesIncompatibleException extends SchemaTypeException {

    private static final long serialVersionUID = 1L;

    protected SchemaType object1;

    protected SchemaType object2;

    public TypesIncompatibleException(SchemaType newobj1, SchemaType newobj2) {
        super("Incompatible schema-types");
        object1 = newobj1;
        object2 = newobj2;
    }

    public TypesIncompatibleException(Exception other) {
        super(other);
    }
}
