package oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentMeansIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PenaltySurchargePercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PrepaidPaymentReferenceIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReferenceEventCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.SettlementDiscountPercentType;

/**
 * <p>Java class for PaymentTermsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentTermsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ID" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}PaymentMeansID" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}PrepaidPaymentReferenceID" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Note" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}ReferenceEventCode" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}SettlementDiscountPercent" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}PenaltySurchargePercent" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2}Amount" minOccurs="0"/>
 *         &lt;element name="SettlementPeriod" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}PeriodType" minOccurs="0"/>
 *         &lt;element name="PenaltyPeriod" type="{urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2}PeriodType" minOccurs="0"/>
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
@XmlType(name = "PaymentTermsType", propOrder = { "id", "paymentMeansID", "prepaidPaymentReferenceID", "note", "referenceEventCode", "settlementDiscountPercent", "penaltySurchargePercent", "amount", "settlementPeriod", "penaltyPeriod" })
public class PaymentTermsType {

    @XmlElement(name = "ID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected IDType id;

    @XmlElement(name = "PaymentMeansID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected PaymentMeansIDType paymentMeansID;

    @XmlElement(name = "PrepaidPaymentReferenceID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected PrepaidPaymentReferenceIDType prepaidPaymentReferenceID;

    @XmlElement(name = "Note", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected List<NoteType> note;

    @XmlElement(name = "ReferenceEventCode", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected ReferenceEventCodeType referenceEventCode;

    @XmlElement(name = "SettlementDiscountPercent", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected SettlementDiscountPercentType settlementDiscountPercent;

    @XmlElement(name = "PenaltySurchargePercent", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected PenaltySurchargePercentType penaltySurchargePercent;

    @XmlElement(name = "Amount", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
    protected AmountType amount;

    @XmlElement(name = "SettlementPeriod")
    protected PeriodType settlementPeriod;

    @XmlElement(name = "PenaltyPeriod")
    protected PeriodType penaltyPeriod;

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
     * Gets the value of the paymentMeansID property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentMeansIDType }
     *     
     */
    public PaymentMeansIDType getPaymentMeansID() {
        return paymentMeansID;
    }

    /**
     * Sets the value of the paymentMeansID property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentMeansIDType }
     *     
     */
    public void setPaymentMeansID(PaymentMeansIDType value) {
        this.paymentMeansID = value;
    }

    /**
     * Gets the value of the prepaidPaymentReferenceID property.
     * 
     * @return
     *     possible object is
     *     {@link PrepaidPaymentReferenceIDType }
     *     
     */
    public PrepaidPaymentReferenceIDType getPrepaidPaymentReferenceID() {
        return prepaidPaymentReferenceID;
    }

    /**
     * Sets the value of the prepaidPaymentReferenceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrepaidPaymentReferenceIDType }
     *     
     */
    public void setPrepaidPaymentReferenceID(PrepaidPaymentReferenceIDType value) {
        this.prepaidPaymentReferenceID = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NoteType }
     * 
     * 
     */
    public List<NoteType> getNote() {
        if (note == null) {
            note = new ArrayList<NoteType>();
        }
        return this.note;
    }

    /**
     * Gets the value of the referenceEventCode property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceEventCodeType }
     *     
     */
    public ReferenceEventCodeType getReferenceEventCode() {
        return referenceEventCode;
    }

    /**
     * Sets the value of the referenceEventCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceEventCodeType }
     *     
     */
    public void setReferenceEventCode(ReferenceEventCodeType value) {
        this.referenceEventCode = value;
    }

    /**
     * Gets the value of the settlementDiscountPercent property.
     * 
     * @return
     *     possible object is
     *     {@link SettlementDiscountPercentType }
     *     
     */
    public SettlementDiscountPercentType getSettlementDiscountPercent() {
        return settlementDiscountPercent;
    }

    /**
     * Sets the value of the settlementDiscountPercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link SettlementDiscountPercentType }
     *     
     */
    public void setSettlementDiscountPercent(SettlementDiscountPercentType value) {
        this.settlementDiscountPercent = value;
    }

    /**
     * Gets the value of the penaltySurchargePercent property.
     * 
     * @return
     *     possible object is
     *     {@link PenaltySurchargePercentType }
     *     
     */
    public PenaltySurchargePercentType getPenaltySurchargePercent() {
        return penaltySurchargePercent;
    }

    /**
     * Sets the value of the penaltySurchargePercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link PenaltySurchargePercentType }
     *     
     */
    public void setPenaltySurchargePercent(PenaltySurchargePercentType value) {
        this.penaltySurchargePercent = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setAmount(AmountType value) {
        this.amount = value;
    }

    /**
     * Gets the value of the settlementPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link PeriodType }
     *     
     */
    public PeriodType getSettlementPeriod() {
        return settlementPeriod;
    }

    /**
     * Sets the value of the settlementPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeriodType }
     *     
     */
    public void setSettlementPeriod(PeriodType value) {
        this.settlementPeriod = value;
    }

    /**
     * Gets the value of the penaltyPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link PeriodType }
     *     
     */
    public PeriodType getPenaltyPeriod() {
        return penaltyPeriod;
    }

    /**
     * Sets the value of the penaltyPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeriodType }
     *     
     */
    public void setPenaltyPeriod(PeriodType value) {
        this.penaltyPeriod = value;
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
