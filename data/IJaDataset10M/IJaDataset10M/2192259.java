package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>Java class for CategoriesParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CategoriesParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rootsOnly">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>boolean">
 *                 &lt;attribute name="urlKey" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategoriesParameters", propOrder = { "rootsOnly" })
public class CategoriesParameters {

    @XmlElement(required = true)
    protected CategoriesParameters.RootsOnly rootsOnly;

    /**
     * Gets the value of the rootsOnly property.
     * 
     * @return
     *     possible object is
     *     {@link CategoriesParameters.RootsOnly }
     *     
     */
    public CategoriesParameters.RootsOnly getRootsOnly() {
        return rootsOnly;
    }

    /**
     * Sets the value of the rootsOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link CategoriesParameters.RootsOnly }
     *     
     */
    public void setRootsOnly(CategoriesParameters.RootsOnly value) {
        this.rootsOnly = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>boolean">
     *       &lt;attribute name="urlKey" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "value" })
    public static class RootsOnly {

        @XmlValue
        protected boolean value;

        @XmlAttribute(required = true)
        protected String urlKey;

        /**
         * Gets the value of the value property.
         * 
         */
        public boolean isValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         */
        public void setValue(boolean value) {
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
    }
}
