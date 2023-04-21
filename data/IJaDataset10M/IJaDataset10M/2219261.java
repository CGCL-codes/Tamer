package org.uddi.api_v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for result complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="result">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:uddi-org:api_v3}errInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="keyType" type="{urn:uddi-org:api_v3}keyType" />
 *       &lt;attribute name="errno" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "result", propOrder = { "errInfo" })
public class Result {

    protected ErrInfo errInfo;

    @XmlAttribute
    protected KeyType keyType;

    @XmlAttribute(required = true)
    protected int errno;

    /**
     * Gets the value of the errInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ErrInfo }
     *     
     */
    public ErrInfo getErrInfo() {
        return errInfo;
    }

    /**
     * Sets the value of the errInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrInfo }
     *     
     */
    public void setErrInfo(ErrInfo value) {
        this.errInfo = value;
    }

    /**
     * Gets the value of the keyType property.
     * 
     * @return
     *     possible object is
     *     {@link KeyType }
     *     
     */
    public KeyType getKeyType() {
        return keyType;
    }

    /**
     * Sets the value of the keyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyType }
     *     
     */
    public void setKeyType(KeyType value) {
        this.keyType = value;
    }

    /**
     * Gets the value of the errno property.
     * 
     */
    public int getErrno() {
        return errno;
    }

    /**
     * Sets the value of the errno property.
     * 
     */
    public void setErrno(int value) {
        this.errno = value;
    }
}
