package edu.unc.med.lccc.tcgasra.xsdobj.study;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 *         Reusable external links type to encode URL links, Entrez links, and db_xref links.
 *       
 * 
 * <p>Java class for LinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="SRA_LINK" type="{}SraLinkType"/>
 *         &lt;element name="URL_LINK">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="XREF_LINK">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="DB" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ENTREZ_LINK">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DB" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;choice>
 *                     &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *                     &lt;element name="QUERY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;/choice>
 *                   &lt;element name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinkType", propOrder = { "sralink", "urllink", "xreflink", "entrezlink" })
public class LinkType {

    @XmlElement(name = "SRA_LINK")
    protected SraLinkType sralink;

    @XmlElement(name = "URL_LINK")
    protected LinkType.URLLINK urllink;

    @XmlElement(name = "XREF_LINK")
    protected LinkType.XREFLINK xreflink;

    @XmlElement(name = "ENTREZ_LINK")
    protected LinkType.ENTREZLINK entrezlink;

    /**
     * Gets the value of the sralink property.
     * 
     * @return
     *     possible object is
     *     {@link SraLinkType }
     *     
     */
    public SraLinkType getSRALINK() {
        return sralink;
    }

    /**
     * Sets the value of the sralink property.
     * 
     * @param value
     *     allowed object is
     *     {@link SraLinkType }
     *     
     */
    public void setSRALINK(SraLinkType value) {
        this.sralink = value;
    }

    /**
     * Gets the value of the urllink property.
     * 
     * @return
     *     possible object is
     *     {@link LinkType.URLLINK }
     *     
     */
    public LinkType.URLLINK getURLLINK() {
        return urllink;
    }

    /**
     * Sets the value of the urllink property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkType.URLLINK }
     *     
     */
    public void setURLLINK(LinkType.URLLINK value) {
        this.urllink = value;
    }

    /**
     * Gets the value of the xreflink property.
     * 
     * @return
     *     possible object is
     *     {@link LinkType.XREFLINK }
     *     
     */
    public LinkType.XREFLINK getXREFLINK() {
        return xreflink;
    }

    /**
     * Sets the value of the xreflink property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkType.XREFLINK }
     *     
     */
    public void setXREFLINK(LinkType.XREFLINK value) {
        this.xreflink = value;
    }

    /**
     * Gets the value of the entrezlink property.
     * 
     * @return
     *     possible object is
     *     {@link LinkType.ENTREZLINK }
     *     
     */
    public LinkType.ENTREZLINK getENTREZLINK() {
        return entrezlink;
    }

    /**
     * Sets the value of the entrezlink property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkType.ENTREZLINK }
     *     
     */
    public void setENTREZLINK(LinkType.ENTREZLINK value) {
        this.entrezlink = value;
    }

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
     *         &lt;element name="DB" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;choice>
     *           &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
     *           &lt;element name="QUERY" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;/choice>
     *         &lt;element name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "db", "id", "query", "label" })
    public static class ENTREZLINK {

        @XmlElement(name = "DB", required = true)
        protected String db;

        @XmlElement(name = "ID")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger id;

        @XmlElement(name = "QUERY")
        protected String query;

        @XmlElement(name = "LABEL")
        protected String label;

        /**
         * Gets the value of the db property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDB() {
            return db;
        }

        /**
         * Sets the value of the db property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDB(String value) {
            this.db = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setID(BigInteger value) {
            this.id = value;
        }

        /**
         * Gets the value of the query property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQUERY() {
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
        public void setQUERY(String value) {
            this.query = value;
        }

        /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLABEL() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLABEL(String value) {
            this.label = value;
        }
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {  })
    public static class URLLINK {

        @XmlElement(name = "LABEL", required = true)
        protected String label;

        @XmlElement(name = "URL", required = true)
        @XmlSchemaType(name = "anyURI")
        protected String url;

        /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLABEL() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLABEL(String value) {
            this.label = value;
        }

        /**
         * Gets the value of the url property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getURL() {
            return url;
        }

        /**
         * Sets the value of the url property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setURL(String value) {
            this.url = value;
        }
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="DB" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {  })
    public static class XREFLINK {

        @XmlElement(name = "DB", required = true)
        protected String db;

        @XmlElement(name = "ID", required = true)
        protected String id;

        @XmlElement(name = "LABEL")
        protected String label;

        /**
         * Gets the value of the db property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDB() {
            return db;
        }

        /**
         * Sets the value of the db property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDB(String value) {
            this.db = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
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
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLABEL() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLABEL(String value) {
            this.label = value;
        }
    }
}
