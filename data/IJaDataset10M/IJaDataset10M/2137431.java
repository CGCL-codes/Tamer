package org.herasaf.xacml.core.policy.impl.jibx;

import java.io.Serializable;
import org.herasaf.xacml.core.function.Function;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="urn:oasis:names:tc:xacml:2.0:policy:schema:os" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ResourceMatchType">
 *   &lt;xs:sequence>
 *     &lt;xs:element ref="ns:AttributeValue"/>
 *     &lt;xs:choice>
 *       &lt;xs:element ref="ns:ResourceAttributeDesignator"/>
 *       &lt;xs:element ref="ns:AttributeSelector"/>
 *     &lt;/xs:choice>
 *   &lt;/xs:sequence>
 *   &lt;xs:attribute type="xs:string" use="required" name="MatchId"/>
 * &lt;/xs:complexType>
 * 
 * &lt;xs:element xmlns:ns="urn:oasis:names:tc:xacml:2.0:policy:schema:os" xmlns:xs="http://www.w3.org/2001/XMLSchema" type="ns:AttributeValueType" substitutionGroup="ns:Expression" name="AttributeValue"/>
 * 
 * &lt;xs:element xmlns:ns="urn:oasis:names:tc:xacml:2.0:policy:schema:os" xmlns:xs="http://www.w3.org/2001/XMLSchema" type="ns:AttributeSelectorType" substitutionGroup="ns:Expression" name="AttributeSelector"/>
 * </pre>
 */
public class ResourceMatchType implements Serializable, Match {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6266364137557868978L;

    private AttributeValueType attributeValue;

    private int choiceSelect = -1;

    private static final int RESOURCE_ATTRIBUTE_DESIGNATOR_CHOICE = 0;

    private static final int ATTRIBUTE_SELECTOR_CHOICE = 1;

    private ResourceAttributeDesignator resourceAttributeDesignator;

    private AttributeSelectorType attributeSelector;

    private String matchId;

    protected Function matchFunction;

    /** 
     * Get the 'AttributeValue' element value.
     * 
     * @return value
     */
    public AttributeValueType getAttributeValue() {
        return attributeValue;
    }

    /** 
     * Set the 'AttributeValue' element value.
     * 
     * @param attributeValue
     */
    public void setAttributeValue(AttributeValueType attributeValue) {
        this.attributeValue = attributeValue;
    }

    private void setChoiceSelect(int choice) {
        if (choiceSelect == -1) {
            choiceSelect = choice;
        } else if (choiceSelect != choice) {
            throw new IllegalStateException("Need to call clearChoiceSelect() before changing existing choice");
        }
    }

    /** 
     * Clear the choice selection.
     */
    public void clearChoiceSelect() {
        choiceSelect = -1;
    }

    /** 
     * Check if ResourceAttributeDesignator is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifResourceAttributeDesignator() {
        return choiceSelect == RESOURCE_ATTRIBUTE_DESIGNATOR_CHOICE;
    }

    /** 
     * Get the 'ResourceAttributeDesignator' element value.
     * 
     * @return value
     */
    public ResourceAttributeDesignator getResourceAttributeDesignator() {
        return resourceAttributeDesignator;
    }

    /** 
     * Set the 'ResourceAttributeDesignator' element value.
     * 
     * @param resourceAttributeDesignator
     */
    public void setResourceAttributeDesignator(ResourceAttributeDesignator resourceAttributeDesignator) {
        setChoiceSelect(RESOURCE_ATTRIBUTE_DESIGNATOR_CHOICE);
        this.resourceAttributeDesignator = resourceAttributeDesignator;
    }

    /** 
     * Check if AttributeSelector is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifAttributeSelector() {
        return choiceSelect == ATTRIBUTE_SELECTOR_CHOICE;
    }

    /** 
     * Get the 'AttributeSelector' element value.
     * 
     * @return value
     */
    public AttributeSelectorType getAttributeSelector() {
        return attributeSelector;
    }

    /** 
     * Set the 'AttributeSelector' element value.
     * 
     * @param attributeSelector
     */
    public void setAttributeSelector(AttributeSelectorType attributeSelector) {
        setChoiceSelect(ATTRIBUTE_SELECTOR_CHOICE);
        this.attributeSelector = attributeSelector;
    }

    /** 
     * Get the 'MatchId' attribute value.
     * 
     * @return value
     */
    public String getMatchId() {
        return matchId;
    }

    /** 
     * Set the 'MatchId' attribute value.
     * 
     * @param matchId
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Function getMatchFunction() {
        return matchFunction;
    }

    /**
     * Sets the value of the matchFunction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setMatchFunction(Function value) {
        this.matchFunction = value;
    }

    public AttributeDesignatorType getAttributeDesignator() {
        return getResourceAttributeDesignator();
    }
}
