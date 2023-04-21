package com.serena.xmlbridge.adapter.ttwebservice.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Transition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Transition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transitionID" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fromState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{urn:ttwebservices}Transition-Type"/>
 *         &lt;element name="fullyQualifiedPostIssueProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transitionAttributes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Transition", propOrder = { "transitionID", "name", "fromState", "toState", "type", "fullyQualifiedPostIssueProjectName", "transitionAttributes" })
public class Transition {

    @XmlElement(required = true)
    protected BigInteger transitionID;

    @XmlElementRef(name = "name", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<String> name;

    @XmlElementRef(name = "fromState", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<String> fromState;

    @XmlElementRef(name = "toState", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<String> toState;

    @XmlElement(required = true)
    protected TransitionType type;

    @XmlElementRef(name = "fullyQualifiedPostIssueProjectName", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<String> fullyQualifiedPostIssueProjectName;

    protected List<String> transitionAttributes;

    /**
     * Gets the value of the transitionID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTransitionID() {
        return transitionID;
    }

    /**
     * Sets the value of the transitionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTransitionID(BigInteger value) {
        this.transitionID = value;
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
     * Gets the value of the fromState property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFromState() {
        return fromState;
    }

    /**
     * Sets the value of the fromState property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFromState(JAXBElement<String> value) {
        this.fromState = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the toState property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getToState() {
        return toState;
    }

    /**
     * Sets the value of the toState property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setToState(JAXBElement<String> value) {
        this.toState = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TransitionType }
     *     
     */
    public TransitionType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransitionType }
     *     
     */
    public void setType(TransitionType value) {
        this.type = value;
    }

    /**
     * Gets the value of the fullyQualifiedPostIssueProjectName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFullyQualifiedPostIssueProjectName() {
        return fullyQualifiedPostIssueProjectName;
    }

    /**
     * Sets the value of the fullyQualifiedPostIssueProjectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFullyQualifiedPostIssueProjectName(JAXBElement<String> value) {
        this.fullyQualifiedPostIssueProjectName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the transitionAttributes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transitionAttributes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransitionAttributes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTransitionAttributes() {
        if (transitionAttributes == null) {
            transitionAttributes = new ArrayList<String>();
        }
        return this.transitionAttributes;
    }
}
