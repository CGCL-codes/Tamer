package org.biocatalogue.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for SoapOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapOperation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *       &lt;sequence>
 *         &lt;element ref="{http://purl.org/dc/elements/1.1/}title"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://purl.org/dc/elements/1.1/}description"/>
 *         &lt;element name="parameterOrder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://purl.org/dc/terms/}created"/>
 *         &lt;element name="archived" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="inputs" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *                 &lt;sequence>
 *                   &lt;element name="soapInput" type="{http://www.biocatalogue.org/2009/xml/rest}SoapInput" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="outputs" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *                 &lt;sequence>
 *                   &lt;element name="soapOutput" type="{http://www.biocatalogue.org/2009/xml/rest}SoapOutput" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ancestors" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="service" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *                   &lt;element name="soapService" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
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
 *                   &lt;element name="inputs" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *                   &lt;element name="outputs" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *                   &lt;element name="annotations" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *                   &lt;element name="annotationsOnAll" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
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
@XmlType(name = "SoapOperation", propOrder = { "title", "name", "description", "parameterOrder", "created", "archived", "inputs", "outputs", "ancestors", "related" })
public class SoapOperation extends ResourceLink {

    @XmlElementRef(name = "title", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class)
    protected JAXBElement<String> title;

    @XmlElement(required = true)
    protected String name;

    @XmlElementRef(name = "description", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class)
    protected JAXBElement<String> description;

    @XmlElement(required = true)
    protected String parameterOrder;

    @XmlElement(namespace = "http://purl.org/dc/terms/", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar archived;

    protected SoapOperation.Inputs inputs;

    protected SoapOperation.Outputs outputs;

    protected SoapOperation.Ancestors ancestors;

    protected SoapOperation.Related related;

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
     * Gets the value of the parameterOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterOrder() {
        return parameterOrder;
    }

    /**
     * Sets the value of the parameterOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterOrder(String value) {
        this.parameterOrder = value;
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
     * Gets the value of the inputs property.
     * 
     * @return
     *     possible object is
     *     {@link SoapOperation.Inputs }
     *     
     */
    public SoapOperation.Inputs getInputs() {
        return inputs;
    }

    /**
     * Sets the value of the inputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapOperation.Inputs }
     *     
     */
    public void setInputs(SoapOperation.Inputs value) {
        this.inputs = value;
    }

    /**
     * Gets the value of the outputs property.
     * 
     * @return
     *     possible object is
     *     {@link SoapOperation.Outputs }
     *     
     */
    public SoapOperation.Outputs getOutputs() {
        return outputs;
    }

    /**
     * Sets the value of the outputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapOperation.Outputs }
     *     
     */
    public void setOutputs(SoapOperation.Outputs value) {
        this.outputs = value;
    }

    /**
     * Gets the value of the ancestors property.
     * 
     * @return
     *     possible object is
     *     {@link SoapOperation.Ancestors }
     *     
     */
    public SoapOperation.Ancestors getAncestors() {
        return ancestors;
    }

    /**
     * Sets the value of the ancestors property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapOperation.Ancestors }
     *     
     */
    public void setAncestors(SoapOperation.Ancestors value) {
        this.ancestors = value;
    }

    /**
     * Gets the value of the related property.
     * 
     * @return
     *     possible object is
     *     {@link SoapOperation.Related }
     *     
     */
    public SoapOperation.Related getRelated() {
        return related;
    }

    /**
     * Sets the value of the related property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapOperation.Related }
     *     
     */
    public void setRelated(SoapOperation.Related value) {
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
     *         &lt;element name="soapService" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "service", "soapService" })
    public static class Ancestors {

        @XmlElement(required = true)
        protected ResourceLink service;

        @XmlElement(required = true)
        protected ResourceLink soapService;

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

        /**
         * Gets the value of the soapService property.
         * 
         * @return
         *     possible object is
         *     {@link ResourceLink }
         *     
         */
        public ResourceLink getSoapService() {
            return soapService;
        }

        /**
         * Sets the value of the soapService property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResourceLink }
         *     
         */
        public void setSoapService(ResourceLink value) {
            this.soapService = value;
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
     *         &lt;element name="soapInput" type="{http://www.biocatalogue.org/2009/xml/rest}SoapInput" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "soapInput" })
    public static class Inputs extends ResourceLink {

        protected List<SoapInput> soapInput;

        /**
         * Gets the value of the soapInput property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the soapInput property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSoapInput().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SoapInput }
         * 
         * 
         */
        public List<SoapInput> getSoapInput() {
            if (soapInput == null) {
                soapInput = new ArrayList<SoapInput>();
            }
            return this.soapInput;
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
     *         &lt;element name="soapOutput" type="{http://www.biocatalogue.org/2009/xml/rest}SoapOutput" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "soapOutput" })
    public static class Outputs extends ResourceLink {

        protected List<SoapOutput> soapOutput;

        /**
         * Gets the value of the soapOutput property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the soapOutput property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSoapOutput().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SoapOutput }
         * 
         * 
         */
        public List<SoapOutput> getSoapOutput() {
            if (soapOutput == null) {
                soapOutput = new ArrayList<SoapOutput>();
            }
            return this.soapOutput;
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
     *         &lt;element name="inputs" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *         &lt;element name="outputs" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *         &lt;element name="annotations" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *         &lt;element name="annotationsOnAll" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "inputs", "outputs", "annotations", "annotationsOnAll" })
    public static class Related {

        @XmlElement(required = true)
        protected ResourceLink inputs;

        @XmlElement(required = true)
        protected ResourceLink outputs;

        @XmlElement(required = true)
        protected ResourceLink annotations;

        @XmlElement(required = true)
        protected ResourceLink annotationsOnAll;

        /**
         * Gets the value of the inputs property.
         * 
         * @return
         *     possible object is
         *     {@link ResourceLink }
         *     
         */
        public ResourceLink getInputs() {
            return inputs;
        }

        /**
         * Sets the value of the inputs property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResourceLink }
         *     
         */
        public void setInputs(ResourceLink value) {
            this.inputs = value;
        }

        /**
         * Gets the value of the outputs property.
         * 
         * @return
         *     possible object is
         *     {@link ResourceLink }
         *     
         */
        public ResourceLink getOutputs() {
            return outputs;
        }

        /**
         * Sets the value of the outputs property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResourceLink }
         *     
         */
        public void setOutputs(ResourceLink value) {
            this.outputs = value;
        }

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

        /**
         * Gets the value of the annotationsOnAll property.
         * 
         * @return
         *     possible object is
         *     {@link ResourceLink }
         *     
         */
        public ResourceLink getAnnotationsOnAll() {
            return annotationsOnAll;
        }

        /**
         * Sets the value of the annotationsOnAll property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResourceLink }
         *     
         */
        public void setAnnotationsOnAll(ResourceLink value) {
            this.annotationsOnAll = value;
        }
    }
}
