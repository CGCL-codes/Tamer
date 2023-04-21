package net.sublight.webservice;

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
 *         &lt;element name="AddHashLinkAutomatic4Result" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="points" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addHashLinkAutomatic4Result", "points", "error" })
@XmlRootElement(name = "AddHashLinkAutomatic4Response")
public class AddHashLinkAutomatic4Response {

    @XmlElement(name = "AddHashLinkAutomatic4Result")
    protected boolean addHashLinkAutomatic4Result;

    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double points;

    protected String error;

    /**
     * Gets the value of the addHashLinkAutomatic4Result property.
     * 
     */
    public boolean isAddHashLinkAutomatic4Result() {
        return addHashLinkAutomatic4Result;
    }

    /**
     * Sets the value of the addHashLinkAutomatic4Result property.
     * 
     */
    public void setAddHashLinkAutomatic4Result(boolean value) {
        this.addHashLinkAutomatic4Result = value;
    }

    /**
     * Gets the value of the points property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPoints() {
        return points;
    }

    /**
     * Sets the value of the points property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPoints(Double value) {
        this.points = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }
}
