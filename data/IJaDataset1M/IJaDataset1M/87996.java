package signature.model;

import java.util.Set;

/**
 * {@code IField} models a field.
 */
public interface IField extends IAnnotatableElement {

    /**
     * Returns the name of this field.
     * 
     * @return the name of this field
     */
    String getName();

    /**
     * Returns the type of this field.
     * 
     * @return the type of this field
     */
    ITypeReference getType();

    /**
     * Returns the modifiers of this field.
     * 
     * @return the modifiers of this field
     */
    Set<Modifier> getModifiers();
}
