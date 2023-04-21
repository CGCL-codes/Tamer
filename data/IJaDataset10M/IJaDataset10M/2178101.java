package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ServicesRelatedLinks complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServicesRelatedLinks">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}CollectionCoreRelatedLinks">
 *       &lt;sequence>
 *         &lt;element name="filters" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="filtersOnCurrentResults" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="withSummaries" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="withDeployments" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="withVariants" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="withMonitoring" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *         &lt;element name="withAllSections" type="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServicesRelatedLinks", propOrder = { "filters", "filtersOnCurrentResults", "withSummaries", "withDeployments", "withVariants", "withMonitoring", "withAllSections" })
public class ServicesRelatedLinks extends CollectionCoreRelatedLinks {

    @XmlElement(required = true)
    protected ResourceLink filters;

    @XmlElement(required = true)
    protected ResourceLink filtersOnCurrentResults;

    @XmlElement(required = true)
    protected ResourceLink withSummaries;

    @XmlElement(required = true)
    protected ResourceLink withDeployments;

    @XmlElement(required = true)
    protected ResourceLink withVariants;

    @XmlElement(required = true)
    protected ResourceLink withMonitoring;

    @XmlElement(required = true)
    protected ResourceLink withAllSections;

    /**
     * Gets the value of the filters property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getFilters() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setFilters(ResourceLink value) {
        this.filters = value;
    }

    /**
     * Gets the value of the filtersOnCurrentResults property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getFiltersOnCurrentResults() {
        return filtersOnCurrentResults;
    }

    /**
     * Sets the value of the filtersOnCurrentResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setFiltersOnCurrentResults(ResourceLink value) {
        this.filtersOnCurrentResults = value;
    }

    /**
     * Gets the value of the withSummaries property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getWithSummaries() {
        return withSummaries;
    }

    /**
     * Sets the value of the withSummaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setWithSummaries(ResourceLink value) {
        this.withSummaries = value;
    }

    /**
     * Gets the value of the withDeployments property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getWithDeployments() {
        return withDeployments;
    }

    /**
     * Sets the value of the withDeployments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setWithDeployments(ResourceLink value) {
        this.withDeployments = value;
    }

    /**
     * Gets the value of the withVariants property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getWithVariants() {
        return withVariants;
    }

    /**
     * Sets the value of the withVariants property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setWithVariants(ResourceLink value) {
        this.withVariants = value;
    }

    /**
     * Gets the value of the withMonitoring property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getWithMonitoring() {
        return withMonitoring;
    }

    /**
     * Sets the value of the withMonitoring property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setWithMonitoring(ResourceLink value) {
        this.withMonitoring = value;
    }

    /**
     * Gets the value of the withAllSections property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceLink }
     *     
     */
    public ResourceLink getWithAllSections() {
        return withAllSections;
    }

    /**
     * Sets the value of the withAllSections property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceLink }
     *     
     */
    public void setWithAllSections(ResourceLink value) {
        this.withAllSections = value;
    }
}
