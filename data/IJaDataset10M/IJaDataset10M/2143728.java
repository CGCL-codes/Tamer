package com.sun.java.xml.ns.persistence.orm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 
 *         @Target({TYPE}) @Retention(RUNTIME)
 *         public @interface NamedQuery {
 *           String name();
 *           String query();
 *           QueryHint[] hints() default {};
 *         }
 * 
 *       
 * 
 * <p>Java class for named-query complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="named-query">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="query" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hint" type="{http://java.sun.com/xml/ns/persistence/orm}query-hint" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "named-query", propOrder = { "query", "hint" })
public class NamedQuery {

    @XmlElement(required = true)
    protected String query;

    @XmlElement(required = true)
    protected List<QueryHint> hint;

    @XmlAttribute(required = true)
    protected String name;

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuery(final String value) {
        this.query = value;
    }

    /**
     * Gets the value of the hint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QueryHint }
     * 
     * 
     */
    public List<QueryHint> getHint() {
        if (hint == null) {
            hint = new ArrayList<QueryHint>();
        }
        return this.hint;
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
    public void setName(final String value) {
        this.name = value;
    }
}
