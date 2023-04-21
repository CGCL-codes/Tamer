package com.serena.xmlbridge.adapter.aewebservice.gen;

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
 *         &lt;element name="auth" type="{urn:aewebservices71}Auth" minOccurs="0"/>
 *         &lt;element name="applicationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xmlExportOptions" type="{urn:aewebservices71}FileContents" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "auth", "applicationID", "xmlExportOptions" })
@XmlRootElement(name = "Export")
public class Export {

    @XmlElementRef(name = "auth", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<Auth> auth;

    @XmlElementRef(name = "applicationID", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> applicationID;

    @XmlElementRef(name = "xmlExportOptions", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<FileContents> xmlExportOptions;

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
     * Gets the value of the applicationID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getApplicationID() {
        return applicationID;
    }

    /**
     * Sets the value of the applicationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setApplicationID(JAXBElement<String> value) {
        this.applicationID = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the xmlExportOptions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FileContents }{@code >}
     *     
     */
    public JAXBElement<FileContents> getXmlExportOptions() {
        return xmlExportOptions;
    }

    /**
     * Sets the value of the xmlExportOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FileContents }{@code >}
     *     
     */
    public void setXmlExportOptions(JAXBElement<FileContents> value) {
        this.xmlExportOptions = ((JAXBElement<FileContents>) value);
    }
}
