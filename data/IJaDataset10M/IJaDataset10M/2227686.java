package com.patientis.model.xml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for LabTestXml complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LabTestXml">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="labTestRef" type="{}DisplayXml"/>
 *         &lt;element name="labTestId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="activeInd" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="valueUnit2Ref" type="{}DisplayXml"/>
 *         &lt;element name="valueUnitRef" type="{}DisplayXml"/>
 *         &lt;element name="dataTypeRef" type="{}DisplayXml"/>
 *         &lt;element name="IdentifierModel" type="{}IdentifierXml" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LabTestRangeModel" type="{}LabTestRangeXml" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LabTestXml", propOrder = { "labTestRef", "labTestId", "activeInd", "valueUnit2Ref", "valueUnitRef", "dataTypeRef", "identifierModel", "labTestRangeModel" })
public class LabTestXml {

    @XmlElement(required = true)
    protected DisplayXml labTestRef;

    protected long labTestId;

    @XmlElement(required = true)
    protected BigInteger activeInd;

    @XmlElement(required = true)
    protected DisplayXml valueUnit2Ref;

    @XmlElement(required = true)
    protected DisplayXml valueUnitRef;

    @XmlElement(required = true)
    protected DisplayXml dataTypeRef;

    @XmlElement(name = "IdentifierModel")
    protected List<IdentifierXml> identifierModel;

    @XmlElement(name = "LabTestRangeModel")
    protected List<LabTestRangeXml> labTestRangeModel;

    /**
     * Gets the value of the labTestRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getLabTestRef() {
        return labTestRef;
    }

    /**
     * Sets the value of the labTestRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setLabTestRef(DisplayXml value) {
        this.labTestRef = value;
    }

    /**
     * Gets the value of the labTestId property.
     * 
     */
    public long getLabTestId() {
        return labTestId;
    }

    /**
     * Sets the value of the labTestId property.
     * 
     */
    public void setLabTestId(long value) {
        this.labTestId = value;
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
     * Gets the value of the valueUnit2Ref property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getValueUnit2Ref() {
        return valueUnit2Ref;
    }

    /**
     * Sets the value of the valueUnit2Ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setValueUnit2Ref(DisplayXml value) {
        this.valueUnit2Ref = value;
    }

    /**
     * Gets the value of the valueUnitRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getValueUnitRef() {
        return valueUnitRef;
    }

    /**
     * Sets the value of the valueUnitRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setValueUnitRef(DisplayXml value) {
        this.valueUnitRef = value;
    }

    /**
     * Gets the value of the dataTypeRef property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayXml }
     *     
     */
    public DisplayXml getDataTypeRef() {
        return dataTypeRef;
    }

    /**
     * Sets the value of the dataTypeRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayXml }
     *     
     */
    public void setDataTypeRef(DisplayXml value) {
        this.dataTypeRef = value;
    }

    /**
     * Gets the value of the identifierModel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifierModel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifierModel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdentifierXml }
     * 
     * 
     */
    public List<IdentifierXml> getIdentifierModel() {
        if (identifierModel == null) {
            identifierModel = new ArrayList<IdentifierXml>();
        }
        return this.identifierModel;
    }

    /**
     * Gets the value of the labTestRangeModel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the labTestRangeModel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabTestRangeModel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LabTestRangeXml }
     * 
     * 
     */
    public List<LabTestRangeXml> getLabTestRangeModel() {
        if (labTestRangeModel == null) {
            labTestRangeModel = new ArrayList<LabTestRangeXml>();
        }
        return this.labTestRangeModel;
    }
}
