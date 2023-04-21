package org.istcontract.parser.contractelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Condition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Condition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Formula" type="{http://www.ruleml.org/0.91/xsd}formula-rulebase.type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Condition", namespace = "", propOrder = { "formula" })
public class Condition {

    @XmlElement(name = "Formula", namespace = "", required = true)
    protected FormulaRulebaseType formula;

    /**
     * Gets the value of the formula property.
     * 
     * @return
     *     possible object is
     *     {@link FormulaRulebaseType }
     *     
     */
    public FormulaRulebaseType getFormula() {
        return formula;
    }

    /**
     * Sets the value of the formula property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormulaRulebaseType }
     *     
     */
    public void setFormula(FormulaRulebaseType value) {
        this.formula = value;
    }
}
