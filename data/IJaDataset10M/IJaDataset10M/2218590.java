package org.inxar.jenesis;

/**
 * <code>Type</code> subinterface for array types.
 */
public interface ArrayType extends Type {

    /**
     * Gets the component type
     */
    Type getComponentType();

    /**
     * Gets the number of dimensions of this array type.
     */
    int getDims();
}
