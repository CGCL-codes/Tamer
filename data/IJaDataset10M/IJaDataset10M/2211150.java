package oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PurchaseConditionsLineType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PurchaseConditionsLineType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shipToParty" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}PartyType"/>
 *         &lt;element name="shipFromParty" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}PartyType"/>
 *         &lt;element name="PurchaseConditionsLineItem" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}PurchaseConditionsLineItemType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Hjid" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PurchaseConditionsLineType", propOrder = { "shipToParty", "shipFromParty", "purchaseConditionsLineItem" })
public class PurchaseConditionsLineType {

    @XmlElement(required = true)
    protected PartyType shipToParty;

    @XmlElement(required = true)
    protected PartyType shipFromParty;

    @XmlElement(name = "PurchaseConditionsLineItem", required = true)
    protected List<PurchaseConditionsLineItemType> purchaseConditionsLineItem;

    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * Gets the value of the shipToParty property.
     * 
     * @return
     *     possible object is
     *     {@link PartyType }
     *     
     */
    public PartyType getShipToParty() {
        return shipToParty;
    }

    /**
     * Sets the value of the shipToParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyType }
     *     
     */
    public void setShipToParty(PartyType value) {
        this.shipToParty = value;
    }

    /**
     * Gets the value of the shipFromParty property.
     * 
     * @return
     *     possible object is
     *     {@link PartyType }
     *     
     */
    public PartyType getShipFromParty() {
        return shipFromParty;
    }

    /**
     * Sets the value of the shipFromParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyType }
     *     
     */
    public void setShipFromParty(PartyType value) {
        this.shipFromParty = value;
    }

    /**
     * Gets the value of the purchaseConditionsLineItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the purchaseConditionsLineItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPurchaseConditionsLineItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PurchaseConditionsLineItemType }
     * 
     * 
     */
    public List<PurchaseConditionsLineItemType> getPurchaseConditionsLineItem() {
        if (purchaseConditionsLineItem == null) {
            purchaseConditionsLineItem = new ArrayList<PurchaseConditionsLineItemType>();
        }
        return this.purchaseConditionsLineItem;
    }

    /**
     * Gets the value of the hjid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHjid() {
        return hjid;
    }

    /**
     * Sets the value of the hjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }
}
