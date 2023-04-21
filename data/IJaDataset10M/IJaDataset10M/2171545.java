package com.ekeyman.securesavelib.shared.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ResourceUri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DigitalSignature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {  })
@XmlRootElement(name = "DeleteEncryptionKeysRequest")
public class DeleteEncryptionKeysRequest {

    @XmlElement(name = "ResourceUri", required = true)
    protected String resourceUri;

    @XmlElement(name = "PublicKey", required = true)
    protected String publicKey;

    @XmlElement(name = "Location", required = true)
    protected String location;

    @XmlElement(name = "DigitalSignature", required = true)
    protected String digitalSignature;

    /**
     * Gets the value of the resourceUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceUri() {
        return resourceUri;
    }

    /**
     * Sets the value of the resourceUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceUri(String value) {
        this.resourceUri = value;
    }

    /**
     * Gets the value of the publicKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the value of the publicKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicKey(String value) {
        this.publicKey = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the digitalSignature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDigitalSignature() {
        return digitalSignature;
    }

    /**
     * Sets the value of the digitalSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDigitalSignature(String value) {
        this.digitalSignature = value;
    }
}
