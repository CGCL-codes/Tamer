package org.antlride.core.model;

/**
 * Represent a rule's parameter.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public interface RuleParameter extends ModelElement {

    /**
   * Get the attribute name.
   * 
   * @return The attribute name.
   */
    SourceElement getName();

    /**
   * Get the attribute type.
   * 
   * @return The attribute type.
   */
    SourceElement getType();
}
