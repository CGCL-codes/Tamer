package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v2_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Holder for physiological parameters such as conductivities and capacitances
 * 
 * <p>Java class for physiological_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="physiological_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="IntracellularConductivities" type="{https://chaste.comlab.ox.ac.uk/nss/parameters/2_0}conductivities_type" minOccurs="0"/>
 *         &lt;element name="ExtracellularConductivities" type="{https://chaste.comlab.ox.ac.uk/nss/parameters/2_0}conductivities_type" minOccurs="0"/>
 *         &lt;element name="BathConductivity" type="{https://chaste.comlab.ox.ac.uk/nss/parameters/2_0}conductivity_type" minOccurs="0"/>
 *         &lt;element name="SurfaceAreaToVolumeRatio" type="{https://chaste.comlab.ox.ac.uk/nss/parameters/2_0}inverse_length_type" minOccurs="0"/>
 *         &lt;element name="Capacitance" type="{https://chaste.comlab.ox.ac.uk/nss/parameters/2_0}capacitance_type" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "physiological_type", propOrder = {  })
public class PhysiologicalType {

    @XmlElement(name = "IntracellularConductivities")
    protected ConductivitiesType intracellularConductivities;

    @XmlElement(name = "ExtracellularConductivities")
    protected ConductivitiesType extracellularConductivities;

    @XmlElement(name = "BathConductivity")
    protected ConductivityType bathConductivity;

    @XmlElement(name = "SurfaceAreaToVolumeRatio")
    protected InverseLengthType surfaceAreaToVolumeRatio;

    @XmlElement(name = "Capacitance")
    protected CapacitanceType capacitance;

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
     * Gets the value of the bathConductivity property.
     * 
     * @return
     *     possible object is
     *     {@link ConductivityType }
     *     
     */
    public ConductivityType getBathConductivity() {
        return bathConductivity;
    }

    /**
     * Sets the value of the bathConductivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConductivityType }
     *     
     */
    public void setBathConductivity(ConductivityType value) {
        this.bathConductivity = value;
    }

    /**
     * Gets the value of the surfaceAreaToVolumeRatio property.
     * 
     * @return
     *     possible object is
     *     {@link InverseLengthType }
     *     
     */
    public InverseLengthType getSurfaceAreaToVolumeRatio() {
        return surfaceAreaToVolumeRatio;
    }

    /**
     * Sets the value of the surfaceAreaToVolumeRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link InverseLengthType }
     *     
     */
    public void setSurfaceAreaToVolumeRatio(InverseLengthType value) {
        this.surfaceAreaToVolumeRatio = value;
    }

    /**
     * Gets the value of the capacitance property.
     * 
     * @return
     *     possible object is
     *     {@link CapacitanceType }
     *     
     */
    public CapacitanceType getCapacitance() {
        return capacitance;
    }

    /**
     * Sets the value of the capacitance property.
     * 
     * @param value
     *     allowed object is
     *     {@link CapacitanceType }
     *     
     */
    public void setCapacitance(CapacitanceType value) {
        this.capacitance = value;
    }
}
