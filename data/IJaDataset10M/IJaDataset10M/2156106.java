package net.sf.istcontract.aws.input.contract;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for oid.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="oid.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/0.91/xsd}oid.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/0.91/xsd}oid.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oid.type", propOrder = { "ind", "data", "var", "skolem", "reify", "expr", "plex" })
public class OidType {

    @XmlElement(name = "Ind", namespace = "http://www.ruleml.org/0.91/xsd")
    protected IndType ind;

    @XmlElement(name = "Data", namespace = "http://www.ruleml.org/0.91/xsd")
    protected Object data;

    @XmlElement(name = "Var", namespace = "http://www.ruleml.org/0.91/xsd")
    protected VarType var;

    @XmlElement(name = "Skolem", namespace = "http://www.ruleml.org/0.91/xsd")
    protected SkolemType skolem;

    @XmlElement(name = "Reify", namespace = "http://www.ruleml.org/0.91/xsd")
    protected ReifyType reify;

    @XmlElement(name = "Expr", namespace = "http://www.ruleml.org/0.91/xsd")
    protected ExprType expr;

    @XmlElement(name = "Plex", namespace = "http://www.ruleml.org/0.91/xsd")
    protected PlexType plex;

    /**
     * Gets the value of the ind property.
     * 
     * @return
     *     possible object is
     *     {@link IndType }
     *     
     */
    public IndType getInd() {
        return ind;
    }

    /**
     * Sets the value of the ind property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndType }
     *     
     */
    public void setInd(IndType value) {
        this.ind = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setData(Object value) {
        this.data = value;
    }

    /**
     * Gets the value of the var property.
     * 
     * @return
     *     possible object is
     *     {@link VarType }
     *     
     */
    public VarType getVar() {
        return var;
    }

    /**
     * Sets the value of the var property.
     * 
     * @param value
     *     allowed object is
     *     {@link VarType }
     *     
     */
    public void setVar(VarType value) {
        this.var = value;
    }

    /**
     * Gets the value of the skolem property.
     * 
     * @return
     *     possible object is
     *     {@link SkolemType }
     *     
     */
    public SkolemType getSkolem() {
        return skolem;
    }

    /**
     * Sets the value of the skolem property.
     * 
     * @param value
     *     allowed object is
     *     {@link SkolemType }
     *     
     */
    public void setSkolem(SkolemType value) {
        this.skolem = value;
    }

    /**
     * Gets the value of the reify property.
     * 
     * @return
     *     possible object is
     *     {@link ReifyType }
     *     
     */
    public ReifyType getReify() {
        return reify;
    }

    /**
     * Sets the value of the reify property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReifyType }
     *     
     */
    public void setReify(ReifyType value) {
        this.reify = value;
    }

    /**
     * Gets the value of the expr property.
     * 
     * @return
     *     possible object is
     *     {@link ExprType }
     *     
     */
    public ExprType getExpr() {
        return expr;
    }

    /**
     * Sets the value of the expr property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExprType }
     *     
     */
    public void setExpr(ExprType value) {
        this.expr = value;
    }

    /**
     * Gets the value of the plex property.
     * 
     * @return
     *     possible object is
     *     {@link PlexType }
     *     
     */
    public PlexType getPlex() {
        return plex;
    }

    /**
     * Sets the value of the plex property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlexType }
     *     
     */
    public void setPlex(PlexType value) {
        this.plex = value;
    }
}
