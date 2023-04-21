package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for SoapServices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapServices">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.biocatalogue.org/2009/xml/rest}ResourceLink">
 *       &lt;sequence>
 *         &lt;element name="parameters" type="{http://www.biocatalogue.org/2009/xml/rest}SoapServicesParameters"/>
 *         &lt;element name="statistics" type="{http://www.biocatalogue.org/2009/xml/rest}SoapServicesStatistics"/>
 *         &lt;element name="results" type="{http://www.biocatalogue.org/2009/xml/rest}SoapServicesResults"/>
 *         &lt;element name="related" type="{http://www.biocatalogue.org/2009/xml/rest}SoapServicesRelatedLinks"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapServices", propOrder = { "parameters", "statistics", "results", "related" })
public class SoapServices extends ResourceLink {

    @XmlElement(required = true)
    protected SoapServicesParameters parameters;

    @XmlElement(required = true)
    protected SoapServicesStatistics statistics;

    @XmlElement(required = true)
    protected SoapServicesResults results;

    @XmlElement(required = true)
    protected SoapServicesRelatedLinks related;

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link SoapServicesParameters }
     *     
     */
    public SoapServicesParameters getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapServicesParameters }
     *     
     */
    public void setParameters(SoapServicesParameters value) {
        this.parameters = value;
    }

    /**
     * Gets the value of the statistics property.
     * 
     * @return
     *     possible object is
     *     {@link SoapServicesStatistics }
     *     
     */
    public SoapServicesStatistics getStatistics() {
        return statistics;
    }

    /**
     * Sets the value of the statistics property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapServicesStatistics }
     *     
     */
    public void setStatistics(SoapServicesStatistics value) {
        this.statistics = value;
    }

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link SoapServicesResults }
     *     
     */
    public SoapServicesResults getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapServicesResults }
     *     
     */
    public void setResults(SoapServicesResults value) {
        this.results = value;
    }

    /**
     * Gets the value of the related property.
     * 
     * @return
     *     possible object is
     *     {@link SoapServicesRelatedLinks }
     *     
     */
    public SoapServicesRelatedLinks getRelated() {
        return related;
    }

    /**
     * Sets the value of the related property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoapServicesRelatedLinks }
     *     
     */
    public void setRelated(SoapServicesRelatedLinks value) {
        this.related = value;
    }
}
