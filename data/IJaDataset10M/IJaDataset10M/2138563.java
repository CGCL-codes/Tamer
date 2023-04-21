package com.jogamp.gluegen.cgram.types;

public class StructType extends CompoundType {

    public StructType(String name, SizeThunk size, int cvAttributes) {
        this(name, size, cvAttributes, null);
    }

    StructType(String name, SizeThunk size, int cvAttributes, String structName) {
        super(name, size, cvAttributes, structName);
    }

    @Override
    public boolean equals(Object arg) {
        if (arg == null || !(arg instanceof StructType)) {
            return false;
        }
        return super.equals(arg);
    }

    public final boolean isStruct() {
        return true;
    }

    public final boolean isUnion() {
        return false;
    }

    Type newCVVariant(int cvAttributes) {
        StructType t = new StructType(getName(), getSize(), cvAttributes, getStructName());
        t.setFields(getFields());
        return t;
    }
}
