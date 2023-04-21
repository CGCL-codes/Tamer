package org.imsglobal.xsd.imsqti_v2p1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._1998.math.mathml.MathType;
import org.w3._2001.xinclude.IncludeType;

/**
 * <p>Java class for simpleChoice.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="simpleChoice.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}simpleChoice.ContentGroup"/>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}simpleChoice.AttrGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simpleChoice.Type", propOrder = { "content" })
public class SimpleChoiceType {

    @XmlElementRefs({ @XmlElementRef(name = "q", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "br", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "small", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "em", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "sub", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "feedbackBlock", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "tt", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "a", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "b", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "kbd", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "i", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "templateBlock", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "pre", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h6", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h2", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "strong", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "templateInline", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "div", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "table", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "big", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "p", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "code", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "rubricBlock", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "include", namespace = "http://www.w3.org/2001/XInclude", type = JAXBElement.class), @XmlElementRef(name = "hr", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h3", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "sup", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "acronym", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "ol", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "cite", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "feedbackInline", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "abbr", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "samp", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "dfn", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "printedVariable", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "hottext", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "address", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "img", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h1", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "dl", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "object", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "var", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "span", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "blockquote", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "h5", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "math", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "h4", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class), @XmlElementRef(name = "ul", namespace = "http://www.imsglobal.org/xsd/imsqti_v2p1", type = JAXBElement.class) })
    @XmlMixed
    protected List<Serializable> content;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String identifier;

    @XmlAttribute
    protected Boolean fixed;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String templateIdentifier;

    @XmlAttribute
    protected ShowHideType showHide;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;

    @XmlAttribute(name = "class")
    protected List<String> clazz;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang;

    @XmlAttribute
    protected String JLabel;

    /**
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
     * {@link JAXBElement }{@code <}{@link QType }{@code >}
     * {@link JAXBElement }{@code <}{@link SmallType }{@code >}
     * {@link JAXBElement }{@code <}{@link EmType }{@code >}
     * {@link JAXBElement }{@code <}{@link KbdType }{@code >}
     * {@link JAXBElement }{@code <}{@link IType }{@code >}
     * {@link JAXBElement }{@code <}{@link TemplateBlockType }{@code >}
     * {@link JAXBElement }{@code <}{@link PreType }{@code >}
     * {@link JAXBElement }{@code <}{@link H6Type }{@code >}
     * {@link JAXBElement }{@code <}{@link H2Type }{@code >}
     * {@link JAXBElement }{@code <}{@link DivType }{@code >}
     * {@link JAXBElement }{@code <}{@link CodeType }{@code >}
     * {@link JAXBElement }{@code <}{@link IncludeType }{@code >}
     * {@link JAXBElement }{@code <}{@link HrType }{@code >}
     * {@link JAXBElement }{@code <}{@link H3Type }{@code >}
     * {@link JAXBElement }{@code <}{@link CiteType }{@code >}
     * {@link JAXBElement }{@code <}{@link FeedbackInlineType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbbrType }{@code >}
     * {@link JAXBElement }{@code <}{@link SampType }{@code >}
     * {@link JAXBElement }{@code <}{@link DfnType }{@code >}
     * {@link JAXBElement }{@code <}{@link HottextType }{@code >}
     * {@link JAXBElement }{@code <}{@link ImgType }{@code >}
     * {@link JAXBElement }{@code <}{@link H1Type }{@code >}
     * {@link JAXBElement }{@code <}{@link DlType }{@code >}
     * {@link JAXBElement }{@code <}{@link ObjectType }{@code >}
     * {@link JAXBElement }{@code <}{@link BlockquoteType }{@code >}
     * {@link JAXBElement }{@code <}{@link H5Type }{@code >}
     * {@link JAXBElement }{@code <}{@link MathType }{@code >}
     * {@link JAXBElement }{@code <}{@link H4Type }{@code >}
     * {@link JAXBElement }{@code <}{@link UlType }{@code >}
     * {@link JAXBElement }{@code <}{@link BrType }{@code >}
     * {@link JAXBElement }{@code <}{@link SubType }{@code >}
     * {@link JAXBElement }{@code <}{@link FeedbackBlockType }{@code >}
     * {@link JAXBElement }{@code <}{@link TtType }{@code >}
     * {@link JAXBElement }{@code <}{@link AType }{@code >}
     * {@link JAXBElement }{@code <}{@link BType }{@code >}
     * {@link JAXBElement }{@code <}{@link StrongType }{@code >}
     * {@link JAXBElement }{@code <}{@link TemplateInlineType }{@code >}
     * {@link JAXBElement }{@code <}{@link TableType }{@code >}
     * {@link JAXBElement }{@code <}{@link BigType }{@code >}
     * {@link JAXBElement }{@code <}{@link PType }{@code >}
     * {@link JAXBElement }{@code <}{@link RubricBlockType }{@code >}
     * {@link JAXBElement }{@code <}{@link SupType }{@code >}
     * {@link JAXBElement }{@code <}{@link AcronymType }{@code >}
     * {@link JAXBElement }{@code <}{@link OlType }{@code >}
     * {@link JAXBElement }{@code <}{@link PrintedVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AddressType }{@code >}
     * {@link String }
     * {@link JAXBElement }{@code <}{@link VarType }{@code >}
     * {@link JAXBElement }{@code <}{@link SpanType }{@code >}
     * 
     * 
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the fixed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFixed() {
        return fixed;
    }

    /**
     * Sets the value of the fixed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFixed(Boolean value) {
        this.fixed = value;
    }

    /**
     * Gets the value of the templateIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateIdentifier() {
        return templateIdentifier;
    }

    /**
     * Sets the value of the templateIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateIdentifier(String value) {
        this.templateIdentifier = value;
    }

    /**
     * Gets the value of the showHide property.
     * 
     * @return
     *     possible object is
     *     {@link ShowHideType }
     *     
     */
    public ShowHideType getShowHide() {
        return showHide;
    }

    /**
     * Sets the value of the showHide property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowHideType }
     *     
     */
    public void setShowHide(ShowHideType value) {
        this.showHide = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<String>();
        }
        return this.clazz;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the JLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJLabel() {
        return JLabel;
    }

    /**
     * Sets the value of the JLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJLabel(String value) {
        this.JLabel = value;
    }
}
