package com.patientis.model.xml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ApplicationViewRuleXml complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationViewRuleXml">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="businessRuleRef" type="{}DisplayXml"/>
 *         &lt;element name="applicationViewRuleId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="activeInd" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="systemTransactionSeq" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ruleSequence" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="applicationViewId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationViewRuleXml", propOrder = { "businessRuleRef", "applicationViewRuleId", "activeInd", "systemTransactionSeq", "ruleSequence", "applicationViewId" })
public class ApplicationViewRuleXml {

    @XmlElement(required = true)
    protected DisplayXml businessRuleRef;

    protected long applicationViewRuleId;

    @XmlElement(required = true)
    protected BigInteger activeInd;

    protected long systemTransactionSeq;

    @XmlElement(required = true)
    protected BigInteger ruleSequence;

    protected long applicationViewId;

    /**
     * Gets the value of the businessRuleRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getBusinessRuleRef() {
        return businessRuleRef;
    }

    /**
     * Sets the value of the businessRuleRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setBusinessRuleRef(DisplayXml value) {
        this.businessRuleRef = value;
    }

    /**
     * Gets the value of the applicationViewRuleId property.
     * 
     */
    public long getApplicationViewRuleId() {
        return applicationViewRuleId;
    }

    /**
     * Sets the value of the applicationViewRuleId property.
     * 
     */
    public void setApplicationViewRuleId(long value) {
        this.applicationViewRuleId = value;
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
     * Gets the value of the ruleSequence property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRuleSequence() {
        return ruleSequence;
    }

    /**
     * Sets the value of the ruleSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRuleSequence(BigInteger value) {
        this.ruleSequence = value;
    }

    /**
     * Gets the value of the applicationViewId property.
     * 
     */
    public long getApplicationViewId() {
        return applicationViewId;
    }

    /**
     * Sets the value of the applicationViewId property.
     * 
     */
    public void setApplicationViewId(long value) {
        this.applicationViewId = value;
    }
}
