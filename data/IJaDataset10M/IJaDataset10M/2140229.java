package org.mbari.vars.model;

/**
 * Common interface used by Associations, LinkTemplates and LinkRealizations
 * @author  brian
 */
public interface ILink {

    String DELIMITER_REGEXP = " \\| ";

    /**
     * Delimiter for String representation of a <code>LinkTemplate</code>.
     */
    String DELIMITER = " | ";

    /**
     * String text to indicate an unconstrained <code>LinkTemplate</code>
     * attribute.
     */
    String NIL = "nil";

    /**
     * self is a flag indicating that the toConcept is the same as the fromConcept.
     * By the same I mean the same instance.
     */
    String SELF = "self";

    /**
	 * Get the <code>Association</code> 'fromConcept' attribute. If the parent is an <code>Observation</code>, the 'fromConcept' is the parents 'Concept Name'. If the parent is an <code>Association</code> then the 'fromConcept' is the parents 'toConcept'.
	 * @return  The 'fromConcept' of this <code>Association</code>.<b>null  </b> is returned if the Association has no parent.
	 * @uml.property  name="fromConcept"
	 */
    String getFromConcept();

    /**
     * Get the <code>Association</code> 'linkName' attribute.
     * 
     * @return The <code>Association</code> 'linkName' attribute.
     * @uml.property name="linkName"
     */
    String getLinkName();

    /**
     * Get the <code>Association</code> 'linkValue' attribute.
     * 
     * @return The <code>Association</code> 'linkValue' attribute.
     * @uml.property name="linkValue"
     */
    String getLinkValue();

    /**
     * Get the <code>Association</code> 'toConcept' attribute.
     * 
     * @return The <code>Association</code> 'toConcept' attribute.
     * @uml.property name="toConcept"
     */
    String getToConcept();

    /**
     * Set the <code>Association</code> 'linkName' attribute. 'linkName' describes the relationship between "concepts" for example, "krill-eating-squid", "eating" is the linkName. It may also be used to describe the "fromConcept", example: squid-color-self-red
     * 
     * @param linkName_  The String representing the 'linkName' attribute.
     * @uml.property name="linkName"
     */
    void setLinkName(final String linkName_);

    /**
     * Set the <code>Association</code> 'linkValue' attribute. 'linkValue' is an adjective describing the link.
     * 
     * @param linkValue_  The String representing the 'linkValue' attribute.
     * @uml.property name="linkValue"
     */
    void setLinkValue(String linkValue_);

    /**
     * Set the <code>Association</code> 'toConcept' attribute. The describee, or reciepent of the action, defined by 'linkName'.
     * 
     * @param toConcept_  The String representing the 'toConcept' attribute.
     * @uml.property name="toConcept"
     */
    void setToConcept(final String toConcept_);
}
