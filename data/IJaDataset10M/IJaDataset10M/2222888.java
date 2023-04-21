package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for MonitoringStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitoringStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="label" type="{http://www.biocatalogue.org/2009/xml/rest}MonitoringStatusLabel"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="symbol" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="smallSymbol" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="lastChecked" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitoringStatus", propOrder = { "label", "message", "symbol", "smallSymbol", "lastChecked" })
public class MonitoringStatus {

    @XmlElement(required = true)
    protected MonitoringStatusLabel label;

    @XmlElement(required = true)
    protected String message;

    @XmlElement(required = true)
    protected ResourceLink symbol;

    @XmlElement(required = true)
    protected ResourceLink smallSymbol;

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastChecked;

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoringStatusLabel }
     *     
     */
    public MonitoringStatusLabel getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoringStatusLabel }
     *     
     */
    public void setLabel(MonitoringStatusLabel value) {
        this.label = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the symbol property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getSymbol() {
        return symbol;
    }

    /**
     * Sets the value of the symbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setSymbol(ResourceLink value) {
        this.symbol = value;
    }

    /**
     * Gets the value of the smallSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getSmallSymbol() {
        return smallSymbol;
    }

    /**
     * Sets the value of the smallSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setSmallSymbol(ResourceLink value) {
        this.smallSymbol = value;
    }

    /**
     * Gets the value of the lastChecked property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastChecked() {
        return lastChecked;
    }

    /**
     * Sets the value of the lastChecked property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastChecked(XMLGregorianCalendar value) {
        this.lastChecked = value;
    }
}
