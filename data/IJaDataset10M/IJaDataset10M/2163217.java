package org.slaatsoi.prediction.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This element contains information about the kind of prediction to apply to the chosen SLA element.
 * 
 * For instance, to predict about mean time to repair (MTTR):
 * - Qos id: MTTR
 * - Predicto Id: uk.ac.city.soi.everestplus.predictor.MTTRredictor
 * 
 * <p>Java class for PredictorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PredictorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PredictorId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ConfigurationParameters" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Parameter" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AuxiliaryVariables" type="{http://www.slaatsoi.org/prediction-policy}AuxiliaryVariablesType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PredictorType", propOrder = { "predictorId", "configurationParameters", "auxiliaryVariables" })
public class PredictorType implements Serializable {

    @XmlElement(name = "PredictorId", required = true)
    protected String predictorId;

    @XmlElement(name = "ConfigurationParameters")
    protected PredictorType.ConfigurationParameters configurationParameters;

    @XmlElement(name = "AuxiliaryVariables")
    protected AuxiliaryVariablesType auxiliaryVariables;

    /**
     * Gets the value of the predictorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPredictorId() {
        return predictorId;
    }

    /**
     * Sets the value of the predictorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPredictorId(String value) {
        this.predictorId = value;
    }

    /**
     * Gets the value of the configurationParameters property.
     * 
     * @return
     *     possible object is
     *     {@link PredictorType.ConfigurationParameters }
     *     
     */
    public PredictorType.ConfigurationParameters getConfigurationParameters() {
        return configurationParameters;
    }

    /**
     * Sets the value of the configurationParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link PredictorType.ConfigurationParameters }
     *     
     */
    public void setConfigurationParameters(PredictorType.ConfigurationParameters value) {
        this.configurationParameters = value;
    }

    /**
     * Gets the value of the auxiliaryVariables property.
     * 
     * @return
     *     possible object is
     *     {@link AuxiliaryVariablesType }
     *     
     */
    public AuxiliaryVariablesType getAuxiliaryVariables() {
        return auxiliaryVariables;
    }

    /**
     * Sets the value of the auxiliaryVariables property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuxiliaryVariablesType }
     *     
     */
    public void setAuxiliaryVariables(AuxiliaryVariablesType value) {
        this.auxiliaryVariables = value;
    }

    /**
     * 
     *         					The parameters necessary to initialise the
     *         					predictor. Everest-Plus dynamically load a predicto
     *         					by its Id, whose valuse correspont to the predictor
     *         					class name, and than invoke its constructor selected
     *         					with respect to the number of provided parameters.
     *         				
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Parameter" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
    public static class ConfigurationParameters implements Serializable {

        @XmlElement(name = "Parameter", required = true)
        protected List<PredictorType.ConfigurationParameters.Parameter> parameter;

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
         * {@link PredictorType.ConfigurationParameters.Parameter }
         * 
         * 
         */
        public List<PredictorType.ConfigurationParameters.Parameter> getParameter() {
            if (parameter == null) {
                parameter = new ArrayList<PredictorType.ConfigurationParameters.Parameter>();
            }
            return this.parameter;
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
         *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Parameter implements Serializable {

            @XmlAttribute
            protected String key;

            @XmlAttribute
            protected String value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
