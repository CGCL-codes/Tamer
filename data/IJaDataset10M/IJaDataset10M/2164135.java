package org.ist_contract.messageontology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.ist_contract.domainontology.Actor;
import org.ist_contract.domainontology.PurchaseOrder;

/**
 * <p>Java class for submitOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Buyer" type="{http://www.ist-contract.org/DomainOntology}Actor"/>
 *         &lt;element name="Seller" type="{http://www.ist-contract.org/DomainOntology}Actor"/>
 *         &lt;element ref="{http://www.ist-contract.org/DomainOntology}PurchaseOrder"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitOrder", propOrder = { "buyer", "seller", "purchaseOrder" })
public class SubmitOrder {

    @XmlElement(name = "Buyer", required = true)
    protected Actor buyer;

    @XmlElement(name = "Seller", required = true)
    protected Actor seller;

    @XmlElement(name = "PurchaseOrder", namespace = "http://www.ist-contract.org/DomainOntology", required = true)
    protected PurchaseOrder purchaseOrder;

    /**
     * Gets the value of the buyer property.
     * 
     * @return
     *     possible object is
     *     {@link Actor }
     *     
     */
    public Actor getBuyer() {
        return buyer;
    }

    /**
     * Sets the value of the buyer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actor }
     *     
     */
    public void setBuyer(Actor value) {
        this.buyer = value;
    }

    /**
     * Gets the value of the seller property.
     * 
     * @return
     *     possible object is
     *     {@link Actor }
     *     
     */
    public Actor getSeller() {
        return seller;
    }

    /**
     * Sets the value of the seller property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actor }
     *     
     */
    public void setSeller(Actor value) {
        this.seller = value;
    }

    /**
     * Gets the value of the purchaseOrder property.
     * 
     * @return
     *     possible object is
     *     {@link PurchaseOrder }
     *     
     */
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    /**
     * Sets the value of the purchaseOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link PurchaseOrder }
     *     
     */
    public void setPurchaseOrder(PurchaseOrder value) {
        this.purchaseOrder = value;
    }
}
