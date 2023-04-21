package com.serena.xmlbridge.adapter.ttwebservice.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="auth" type="{urn:ttwebservices}Auth" minOccurs="0"/>
 *         &lt;element name="itemID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attachmentContents" type="{urn:ttwebservices}FileAttachmentContents" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "auth", "itemID", "attachmentContents" })
@XmlRootElement(name = "CreateFileAttachment")
public class CreateFileAttachment {

    @XmlElementRef(name = "auth", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<Auth> auth;

    @XmlElementRef(name = "itemID", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<String> itemID;

    @XmlElementRef(name = "attachmentContents", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<FileAttachmentContents> attachmentContents;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Auth }{@code >}
     *     
     */
    public JAXBElement<Auth> getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Auth }{@code >}
     *     
     */
    public void setAuth(JAXBElement<Auth> value) {
        this.auth = ((JAXBElement<Auth>) value);
    }

    /**
     * Gets the value of the itemID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getItemID() {
        return itemID;
    }

    /**
     * Sets the value of the itemID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setItemID(JAXBElement<String> value) {
        this.itemID = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the attachmentContents property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FileAttachmentContents }{@code >}
     *     
     */
    public JAXBElement<FileAttachmentContents> getAttachmentContents() {
        return attachmentContents;
    }

    /**
     * Sets the value of the attachmentContents property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FileAttachmentContents }{@code >}
     *     
     */
    public void setAttachmentContents(JAXBElement<FileAttachmentContents> value) {
        this.attachmentContents = ((JAXBElement<FileAttachmentContents>) value);
    }
}
