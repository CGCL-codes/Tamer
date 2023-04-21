package org.slasoi.common.eventschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for UnaryLogicOperatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnaryLogicOperatorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Expression" type="{http://www.slaatsoi.org/coremodel}LogicExpressionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnaryLogicOperatorType", namespace = "http://www.slaatsoi.org/coremodel", propOrder = { "expression" })
public class UnaryLogicOperatorType {

    @XmlElement(name = "Expression", required = true)
    protected LogicExpressionType expression;

    /**
     * Gets the value of the expression property.
     * 
     * @return
     *     possible object is
     *     {@link LogicExpressionType }
     *     
     */
    public LogicExpressionType getExpression() {
        return expression;
    }

    /**
     * Sets the value of the expression property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicExpressionType }
     *     
     */
    public void setExpression(LogicExpressionType value) {
        this.expression = value;
    }
}
