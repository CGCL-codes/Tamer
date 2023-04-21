package de.uni_tuebingen.informatik.www_bs2.services.openms.mzquantmldraft.namespace;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for ExperimentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExperimentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="digestion" maxOccurs="unbounded">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="pepsin_a"/>
 *               &lt;enumeration value="chymotrypsin"/>
 *               &lt;enumeration value="proteinkinase_k"/>
 *               &lt;enumeration value="trypsin"/>
 *               &lt;enumeration value="no_enzyme"/>
 *               &lt;enumeration value="unknown_enzyme"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="labels" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="label" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="shift" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="experimentID" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="massMeasurement" type="{http://www-bs2.informatik.uni-tuebingen.de/services/OpenMS/mzQuantMLdraft/namespace}MassType" />
 *       &lt;attribute name="fileInfo">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="dataprocessingRefs" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExperimentType", propOrder = { "digestion", "labels" })
public class ExperimentType {

    @XmlElement(required = true)
    protected List<String> digestion;

    protected ExperimentType.Labels labels;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String experimentID;

    @XmlAttribute
    protected MassType massMeasurement;

    @XmlAttribute
    protected String fileInfo;

    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREFS")
    protected List<Object> dataprocessingRefs;

    /**
     * Gets the value of the digestion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the digestion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDigestion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDigestion() {
        if (digestion == null) {
            digestion = new ArrayList<String>();
        }
        return this.digestion;
    }

    /**
     * Gets the value of the labels property.
     * 
     * @return
     *     possible object is
     *     {@link ExperimentType.Labels }
     *     
     */
    public ExperimentType.Labels getLabels() {
        return labels;
    }

    /**
     * Sets the value of the labels property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExperimentType.Labels }
     *     
     */
    public void setLabels(ExperimentType.Labels value) {
        this.labels = value;
    }

    /**
     * Gets the value of the experimentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExperimentID() {
        return experimentID;
    }

    /**
     * Sets the value of the experimentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExperimentID(String value) {
        this.experimentID = value;
    }

    /**
     * Gets the value of the massMeasurement property.
     * 
     * @return
     *     possible object is
     *     {@link MassType }
     *     
     */
    public MassType getMassMeasurement() {
        return massMeasurement;
    }

    /**
     * Sets the value of the massMeasurement property.
     * 
     * @param value
     *     allowed object is
     *     {@link MassType }
     *     
     */
    public void setMassMeasurement(MassType value) {
        this.massMeasurement = value;
    }

    /**
     * Gets the value of the fileInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileInfo() {
        return fileInfo;
    }

    /**
     * Sets the value of the fileInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileInfo(String value) {
        this.fileInfo = value;
    }

    /**
     * Gets the value of the dataprocessingRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataprocessingRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataprocessingRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getDataprocessingRefs() {
        if (dataprocessingRefs == null) {
            dataprocessingRefs = new ArrayList<Object>();
        }
        return this.dataprocessingRefs;
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
     *         &lt;element name="label" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="shift" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
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
    @XmlType(name = "", propOrder = { "label" })
    public static class Labels {

        @XmlElement(required = true)
        protected List<ExperimentType.Labels.Label> label;

        /**
         * Gets the value of the label property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the label property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLabel().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ExperimentType.Labels.Label }
         * 
         * 
         */
        public List<ExperimentType.Labels.Label> getLabel() {
            if (label == null) {
                label = new ArrayList<ExperimentType.Labels.Label>();
            }
            return this.label;
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
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="shift" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Label {

            @XmlAttribute(required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlID
            @XmlSchemaType(name = "ID")
            protected String id;

            @XmlAttribute(required = true)
            protected String name;

            @XmlAttribute(required = true)
            protected double shift;

            /**
             * Gets the value of the id property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setId(String value) {
                this.id = value;
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
             * Gets the value of the shift property.
             * 
             */
            public double getShift() {
                return shift;
            }

            /**
             * Sets the value of the shift property.
             * 
             */
            public void setShift(double value) {
                this.shift = value;
            }
        }
    }
}
