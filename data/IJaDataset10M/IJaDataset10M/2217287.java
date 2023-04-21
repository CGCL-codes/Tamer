package com.google.checkout.sdk.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for CartExpiration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CartExpiration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="good-until-date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CartExpiration", namespace = "http://checkout.google.com/schema/2", propOrder = {  })
public class CartExpiration {

    @XmlElement(name = "good-until-date", namespace = "http://checkout.google.com/schema/2", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar goodUntilDate;

    /**
     * Gets the value of the goodUntilDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGoodUntilDate() {
        return goodUntilDate;
    }

    /**
     * Sets the value of the goodUntilDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGoodUntilDate(XMLGregorianCalendar value) {
        this.goodUntilDate = value;
    }
}
