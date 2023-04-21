package org.imsglobal.xsd.imsqti_v2p1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for textEntryInteraction.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="textEntryInteraction.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}textEntryInteraction.AttrGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "textEntryInteraction.Type")
public class TextEntryInteractionType {

    @XmlAttribute(name = "base")
    protected Integer spectatusBase;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String stringIdentifier;

    @XmlAttribute
    protected Integer expectedLength;

    @XmlAttribute
    protected String patternMask;

    @XmlAttribute
    protected String placeholderText;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String responseIdentifier;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;

    @XmlAttribute(name = "class")
    protected List<String> clazz;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang;

    @XmlAttribute
    protected String JLabel;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    protected String base;

    /**
     * Gets the value of the spectatusBase property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSpectatusBase() {
        return spectatusBase;
    }

    /**
     * Sets the value of the spectatusBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSpectatusBase(Integer value) {
        this.spectatusBase = value;
    }

    /**
     * Gets the value of the stringIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringIdentifier() {
        return stringIdentifier;
    }

    /**
     * Sets the value of the stringIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringIdentifier(String value) {
        this.stringIdentifier = value;
    }

    /**
     * Gets the value of the expectedLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExpectedLength() {
        return expectedLength;
    }

    /**
     * Sets the value of the expectedLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExpectedLength(Integer value) {
        this.expectedLength = value;
    }

    /**
     * Gets the value of the patternMask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatternMask() {
        return patternMask;
    }

    /**
     * Sets the value of the patternMask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatternMask(String value) {
        this.patternMask = value;
    }

    /**
     * Gets the value of the placeholderText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceholderText() {
        return placeholderText;
    }

    /**
     * Sets the value of the placeholderText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceholderText(String value) {
        this.placeholderText = value;
    }

    /**
     * Gets the value of the responseIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    /**
     * Sets the value of the responseIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseIdentifier(String value) {
        this.responseIdentifier = value;
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

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase() {
        return base;
    }

    /**
     * Sets the value of the base property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase(String value) {
        this.base = value;
    }
}
