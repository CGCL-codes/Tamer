package com.earnware.soap.contactmgr_2_0;

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
 *       &lt;sequence>
 *         &lt;element name="GetContactsByModifiedResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getContactsByModifiedResult" })
@XmlRootElement(name = "GetContactsByModifiedResponse")
public class GetContactsByModifiedResponse {

    @XmlElement(name = "GetContactsByModifiedResult")
    protected String getContactsByModifiedResult;

    /**
     * Gets the value of the getContactsByModifiedResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetContactsByModifiedResult() {
        return getContactsByModifiedResult;
    }

    /**
     * Sets the value of the getContactsByModifiedResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetContactsByModifiedResult(String value) {
        this.getContactsByModifiedResult = value;
    }
}
