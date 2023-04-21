package nz.ac.massey.xmldad.bookquery;

/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Documents%20and%20Settings/njones/workspace/XMLDAD_WebService/xsd/bookQuery.xsd line 43)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="ok"/>
 *             &lt;enumeration value="error"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ResponseType {

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStatus();

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStatus(java.lang.String value);

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMessage();

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMessage(java.lang.String value);
}
