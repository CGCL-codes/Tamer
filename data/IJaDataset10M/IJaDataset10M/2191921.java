package org.biocatalogue.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for Service complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Service">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *       &lt;sequence>
 *         &lt;element ref="{http://purl.org/dc/elements/1.1/}title"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="originalSubmitter" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element ref="{http://purl.org/dc/elements/1.1/}description"/>
 *         &lt;element name="serviceTechnologyTypes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="type" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceTechnologyType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="latestMonitoringStatus" type="{http://www.biocatalogue.org/2009/xml/rest}MonitoringStatus"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}created"/>
 *         &lt;element name="archived" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="summary" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceSummary" minOccurs="0"/>
 *         &lt;element name="deployments" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *                 &lt;sequence>
 *                   &lt;element name="serviceDeployment" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceDeployment" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="variants" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *                 &lt;sequence>
 *                   &lt;choice maxOccurs="unbounded">
 *                     &lt;element name="soapService" type="{http://www.biocatalogue.org/2009/xml/rest}SoapService" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="restService" type="{http://www.biocatalogue.org/2009/xml/rest}RestService" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="monitoring" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *                 &lt;sequence>
 *                   &lt;element name="tests">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="serviceTest" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceTest" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="related" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceRelatedLinks" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Service", propOrder = { "title", "name", "originalSubmitter", "description", "serviceTechnologyTypes", "latestMonitoringStatus", "created", "archived", "summary", "deployments", "variants", "monitoring", "related" })
public class Service extends ResourceLink {

    @XmlElementRef(name = "title", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class)
    protected JAXBElement<String> title;

    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = true)
    protected ResourceLink originalSubmitter;

    @XmlElementRef(name = "description", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class)
    protected JAXBElement<String> description;

    @XmlElement(required = true)
    protected Service.ServiceTechnologyTypes serviceTechnologyTypes;

    @XmlElement(required = true)
    protected MonitoringStatus latestMonitoringStatus;

    @XmlElement(namespace = "http://purl.org/dc/terms/", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar archived;

    protected ServiceSummary summary;

    protected Service.Deployments deployments;

    protected Service.Variants variants;

    protected Service.Monitoring monitoring;

    protected ServiceRelatedLinks related;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTitle(JAXBElement<String> value) {
        this.title = ((JAXBElement<String>) value);
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
     * Gets the value of the originalSubmitter property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getOriginalSubmitter() {
        return originalSubmitter;
    }

    /**
     * Sets the value of the originalSubmitter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setOriginalSubmitter(ResourceLink value) {
        this.originalSubmitter = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescription(JAXBElement<String> value) {
        this.description = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the serviceTechnologyTypes property.
     * 
     * @return
     *     possible object is
     *     {@link Service.ServiceTechnologyTypes }
     *     
     */
    public Service.ServiceTechnologyTypes getServiceTechnologyTypes() {
        return serviceTechnologyTypes;
    }

    /**
     * Sets the value of the serviceTechnologyTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Service.ServiceTechnologyTypes }
     *     
     */
    public void setServiceTechnologyTypes(Service.ServiceTechnologyTypes value) {
        this.serviceTechnologyTypes = value;
    }

    /**
     * Gets the value of the latestMonitoringStatus property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoringStatus }
     *     
     */
    public MonitoringStatus getLatestMonitoringStatus() {
        return latestMonitoringStatus;
    }

    /**
     * Sets the value of the latestMonitoringStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoringStatus }
     *     
     */
    public void setLatestMonitoringStatus(MonitoringStatus value) {
        this.latestMonitoringStatus = value;
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
     * Gets the value of the archived property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getArchived() {
        return archived;
    }

    /**
     * Sets the value of the archived property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setArchived(XMLGregorianCalendar value) {
        this.archived = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceSummary }
     *     
     */
    public ServiceSummary getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceSummary }
     *     
     */
    public void setSummary(ServiceSummary value) {
        this.summary = value;
    }

    /**
     * Gets the value of the deployments property.
     * 
     * @return
     *     possible object is
     *     {@link Service.Deployments }
     *     
     */
    public Service.Deployments getDeployments() {
        return deployments;
    }

    /**
     * Sets the value of the deployments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Service.Deployments }
     *     
     */
    public void setDeployments(Service.Deployments value) {
        this.deployments = value;
    }

    /**
     * Gets the value of the variants property.
     * 
     * @return
     *     possible object is
     *     {@link Service.Variants }
     *     
     */
    public Service.Variants getVariants() {
        return variants;
    }

    /**
     * Sets the value of the variants property.
     * 
     * @param value
     *     allowed object is
     *     {@link Service.Variants }
     *     
     */
    public void setVariants(Service.Variants value) {
        this.variants = value;
    }

    /**
     * Gets the value of the monitoring property.
     * 
     * @return
     *     possible object is
     *     {@link Service.Monitoring }
     *     
     */
    public Service.Monitoring getMonitoring() {
        return monitoring;
    }

    /**
     * Sets the value of the monitoring property.
     * 
     * @param value
     *     allowed object is
     *     {@link Service.Monitoring }
     *     
     */
    public void setMonitoring(Service.Monitoring value) {
        this.monitoring = value;
    }

    /**
     * Gets the value of the related property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceRelatedLinks }
     *     
     */
    public ServiceRelatedLinks getRelated() {
        return related;
    }

    /**
     * Sets the value of the related property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceRelatedLinks }
     *     
     */
    public void setRelated(ServiceRelatedLinks value) {
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
     *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
     *       &lt;sequence>
     *         &lt;element name="serviceDeployment" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceDeployment" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "serviceDeployment" })
    public static class Deployments extends ResourceLink {

        @XmlElement(required = true)
        protected List<ServiceDeployment> serviceDeployment;

        /**
         * Gets the value of the serviceDeployment property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the serviceDeployment property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getServiceDeployment().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ServiceDeployment }
         * 
         * 
         */
        public List<ServiceDeployment> getServiceDeployment() {
            if (serviceDeployment == null) {
                serviceDeployment = new ArrayList<ServiceDeployment>();
            }
            return this.serviceDeployment;
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
     *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
     *       &lt;sequence>
     *         &lt;element name="tests">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="serviceTest" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceTest" maxOccurs="unbounded" minOccurs="0"/>
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
    @XmlType(name = "", propOrder = { "tests" })
    public static class Monitoring extends ResourceLink {

        @XmlElement(required = true)
        protected Service.Monitoring.Tests tests;

        /**
         * Gets the value of the tests property.
         * 
         * @return
         *     possible object is
         *     {@link Service.Monitoring.Tests }
         *     
         */
        public Service.Monitoring.Tests getTests() {
            return tests;
        }

        /**
         * Sets the value of the tests property.
         * 
         * @param value
         *     allowed object is
         *     {@link Service.Monitoring.Tests }
         *     
         */
        public void setTests(Service.Monitoring.Tests value) {
            this.tests = value;
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
         *         &lt;element name="serviceTest" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceTest" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "serviceTest" })
        public static class Tests {

            protected List<ServiceTest> serviceTest;

            /**
             * Gets the value of the serviceTest property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the serviceTest property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getServiceTest().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ServiceTest }
             * 
             * 
             */
            public List<ServiceTest> getServiceTest() {
                if (serviceTest == null) {
                    serviceTest = new ArrayList<ServiceTest>();
                }
                return this.serviceTest;
            }
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
     *         &lt;element name="type" type="{http://www.biocatalogue.org/2009/xml/rest}ServiceTechnologyType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "type" })
    public static class ServiceTechnologyTypes {

        @XmlElement(required = true)
        protected List<ServiceTechnologyType> type;

        /**
         * Gets the value of the type property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the type property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ServiceTechnologyType }
         * 
         * 
         */
        public List<ServiceTechnologyType> getType() {
            if (type == null) {
                type = new ArrayList<ServiceTechnologyType>();
            }
            return this.type;
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
     *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
     *       &lt;sequence>
     *         &lt;choice maxOccurs="unbounded">
     *           &lt;element name="soapService" type="{http://www.biocatalogue.org/2009/xml/rest}SoapService" maxOccurs="unbounded" minOccurs="0"/>
     *           &lt;element name="restService" type="{http://www.biocatalogue.org/2009/xml/rest}RestService" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "soapServiceOrRestService" })
    public static class Variants extends ResourceLink {

        @XmlElements({ @XmlElement(name = "soapService", type = SoapService.class), @XmlElement(name = "restService", type = RestService.class) })
        protected List<ResourceLink> soapServiceOrRestService;

        /**
         * Gets the value of the soapServiceOrRestService property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the soapServiceOrRestService property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSoapServiceOrRestService().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SoapService }
         * {@link RestService }
         * 
         * 
         */
        public List<ResourceLink> getSoapServiceOrRestService() {
            if (soapServiceOrRestService == null) {
                soapServiceOrRestService = new ArrayList<ResourceLink>();
            }
            return this.soapServiceOrRestService;
        }
    }
}
