package org.xmlsoap.schemas.ws._2003._03.addressing;

/**
 * Java content class for ServiceNameType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://schemas.xmlsoap.org/ws/2003/03/addressing/ line 67)
 * <p>
 * <pre>
 * &lt;complexType name="ServiceNameType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>QName">
 *       &lt;attribute name="PortName" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ServiceNameType {

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.namespace.QName}
     */
    javax.xml.namespace.QName getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.namespace.QName}
     */
    void setValue(javax.xml.namespace.QName value);

    /**
     * Gets the value of the portName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPortName();

    /**
     * Sets the value of the portName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPortName(java.lang.String value);
}
