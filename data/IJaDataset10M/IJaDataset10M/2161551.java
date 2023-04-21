package com.jawise.serviceadapter.test.svc.soap.computerparts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Order complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Order">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parts" type="{http://soap.svc.test.serviceadapter.jawise.com}ArrayOfPart" minOccurs="0"/>
 *         &lt;element name="requestdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Order", propOrder = { "email", "id", "parts", "requestdate", "telephone", "total" })
public class Order {

    @XmlElementRef(name = "email", namespace = "http://soap.svc.test.serviceadapter.jawise.com", type = JAXBElement.class)
    protected JAXBElement<String> email;

    @XmlElementRef(name = "id", namespace = "http://soap.svc.test.serviceadapter.jawise.com", type = JAXBElement.class)
    protected JAXBElement<String> id;

    @XmlElementRef(name = "parts", namespace = "http://soap.svc.test.serviceadapter.jawise.com", type = JAXBElement.class)
    protected JAXBElement<ArrayOfPart> parts;

    @XmlElementRef(name = "requestdate", namespace = "http://soap.svc.test.serviceadapter.jawise.com", type = JAXBElement.class)
    protected JAXBElement<String> requestdate;

    @XmlElementRef(name = "telephone", namespace = "http://soap.svc.test.serviceadapter.jawise.com", type = JAXBElement.class)
    protected JAXBElement<String> telephone;

    @XmlElementRef(name = "total", namespace = "http://soap.svc.test.serviceadapter.jawise.com", type = JAXBElement.class)
    protected JAXBElement<String> total;

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmail(JAXBElement<String> value) {
        this.email = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setId(JAXBElement<String> value) {
        this.id = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the parts property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPart }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPart> getParts() {
        return parts;
    }

    /**
     * Sets the value of the parts property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPart }{@code >}
     *     
     */
    public void setParts(JAXBElement<ArrayOfPart> value) {
        this.parts = ((JAXBElement<ArrayOfPart>) value);
    }

    /**
     * Gets the value of the requestdate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRequestdate() {
        return requestdate;
    }

    /**
     * Sets the value of the requestdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRequestdate(JAXBElement<String> value) {
        this.requestdate = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the telephone property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelephone() {
        return telephone;
    }

    /**
     * Sets the value of the telephone property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelephone(JAXBElement<String> value) {
        this.telephone = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTotal(JAXBElement<String> value) {
        this.total = ((JAXBElement<String>) value);
    }
}
