package net.sf.istcontract.aws.observer.ontology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PredicateValuesQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PredicateValuesQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allPredictes" type="{ontology.observer.aws.istcontract.sf.net}AllConstant"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PredicateValuesQuery", propOrder = { "allPredictes" })
public class PredicateValuesQuery {

    @XmlElement(required = true)
    protected AllConstant allPredictes;

    /**
     * Gets the value of the allPredictes property.
     * 
     * @return
     *     possible object is
     *     {@link AllConstant }
     *     
     */
    public AllConstant getAllPredictes() {
        return allPredictes;
    }

    /**
     * Sets the value of the allPredictes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllConstant }
     *     
     */
    public void setAllPredictes(AllConstant value) {
        this.allPredictes = value;
    }
}
