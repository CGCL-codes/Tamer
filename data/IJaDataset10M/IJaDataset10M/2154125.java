package org.jaffa.patterns.library.object_lookup_meta_2_0.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for breakupCriteriaField complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="breakupCriteriaField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataType" type="{}dataTypes"/>
 *         &lt;element name="DomainField" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Values" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "breakupCriteriaField", propOrder = { "dataType", "domainField", "operator", "values" })
public class BreakupCriteriaField {

    @XmlElement(name = "DataType", required = true)
    protected String dataType;

    @XmlElement(name = "DomainField", required = true)
    protected String domainField;

    @XmlElement(name = "Operator", required = true)
    protected String operator;

    @XmlElement(name = "Values", required = true)
    protected String values;

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the domainField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainField() {
        return domainField;
    }

    /**
     * Sets the value of the domainField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainField(String value) {
        this.domainField = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperator(String value) {
        this.operator = value;
    }

    /**
     * Gets the value of the values property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValues() {
        return values;
    }

    /**
     * Sets the value of the values property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValues(String value) {
        this.values = value;
    }
}
