package org.slasoi.common.eventschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NotEqual complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotEqual">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.slaatsoi.org/coremodel}PredicateType">
 *       &lt;sequence>
 *         &lt;element name="OperandA" type="{http://www.slaatsoi.org/coremodel}Property"/>
 *         &lt;element name="OperandB" type="{http://www.slaatsoi.org/coremodel}Property"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotEqual", namespace = "http://www.slaatsoi.org/coremodel", propOrder = { "operandA", "operandB" })
public class NotEqual extends PredicateType {

    @XmlElement(name = "OperandA", required = true)
    protected Property operandA;

    @XmlElement(name = "OperandB", required = true)
    protected Property operandB;

    /**
     * Gets the value of the operandA property.
     * 
     * @return
     *     possible object is
     *     {@link Property }
     *     
     */
    public Property getOperandA() {
        return operandA;
    }

    /**
     * Sets the value of the operandA property.
     * 
     * @param value
     *     allowed object is
     *     {@link Property }
     *     
     */
    public void setOperandA(Property value) {
        this.operandA = value;
    }

    /**
     * Gets the value of the operandB property.
     * 
     * @return
     *     possible object is
     *     {@link Property }
     *     
     */
    public Property getOperandB() {
        return operandB;
    }

    /**
     * Sets the value of the operandB property.
     * 
     * @param value
     *     allowed object is
     *     {@link Property }
     *     
     */
    public void setOperandB(Property value) {
        this.operandB = value;
    }
}
