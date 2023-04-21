package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v1_0;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for simulation_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="simulation_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="SpaceDimension" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SimulationDuration" type="{}time_type" minOccurs="0"/>
 *         &lt;element name="Domain" type="{}domain_type" minOccurs="0"/>
 *         &lt;element name="Mesh" type="{}mesh_type" minOccurs="0"/>
 *         &lt;element name="IonicModels" type="{}ionic_models_type" minOccurs="0"/>
 *         &lt;element name="Stimuli" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Stimulus" type="{}stimulus_type" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CellHeterogeneities" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CellHeterogeneity" type="{}cell_heterogeneity_type" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ConductivityHeterogeneities" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ConductivityHeterogeneity" type="{}conductivity_heterogeneity_type" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OutputDirectory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OutputFilenamePrefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simulation_type", propOrder = {  })
public class SimulationType {

    @XmlElement(name = "SpaceDimension")
    protected BigInteger spaceDimension;

    @XmlElement(name = "SimulationDuration")
    protected TimeType simulationDuration;

    @XmlElement(name = "Domain")
    protected DomainType domain;

    @XmlElement(name = "Mesh")
    protected MeshType mesh;

    @XmlElement(name = "IonicModels")
    protected IonicModelsType ionicModels;

    @XmlElement(name = "Stimuli")
    protected SimulationType.Stimuli stimuli;

    @XmlElement(name = "CellHeterogeneities")
    protected SimulationType.CellHeterogeneities cellHeterogeneities;

    @XmlElement(name = "ConductivityHeterogeneities")
    protected SimulationType.ConductivityHeterogeneities conductivityHeterogeneities;

    @XmlElement(name = "OutputDirectory")
    protected String outputDirectory;

    @XmlElement(name = "OutputFilenamePrefix")
    protected String outputFilenamePrefix;

    /**
     * Gets the value of the spaceDimension property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSpaceDimension() {
        return spaceDimension;
    }

    /**
     * Sets the value of the spaceDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSpaceDimension(BigInteger value) {
        this.spaceDimension = value;
    }

    /**
     * Gets the value of the simulationDuration property.
     * 
     * @return
     *     possible object is
     *     {@link TimeType }
     *     
     */
    public TimeType getSimulationDuration() {
        return simulationDuration;
    }

    /**
     * Sets the value of the simulationDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeType }
     *     
     */
    public void setSimulationDuration(TimeType value) {
        this.simulationDuration = value;
    }

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link DomainType }
     *     
     */
    public DomainType getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainType }
     *     
     */
    public void setDomain(DomainType value) {
        this.domain = value;
    }

    /**
     * Gets the value of the mesh property.
     * 
     * @return
     *     possible object is
     *     {@link MeshType }
     *     
     */
    public MeshType getMesh() {
        return mesh;
    }

    /**
     * Sets the value of the mesh property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeshType }
     *     
     */
    public void setMesh(MeshType value) {
        this.mesh = value;
    }

    /**
     * Gets the value of the ionicModels property.
     * 
     * @return
     *     possible object is
     *     {@link IonicModelsType }
     *     
     */
    public IonicModelsType getIonicModels() {
        return ionicModels;
    }

    /**
     * Sets the value of the ionicModels property.
     * 
     * @param value
     *     allowed object is
     *     {@link IonicModelsType }
     *     
     */
    public void setIonicModels(IonicModelsType value) {
        this.ionicModels = value;
    }

    /**
     * Gets the value of the stimuli property.
     * 
     * @return
     *     possible object is
     *     {@link SimulationType.Stimuli }
     *     
     */
    public SimulationType.Stimuli getStimuli() {
        return stimuli;
    }

    /**
     * Sets the value of the stimuli property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimulationType.Stimuli }
     *     
     */
    public void setStimuli(SimulationType.Stimuli value) {
        this.stimuli = value;
    }

    /**
     * Gets the value of the cellHeterogeneities property.
     * 
     * @return
     *     possible object is
     *     {@link SimulationType.CellHeterogeneities }
     *     
     */
    public SimulationType.CellHeterogeneities getCellHeterogeneities() {
        return cellHeterogeneities;
    }

    /**
     * Sets the value of the cellHeterogeneities property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimulationType.CellHeterogeneities }
     *     
     */
    public void setCellHeterogeneities(SimulationType.CellHeterogeneities value) {
        this.cellHeterogeneities = value;
    }

    /**
     * Gets the value of the conductivityHeterogeneities property.
     * 
     * @return
     *     possible object is
     *     {@link SimulationType.ConductivityHeterogeneities }
     *     
     */
    public SimulationType.ConductivityHeterogeneities getConductivityHeterogeneities() {
        return conductivityHeterogeneities;
    }

    /**
     * Sets the value of the conductivityHeterogeneities property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimulationType.ConductivityHeterogeneities }
     *     
     */
    public void setConductivityHeterogeneities(SimulationType.ConductivityHeterogeneities value) {
        this.conductivityHeterogeneities = value;
    }

    /**
     * Gets the value of the outputDirectory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Sets the value of the outputDirectory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputDirectory(String value) {
        this.outputDirectory = value;
    }

    /**
     * Gets the value of the outputFilenamePrefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputFilenamePrefix() {
        return outputFilenamePrefix;
    }

    /**
     * Sets the value of the outputFilenamePrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputFilenamePrefix(String value) {
        this.outputFilenamePrefix = value;
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
     *         &lt;element name="CellHeterogeneity" type="{}cell_heterogeneity_type" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "cellHeterogeneity" })
    public static class CellHeterogeneities {

        @XmlElement(name = "CellHeterogeneity")
        protected List<CellHeterogeneityType> cellHeterogeneity;

        /**
         * Gets the value of the cellHeterogeneity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cellHeterogeneity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCellHeterogeneity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CellHeterogeneityType }
         * 
         * 
         */
        public List<CellHeterogeneityType> getCellHeterogeneity() {
            if (cellHeterogeneity == null) {
                cellHeterogeneity = new ArrayList<CellHeterogeneityType>();
            }
            return this.cellHeterogeneity;
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
     *         &lt;element name="ConductivityHeterogeneity" type="{}conductivity_heterogeneity_type" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "conductivityHeterogeneity" })
    public static class ConductivityHeterogeneities {

        @XmlElement(name = "ConductivityHeterogeneity")
        protected List<ConductivityHeterogeneityType> conductivityHeterogeneity;

        /**
         * Gets the value of the conductivityHeterogeneity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the conductivityHeterogeneity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getConductivityHeterogeneity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ConductivityHeterogeneityType }
         * 
         * 
         */
        public List<ConductivityHeterogeneityType> getConductivityHeterogeneity() {
            if (conductivityHeterogeneity == null) {
                conductivityHeterogeneity = new ArrayList<ConductivityHeterogeneityType>();
            }
            return this.conductivityHeterogeneity;
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
     *         &lt;element name="Stimulus" type="{}stimulus_type" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "stimulus" })
    public static class Stimuli {

        @XmlElement(name = "Stimulus", required = true)
        protected List<StimulusType> stimulus;

        /**
         * Gets the value of the stimulus property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the stimulus property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getStimulus().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StimulusType }
         * 
         * 
         */
        public List<StimulusType> getStimulus() {
            if (stimulus == null) {
                stimulus = new ArrayList<StimulusType>();
            }
            return this.stimulus;
        }
    }
}
