package org.authsum.stitches.domain;

import java.io.Serializable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ScaledImage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScaledImage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeValue" type="{http://domain.stitches.authsum.org}AttributeValue" minOccurs="0"/>
 *         &lt;element name="beforeDelete" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="beforeInsert" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="beforeUpdate" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="binaryData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="binaryDataSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="s3Asset" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScaledImage", propOrder = { "attributeValue", "beforeDelete", "beforeInsert", "beforeUpdate", "binaryData", "binaryDataSize", "height", "id", "s3Asset", "version", "width" })
public class ScaledImage implements Serializable {

    @XmlElementRef(name = "attributeValue", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<AttributeValue> attributeValue;

    @XmlElementRef(name = "beforeDelete", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<Object> beforeDelete;

    @XmlElementRef(name = "beforeInsert", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<Object> beforeInsert;

    @XmlElementRef(name = "beforeUpdate", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<Object> beforeUpdate;

    @XmlElementRef(name = "binaryData", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<byte[]> binaryData;

    protected Integer binaryDataSize;

    protected Integer height;

    @XmlElementRef(name = "id", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<Long> id;

    @XmlElementRef(name = "s3Asset", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<Object> s3Asset;

    @XmlElementRef(name = "version", namespace = "http://domain.stitches.authsum.org", type = JAXBElement.class)
    protected JAXBElement<Long> version;

    protected Integer width;

    /**
     * Gets the value of the attributeValue property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AttributeValue }{@code >}
     *     
     */
    public JAXBElement<AttributeValue> getAttributeValue() {
        return attributeValue;
    }

    /**
     * Sets the value of the attributeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AttributeValue }{@code >}
     *     
     */
    public void setAttributeValue(JAXBElement<AttributeValue> value) {
        this.attributeValue = ((JAXBElement<AttributeValue>) value);
    }

    /**
     * Gets the value of the beforeDelete property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<Object> getBeforeDelete() {
        return beforeDelete;
    }

    /**
     * Sets the value of the beforeDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setBeforeDelete(JAXBElement<Object> value) {
        this.beforeDelete = ((JAXBElement<Object>) value);
    }

    /**
     * Gets the value of the beforeInsert property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<Object> getBeforeInsert() {
        return beforeInsert;
    }

    /**
     * Sets the value of the beforeInsert property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setBeforeInsert(JAXBElement<Object> value) {
        this.beforeInsert = ((JAXBElement<Object>) value);
    }

    /**
     * Gets the value of the beforeUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<Object> getBeforeUpdate() {
        return beforeUpdate;
    }

    /**
     * Sets the value of the beforeUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setBeforeUpdate(JAXBElement<Object> value) {
        this.beforeUpdate = ((JAXBElement<Object>) value);
    }

    /**
     * Gets the value of the binaryData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getBinaryData() {
        return binaryData;
    }

    /**
     * Sets the value of the binaryData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setBinaryData(JAXBElement<byte[]> value) {
        this.binaryData = ((JAXBElement<byte[]>) value);
    }

    /**
     * Gets the value of the binaryDataSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBinaryDataSize() {
        return binaryDataSize;
    }

    /**
     * Sets the value of the binaryDataSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBinaryDataSize(Integer value) {
        this.binaryDataSize = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHeight(Integer value) {
        this.height = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setId(JAXBElement<Long> value) {
        this.id = ((JAXBElement<Long>) value);
    }

    /**
     * Gets the value of the s3Asset property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<Object> getS3Asset() {
        return s3Asset;
    }

    /**
     * Sets the value of the s3Asset property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setS3Asset(JAXBElement<Object> value) {
        this.s3Asset = ((JAXBElement<Object>) value);
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setVersion(JAXBElement<Long> value) {
        this.version = ((JAXBElement<Long>) value);
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWidth(Integer value) {
        this.width = value;
    }
}
