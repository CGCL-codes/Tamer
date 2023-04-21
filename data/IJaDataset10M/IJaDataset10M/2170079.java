package com.idna.trace.model.rr.xsd;

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
 *       &lt;choice>
 *         &lt;element ref="{}AddressInFull"/>
 *         &lt;element ref="{}AddressDetails"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addressInFull", "addressDetails" })
@XmlRootElement(name = "Address")
public class Address {

    @XmlElement(name = "AddressInFull")
    protected String addressInFull;

    @XmlElement(name = "AddressDetails")
    protected AddressDetails addressDetails;

    /**
     * Gets the value of the addressInFull property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressInFull() {
        return addressInFull;
    }

    /**
     * Sets the value of the addressInFull property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressInFull(String value) {
        this.addressInFull = value;
    }

    /**
     * Gets the value of the addressDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AddressDetails }
     *     
     */
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    /**
     * Sets the value of the addressDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressDetails }
     *     
     */
    public void setAddressDetails(AddressDetails value) {
        this.addressDetails = value;
    }
}
