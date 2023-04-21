package org.brandao.brutos.type;

/**
 * Allows the creation serializable types.
 *
 * @author Afonso Brandao
 */
public interface SerializableType extends Type {

    /**
     * Set the class type.
     * @param classType Class type.
     */
    public void setClassType(Class classType);
}
