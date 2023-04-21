package com.ekeyman.shared.schema;

import java.math.BigInteger;
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
 *         &lt;element name="KeyBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="IvBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="SaltBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="IterationCount" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
@XmlRootElement(name = "CreateEncryptionKeysResponse")
public class CreateEncryptionKeysResponse {

    @XmlElement(name = "KeyBytes", required = true)
    protected byte[] keyBytes;

    @XmlElement(name = "IvBytes", required = true)
    protected byte[] ivBytes;

    @XmlElement(name = "SaltBytes", required = true)
    protected byte[] saltBytes;

    @XmlElement(name = "IterationCount", required = true)
    protected BigInteger iterationCount;

    /**
     * Gets the value of the keyBytes property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getKeyBytes() {
        return keyBytes;
    }

    /**
     * Sets the value of the keyBytes property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKeyBytes(byte[] value) {
        this.keyBytes = ((byte[]) value);
    }

    /**
     * Gets the value of the ivBytes property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getIvBytes() {
        return ivBytes;
    }

    /**
     * Sets the value of the ivBytes property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setIvBytes(byte[] value) {
        this.ivBytes = ((byte[]) value);
    }

    /**
     * Gets the value of the saltBytes property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSaltBytes() {
        return saltBytes;
    }

    /**
     * Sets the value of the saltBytes property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSaltBytes(byte[] value) {
        this.saltBytes = ((byte[]) value);
    }

    /**
     * Gets the value of the iterationCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIterationCount() {
        return iterationCount;
    }

    /**
     * Sets the value of the iterationCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIterationCount(BigInteger value) {
        this.iterationCount = value;
    }
}
