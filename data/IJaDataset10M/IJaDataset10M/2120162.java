package companypo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="companyInfo" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="contactInfo" type="{http://xml.netbeans.org/schema/commonSchema}companyInfoType"/>
 *                   &lt;element name="location" type="{http://xml.netbeans.org/schema/commonSchema}companyLocationType"/>
 *                   &lt;element name="businessType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="Agriculture"/>
 *                         &lt;enumeration value="Mining"/>
 *                         &lt;enumeration value="Real Estate"/>
 *                         &lt;enumeration value="Services"/>
 *                         &lt;enumeration value="IT"/>
 *                         &lt;enumeration value="Transportation"/>
 *                         &lt;enumeration value="Other"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="employees" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="anualRevenue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="years" type="{http://xml.netbeans.org/schema/commonSchema}years"/>
 *                   &lt;element name="products">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="product" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
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
@XmlType(name = "", propOrder = { "companyInfo" })
@XmlRootElement(name = "companies", namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema")
public class Companies {

    @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
    protected List<Companies.CompanyInfo> companyInfo;

    /**
     * Gets the value of the companyInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the companyInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompanyInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Companies.CompanyInfo }
     * 
     * 
     */
    public List<Companies.CompanyInfo> getCompanyInfo() {
        if (companyInfo == null) {
            companyInfo = new ArrayList<Companies.CompanyInfo>();
        }
        return this.companyInfo;
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
     *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="contactInfo" type="{http://xml.netbeans.org/schema/commonSchema}companyInfoType"/>
     *         &lt;element name="location" type="{http://xml.netbeans.org/schema/commonSchema}companyLocationType"/>
     *         &lt;element name="businessType">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="Agriculture"/>
     *               &lt;enumeration value="Mining"/>
     *               &lt;enumeration value="Real Estate"/>
     *               &lt;enumeration value="Services"/>
     *               &lt;enumeration value="IT"/>
     *               &lt;enumeration value="Transportation"/>
     *               &lt;enumeration value="Other"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="employees" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="anualRevenue" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="years" type="{http://xml.netbeans.org/schema/commonSchema}years"/>
     *         &lt;element name="products">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="product" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "description", "contactInfo", "location", "businessType", "employees", "anualRevenue", "years", "products" })
    public static class CompanyInfo {

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected String description;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected CompanyInfoType contactInfo;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected CompanyLocationType location;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected String businessType;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected BigInteger employees;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema")
        protected int anualRevenue;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected BigInteger years;

        @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema", required = true)
        protected Companies.CompanyInfo.Products products;

        @XmlAttribute(name = "name")
        @XmlSchemaType(name = "anySimpleType")
        protected String name;

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

        /**
         * Gets the value of the contactInfo property.
         * 
         * @return
         *     possible object is
         *     {@link CompanyInfoType }
         *     
         */
        public CompanyInfoType getContactInfo() {
            return contactInfo;
        }

        /**
         * Sets the value of the contactInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link CompanyInfoType }
         *     
         */
        public void setContactInfo(CompanyInfoType value) {
            this.contactInfo = value;
        }

        /**
         * Gets the value of the location property.
         * 
         * @return
         *     possible object is
         *     {@link CompanyLocationType }
         *     
         */
        public CompanyLocationType getLocation() {
            return location;
        }

        /**
         * Sets the value of the location property.
         * 
         * @param value
         *     allowed object is
         *     {@link CompanyLocationType }
         *     
         */
        public void setLocation(CompanyLocationType value) {
            this.location = value;
        }

        /**
         * Gets the value of the businessType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBusinessType() {
            return businessType;
        }

        /**
         * Sets the value of the businessType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBusinessType(String value) {
            this.businessType = value;
        }

        /**
         * Gets the value of the employees property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getEmployees() {
            return employees;
        }

        /**
         * Sets the value of the employees property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setEmployees(BigInteger value) {
            this.employees = value;
        }

        /**
         * Gets the value of the anualRevenue property.
         * 
         */
        public int getAnualRevenue() {
            return anualRevenue;
        }

        /**
         * Sets the value of the anualRevenue property.
         * 
         */
        public void setAnualRevenue(int value) {
            this.anualRevenue = value;
        }

        /**
         * Gets the value of the years property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getYears() {
            return years;
        }

        /**
         * Sets the value of the years property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setYears(BigInteger value) {
            this.years = value;
        }

        /**
         * Gets the value of the products property.
         * 
         * @return
         *     possible object is
         *     {@link Companies.CompanyInfo.Products }
         *     
         */
        public Companies.CompanyInfo.Products getProducts() {
            return products;
        }

        /**
         * Sets the value of the products property.
         * 
         * @param value
         *     allowed object is
         *     {@link Companies.CompanyInfo.Products }
         *     
         */
        public void setProducts(Companies.CompanyInfo.Products value) {
            this.products = value;
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="product" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
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
        @XmlType(name = "", propOrder = { "product" })
        public static class Products {

            @XmlElement(namespace = "http://xml.netbeans.org/schema/companyInfoXmlSchema")
            protected List<Companies.CompanyInfo.Products.Product> product;

            /**
             * Gets the value of the product property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the product property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getProduct().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Companies.CompanyInfo.Products.Product }
             * 
             * 
             */
            public List<Companies.CompanyInfo.Products.Product> getProduct() {
                if (product == null) {
                    product = new ArrayList<Companies.CompanyInfo.Products.Product>();
                }
                return this.product;
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
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Product {

                @XmlAttribute(name = "name")
                protected String name;

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
            }
        }
    }
}
