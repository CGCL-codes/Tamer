package com.sun.java.xml.ns.persistence.orm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 
 *         @Target({METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface Id {}
 * 
 *       
 * 
 * <p>Java class for id complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="id">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="column" type="{http://java.sun.com/xml/ns/persistence/orm}column" minOccurs="0"/>
 *         &lt;element name="generated-value" type="{http://java.sun.com/xml/ns/persistence/orm}generated-value" minOccurs="0"/>
 *         &lt;element name="temporal" type="{http://java.sun.com/xml/ns/persistence/orm}temporal" minOccurs="0"/>
 *         &lt;element name="table-generator" type="{http://java.sun.com/xml/ns/persistence/orm}table-generator" minOccurs="0"/>
 *         &lt;element name="sequence-generator" type="{http://java.sun.com/xml/ns/persistence/orm}sequence-generator" minOccurs="0"/>
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
@XmlType(name = "id", propOrder = { "column", "generatedValue", "temporal", "tableGenerator", "sequenceGenerator" })
public class Id {

    protected Column column;

    @XmlElement(name = "generated-value")
    protected GeneratedValue generatedValue;

    protected TemporalType temporal;

    @XmlElement(name = "table-generator")
    protected TableGenerator tableGenerator;

    @XmlElement(name = "sequence-generator")
    protected SequenceGenerator sequenceGenerator;

    @XmlAttribute(required = true)
    protected String name;

    /**
     * Gets the value of the column property.
     * 
     * @return
     *     possible object is
     *     {@link Column }
     *     
     */
    public Column getColumn() {
        return column;
    }

    /**
     * Sets the value of the column property.
     * 
     * @param value
     *     allowed object is
     *     {@link Column }
     *     
     */
    public void setColumn(final Column value) {
        this.column = value;
    }

    /**
     * Gets the value of the generatedValue property.
     * 
     * @return
     *     possible object is
     *     {@link GeneratedValue }
     *     
     */
    public GeneratedValue getGeneratedValue() {
        return generatedValue;
    }

    /**
     * Sets the value of the generatedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneratedValue }
     *     
     */
    public void setGeneratedValue(final GeneratedValue value) {
        this.generatedValue = value;
    }

    /**
     * Gets the value of the temporal property.
     * 
     * @return
     *     possible object is
     *     {@link TemporalType }
     *     
     */
    public TemporalType getTemporal() {
        return temporal;
    }

    /**
     * Sets the value of the temporal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TemporalType }
     *     
     */
    public void setTemporal(final TemporalType value) {
        this.temporal = value;
    }

    /**
     * Gets the value of the tableGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link TableGenerator }
     *     
     */
    public TableGenerator getTableGenerator() {
        return tableGenerator;
    }

    /**
     * Sets the value of the tableGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableGenerator }
     *     
     */
    public void setTableGenerator(final TableGenerator value) {
        this.tableGenerator = value;
    }

    /**
     * Gets the value of the sequenceGenerator property.
     * 
     * @return
     *     possible object is
     *     {@link SequenceGenerator }
     *     
     */
    public SequenceGenerator getSequenceGenerator() {
        return sequenceGenerator;
    }

    /**
     * Sets the value of the sequenceGenerator property.
     * 
     * @param value
     *     allowed object is
     *     {@link SequenceGenerator }
     *     
     */
    public void setSequenceGenerator(final SequenceGenerator value) {
        this.sequenceGenerator = value;
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
