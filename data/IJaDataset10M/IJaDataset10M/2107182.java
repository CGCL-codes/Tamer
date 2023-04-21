package de.fau.cs.dosis.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="slug" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="technicalStatus" type="{http://dosis/schema}xml-technical-status"/>
 *         &lt;element name="owner" type="{http://dosis/schema}user-reference"/>
 *         &lt;element name="substanceClasses" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://dosis/schema}xml-brandname" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="current" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://dosis/schema}xml-active-ingredient-revision" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="guid" use="required" type="{http://dosis/schema}guid" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "slug", "name", "technicalStatus", "owner", "substanceClasses", "xmlBrandname", "current", "xmlActiveIngredientRevision" })
@XmlRootElement(name = "xml-active-ingredient")
public class XmlActiveIngredient {

    @XmlElement(required = true)
    protected String slug;

    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = true)
    protected XmlTechnicalStatus technicalStatus;

    @XmlElement(required = true)
    protected UserReference owner;

    @XmlElement(required = true)
    protected String substanceClasses;

    @XmlElement(name = "xml-brandname", namespace = "http://dosis/schema")
    protected List<XmlBrandname> xmlBrandname;

    protected String current;

    @XmlElement(name = "xml-active-ingredient-revision", namespace = "http://dosis/schema")
    protected List<XmlActiveIngredientRevision> xmlActiveIngredientRevision;

    @XmlAttribute(required = true)
    protected String guid;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the slug property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Sets the value of the slug property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSlug(String value) {
        this.slug = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the technicalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link XmlTechnicalStatus }
     *     
     */
    public XmlTechnicalStatus getTechnicalStatus() {
        return technicalStatus;
    }

    /**
     * Sets the value of the technicalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTechnicalStatus }
     *     
     */
    public void setTechnicalStatus(XmlTechnicalStatus value) {
        this.technicalStatus = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link UserReference }
     *     
     */
    public UserReference getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserReference }
     *     
     */
    public void setOwner(UserReference value) {
        this.owner = value;
    }

    /**
     * Gets the value of the substanceClasses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstanceClasses() {
        return substanceClasses;
    }

    /**
     * Sets the value of the substanceClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstanceClasses(String value) {
        this.substanceClasses = value;
    }

    /**
     * Gets the value of the xmlBrandname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xmlBrandname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXmlBrandname().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlBrandname }
     * 
     * 
     */
    public List<XmlBrandname> getXmlBrandname() {
        if (xmlBrandname == null) {
            xmlBrandname = new ArrayList<XmlBrandname>();
        }
        return this.xmlBrandname;
    }

    /**
     * Gets the value of the current property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrent() {
        return current;
    }

    /**
     * Sets the value of the current property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrent(String value) {
        this.current = value;
    }

    /**
     * Gets the value of the xmlActiveIngredientRevision property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xmlActiveIngredientRevision property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXmlActiveIngredientRevision().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlActiveIngredientRevision }
     * 
     * 
     */
    public List<XmlActiveIngredientRevision> getXmlActiveIngredientRevision() {
        if (xmlActiveIngredientRevision == null) {
            xmlActiveIngredientRevision = new ArrayList<XmlActiveIngredientRevision>();
        }
        return this.xmlActiveIngredientRevision;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
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
}
