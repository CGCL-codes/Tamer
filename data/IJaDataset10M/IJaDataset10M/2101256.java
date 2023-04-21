package org.ist_contract.domainontology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for good complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="good">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="goodId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="goodDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "good", propOrder = { "goodId", "orderId", "goodDescription" })
public class Good {

    protected int goodId;

    protected int orderId;

    @XmlElement(required = true)
    protected String goodDescription;

    /**
     * Gets the value of the goodId property.
     * 
     */
    public int getGoodId() {
        return goodId;
    }

    /**
     * Sets the value of the goodId property.
     * 
     */
    public void setGoodId(int value) {
        this.goodId = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     */
    public void setOrderId(int value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the goodDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGoodDescription() {
        return goodDescription;
    }

    /**
     * Sets the value of the goodDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGoodDescription(String value) {
        this.goodDescription = value;
    }
}
