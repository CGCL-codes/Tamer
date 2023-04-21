package de.anhquan.codegen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ClassDescriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassDescriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Package" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Template" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PrimaryKeyType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EntityClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassDescriptionType", propOrder = { "name", "_package", "template", "primaryKeyType", "entityClass" })
public class ClassDescriptionType {

    @XmlElement(name = "Name", required = true)
    protected String name;

    @XmlElement(name = "Package", required = true)
    protected String _package;

    @XmlElement(name = "Template", required = true)
    protected String template;

    @XmlElement(name = "PrimaryKeyType", required = true)
    protected String primaryKeyType;

    @XmlElement(name = "EntityClass", required = true)
    protected String entityClass;

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
     * Gets the value of the package property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Sets the value of the package property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackage(String value) {
        this._package = value;
    }

    /**
     * Gets the value of the template property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets the value of the template property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplate(String value) {
        this.template = value;
    }

    /**
     * Gets the value of the primaryKeyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    /**
     * Sets the value of the primaryKeyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryKeyType(String value) {
        this.primaryKeyType = value;
    }

    /**
     * Gets the value of the entityClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityClass() {
        return entityClass;
    }

    /**
     * Sets the value of the entityClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityClass(String value) {
        this.entityClass = value;
    }
}
