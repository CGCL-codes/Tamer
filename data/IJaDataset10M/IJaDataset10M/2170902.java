package com.serena.xmlbridge.adapter.aewebservice.gen;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for FileAttachment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileAttachment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="showAsImage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="modificationDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessType" type="{urn:aewebservices71}Attachment-Access-Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileAttachment", propOrder = { "id", "name", "fileName", "showAsImage", "modificationDateTime", "url", "accessType" })
@XmlSeeAlso({ FileAttachmentContents.class })
public class FileAttachment {

    protected BigInteger id;

    @XmlElementRef(name = "name", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> name;

    @XmlElementRef(name = "fileName", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> fileName;

    protected boolean showAsImage;

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modificationDateTime;

    @XmlElementRef(name = "url", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> url;

    @XmlElement(required = true, defaultValue = "ATTACHACCESS-DEFAULT")
    protected AttachmentAccessType accessType;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFileName(JAXBElement<String> value) {
        this.fileName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the showAsImage property.
     * 
     */
    public boolean isShowAsImage() {
        return showAsImage;
    }

    /**
     * Sets the value of the showAsImage property.
     * 
     */
    public void setShowAsImage(boolean value) {
        this.showAsImage = value;
    }

    /**
     * Gets the value of the modificationDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModificationDateTime() {
        return modificationDateTime;
    }

    /**
     * Sets the value of the modificationDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModificationDateTime(XMLGregorianCalendar value) {
        this.modificationDateTime = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUrl(JAXBElement<String> value) {
        this.url = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the accessType property.
     * 
     * @return
     *     possible object is
     *     {@link AttachmentAccessType }
     *     
     */
    public AttachmentAccessType getAccessType() {
        return accessType;
    }

    /**
     * Sets the value of the accessType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttachmentAccessType }
     *     
     */
    public void setAccessType(AttachmentAccessType value) {
        this.accessType = value;
    }
}
