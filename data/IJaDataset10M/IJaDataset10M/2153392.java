package com.amazonaws.ec2.doc._2009_04_04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DescribeAddressesResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DescribeAddressesResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addressesSet" type="{http://ec2.amazonaws.com/doc/2009-04-04/}DescribeAddressesResponseInfoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DescribeAddressesResponseType", propOrder = { "requestId", "addressesSet" })
public class DescribeAddressesResponseType {

    @XmlElement(required = true)
    protected String requestId;

    @XmlElement(required = true)
    protected DescribeAddressesResponseInfoType addressesSet;

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the addressesSet property.
     * 
     * @return
     *     possible object is
     *     {@link DescribeAddressesResponseInfoType }
     *     
     */
    public DescribeAddressesResponseInfoType getAddressesSet() {
        return addressesSet;
    }

    /**
     * Sets the value of the addressesSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescribeAddressesResponseInfoType }
     *     
     */
    public void setAddressesSet(DescribeAddressesResponseInfoType value) {
        this.addressesSet = value;
    }
}
