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
 *         &lt;element name="ReportModel" type="{}ReportXml"/>
 *         &lt;element name="Transaction" type="{}InterfaceTransaction"/>
 *         &lt;element name="BeforeReportModel" type="{}ReportXml"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "reportModel", "transaction", "beforeReportModel" })
@XmlRootElement(name = "ReportTransaction")
public class ReportTransaction {

    @XmlElement(name = "ReportModel", required = true)
    protected ReportXml reportModel;

    @XmlElement(name = "Transaction", required = true)
    protected InterfaceTransaction transaction;

    @XmlElement(name = "BeforeReportModel", required = true)
    protected ReportXml beforeReportModel;

    /**
     * Gets the value of the reportModel property.
     * 
     * @return
     *     possible object is
     *     {@link ReportXml }
     *     
     */
    public ReportXml getReportModel() {
        return reportModel;
    }

    /**
     * Sets the value of the reportModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportXml }
     *     
     */
    public void setReportModel(ReportXml value) {
        this.reportModel = value;
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
     * Gets the value of the beforeReportModel property.
     * 
     * @return
     *     possible object is
     *     {@link ReportXml }
     *     
     */
    public ReportXml getBeforeReportModel() {
        return beforeReportModel;
    }

    /**
     * Sets the value of the beforeReportModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportXml }
     *     
     */
    public void setBeforeReportModel(ReportXml value) {
        this.beforeReportModel = value;
    }
}
