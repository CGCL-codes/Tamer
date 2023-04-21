package com.patientis.model.xml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AllergyXml complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllergyXml">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reactionRef" type="{}DisplayXml"/>
 *         &lt;element name="reportDt" type="{}DateTimeXml"/>
 *         &lt;element name="reportDtOffset" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="reportUserRef" type="{}DisplayXml"/>
 *         &lt;element name="reportUserText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reportContactId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="severityRef" type="{}DisplayXml"/>
 *         &lt;element name="sensitivityRef" type="{}DisplayXml"/>
 *         &lt;element name="systemTransactionSeq" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="activeInd" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="allergyId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="allergyClassRef" type="{}DisplayXml"/>
 *         &lt;element name="onsetDt" type="{}DateTimeXml"/>
 *         &lt;element name="onsetDtOffset" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="onsetFreeText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="allergyStatusDt" type="{}DateTimeXml"/>
 *         &lt;element name="allergyStatusDtOffset" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="allergyStatusRef" type="{}DisplayXml"/>
 *         &lt;element name="allergyTypeRef" type="{}DisplayXml"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AllergyXml", propOrder = { "reactionRef", "reportDt", "reportDtOffset", "reportUserRef", "reportUserText", "reportContactId", "severityRef", "sensitivityRef", "systemTransactionSeq", "activeInd", "allergyId", "allergyClassRef", "onsetDt", "onsetDtOffset", "onsetFreeText", "allergyStatusDt", "allergyStatusDtOffset", "allergyStatusRef", "allergyTypeRef" })
public class AllergyXml {

    @XmlElement(required = true)
    protected DisplayXml reactionRef;

    @XmlElement(required = true)
    protected DateTimeXml reportDt;

    @XmlElement(required = true)
    protected BigInteger reportDtOffset;

    @XmlElement(required = true)
    protected DisplayXml reportUserRef;

    @XmlElement(required = true)
    protected String reportUserText;

    protected long reportContactId;

    @XmlElement(required = true)
    protected DisplayXml severityRef;

    @XmlElement(required = true)
    protected DisplayXml sensitivityRef;

    protected long systemTransactionSeq;

    @XmlElement(required = true)
    protected BigInteger activeInd;

    protected long allergyId;

    @XmlElement(required = true)
    protected DisplayXml allergyClassRef;

    @XmlElement(required = true)
    protected DateTimeXml onsetDt;

    @XmlElement(required = true)
    protected BigInteger onsetDtOffset;

    @XmlElement(required = true)
    protected String onsetFreeText;

    @XmlElement(required = true)
    protected DateTimeXml allergyStatusDt;

    @XmlElement(required = true)
    protected BigInteger allergyStatusDtOffset;

    @XmlElement(required = true)
    protected DisplayXml allergyStatusRef;

    @XmlElement(required = true)
    protected DisplayXml allergyTypeRef;

    /**
     * Gets the value of the reactionRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getReactionRef() {
        return reactionRef;
    }

    /**
     * Sets the value of the reactionRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setReactionRef(DisplayXml value) {
        this.reactionRef = value;
    }

    /**
     * Gets the value of the reportDt property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeXml }
     *     
     */
    public DateTimeXml getReportDt() {
        return reportDt;
    }

    /**
     * Sets the value of the reportDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeXml }
     *     
     */
    public void setReportDt(DateTimeXml value) {
        this.reportDt = value;
    }

    /**
     * Gets the value of the reportDtOffset property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReportDtOffset() {
        return reportDtOffset;
    }

    /**
     * Sets the value of the reportDtOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReportDtOffset(BigInteger value) {
        this.reportDtOffset = value;
    }

    /**
     * Gets the value of the reportUserRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getReportUserRef() {
        return reportUserRef;
    }

    /**
     * Sets the value of the reportUserRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setReportUserRef(DisplayXml value) {
        this.reportUserRef = value;
    }

    /**
     * Gets the value of the reportUserText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportUserText() {
        return reportUserText;
    }

    /**
     * Sets the value of the reportUserText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportUserText(String value) {
        this.reportUserText = value;
    }

    /**
     * Gets the value of the reportContactId property.
     * 
     */
    public long getReportContactId() {
        return reportContactId;
    }

    /**
     * Sets the value of the reportContactId property.
     * 
     */
    public void setReportContactId(long value) {
        this.reportContactId = value;
    }

    /**
     * Gets the value of the severityRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getSeverityRef() {
        return severityRef;
    }

    /**
     * Sets the value of the severityRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setSeverityRef(DisplayXml value) {
        this.severityRef = value;
    }

    /**
     * Gets the value of the sensitivityRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getSensitivityRef() {
        return sensitivityRef;
    }

    /**
     * Sets the value of the sensitivityRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setSensitivityRef(DisplayXml value) {
        this.sensitivityRef = value;
    }

    /**
     * Gets the value of the systemTransactionSeq property.
     * 
     */
    public long getSystemTransactionSeq() {
        return systemTransactionSeq;
    }

    /**
     * Sets the value of the systemTransactionSeq property.
     * 
     */
    public void setSystemTransactionSeq(long value) {
        this.systemTransactionSeq = value;
    }

    /**
     * Gets the value of the activeInd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getActiveInd() {
        return activeInd;
    }

    /**
     * Sets the value of the activeInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setActiveInd(BigInteger value) {
        this.activeInd = value;
    }

    /**
     * Gets the value of the allergyId property.
     * 
     */
    public long getAllergyId() {
        return allergyId;
    }

    /**
     * Sets the value of the allergyId property.
     * 
     */
    public void setAllergyId(long value) {
        this.allergyId = value;
    }

    /**
     * Gets the value of the allergyClassRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getAllergyClassRef() {
        return allergyClassRef;
    }

    /**
     * Sets the value of the allergyClassRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setAllergyClassRef(DisplayXml value) {
        this.allergyClassRef = value;
    }

    /**
     * Gets the value of the onsetDt property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeXml }
     *     
     */
    public DateTimeXml getOnsetDt() {
        return onsetDt;
    }

    /**
     * Sets the value of the onsetDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeXml }
     *     
     */
    public void setOnsetDt(DateTimeXml value) {
        this.onsetDt = value;
    }

    /**
     * Gets the value of the onsetDtOffset property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOnsetDtOffset() {
        return onsetDtOffset;
    }

    /**
     * Sets the value of the onsetDtOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOnsetDtOffset(BigInteger value) {
        this.onsetDtOffset = value;
    }

    /**
     * Gets the value of the onsetFreeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnsetFreeText() {
        return onsetFreeText;
    }

    /**
     * Sets the value of the onsetFreeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnsetFreeText(String value) {
        this.onsetFreeText = value;
    }

    /**
     * Gets the value of the allergyStatusDt property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeXml }
     *     
     */
    public DateTimeXml getAllergyStatusDt() {
        return allergyStatusDt;
    }

    /**
     * Sets the value of the allergyStatusDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeXml }
     *     
     */
    public void setAllergyStatusDt(DateTimeXml value) {
        this.allergyStatusDt = value;
    }

    /**
     * Gets the value of the allergyStatusDtOffset property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAllergyStatusDtOffset() {
        return allergyStatusDtOffset;
    }

    /**
     * Sets the value of the allergyStatusDtOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAllergyStatusDtOffset(BigInteger value) {
        this.allergyStatusDtOffset = value;
    }

    /**
     * Gets the value of the allergyStatusRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getAllergyStatusRef() {
        return allergyStatusRef;
    }

    /**
     * Sets the value of the allergyStatusRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setAllergyStatusRef(DisplayXml value) {
        this.allergyStatusRef = value;
    }

    /**
     * Gets the value of the allergyTypeRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getAllergyTypeRef() {
        return allergyTypeRef;
    }

    /**
     * Sets the value of the allergyTypeRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setAllergyTypeRef(DisplayXml value) {
        this.allergyTypeRef = value;
    }
}
