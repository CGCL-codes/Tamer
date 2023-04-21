package oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ActualDespatchDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ActualDespatchTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EstimatedDespatchDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EstimatedDespatchTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RequestedDespatchDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RequestedDespatchTimeType;

/**
 * <p>Java class for DespatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DespatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ID" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}RequestedDespatchDate" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}RequestedDespatchTime" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}EstimatedDespatchDate" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}EstimatedDespatchTime" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ActualDespatchDate" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ActualDespatchTime" minOccurs="0"/>
 *         &lt;element name="DespatchAddress" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}AddressType" minOccurs="0"/>
 *         &lt;element name="DespatchParty" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}PartyType" minOccurs="0"/>
 *         &lt;element name="Contact" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}ContactType" minOccurs="0"/>
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
@XmlType(name = "DespatchType", propOrder = { "id", "requestedDespatchDate", "requestedDespatchTime", "estimatedDespatchDate", "estimatedDespatchTime", "actualDespatchDate", "actualDespatchTime", "despatchAddress", "despatchParty", "contact" })
public class DespatchType {

    @XmlElement(name = "ID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected IDType id;

    @XmlElement(name = "RequestedDespatchDate", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected RequestedDespatchDateType requestedDespatchDate;

    @XmlElement(name = "RequestedDespatchTime", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected RequestedDespatchTimeType requestedDespatchTime;

    @XmlElement(name = "EstimatedDespatchDate", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected EstimatedDespatchDateType estimatedDespatchDate;

    @XmlElement(name = "EstimatedDespatchTime", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected EstimatedDespatchTimeType estimatedDespatchTime;

    @XmlElement(name = "ActualDespatchDate", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected ActualDespatchDateType actualDespatchDate;

    @XmlElement(name = "ActualDespatchTime", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected ActualDespatchTimeType actualDespatchTime;

    @XmlElement(name = "DespatchAddress")
    protected AddressType despatchAddress;

    @XmlElement(name = "DespatchParty")
    protected PartyType despatchParty;

    @XmlElement(name = "Contact")
    protected ContactType contact;

    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link IDType }
     *     
     */
    public IDType getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDType }
     *     
     */
    public void setID(IDType value) {
        this.id = value;
    }

    /**
     * Gets the value of the requestedDespatchDate property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedDespatchDateType }
     *     
     */
    public RequestedDespatchDateType getRequestedDespatchDate() {
        return requestedDespatchDate;
    }

    /**
     * Sets the value of the requestedDespatchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedDespatchDateType }
     *     
     */
    public void setRequestedDespatchDate(RequestedDespatchDateType value) {
        this.requestedDespatchDate = value;
    }

    /**
     * Gets the value of the requestedDespatchTime property.
     * 
     * @return
     *     possible object is
     *     {@link RequestedDespatchTimeType }
     *     
     */
    public RequestedDespatchTimeType getRequestedDespatchTime() {
        return requestedDespatchTime;
    }

    /**
     * Sets the value of the requestedDespatchTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestedDespatchTimeType }
     *     
     */
    public void setRequestedDespatchTime(RequestedDespatchTimeType value) {
        this.requestedDespatchTime = value;
    }

    /**
     * Gets the value of the estimatedDespatchDate property.
     * 
     * @return
     *     possible object is
     *     {@link EstimatedDespatchDateType }
     *     
     */
    public EstimatedDespatchDateType getEstimatedDespatchDate() {
        return estimatedDespatchDate;
    }

    /**
     * Sets the value of the estimatedDespatchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstimatedDespatchDateType }
     *     
     */
    public void setEstimatedDespatchDate(EstimatedDespatchDateType value) {
        this.estimatedDespatchDate = value;
    }

    /**
     * Gets the value of the estimatedDespatchTime property.
     * 
     * @return
     *     possible object is
     *     {@link EstimatedDespatchTimeType }
     *     
     */
    public EstimatedDespatchTimeType getEstimatedDespatchTime() {
        return estimatedDespatchTime;
    }

    /**
     * Sets the value of the estimatedDespatchTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstimatedDespatchTimeType }
     *     
     */
    public void setEstimatedDespatchTime(EstimatedDespatchTimeType value) {
        this.estimatedDespatchTime = value;
    }

    /**
     * Gets the value of the actualDespatchDate property.
     * 
     * @return
     *     possible object is
     *     {@link ActualDespatchDateType }
     *     
     */
    public ActualDespatchDateType getActualDespatchDate() {
        return actualDespatchDate;
    }

    /**
     * Sets the value of the actualDespatchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActualDespatchDateType }
     *     
     */
    public void setActualDespatchDate(ActualDespatchDateType value) {
        this.actualDespatchDate = value;
    }

    /**
     * Gets the value of the actualDespatchTime property.
     * 
     * @return
     *     possible object is
     *     {@link ActualDespatchTimeType }
     *     
     */
    public ActualDespatchTimeType getActualDespatchTime() {
        return actualDespatchTime;
    }

    /**
     * Sets the value of the actualDespatchTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActualDespatchTimeType }
     *     
     */
    public void setActualDespatchTime(ActualDespatchTimeType value) {
        this.actualDespatchTime = value;
    }

    /**
     * Gets the value of the despatchAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getDespatchAddress() {
        return despatchAddress;
    }

    /**
     * Sets the value of the despatchAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setDespatchAddress(AddressType value) {
        this.despatchAddress = value;
    }

    /**
     * Gets the value of the despatchParty property.
     * 
     * @return
     *     possible object is
     *     {@link PartyType }
     *     
     */
    public PartyType getDespatchParty() {
        return despatchParty;
    }

    /**
     * Sets the value of the despatchParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyType }
     *     
     */
    public void setDespatchParty(PartyType value) {
        this.despatchParty = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link ContactType }
     *     
     */
    public ContactType getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactType }
     *     
     */
    public void setContact(ContactType value) {
        this.contact = value;
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
