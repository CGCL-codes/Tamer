package org.istcontract.parser.contractelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for strong.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="strong.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/0.91/xsd}strong.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/0.91/xsd}strong.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "strong.type", propOrder = { "atom", "and", "or", "neg", "implies", "equivalent", "forall", "exists" })
public class StrongType {

    @XmlElement(name = "Atom")
    protected AtomType atom;

    @XmlElement(name = "And")
    protected AndInnerType and;

    @XmlElement(name = "Or")
    protected OrInnerType or;

    @XmlElement(name = "Neg")
    protected NegType neg;

    @XmlElement(name = "Implies")
    protected ImpliesType implies;

    @XmlElement(name = "Equivalent")
    protected EquivalentType equivalent;

    @XmlElement(name = "Forall")
    protected ForallType forall;

    @XmlElement(name = "Exists")
    protected ExistsType exists;

    /**
     * Gets the value of the atom property.
     * 
     * @return
     *     possible object is
     *     {@link AtomType }
     *     
     */
    public AtomType getAtom() {
        return atom;
    }

    /**
     * Sets the value of the atom property.
     * 
     * @param value
     *     allowed object is
     *     {@link AtomType }
     *     
     */
    public void setAtom(AtomType value) {
        this.atom = value;
    }

    /**
     * Gets the value of the and property.
     * 
     * @return
     *     possible object is
     *     {@link AndInnerType }
     *     
     */
    public AndInnerType getAnd() {
        return and;
    }

    /**
     * Sets the value of the and property.
     * 
     * @param value
     *     allowed object is
     *     {@link AndInnerType }
     *     
     */
    public void setAnd(AndInnerType value) {
        this.and = value;
    }

    /**
     * Gets the value of the or property.
     * 
     * @return
     *     possible object is
     *     {@link OrInnerType }
     *     
     */
    public OrInnerType getOr() {
        return or;
    }

    /**
     * Sets the value of the or property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrInnerType }
     *     
     */
    public void setOr(OrInnerType value) {
        this.or = value;
    }

    /**
     * Gets the value of the neg property.
     * 
     * @return
     *     possible object is
     *     {@link NegType }
     *     
     */
    public NegType getNeg() {
        return neg;
    }

    /**
     * Sets the value of the neg property.
     * 
     * @param value
     *     allowed object is
     *     {@link NegType }
     *     
     */
    public void setNeg(NegType value) {
        this.neg = value;
    }

    /**
     * Gets the value of the implies property.
     * 
     * @return
     *     possible object is
     *     {@link ImpliesType }
     *     
     */
    public ImpliesType getImplies() {
        return implies;
    }

    /**
     * Sets the value of the implies property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImpliesType }
     *     
     */
    public void setImplies(ImpliesType value) {
        this.implies = value;
    }

    /**
     * Gets the value of the equivalent property.
     * 
     * @return
     *     possible object is
     *     {@link EquivalentType }
     *     
     */
    public EquivalentType getEquivalent() {
        return equivalent;
    }

    /**
     * Sets the value of the equivalent property.
     * 
     * @param value
     *     allowed object is
     *     {@link EquivalentType }
     *     
     */
    public void setEquivalent(EquivalentType value) {
        this.equivalent = value;
    }

    /**
     * Gets the value of the forall property.
     * 
     * @return
     *     possible object is
     *     {@link ForallType }
     *     
     */
    public ForallType getForall() {
        return forall;
    }

    /**
     * Sets the value of the forall property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForallType }
     *     
     */
    public void setForall(ForallType value) {
        this.forall = value;
    }

    /**
     * Gets the value of the exists property.
     * 
     * @return
     *     possible object is
     *     {@link ExistsType }
     *     
     */
    public ExistsType getExists() {
        return exists;
    }

    /**
     * Sets the value of the exists property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExistsType }
     *     
     */
    public void setExists(ExistsType value) {
        this.exists = value;
    }
}
