package org.istcontract.parser.contractelements;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for Expr.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Expr.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/0.91/xsd}Expr.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/0.91/xsd}Expr.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Expr.type", propOrder = { "content" })
public class ExprType {

    @XmlElementRefs({ @XmlElementRef(name = "Fun", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "resl", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Ind", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "oid", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Skolem", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Plex", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Reify", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Expr", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "op", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Var", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "arg", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "Data", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "slot", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class), @XmlElementRef(name = "repo", namespace = "http://www.ruleml.org/0.91/xsd", type = JAXBElement.class) })
    protected List<JAXBElement<?>> content;

    @XmlAttribute
    protected String type;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String in;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Repo" is used by two different parts of a schema. See: 
     * line 86 of http://www.ruleml.org/0.91/xsd/modules/expr_module.xsd
     * line 84 of http://www.ruleml.org/0.91/xsd/modules/expr_module.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link FunType }{@code >}
     * {@link JAXBElement }{@code <}{@link ReslType }{@code >}
     * {@link JAXBElement }{@code <}{@link IndType }{@code >}
     * {@link JAXBElement }{@code <}{@link OidType }{@code >}
     * {@link JAXBElement }{@code <}{@link SkolemType }{@code >}
     * {@link JAXBElement }{@code <}{@link PlexType }{@code >}
     * {@link JAXBElement }{@code <}{@link ReifyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExprType }{@code >}
     * {@link JAXBElement }{@code <}{@link OpExprType }{@code >}
     * {@link JAXBElement }{@code <}{@link VarType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArgType }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link RepoType }{@code >}
     * {@link JAXBElement }{@code <}{@link SlotType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the in property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn() {
        if (in == null) {
            return "no";
        } else {
            return in;
        }
    }

    /**
     * Sets the value of the in property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn(String value) {
        this.in = value;
    }
}
