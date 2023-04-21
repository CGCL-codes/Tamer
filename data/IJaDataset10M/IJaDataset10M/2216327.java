package com.google.code.solrdimension.parsers.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 				exactDimensionValue type, extends the base dimensionvalue, enforce a match phrase
 * 			
 * 
 * <p>Java class for exactDimensionValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exactDimensionValue">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.solr-dimensions.com}dimensionValue">
 *       &lt;attribute name="matchphrase" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exactDimensionValue")
public class ExactDimensionValue extends DimensionValue {

    @XmlAttribute(required = true)
    protected String matchphrase;

    /**
     * Gets the value of the matchphrase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchphrase() {
        return matchphrase;
    }

    /**
     * Sets the value of the matchphrase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchphrase(String value) {
        this.matchphrase = value;
    }
}
