package org.okkam.core.match.query.annotated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="QueryString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QueryContext" type="{http://www.okkam.org/schemas/AnnotatedQuery}Context" minOccurs="0"/>
 *         &lt;element name="QueryMetadata" type="{http://www.okkam.org/schemas/AnnotatedQuery}Metadata" minOccurs="0"/>
 *         &lt;element name="QueryAnnotation" type="{http://www.okkam.org/schemas/AnnotatedQuery}Annotation"/>
 *         &lt;element name="QueryExpansion" type="{http://www.okkam.org/schemas/AnnotatedQuery}ExpansionHints" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "queryString", "queryContext", "queryMetadata", "queryAnnotation", "queryExpansion" })
@XmlRootElement(name = "AnnotatedQuery")
public class AnnotatedQuery {

    @XmlElement(name = "QueryString", required = true)
    protected String queryString;

    @XmlElement(name = "QueryContext")
    protected Context queryContext;

    @XmlElement(name = "QueryMetadata")
    protected Metadata queryMetadata;

    @XmlElement(name = "QueryAnnotation", required = true)
    protected Annotation queryAnnotation;

    @XmlElement(name = "QueryExpansion")
    protected ExpansionHints queryExpansion;

    /**
     * Gets the value of the queryString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Sets the value of the queryString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryString(String value) {
        this.queryString = value;
    }

    /**
     * Gets the value of the queryContext property.
     * 
     * @return
     *     possible object is
     *     {@link Context }
     *     
     */
    public Context getQueryContext() {
        return queryContext;
    }

    /**
     * Sets the value of the queryContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Context }
     *     
     */
    public void setQueryContext(Context value) {
        this.queryContext = value;
    }

    /**
     * Gets the value of the queryMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link Metadata }
     *     
     */
    public Metadata getQueryMetadata() {
        return queryMetadata;
    }

    /**
     * Sets the value of the queryMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Metadata }
     *     
     */
    public void setQueryMetadata(Metadata value) {
        this.queryMetadata = value;
    }

    /**
     * Gets the value of the queryAnnotation property.
     * 
     * @return
     *     possible object is
     *     {@link Annotation }
     *     
     */
    public Annotation getQueryAnnotation() {
        return queryAnnotation;
    }

    /**
     * Sets the value of the queryAnnotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Annotation }
     *     
     */
    public void setQueryAnnotation(Annotation value) {
        this.queryAnnotation = value;
    }

    /**
     * Gets the value of the queryExpansion property.
     * 
     * @return
     *     possible object is
     *     {@link ExpansionHints }
     *     
     */
    public ExpansionHints getQueryExpansion() {
        return queryExpansion;
    }

    /**
     * Sets the value of the queryExpansion property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpansionHints }
     *     
     */
    public void setQueryExpansion(ExpansionHints value) {
        this.queryExpansion = value;
    }
}
