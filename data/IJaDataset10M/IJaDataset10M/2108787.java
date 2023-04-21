package de.beas.explicanto.client.sec.jaxb;

/**
 * Java content class for Location complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/Explicanto/ExplicantoClient/autogen/explicantoScreenplay.xsd line 199)
 * <p>
 * <pre>
 * &lt;complexType name="Location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="information" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Location {

    /**
     * Gets the value of the information property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInformation();

    /**
     * Sets the value of the information property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInformation(java.lang.String value);

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * Gets the value of the id property.
     * 
     */
    int getId();

    /**
     * Sets the value of the id property.
     * 
     */
    void setId(int value);
}
