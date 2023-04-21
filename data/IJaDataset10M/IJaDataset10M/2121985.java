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
 *         &lt;element name="code">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="none"/>
 *               &lt;enumeration value="daily"/>
 *               &lt;enumeration value="weekly"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public interface EmailDigestFrequency extends SchemaEntity {

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link EmailDigestFrequencyCode }
     *     
     */
    EmailDigestFrequencyCode getCode();

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailDigestFrequencyCode }
     *     
     */
    void setCode(EmailDigestFrequencyCode value);
}
