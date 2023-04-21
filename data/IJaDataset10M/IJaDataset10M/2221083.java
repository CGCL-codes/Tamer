package com.earnware.soap.contactmgr_2_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;sequence>
 *         &lt;element name="sessionToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acctMgrId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="contactXml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="folderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sessionToken", "acctMgrId", "contactXml", "folderId" })
@XmlRootElement(name = "CreateContact")
public class CreateContact {

    protected String sessionToken;

    protected int acctMgrId;

    protected String contactXml;

    protected int folderId;

    /**
     * Gets the value of the sessionToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Sets the value of the sessionToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionToken(String value) {
        this.sessionToken = value;
    }

    /**
     * Gets the value of the acctMgrId property.
     * 
     */
    public int getAcctMgrId() {
        return acctMgrId;
    }

    /**
     * Sets the value of the acctMgrId property.
     * 
     */
    public void setAcctMgrId(int value) {
        this.acctMgrId = value;
    }

    /**
     * Gets the value of the contactXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactXml() {
        return contactXml;
    }

    /**
     * Sets the value of the contactXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactXml(String value) {
        this.contactXml = value;
    }

    /**
     * Gets the value of the folderId property.
     * 
     */
    public int getFolderId() {
        return folderId;
    }

    /**
     * Sets the value of the folderId property.
     * 
     */
    public void setFolderId(int value) {
        this.folderId = value;
    }
}
