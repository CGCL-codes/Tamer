package org.inxar.jenesis;

/**
 * <code>Declaration</code> subinterface for formal parameter.  
 */
public interface FormalParameter extends Declaration {

    /**
     * Getter method for the formal parameter identifier.
     */
    String getName();

    /**
     * Getter method for the formal parameter type.
     */
    Type getType();

    /**
     * Getter method for the formal parameter <code>isFinal</code>
     * flag.  
     */
    boolean isFinal();

    /**
     * Setter method for the formal parameter <code>isFinal</code>
     * flag.  
     */
    void isFinal(boolean value);

    /**
     * Setter method for the formal parameter identifier.
     */
    void setName(String id);

    /**
     * Setter method for the formal parameter type.
     */
    void setType(Type type);
}
