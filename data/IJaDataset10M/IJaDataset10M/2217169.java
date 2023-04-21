package org.imsglobal.xsd.imsqti_v2p1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for outcomeVariable.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="outcomeVariable.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}outcomeVariable.ContentGroup"/>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}outcomeVariable.AttrGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "outcomeVariable.Type", propOrder = { "value" })
public class OutcomeVariableType {

    protected List<ValueType> value;

    @XmlAttribute
    protected List<ViewType> view;

    @XmlAttribute
    protected String interpretation;

    @XmlAttribute
    protected String longInterpretation;

    @XmlAttribute
    protected Double normalMaximum;

    @XmlAttribute
    protected Double normalMinimum;

    @XmlAttribute
    protected Double masteryValue;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String identifier;

    @XmlAttribute(required = true)
    protected CardinalityType cardinality;

    @XmlAttribute
    protected BaseTypeType baseType;

    /**
     * Gets the value of the value property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the value property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueType }
     * 
     * 
     */
    public List<ValueType> getValue() {
        if (value == null) {
            value = new ArrayList<ValueType>();
        }
        return this.value;
    }

    /**
     * Gets the value of the view property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the view property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getView().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ViewType }
     * 
     * 
     */
    public List<ViewType> getView() {
        if (view == null) {
            view = new ArrayList<ViewType>();
        }
        return this.view;
    }

    /**
     * Gets the value of the interpretation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterpretation() {
        return interpretation;
    }

    /**
     * Sets the value of the interpretation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterpretation(String value) {
        this.interpretation = value;
    }

    /**
     * Gets the value of the longInterpretation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongInterpretation() {
        return longInterpretation;
    }

    /**
     * Sets the value of the longInterpretation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongInterpretation(String value) {
        this.longInterpretation = value;
    }

    /**
     * Gets the value of the normalMaximum property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getNormalMaximum() {
        return normalMaximum;
    }

    /**
     * Sets the value of the normalMaximum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setNormalMaximum(Double value) {
        this.normalMaximum = value;
    }

    /**
     * Gets the value of the normalMinimum property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getNormalMinimum() {
        return normalMinimum;
    }

    /**
     * Sets the value of the normalMinimum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setNormalMinimum(Double value) {
        this.normalMinimum = value;
    }

    /**
     * Gets the value of the masteryValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMasteryValue() {
        return masteryValue;
    }

    /**
     * Sets the value of the masteryValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMasteryValue(Double value) {
        this.masteryValue = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the cardinality property.
     * 
     * @return
     *     possible object is
     *     {@link CardinalityType }
     *     
     */
    public CardinalityType getCardinality() {
        return cardinality;
    }

    /**
     * Sets the value of the cardinality property.
     * 
     * @param value
     *     allowed object is
     *     {@link CardinalityType }
     *     
     */
    public void setCardinality(CardinalityType value) {
        this.cardinality = value;
    }

    /**
     * Gets the value of the baseType property.
     * 
     * @return
     *     possible object is
     *     {@link BaseTypeType }
     *     
     */
    public BaseTypeType getBaseType() {
        return baseType;
    }

    /**
     * Sets the value of the baseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseTypeType }
     *     
     */
    public void setBaseType(BaseTypeType value) {
        this.baseType = value;
    }
}
