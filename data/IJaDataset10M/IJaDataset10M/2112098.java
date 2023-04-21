package org.eucontract.agents.knowledge.ontology.contract;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for What complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="What">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="action-expression" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="state-expression" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "What", propOrder = { "actionExpression", "stateExpression" })
public class What {

    @XmlElement(name = "action-expression")
    protected String actionExpression;

    @XmlElement(name = "state-expression")
    protected String stateExpression;

    /**
     * Gets the value of the actionExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionExpression() {
        return actionExpression;
    }

    /**
     * Sets the value of the actionExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionExpression(String value) {
        this.actionExpression = value;
    }

    /**
     * Gets the value of the stateExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateExpression() {
        return stateExpression;
    }

    /**
     * Sets the value of the stateExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateExpression(String value) {
        this.stateExpression = value;
    }
}
