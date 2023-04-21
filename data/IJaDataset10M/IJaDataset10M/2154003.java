package com.earnware.soap.telxml_2_0;

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
 *         &lt;element name="GetAccountByPhoneNumberResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getAccountByPhoneNumberResult" })
@XmlRootElement(name = "GetAccountByPhoneNumberResponse")
public class GetAccountByPhoneNumberResponse {

    @XmlElement(name = "GetAccountByPhoneNumberResult")
    protected String getAccountByPhoneNumberResult;

    /**
     * Gets the value of the getAccountByPhoneNumberResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAccountByPhoneNumberResult() {
        return getAccountByPhoneNumberResult;
    }

    /**
     * Sets the value of the getAccountByPhoneNumberResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAccountByPhoneNumberResult(String value) {
        this.getAccountByPhoneNumberResult = value;
    }
}
