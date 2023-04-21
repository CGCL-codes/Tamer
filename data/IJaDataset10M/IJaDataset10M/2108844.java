package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for ServiceDeployment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceDeployment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *       &lt;sequence>
 *         &lt;element name="endpoint" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="serviceProvider" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceProvider"/>
 *         &lt;element name="location" type="{http://www.biocatalogue.org/2009/xml/rest}Location"/>
 *         &lt;element name="submitter" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}created"/>
 *         &lt;element name="providedVariant" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="soapService" type="{http://www.biocatalogue.org/2009/xml/rest}SoapService" minOccurs="0"/>
 *                   &lt;element name="restService" type="{http://www.biocatalogue.org/2009/xml/rest}RestService" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ancestors" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="service" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="related" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="annotations" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceDeployment", propOrder = { "endpoint", "serviceProvider", "location", "submitter", "created", "providedVariant", "ancestors", "related" })
public class ServiceDeployment extends ResourceLink {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String endpoint;

    @XmlElement(required = true)
    protected ServiceProvider serviceProvider;

    @XmlElement(required = true)
    protected Location location;

    @XmlElement(required = true)
    protected ResourceLink submitter;

    @XmlElement(namespace = "http://purl.org/dc/terms/", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;

    protected ServiceDeployment.ProvidedVariant providedVariant;

    protected ServiceDeployment.Ancestors ancestors;

    protected ServiceDeployment.Related related;

    /**
     * Gets the value of the endpoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the value of the endpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpoint(String value) {
        this.endpoint = value;
    }

    /**
     * Gets the value of the serviceProvider property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceProvider }
     *     
     */
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Sets the value of the serviceProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceProvider }
     *     
     */
    public void setServiceProvider(ServiceProvider value) {
        this.serviceProvider = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the submitter property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getSubmitter() {
        return submitter;
    }

    /**
     * Sets the value of the submitter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setSubmitter(ResourceLink value) {
        this.submitter = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    /**
     * Gets the value of the providedVariant property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDeployment.ProvidedVariant }
     *     
     */
    public ServiceDeployment.ProvidedVariant getProvidedVariant() {
        return providedVariant;
    }

    /**
     * Sets the value of the providedVariant property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDeployment.ProvidedVariant }
     *     
     */
    public void setProvidedVariant(ServiceDeployment.ProvidedVariant value) {
        this.providedVariant = value;
    }

    /**
     * Gets the value of the ancestors property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDeployment.Ancestors }
     *     
     */
    public ServiceDeployment.Ancestors getAncestors() {
        return ancestors;
    }

    /**
     * Sets the value of the ancestors property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDeployment.Ancestors }
     *     
     */
    public void setAncestors(ServiceDeployment.Ancestors value) {
        this.ancestors = value;
    }

    /**
     * Gets the value of the related property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDeployment.Related }
     *     
     */
    public ServiceDeployment.Related getRelated() {
        return related;
    }

    /**
     * Sets the value of the related property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDeployment.Related }
     *     
     */
    public void setRelated(ServiceDeployment.Related value) {
        this.related = value;
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
     *         &lt;element name="service" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "service" })
    public static class Ancestors {

        @XmlElement(required = true)
        protected ResourceLink service;

        /**
         * Gets the value of the service property.
         * 
         * @return
         *     possible object is
         *     {@link ResourceLink }
         *     
         */
        public ResourceLink getService() {
            return service;
        }

        /**
         * Sets the value of the service property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResourceLink }
         *     
         */
        public void setService(ResourceLink value) {
            this.service = value;
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
     *       &lt;choice>
     *         &lt;element name="soapService" type="{http://www.biocatalogue.org/2009/xml/rest}SoapService" minOccurs="0"/>
     *         &lt;element name="restService" type="{http://www.biocatalogue.org/2009/xml/rest}RestService" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "soapService", "restService" })
    public static class ProvidedVariant {

        protected SoapService soapService;

        protected RestService restService;

        /**
         * Gets the value of the soapService property.
         * 
         * @return
         *     possible object is
         *     {@link SoapService }
         *     
         */
        public SoapService getSoapService() {
            return soapService;
        }

        /**
         * Sets the value of the soapService property.
         * 
         * @param value
         *     allowed object is
         *     {@link SoapService }
         *     
         */
        public void setSoapService(SoapService value) {
            this.soapService = value;
        }

        /**
         * Gets the value of the restService property.
         * 
         * @return
         *     possible object is
         *     {@link RestService }
         *     
         */
        public RestService getRestService() {
            return restService;
        }

        /**
         * Sets the value of the restService property.
         * 
         * @param value
         *     allowed object is
         *     {@link RestService }
         *     
         */
        public void setRestService(RestService value) {
            this.restService = value;
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
     *       &lt;sequence>
     *         &lt;element name="annotations" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "annotations" })
    public static class Related {

        @XmlElement(required = true)
        protected ResourceLink annotations;

        /**
         * Gets the value of the annotations property.
         * 
         * @return
         *     possible object is
         *     {@link ResourceLink }
         *     
         */
        public ResourceLink getAnnotations() {
            return annotations;
        }

        /**
         * Sets the value of the annotations property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResourceLink }
         *     
         */
        public void setAnnotations(ResourceLink value) {
            this.annotations = value;
        }
    }
}
