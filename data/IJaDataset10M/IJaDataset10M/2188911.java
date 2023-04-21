package com.ibm.wala.ssa;

import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;

/**
 * Abstract base class for {@link SSAGetInstruction} and {@link SSAPutInstruction}.
 */
public abstract class SSAFieldAccessInstruction extends SSAInstruction {

    private final FieldReference field;

    private final int ref;

    protected SSAFieldAccessInstruction(FieldReference field, int ref) throws IllegalArgumentException {
        super();
        this.field = field;
        this.ref = ref;
        if (field == null) {
            throw new IllegalArgumentException("field cannot be null");
        }
    }

    public TypeReference getDeclaredFieldType() {
        return field.getFieldType();
    }

    public FieldReference getDeclaredField() {
        return field;
    }

    public int getRef() {
        return ref;
    }

    public boolean isStatic() {
        return ref == -1;
    }

    @Override
    public boolean isPEI() {
        return !isStatic();
    }
}
