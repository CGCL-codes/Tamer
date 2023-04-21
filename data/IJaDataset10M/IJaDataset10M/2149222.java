package com.sun.java.xml.ns.persistence.orm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 
 *         @Target({METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface GeneratedValue {
 *           GenerationType strategy() default AUTO;
 *           String generator() default "";
 *         }
 * 
 *       
 * 
 * <p>Java class for generated-value complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generated-value">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="generator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="strategy" type="{http://java.sun.com/xml/ns/persistence/orm}generation-type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generated-value")
public class GeneratedValue {

    @XmlAttribute
    protected String generator;

    @XmlAttribute
    protected GenerationType strategy;

    /**
     * Gets the value of the generator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenerator() {
        return generator;
    }

    /**
     * Sets the value of the generator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenerator(final String value) {
        this.generator = value;
    }

    /**
     * Gets the value of the strategy property.
     * 
     * @return
     *     possible object is
     *     {@link GenerationType }
     *     
     */
    public GenerationType getStrategy() {
        return strategy;
    }

    /**
     * Sets the value of the strategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenerationType }
     *     
     */
    public void setStrategy(final GenerationType value) {
        this.strategy = value;
    }
}
