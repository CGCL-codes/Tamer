package org.webhop.ywdc.webservices.ups.ratingserviceselectionresponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RatedPackageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RatedPackageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransportationCharges" type="{}TransportationChargesType" minOccurs="0"/>
 *         &lt;element name="ServiceOptionsCharges" type="{}ServiceOptionsChargesType" minOccurs="0"/>
 *         &lt;element name="TotalCharges" type="{}TotalChargesType" minOccurs="0"/>
 *         &lt;element name="Weight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BillingWeight" type="{}BillingWeightType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatedPackageType", propOrder = { "transportationCharges", "serviceOptionsCharges", "totalCharges", "weight", "billingWeight" })
public class RatedPackageType {

    @XmlElement(name = "TransportationCharges")
    protected TransportationChargesType transportationCharges;

    @XmlElement(name = "ServiceOptionsCharges")
    protected ServiceOptionsChargesType serviceOptionsCharges;

    @XmlElement(name = "TotalCharges")
    protected TotalChargesType totalCharges;

    @XmlElement(name = "Weight")
    protected String weight;

    @XmlElement(name = "BillingWeight")
    protected BillingWeightType billingWeight;

    /**
     * Gets the value of the transportationCharges property.
     * 
     * @return
     *     possible object is
     *     {@link TransportationChargesType }
     *     
     */
    public TransportationChargesType getTransportationCharges() {
        return transportationCharges;
    }

    /**
     * Sets the value of the transportationCharges property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransportationChargesType }
     *     
     */
    public void setTransportationCharges(TransportationChargesType value) {
        this.transportationCharges = value;
    }

    /**
     * Gets the value of the serviceOptionsCharges property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceOptionsChargesType }
     *     
     */
    public ServiceOptionsChargesType getServiceOptionsCharges() {
        return serviceOptionsCharges;
    }

    /**
     * Sets the value of the serviceOptionsCharges property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceOptionsChargesType }
     *     
     */
    public void setServiceOptionsCharges(ServiceOptionsChargesType value) {
        this.serviceOptionsCharges = value;
    }

    /**
     * Gets the value of the totalCharges property.
     * 
     * @return
     *     possible object is
     *     {@link TotalChargesType }
     *     
     */
    public TotalChargesType getTotalCharges() {
        return totalCharges;
    }

    /**
     * Sets the value of the totalCharges property.
     * 
     * @param value
     *     allowed object is
     *     {@link TotalChargesType }
     *     
     */
    public void setTotalCharges(TotalChargesType value) {
        this.totalCharges = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * Gets the value of the billingWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BillingWeightType }
     *     
     */
    public BillingWeightType getBillingWeight() {
        return billingWeight;
    }

    /**
     * Sets the value of the billingWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingWeightType }
     *     
     */
    public void setBillingWeight(BillingWeightType value) {
        this.billingWeight = value;
    }
}
