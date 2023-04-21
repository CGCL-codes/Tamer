package com.ontotext.ordi.wsmo4rdf.remote.client.src;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ServiceLimitExceededException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceLimitExceededException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serialVersionUID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceLimitExceededException", propOrder = { "serialVersionUID", "message" })
public class ServiceLimitExceededException {

    @XmlElement(namespace = "http://server.remote.wsmo4rdf.ordi.ontotext.com/", required = true, type = Long.class, nillable = true)
    protected Long serialVersionUID;

    @XmlElement(namespace = "http://server.remote.wsmo4rdf.ordi.ontotext.com/", required = true, nillable = true)
    protected String message;

    /**
     * Gets the value of the serialVersionUID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Sets the value of the serialVersionUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSerialVersionUID(Long value) {
        this.serialVersionUID = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }
}
