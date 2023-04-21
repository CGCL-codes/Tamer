package net.sf.istcontract.parser.contractelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for formula-query.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="formula-query.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/0.91/xsd}formula-query.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/0.91/xsd}formula.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formula-query.type", propOrder = { "rulebase", "atom", "and", "or", "entails", "exists", "neg", "implies", "equivalent", "forall" })
public class FormulaQueryType {

    @XmlElement(name = "Rulebase", namespace = "http://www.ruleml.org/0.91/xsd")
    protected RulebaseType rulebase;

    @XmlElement(name = "Atom", namespace = "http://www.ruleml.org/0.91/xsd")
    protected AtomType atom;

    @XmlElement(name = "And", namespace = "http://www.ruleml.org/0.91/xsd")
    protected AndQueryType and;

    @XmlElement(name = "Or", namespace = "http://www.ruleml.org/0.91/xsd")
    protected OrQueryType or;

    @XmlElement(name = "Entails", namespace = "http://www.ruleml.org/0.91/xsd")
    protected EntailsType entails;

    @XmlElement(name = "Exists", namespace = "http://www.ruleml.org/0.91/xsd")
    protected ExistsType exists;

    @XmlElement(name = "Neg", namespace = "http://www.ruleml.org/0.91/xsd")
    protected NegType neg;

    @XmlElement(name = "Implies", namespace = "http://www.ruleml.org/0.91/xsd")
    protected ImpliesType implies;

    @XmlElement(name = "Equivalent", namespace = "http://www.ruleml.org/0.91/xsd")
    protected EquivalentType equivalent;

    @XmlElement(name = "Forall", namespace = "http://www.ruleml.org/0.91/xsd")
    protected ForallType forall;

    /**
     * Gets the value of the rulebase property.
     * 
     * @return
     *     possible object is
     *     {@link RulebaseType }
     *     
     */
    public RulebaseType getRulebase() {
        return rulebase;
    }

    /**
     * Sets the value of the rulebase property.
     * 
     * @param value
     *     allowed object is
     *     {@link RulebaseType }
     *     
     */
    public void setRulebase(RulebaseType value) {
        this.rulebase = value;
    }

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
     *     {@link AndQueryType }
     *     
     */
    public AndQueryType getAnd() {
        return and;
    }

    /**
     * Sets the value of the and property.
     * 
     * @param value
     *     allowed object is
     *     {@link AndQueryType }
     *     
     */
    public void setAnd(AndQueryType value) {
        this.and = value;
    }

    /**
     * Gets the value of the or property.
     * 
     * @return
     *     possible object is
     *     {@link OrQueryType }
     *     
     */
    public OrQueryType getOr() {
        return or;
    }

    /**
     * Sets the value of the or property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrQueryType }
     *     
     */
    public void setOr(OrQueryType value) {
        this.or = value;
    }

    /**
     * Gets the value of the entails property.
     * 
     * @return
     *     possible object is
     *     {@link EntailsType }
     *     
     */
    public EntailsType getEntails() {
        return entails;
    }

    /**
     * Sets the value of the entails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntailsType }
     *     
     */
    public void setEntails(EntailsType value) {
        this.entails = value;
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
}
