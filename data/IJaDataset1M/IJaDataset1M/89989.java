package com.serena.xmlbridge.adapter.aewebservice.gen;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="auth" type="{urn:aewebservices71}Auth" minOccurs="0"/>
 *         &lt;element name="queryRange" type="{urn:aewebservices71}QueryRange" minOccurs="0"/>
 *         &lt;element name="reportUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="solutionID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="solutionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="projectID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="projectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="projectUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tableID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="tableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportCategory" type="{urn:aewebservices71}ReportCategory" minOccurs="0"/>
 *         &lt;element name="reportAccessLevel" type="{urn:aewebservices71}ReportAccessLevel" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "auth", "queryRange", "reportUUID", "reportName", "reportID", "solutionID", "solutionName", "projectID", "projectName", "projectUUID", "tableID", "tableName", "reportCategory", "reportAccessLevel" })
@XmlRootElement(name = "RunReport")
public class RunReport {

    @XmlElementRef(name = "auth", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<Auth> auth;

    @XmlElementRef(name = "queryRange", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<QueryRange> queryRange;

    @XmlElementRef(name = "reportUUID", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> reportUUID;

    @XmlElementRef(name = "reportName", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> reportName;

    protected BigInteger reportID;

    protected BigInteger solutionID;

    @XmlElementRef(name = "solutionName", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> solutionName;

    protected BigInteger projectID;

    @XmlElementRef(name = "projectName", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> projectName;

    @XmlElementRef(name = "projectUUID", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> projectUUID;

    protected BigInteger tableID;

    @XmlElementRef(name = "tableName", namespace = "urn:aewebservices71", type = JAXBElement.class)
    protected JAXBElement<String> tableName;

    protected ReportCategory reportCategory;

    protected ReportAccessLevel reportAccessLevel;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Auth }{@code >}
     *     
     */
    public JAXBElement<Auth> getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Auth }{@code >}
     *     
     */
    public void setAuth(JAXBElement<Auth> value) {
        this.auth = ((JAXBElement<Auth>) value);
    }

    /**
     * Gets the value of the queryRange property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QueryRange }{@code >}
     *     
     */
    public JAXBElement<QueryRange> getQueryRange() {
        return queryRange;
    }

    /**
     * Sets the value of the queryRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QueryRange }{@code >}
     *     
     */
    public void setQueryRange(JAXBElement<QueryRange> value) {
        this.queryRange = ((JAXBElement<QueryRange>) value);
    }

    /**
     * Gets the value of the reportUUID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReportUUID() {
        return reportUUID;
    }

    /**
     * Sets the value of the reportUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReportUUID(JAXBElement<String> value) {
        this.reportUUID = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the reportName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReportName() {
        return reportName;
    }

    /**
     * Sets the value of the reportName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReportName(JAXBElement<String> value) {
        this.reportName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the reportID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReportID() {
        return reportID;
    }

    /**
     * Sets the value of the reportID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReportID(BigInteger value) {
        this.reportID = value;
    }

    /**
     * Gets the value of the solutionID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSolutionID() {
        return solutionID;
    }

    /**
     * Sets the value of the solutionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSolutionID(BigInteger value) {
        this.solutionID = value;
    }

    /**
     * Gets the value of the solutionName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSolutionName() {
        return solutionName;
    }

    /**
     * Sets the value of the solutionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSolutionName(JAXBElement<String> value) {
        this.solutionName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the projectID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getProjectID() {
        return projectID;
    }

    /**
     * Sets the value of the projectID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setProjectID(BigInteger value) {
        this.projectID = value;
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProjectName(JAXBElement<String> value) {
        this.projectName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the projectUUID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProjectUUID() {
        return projectUUID;
    }

    /**
     * Sets the value of the projectUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProjectUUID(JAXBElement<String> value) {
        this.projectUUID = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the tableID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTableID() {
        return tableID;
    }

    /**
     * Sets the value of the tableID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTableID(BigInteger value) {
        this.tableID = value;
    }

    /**
     * Gets the value of the tableName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTableName() {
        return tableName;
    }

    /**
     * Sets the value of the tableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTableName(JAXBElement<String> value) {
        this.tableName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the reportCategory property.
     * 
     * @return
     *     possible object is
     *     {@link ReportCategory }
     *     
     */
    public ReportCategory getReportCategory() {
        return reportCategory;
    }

    /**
     * Sets the value of the reportCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportCategory }
     *     
     */
    public void setReportCategory(ReportCategory value) {
        this.reportCategory = value;
    }

    /**
     * Gets the value of the reportAccessLevel property.
     * 
     * @return
     *     possible object is
     *     {@link ReportAccessLevel }
     *     
     */
    public ReportAccessLevel getReportAccessLevel() {
        return reportAccessLevel;
    }

    /**
     * Sets the value of the reportAccessLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportAccessLevel }
     *     
     */
    public void setReportAccessLevel(ReportAccessLevel value) {
        this.reportAccessLevel = value;
    }
}
