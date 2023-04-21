package net.sourceforge.jenesis4java;

import java.util.List;

/**
 * <code>Declaration</code> subinterface for interfaces.
 */
public interface Interface extends TypeDeclaration {

    /**
     * Adds the given string to the list of extends clauses and returns the
     * <code>Interface</code>.
     */
    Interface addExtends(String type);

    /**
     * Gets the list of extends clauses as an list of <code>String</code>.
     */
    List<String> getExtends();

    /**
     * Adds a new int constant to this interface with the given name and value.
     * This is a convenience method.
     */
    Constant newConstant(String name, int value);

    /**
     * Adds a new constant to this interface.
     */
    Constant newConstant(Type type, String name);

    /**
     * Adds a new abstract method signature to this interface.
     */
    AbstractMethod newMethod(Type type, String name);
}
