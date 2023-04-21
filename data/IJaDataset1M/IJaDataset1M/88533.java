package org.biocatalogue.generated;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>Java class for TagsParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TagsParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sort">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="urlKey" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="sort"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="urlValue">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="counts"/>
 *                       &lt;enumeration value="name"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.biocatalogue.org/2009/xml/rest}page"/>
 *         &lt;element ref="{http://www.biocatalogue.org/2009/xml/rest}pageSize"/>
 *         &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TagsParameters", propOrder = { "sort", "page", "pageSize", "limit" })
public class TagsParameters {

    @XmlElement(required = true)
    protected TagsParameters.Sort sort;

    @XmlElement(required = true)
    protected PageParameter page;

    @XmlElement(required = true)
    protected PageSizeParameter pageSize;

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger limit;

    /**
     * Gets the value of the sort property.
     * 
     * @return
     *     possible object is
     *     {@link TagsParameters.Sort }
     *     
     */
    public TagsParameters.Sort getSort() {
        return sort;
    }

    /**
     * Sets the value of the sort property.
     * 
     * @param value
     *     allowed object is
     *     {@link TagsParameters.Sort }
     *     
     */
    public void setSort(TagsParameters.Sort value) {
        this.sort = value;
    }

    /**
     * Gets the value of the page property.
     * 
     * @return
     *     possible object is
     *     {@link PageParameter }
     *     
     */
    public PageParameter getPage() {
        return page;
    }

    /**
     * Sets the value of the page property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageParameter }
     *     
     */
    public void setPage(PageParameter value) {
        this.page = value;
    }

    /**
     * Gets the value of the pageSize property.
     * 
     * @return
     *     possible object is
     *     {@link PageSizeParameter }
     *     
     */
    public PageSizeParameter getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageSizeParameter }
     *     
     */
    public void setPageSize(PageSizeParameter value) {
        this.pageSize = value;
    }

    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLimit(BigInteger value) {
        this.limit = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="urlKey" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="sort"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="urlValue">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="counts"/>
     *             &lt;enumeration value="name"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "value" })
    public static class Sort {

        @XmlValue
        protected String value;

        @XmlAttribute(required = true)
        protected String urlKey;

        @XmlAttribute
        protected String urlValue;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the urlKey property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUrlKey() {
            return urlKey;
        }

        /**
         * Sets the value of the urlKey property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUrlKey(String value) {
            this.urlKey = value;
        }

        /**
         * Gets the value of the urlValue property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUrlValue() {
            return urlValue;
        }

        /**
         * Sets the value of the urlValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUrlValue(String value) {
            this.urlValue = value;
        }
    }
}
