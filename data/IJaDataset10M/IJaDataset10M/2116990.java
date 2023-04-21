package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for conductivity_heterogeneity_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conductivity_heterogeneity_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IntracellularConductivities" type="{}conductivities_type" minOccurs="0"/>
 *         &lt;element name="ExtracellularConductivities" type="{}conductivities_type" minOccurs="0"/>
 *         &lt;element name="Location" type="{}location_type"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conductivity_heterogeneity_type", propOrder = {  })
public class ConductivityHeterogeneityType {

    @XmlElement(name = "IntracellularConductivities")
    protected ConductivitiesType intracellularConductivities;

    @XmlElement(name = "ExtracellularConductivities")
    protected ConductivitiesType extracellularConductivities;

    @XmlElement(name = "Location", required = true)
    protected LocationType location;

    /**
     * Gets the value of the intracellularConductivities property.
     * 
     * @return
     *     possible object is
     *     {@link ConductivitiesType }
     *     
     */
    public ConductivitiesType getIntracellularConductivities() {
        return intracellularConductivities;
    }

    /**
     * Sets the value of the intracellularConductivities property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConductivitiesType }
     *     
     */
    public void setIntracellularConductivities(ConductivitiesType value) {
        this.intracellularConductivities = value;
    }

    /**
     * Gets the value of the extracellularConductivities property.
     * 
     * @return
     *     possible object is
     *     {@link ConductivitiesType }
     *     
     */
    public ConductivitiesType getExtracellularConductivities() {
        return extracellularConductivities;
    }

    /**
     * Sets the value of the extracellularConductivities property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConductivitiesType }
     *     
     */
    public void setExtracellularConductivities(ConductivitiesType value) {
        this.extracellularConductivities = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link LocationType }
     *     
     */
    public LocationType getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationType }
     *     
     */
    public void setLocation(LocationType value) {
        this.location = value;
    }
}
