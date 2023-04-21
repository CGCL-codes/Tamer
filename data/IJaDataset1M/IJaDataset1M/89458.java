package com.patientis.model.xml;

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
 *         &lt;element name="PatientModel" type="{}PatientXml"/>
 *         &lt;element name="Transaction" type="{}InterfaceTransaction"/>
 *         &lt;element name="BeforePatientModel" type="{}PatientXml"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "patientModel", "transaction", "beforePatientModel" })
@XmlRootElement(name = "PatientTransaction")
public class PatientTransaction {

    @XmlElement(name = "PatientModel", required = true)
    protected PatientXml patientModel;

    @XmlElement(name = "Transaction", required = true)
    protected InterfaceTransaction transaction;

    @XmlElement(name = "BeforePatientModel", required = true)
    protected PatientXml beforePatientModel;

    /**
     * Gets the value of the patientModel property.
     * 
     * @return
     *     possible object is
     *     {@link PatientXml }
     *     
     */
    public PatientXml getPatientModel() {
        return patientModel;
    }

    /**
     * Sets the value of the patientModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientXml }
     *     
     */
    public void setPatientModel(PatientXml value) {
        this.patientModel = value;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * @return
     *     possible object is
     *     {@link InterfaceTransaction }
     *     
     */
    public InterfaceTransaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the value of the transaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterfaceTransaction }
     *     
     */
    public void setTransaction(InterfaceTransaction value) {
        this.transaction = value;
    }

    /**
     * Gets the value of the beforePatientModel property.
     * 
     * @return
     *     possible object is
     *     {@link PatientXml }
     *     
     */
    public PatientXml getBeforePatientModel() {
        return beforePatientModel;
    }

    /**
     * Sets the value of the beforePatientModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientXml }
     *     
     */
    public void setBeforePatientModel(PatientXml value) {
        this.beforePatientModel = value;
    }
}
