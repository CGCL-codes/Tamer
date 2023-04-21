package com.serena.xmlbridge.adapter.ttwebservice.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="auth" type="{urn:ttwebservices}Auth" minOccurs="0"/>
 *         &lt;element name="itemList" type="{urn:ttwebservices}TTItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="transitionId" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "auth", "itemList", "transitionId" })
@XmlRootElement(name = "UpdateItems")
public class UpdateItems {

    @XmlElementRef(name = "auth", namespace = "urn:ttwebservices", type = JAXBElement.class)
    protected JAXBElement<Auth> auth;

    protected List<TTItem> itemList;

    @XmlElement(required = true)
    protected BigInteger transitionId;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Auth }{@code >}
     *     
     */
    public JAXBElement<Auth> getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Auth }{@code >}
     *     
     */
    public void setAuth(JAXBElement<Auth> value) {
        this.auth = ((JAXBElement<Auth>) value);
    }

    /**
     * Gets the value of the itemList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TTItem }
     * 
     * 
     */
    public List<TTItem> getItemList() {
        if (itemList == null) {
            itemList = new ArrayList<TTItem>();
        }
        return this.itemList;
    }

    /**
     * Gets the value of the transitionId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTransitionId() {
        return transitionId;
    }

    /**
     * Sets the value of the transitionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTransitionId(BigInteger value) {
        this.transitionId = value;
    }
}
