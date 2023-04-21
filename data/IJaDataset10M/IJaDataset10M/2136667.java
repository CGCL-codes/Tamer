package com.patientis.model.xml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PatientPortalUserXml complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PatientPortalUserXml">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="patientPortalUser" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="portalRoleRef" type="{}DisplayXml"/>
 *         &lt;element name="activeInd" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="roleEndDt" type="{}DateTimeXml"/>
 *         &lt;element name="roleEndDtOffset" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="roleStartDt" type="{}DateTimeXml"/>
 *         &lt;element name="roleStartDtOffset" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="patientId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="systemTransactionSeq" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="portalUser" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientPortalUserXml", propOrder = { "patientPortalUser", "portalRoleRef", "activeInd", "roleEndDt", "roleEndDtOffset", "roleStartDt", "roleStartDtOffset", "patientId", "systemTransactionSeq", "portalUser" })
public class PatientPortalUserXml {

    protected long patientPortalUser;

    @XmlElement(required = true)
    protected DisplayXml portalRoleRef;

    @XmlElement(required = true)
    protected BigInteger activeInd;

    @XmlElement(required = true)
    protected DateTimeXml roleEndDt;

    @XmlElement(required = true)
    protected BigInteger roleEndDtOffset;

    @XmlElement(required = true)
    protected DateTimeXml roleStartDt;

    @XmlElement(required = true)
    protected BigInteger roleStartDtOffset;

    protected long patientId;

    protected long systemTransactionSeq;

    protected long portalUser;

    /**
     * Gets the value of the patientPortalUser property.
     * 
     */
    public long getPatientPortalUser() {
        return patientPortalUser;
    }

    /**
     * Sets the value of the patientPortalUser property.
     * 
     */
    public void setPatientPortalUser(long value) {
        this.patientPortalUser = value;
    }

    /**
     * Gets the value of the portalRoleRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getPortalRoleRef() {
        return portalRoleRef;
    }

    /**
     * Sets the value of the portalRoleRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setPortalRoleRef(DisplayXml value) {
        this.portalRoleRef = value;
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
     * Gets the value of the roleEndDt property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeXml }
     *     
     */
    public DateTimeXml getRoleEndDt() {
        return roleEndDt;
    }

    /**
     * Sets the value of the roleEndDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeXml }
     *     
     */
    public void setRoleEndDt(DateTimeXml value) {
        this.roleEndDt = value;
    }

    /**
     * Gets the value of the roleEndDtOffset property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRoleEndDtOffset() {
        return roleEndDtOffset;
    }

    /**
     * Sets the value of the roleEndDtOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRoleEndDtOffset(BigInteger value) {
        this.roleEndDtOffset = value;
    }

    /**
     * Gets the value of the roleStartDt property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeXml }
     *     
     */
    public DateTimeXml getRoleStartDt() {
        return roleStartDt;
    }

    /**
     * Sets the value of the roleStartDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeXml }
     *     
     */
    public void setRoleStartDt(DateTimeXml value) {
        this.roleStartDt = value;
    }

    /**
     * Gets the value of the roleStartDtOffset property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRoleStartDtOffset() {
        return roleStartDtOffset;
    }

    /**
     * Sets the value of the roleStartDtOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRoleStartDtOffset(BigInteger value) {
        this.roleStartDtOffset = value;
    }

    /**
     * Gets the value of the patientId property.
     * 
     */
    public long getPatientId() {
        return patientId;
    }

    /**
     * Sets the value of the patientId property.
     * 
     */
    public void setPatientId(long value) {
        this.patientId = value;
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
     * Gets the value of the portalUser property.
     * 
     */
    public long getPortalUser() {
        return portalUser;
    }

    /**
     * Sets the value of the portalUser property.
     * 
     */
    public void setPortalUser(long value) {
        this.portalUser = value;
    }
}
