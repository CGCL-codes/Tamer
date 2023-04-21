package org.isurf.gdssu.datapool.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for synchronizationcatalog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="synchronizationcatalog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datapoolglntarget" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="partyglnsource" type="{http://ws.datapool.gdssu.isurf.org/}party" minOccurs="0"/>
 *         &lt;element name="partyglntarget" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "synchronizationcatalog", propOrder = { "datapoolglntarget", "id", "partyglnsource", "partyglntarget" })
public class Synchronizationcatalog {

    protected String datapoolglntarget;

    protected Integer id;

    protected Party partyglnsource;

    protected String partyglntarget;

    /**
     * Gets the value of the datapoolglntarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatapoolglntarget() {
        return datapoolglntarget;
    }

    /**
     * Sets the value of the datapoolglntarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatapoolglntarget(String value) {
        this.datapoolglntarget = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the partyglnsource property.
     * 
     * @return
     *     possible object is
     *     {@link Party }
     *     
     */
    public Party getPartyglnsource() {
        return partyglnsource;
    }

    /**
     * Sets the value of the partyglnsource property.
     * 
     * @param value
     *     allowed object is
     *     {@link Party }
     *     
     */
    public void setPartyglnsource(Party value) {
        this.partyglnsource = value;
    }

    /**
     * Gets the value of the partyglntarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyglntarget() {
        return partyglntarget;
    }

    /**
     * Sets the value of the partyglntarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyglntarget(String value) {
        this.partyglntarget = value;
    }
}
