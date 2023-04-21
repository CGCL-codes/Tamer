package org.jaffa.patterns.library.object_maintenance_meta_2_0.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for relatedObjectJoinBetween complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="relatedObjectJoinBetween">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DomainField" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RelatedObjectFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RelatedObjectDomainField" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relatedObjectJoinBetween", propOrder = { "name", "domainField", "relatedObjectFieldName", "relatedObjectDomainField" })
public class RelatedObjectJoinBetween {

    @XmlElement(name = "Name", required = true)
    protected String name;

    @XmlElement(name = "DomainField", required = true)
    protected String domainField;

    @XmlElement(name = "RelatedObjectFieldName", required = true)
    protected String relatedObjectFieldName;

    @XmlElement(name = "RelatedObjectDomainField", required = true)
    protected String relatedObjectDomainField;

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
     * Gets the value of the domainField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainField() {
        return domainField;
    }

    /**
     * Sets the value of the domainField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainField(String value) {
        this.domainField = value;
    }

    /**
     * Gets the value of the relatedObjectFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelatedObjectFieldName() {
        return relatedObjectFieldName;
    }

    /**
     * Sets the value of the relatedObjectFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelatedObjectFieldName(String value) {
        this.relatedObjectFieldName = value;
    }

    /**
     * Gets the value of the relatedObjectDomainField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelatedObjectDomainField() {
        return relatedObjectDomainField;
    }

    /**
     * Sets the value of the relatedObjectDomainField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelatedObjectDomainField(String value) {
        this.relatedObjectDomainField = value;
    }
}
