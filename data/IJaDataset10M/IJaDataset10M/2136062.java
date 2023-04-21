package org.imsglobal.xsd.imsqti_v2p1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for outcomeCondition.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="outcomeCondition.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}outcomeCondition.ContentGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "outcomeCondition.Type", propOrder = { "outcomeIf", "outcomeElseIf", "outcomeElse" })
public class OutcomeConditionType {

    @XmlElement(required = true)
    protected OutcomeIfType outcomeIf;

    protected List<OutcomeElseIfType> outcomeElseIf;

    protected OutcomeElseType outcomeElse;

    /**
     * Gets the value of the outcomeIf property.
     * 
     * @return
     *     possible object is
     *     {@link OutcomeIfType }
     *     
     */
    public OutcomeIfType getOutcomeIf() {
        return outcomeIf;
    }

    /**
     * Sets the value of the outcomeIf property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutcomeIfType }
     *     
     */
    public void setOutcomeIf(OutcomeIfType value) {
        this.outcomeIf = value;
    }

    /**
     * Gets the value of the outcomeElseIf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outcomeElseIf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutcomeElseIf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutcomeElseIfType }
     * 
     * 
     */
    public List<OutcomeElseIfType> getOutcomeElseIf() {
        if (outcomeElseIf == null) {
            outcomeElseIf = new ArrayList<OutcomeElseIfType>();
        }
        return this.outcomeElseIf;
    }

    /**
     * Gets the value of the outcomeElse property.
     * 
     * @return
     *     possible object is
     *     {@link OutcomeElseType }
     *     
     */
    public OutcomeElseType getOutcomeElse() {
        return outcomeElse;
    }

    /**
     * Sets the value of the outcomeElse property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutcomeElseType }
     *     
     */
    public void setOutcomeElse(OutcomeElseType value) {
        this.outcomeElse = value;
    }
}
