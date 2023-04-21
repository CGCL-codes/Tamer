package uk.ac.ebi.rhea.ws.response.sbml1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Model complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Model">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.sbml.org/sbml/level1}SBase">
 *       &lt;sequence>
 *         &lt;element name="listOfUnitDefinitions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="unitDefinition" type="{http://www.sbml.org/sbml/level1}UnitDefinition" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="listOfCompartments">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="compartment" type="{http://www.sbml.org/sbml/level1}Compartment" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="listOfSpecies" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="species" type="{http://www.sbml.org/sbml/level1}Species" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="listOfParameters" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="parameter" type="{http://www.sbml.org/sbml/level1}Parameter" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="listOfRules" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded">
 *                   &lt;element name="algebraicRule" type="{http://www.sbml.org/sbml/level1}AlgebraicRule" minOccurs="0"/>
 *                   &lt;element name="compartmentVolumeRule" type="{http://www.sbml.org/sbml/level1}CompartmentVolumeRule" minOccurs="0"/>
 *                   &lt;element name="speciesConcentrationRule" type="{http://www.sbml.org/sbml/level1}SpeciesConcentrationRule" minOccurs="0"/>
 *                   &lt;element name="parameterRule" type="{http://www.sbml.org/sbml/level1}ParameterRule" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="listOfReactions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="reaction" type="{http://www.sbml.org/sbml/level1}Reaction" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.sbml.org/sbml/level1}SName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Model", propOrder = { "listOfUnitDefinitions", "listOfCompartments", "listOfSpecies", "listOfParameters", "listOfRules", "listOfReactions" })
public class Model extends SBase {

    protected Model.ListOfUnitDefinitions listOfUnitDefinitions;

    @XmlElement(required = true)
    protected Model.ListOfCompartments listOfCompartments;

    protected Model.ListOfSpecies listOfSpecies;

    protected Model.ListOfParameters listOfParameters;

    protected Model.ListOfRules listOfRules;

    protected Model.ListOfReactions listOfReactions;

    @XmlAttribute
    protected String name;

    /**
     * Gets the value of the listOfUnitDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link Model.ListOfUnitDefinitions }
     *     
     */
    public Model.ListOfUnitDefinitions getListOfUnitDefinitions() {
        return listOfUnitDefinitions;
    }

    /**
     * Sets the value of the listOfUnitDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.ListOfUnitDefinitions }
     *     
     */
    public void setListOfUnitDefinitions(Model.ListOfUnitDefinitions value) {
        this.listOfUnitDefinitions = value;
    }

    /**
     * Gets the value of the listOfCompartments property.
     * 
     * @return
     *     possible object is
     *     {@link Model.ListOfCompartments }
     *     
     */
    public Model.ListOfCompartments getListOfCompartments() {
        return listOfCompartments;
    }

    /**
     * Sets the value of the listOfCompartments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.ListOfCompartments }
     *     
     */
    public void setListOfCompartments(Model.ListOfCompartments value) {
        this.listOfCompartments = value;
    }

    /**
     * Gets the value of the listOfSpecies property.
     * 
     * @return
     *     possible object is
     *     {@link Model.ListOfSpecies }
     *     
     */
    public Model.ListOfSpecies getListOfSpecies() {
        return listOfSpecies;
    }

    /**
     * Sets the value of the listOfSpecies property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.ListOfSpecies }
     *     
     */
    public void setListOfSpecies(Model.ListOfSpecies value) {
        this.listOfSpecies = value;
    }

    /**
     * Gets the value of the listOfParameters property.
     * 
     * @return
     *     possible object is
     *     {@link Model.ListOfParameters }
     *     
     */
    public Model.ListOfParameters getListOfParameters() {
        return listOfParameters;
    }

    /**
     * Sets the value of the listOfParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.ListOfParameters }
     *     
     */
    public void setListOfParameters(Model.ListOfParameters value) {
        this.listOfParameters = value;
    }

    /**
     * Gets the value of the listOfRules property.
     * 
     * @return
     *     possible object is
     *     {@link Model.ListOfRules }
     *     
     */
    public Model.ListOfRules getListOfRules() {
        return listOfRules;
    }

    /**
     * Sets the value of the listOfRules property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.ListOfRules }
     *     
     */
    public void setListOfRules(Model.ListOfRules value) {
        this.listOfRules = value;
    }

    /**
     * Gets the value of the listOfReactions property.
     * 
     * @return
     *     possible object is
     *     {@link Model.ListOfReactions }
     *     
     */
    public Model.ListOfReactions getListOfReactions() {
        return listOfReactions;
    }

    /**
     * Sets the value of the listOfReactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model.ListOfReactions }
     *     
     */
    public void setListOfReactions(Model.ListOfReactions value) {
        this.listOfReactions = value;
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
     *         &lt;element name="compartment" type="{http://www.sbml.org/sbml/level1}Compartment" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "compartment" })
    public static class ListOfCompartments {

        @XmlElement(required = true)
        protected List<Compartment> compartment;

        /**
         * Gets the value of the compartment property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the compartment property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCompartment().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Compartment }
         * 
         * 
         */
        public List<Compartment> getCompartment() {
            if (compartment == null) {
                compartment = new ArrayList<Compartment>();
            }
            return this.compartment;
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
     *         &lt;element name="parameter" type="{http://www.sbml.org/sbml/level1}Parameter" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "parameter" })
    public static class ListOfParameters {

        @XmlElement(required = true)
        protected List<Parameter> parameter;

        /**
         * Gets the value of the parameter property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the parameter property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getParameter().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Parameter }
         * 
         * 
         */
        public List<Parameter> getParameter() {
            if (parameter == null) {
                parameter = new ArrayList<Parameter>();
            }
            return this.parameter;
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
     *         &lt;element name="reaction" type="{http://www.sbml.org/sbml/level1}Reaction" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "reaction" })
    public static class ListOfReactions {

        @XmlElement(required = true)
        protected List<Reaction> reaction;

        /**
         * Gets the value of the reaction property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the reaction property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getReaction().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Reaction }
         * 
         * 
         */
        public List<Reaction> getReaction() {
            if (reaction == null) {
                reaction = new ArrayList<Reaction>();
            }
            return this.reaction;
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
     *       &lt;choice maxOccurs="unbounded">
     *         &lt;element name="algebraicRule" type="{http://www.sbml.org/sbml/level1}AlgebraicRule" minOccurs="0"/>
     *         &lt;element name="compartmentVolumeRule" type="{http://www.sbml.org/sbml/level1}CompartmentVolumeRule" minOccurs="0"/>
     *         &lt;element name="speciesConcentrationRule" type="{http://www.sbml.org/sbml/level1}SpeciesConcentrationRule" minOccurs="0"/>
     *         &lt;element name="parameterRule" type="{http://www.sbml.org/sbml/level1}ParameterRule" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule" })
    public static class ListOfRules {

        @XmlElements({ @XmlElement(name = "compartmentVolumeRule", type = CompartmentVolumeRule.class), @XmlElement(name = "parameterRule", type = ParameterRule.class), @XmlElement(name = "speciesConcentrationRule", type = SpeciesConcentrationRule.class), @XmlElement(name = "algebraicRule", type = AlgebraicRule.class) })
        protected List<Rule> algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule;

        /**
         * Gets the value of the algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAlgebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CompartmentVolumeRule }
         * {@link ParameterRule }
         * {@link SpeciesConcentrationRule }
         * {@link AlgebraicRule }
         * 
         * 
         */
        public List<Rule> getAlgebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule() {
            if (algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule == null) {
                algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule = new ArrayList<Rule>();
            }
            return this.algebraicRuleOrCompartmentVolumeRuleOrSpeciesConcentrationRule;
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
     *         &lt;element name="species" type="{http://www.sbml.org/sbml/level1}Species" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "species" })
    public static class ListOfSpecies {

        @XmlElement(required = true)
        protected List<Species> species;

        /**
         * Gets the value of the species property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the species property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSpecies().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Species }
         * 
         * 
         */
        public List<Species> getSpecies() {
            if (species == null) {
                species = new ArrayList<Species>();
            }
            return this.species;
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
     *         &lt;element name="unitDefinition" type="{http://www.sbml.org/sbml/level1}UnitDefinition" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "unitDefinition" })
    public static class ListOfUnitDefinitions {

        @XmlElement(required = true)
        protected List<UnitDefinition> unitDefinition;

        /**
         * Gets the value of the unitDefinition property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the unitDefinition property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUnitDefinition().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link UnitDefinition }
         * 
         * 
         */
        public List<UnitDefinition> getUnitDefinition() {
            if (unitDefinition == null) {
                unitDefinition = new ArrayList<UnitDefinition>();
            }
            return this.unitDefinition;
        }
    }
}
