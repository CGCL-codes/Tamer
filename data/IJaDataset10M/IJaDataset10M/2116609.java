package org.slasoi.common.eventschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Appliance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Appliance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="diskImage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Appliance", propOrder = { "diskImage" })
public class Appliance {

    @XmlElement(required = true)
    protected String diskImage;

    /**
     * Gets the value of the diskImage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiskImage() {
        return diskImage;
    }

    /**
     * Sets the value of the diskImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiskImage(String value) {
        this.diskImage = value;
    }
}
