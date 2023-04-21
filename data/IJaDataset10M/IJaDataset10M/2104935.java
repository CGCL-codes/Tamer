package com.google.code.linkedinapi.schema;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}proficiency"/>
 *         &lt;element ref="{}id"/>
 *         &lt;element name="language" type="{}name-type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public interface Language extends SchemaEntity {

    /**
     * Gets the value of the proficiency property.
     * 
     * @return
     *     possible object is
     *     {@link Proficiency }
     *     
     */
    Proficiency getProficiency();

    /**
     * Sets the value of the proficiency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Proficiency }
     *     
     */
    void setProficiency(Proficiency value);

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    String getId();

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    void setId(String value);

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link NameType }
     *     
     */
    NameType getLanguage();

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameType }
     *     
     */
    void setLanguage(NameType value);
}
