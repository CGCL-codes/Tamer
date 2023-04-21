package org.herasaf.xacml.core.policy.impl;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.herasaf.xacml.core.converter.URNToFunctionConverter;
import org.herasaf.xacml.core.function.Function;

/**
 * <p>Java class for SubjectMatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectMatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeValue"/>
 *         &lt;choice>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}SubjectAttributeDesignator"/>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeSelector"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="MatchId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 46, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectMatchType", propOrder = { "attributeValue", "subjectAttributeDesignator", "attributeSelector" })
public class SubjectMatchType implements Serializable, Match {

    private static final long serialVersionUID = 632768732L;

    @XmlElement(name = "AttributeValue", required = true)
    protected AttributeValueType attributeValue;

    @XmlElement(name = "SubjectAttributeDesignator")
    protected SubjectAttributeDesignatorType subjectAttributeDesignator;

    @XmlElement(name = "AttributeSelector")
    protected AttributeSelectorType attributeSelector;

    @XmlAttribute(name = "MatchId", required = true)
    @XmlJavaTypeAdapter(URNToFunctionConverter.class)
    @XmlSchemaType(name = "anyURI")
    protected Function matchFunction;

    public AttributeValueType getAttributeValue() {
        return attributeValue;
    }

    /**
     * Sets the value of the attributeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueType }
     *     
     */
    public void setAttributeValue(AttributeValueType value) {
        this.attributeValue = value;
    }

    /**
     * Gets the value of the subjectAttributeDesignator property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectAttributeDesignatorType }
     *     
     */
    public SubjectAttributeDesignatorType getSubjectAttributeDesignator() {
        return subjectAttributeDesignator;
    }

    /**
     * Sets the value of the subjectAttributeDesignator property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectAttributeDesignatorType }
     *     
     */
    public void setSubjectAttributeDesignator(SubjectAttributeDesignatorType value) {
        this.subjectAttributeDesignator = value;
    }

    public AttributeSelectorType getAttributeSelector() {
        return attributeSelector;
    }

    /**
     * Sets the value of the attributeSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeSelectorType }
     *     
     */
    public void setAttributeSelector(AttributeSelectorType value) {
        this.attributeSelector = value;
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
        return getSubjectAttributeDesignator();
    }
}
