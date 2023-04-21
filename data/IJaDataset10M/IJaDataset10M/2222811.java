package net.sf.crsx.generator.codegen.common;

import net.sf.crsx.generator.codegen.Type;

/**
 * 
 * @author villardl
 */
public class BaseArrayType implements Type {

    /** Base type */
    protected final Type baseType;

    public BaseArrayType(Type baseType) {
        this.baseType = baseType;
    }

    public Type getBaseType() {
        return baseType;
    }

    public boolean isArray() {
        return true;
    }

    public boolean isPrimitive() {
        return false;
    }

    public String toString() {
        return baseType.toString();
    }
}
