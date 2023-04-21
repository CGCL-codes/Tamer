package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RegistryRelatedLinks complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistryRelatedLinks">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annotationsBy" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="services" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistryRelatedLinks", propOrder = { "annotationsBy", "services" })
public class RegistryRelatedLinks {

    @XmlElement(required = true)
    protected ResourceLink annotationsBy;

    @XmlElement(required = true)
    protected ResourceLink services;

    /**
     * Gets the value of the annotationsBy property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getAnnotationsBy() {
        return annotationsBy;
    }

    /**
     * Sets the value of the annotationsBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setAnnotationsBy(ResourceLink value) {
        this.annotationsBy = value;
    }

    /**
     * Gets the value of the services property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getServices() {
        return services;
    }

    /**
     * Sets the value of the services property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setServices(ResourceLink value) {
        this.services = value;
    }
}
