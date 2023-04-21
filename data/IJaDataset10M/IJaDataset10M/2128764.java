package org.inxar.jenesis;

/**
 * <code>Declaration</code> subinterface for methods including
 * abstract and class methods.  
 */
public interface Method extends Member {

    /**
     * Adds a new <code>FormalParameter</code> to this method
     * signature with the given type and name and returns the
     * <code>Method</code>.  
     */
    FormalParameter addParameter(Type type, String name);

    /**
     * Adds this string to the list of throws and returns the
     * <code>Method</code>.  
     */
    void addThrows(String type);

    /**
     * Gets the list of formal parameter declarations as an iterator
     * of <code>FormalParameter</code>.  
     */
    Iterator getParameters();

    /**
     * Gets the list of throws clauses as an iterator of
     * <code>String</code>.  
     */
    Iterator getThrows();

    /**
     * Gets the (return) type of this method.
     */
    Type getType();

    /**
     * Accessor method for the isAbstract flag.
     */
    boolean isAbstract();

    /**
     * Mutator method for the isAbstract flag.
     */
    void isAbstract(boolean value);

    /**
     * Sets the (return) type of this method.
     */
    void setType(Type type);
}
