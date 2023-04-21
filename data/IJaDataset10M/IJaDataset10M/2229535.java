package org.mcisb.massspectrometry.pride.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="ExperimentAccession" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Reference" type="{}ReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ShortLabel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Protocol">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ProtocolName">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="ProtocolSteps" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="StepDescription" type="{}paramType" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="mzData">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="cvLookup" type="{}cvLookupType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="description">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="admin" type="{}adminType"/>
 *                             &lt;element name="instrument" type="{}instrumentDescriptionType"/>
 *                             &lt;element name="dataProcessing" type="{}dataProcessingType"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="spectrumList">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="spectrum" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;extension base="{}spectrumType">
 *                                   &lt;/extension>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.05" />
 *                 &lt;attribute name="accessionNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GelFreeIdentification" type="{}GelFreeIdentificationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TwoDimensionalIdentification" type="{}TwoDimensionalIdentificationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="additional" type="{}paramType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExperimentType", propOrder = { "experimentAccession", "title", "reference", "shortLabel", "protocol", "mzData", "gelFreeIdentification", "twoDimensionalIdentification", "additional" })
public class ExperimentType {

    @XmlElement(name = "ExperimentAccession")
    protected String experimentAccession;

    @XmlElement(name = "Title", required = true)
    protected String title;

    @XmlElement(name = "Reference")
    protected List<ReferenceType> reference;

    @XmlElement(name = "ShortLabel", required = true)
    protected String shortLabel;

    @XmlElement(name = "Protocol", required = true)
    protected ExperimentType.Protocol protocol;

    @XmlElement(required = true)
    protected ExperimentType.MzData mzData;

    @XmlElement(name = "GelFreeIdentification")
    protected List<GelFreeIdentificationType> gelFreeIdentification;

    @XmlElement(name = "TwoDimensionalIdentification")
    protected List<TwoDimensionalIdentificationType> twoDimensionalIdentification;

    protected ParamType additional;

    /**
     * Gets the value of the experimentAccession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExperimentAccession() {
        return experimentAccession;
    }

    /**
     * Sets the value of the experimentAccession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExperimentAccession(String value) {
        this.experimentAccession = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     * 
     * 
     */
    public List<ReferenceType> getReference() {
        if (reference == null) {
            reference = new ArrayList<ReferenceType>();
        }
        return this.reference;
    }

    /**
     * Gets the value of the shortLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * Sets the value of the shortLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortLabel(String value) {
        this.shortLabel = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link ExperimentType.Protocol }
     *     
     */
    public ExperimentType.Protocol getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExperimentType.Protocol }
     *     
     */
    public void setProtocol(ExperimentType.Protocol value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the mzData property.
     * 
     * @return
     *     possible object is
     *     {@link ExperimentType.MzData }
     *     
     */
    public ExperimentType.MzData getMzData() {
        return mzData;
    }

    /**
     * Sets the value of the mzData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExperimentType.MzData }
     *     
     */
    public void setMzData(ExperimentType.MzData value) {
        this.mzData = value;
    }

    /**
     * Gets the value of the gelFreeIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gelFreeIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGelFreeIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelFreeIdentificationType }
     * 
     * 
     */
    public List<GelFreeIdentificationType> getGelFreeIdentification() {
        if (gelFreeIdentification == null) {
            gelFreeIdentification = new ArrayList<GelFreeIdentificationType>();
        }
        return this.gelFreeIdentification;
    }

    /**
     * Gets the value of the twoDimensionalIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the twoDimensionalIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTwoDimensionalIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TwoDimensionalIdentificationType }
     * 
     * 
     */
    public List<TwoDimensionalIdentificationType> getTwoDimensionalIdentification() {
        if (twoDimensionalIdentification == null) {
            twoDimensionalIdentification = new ArrayList<TwoDimensionalIdentificationType>();
        }
        return this.twoDimensionalIdentification;
    }

    /**
     * Gets the value of the additional property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getAdditional() {
        return additional;
    }

    /**
     * Sets the value of the additional property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setAdditional(ParamType value) {
        this.additional = value;
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
     *         &lt;element name="cvLookup" type="{}cvLookupType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="description">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="admin" type="{}adminType"/>
     *                   &lt;element name="instrument" type="{}instrumentDescriptionType"/>
     *                   &lt;element name="dataProcessing" type="{}dataProcessingType"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="spectrumList">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="spectrum" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;extension base="{}spectrumType">
     *                         &lt;/extension>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.05" />
     *       &lt;attribute name="accessionNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "cvLookup", "description", "spectrumList" })
    public static class MzData {

        protected List<CvLookupType> cvLookup;

        @XmlElement(required = true)
        protected ExperimentType.MzData.Description description;

        @XmlElement(required = true)
        protected ExperimentType.MzData.SpectrumList spectrumList;

        @XmlAttribute(required = true)
        protected String version;

        @XmlAttribute(required = true)
        protected String accessionNumber;

        /**
         * Gets the value of the cvLookup property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cvLookup property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCvLookup().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CvLookupType }
         * 
         * 
         */
        public List<CvLookupType> getCvLookup() {
            if (cvLookup == null) {
                cvLookup = new ArrayList<CvLookupType>();
            }
            return this.cvLookup;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link ExperimentType.MzData.Description }
         *     
         */
        public ExperimentType.MzData.Description getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link ExperimentType.MzData.Description }
         *     
         */
        public void setDescription(ExperimentType.MzData.Description value) {
            this.description = value;
        }

        /**
         * Gets the value of the spectrumList property.
         * 
         * @return
         *     possible object is
         *     {@link ExperimentType.MzData.SpectrumList }
         *     
         */
        public ExperimentType.MzData.SpectrumList getSpectrumList() {
            return spectrumList;
        }

        /**
         * Sets the value of the spectrumList property.
         * 
         * @param value
         *     allowed object is
         *     {@link ExperimentType.MzData.SpectrumList }
         *     
         */
        public void setSpectrumList(ExperimentType.MzData.SpectrumList value) {
            this.spectrumList = value;
        }

        /**
         * Gets the value of the version property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVersion() {
            if (version == null) {
                return "1.05";
            } else {
                return version;
            }
        }

        /**
         * Sets the value of the version property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVersion(String value) {
            this.version = value;
        }

        /**
         * Gets the value of the accessionNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccessionNumber() {
            return accessionNumber;
        }

        /**
         * Sets the value of the accessionNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccessionNumber(String value) {
            this.accessionNumber = value;
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
         *         &lt;element name="admin" type="{}adminType"/>
         *         &lt;element name="instrument" type="{}instrumentDescriptionType"/>
         *         &lt;element name="dataProcessing" type="{}dataProcessingType"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "admin", "instrument", "dataProcessing" })
        public static class Description {

            @XmlElement(required = true)
            protected AdminType admin;

            @XmlElement(required = true)
            protected InstrumentDescriptionType instrument;

            @XmlElement(required = true)
            protected DataProcessingType dataProcessing;

            /**
             * Gets the value of the admin property.
             * 
             * @return
             *     possible object is
             *     {@link AdminType }
             *     
             */
            public AdminType getAdmin() {
                return admin;
            }

            /**
             * Sets the value of the admin property.
             * 
             * @param value
             *     allowed object is
             *     {@link AdminType }
             *     
             */
            public void setAdmin(AdminType value) {
                this.admin = value;
            }

            /**
             * Gets the value of the instrument property.
             * 
             * @return
             *     possible object is
             *     {@link InstrumentDescriptionType }
             *     
             */
            public InstrumentDescriptionType getInstrument() {
                return instrument;
            }

            /**
             * Sets the value of the instrument property.
             * 
             * @param value
             *     allowed object is
             *     {@link InstrumentDescriptionType }
             *     
             */
            public void setInstrument(InstrumentDescriptionType value) {
                this.instrument = value;
            }

            /**
             * Gets the value of the dataProcessing property.
             * 
             * @return
             *     possible object is
             *     {@link DataProcessingType }
             *     
             */
            public DataProcessingType getDataProcessing() {
                return dataProcessing;
            }

            /**
             * Sets the value of the dataProcessing property.
             * 
             * @param value
             *     allowed object is
             *     {@link DataProcessingType }
             *     
             */
            public void setDataProcessing(DataProcessingType value) {
                this.dataProcessing = value;
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
         *         &lt;element name="spectrum" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;extension base="{}spectrumType">
         *               &lt;/extension>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "spectrum" })
        public static class SpectrumList {

            protected List<ExperimentType.MzData.SpectrumList.Spectrum> spectrum;

            @XmlAttribute(required = true)
            protected int count;

            /**
             * Gets the value of the spectrum property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the spectrum property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getSpectrum().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ExperimentType.MzData.SpectrumList.Spectrum }
             * 
             * 
             */
            public List<ExperimentType.MzData.SpectrumList.Spectrum> getSpectrum() {
                if (spectrum == null) {
                    spectrum = new ArrayList<ExperimentType.MzData.SpectrumList.Spectrum>();
                }
                return this.spectrum;
            }

            /**
             * Gets the value of the count property.
             * 
             */
            public int getCount() {
                return count;
            }

            /**
             * Sets the value of the count property.
             * 
             */
            public void setCount(int value) {
                this.count = value;
            }

            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;extension base="{}spectrumType">
             *     &lt;/extension>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Spectrum extends SpectrumType {
            }
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
     *         &lt;element name="ProtocolName">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="ProtocolSteps" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="StepDescription" type="{}paramType" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
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
    @XmlType(name = "", propOrder = { "protocolName", "protocolSteps" })
    public static class Protocol {

        @XmlElement(name = "ProtocolName", required = true)
        protected String protocolName;

        @XmlElement(name = "ProtocolSteps")
        protected ExperimentType.Protocol.ProtocolSteps protocolSteps;

        /**
         * Gets the value of the protocolName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProtocolName() {
            return protocolName;
        }

        /**
         * Sets the value of the protocolName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProtocolName(String value) {
            this.protocolName = value;
        }

        /**
         * Gets the value of the protocolSteps property.
         * 
         * @return
         *     possible object is
         *     {@link ExperimentType.Protocol.ProtocolSteps }
         *     
         */
        public ExperimentType.Protocol.ProtocolSteps getProtocolSteps() {
            return protocolSteps;
        }

        /**
         * Sets the value of the protocolSteps property.
         * 
         * @param value
         *     allowed object is
         *     {@link ExperimentType.Protocol.ProtocolSteps }
         *     
         */
        public void setProtocolSteps(ExperimentType.Protocol.ProtocolSteps value) {
            this.protocolSteps = value;
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
         *         &lt;element name="StepDescription" type="{}paramType" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "stepDescription" })
        public static class ProtocolSteps {

            @XmlElement(name = "StepDescription")
            protected List<ParamType> stepDescription;

            /**
             * Gets the value of the stepDescription property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the stepDescription property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getStepDescription().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ParamType }
             * 
             * 
             */
            public List<ParamType> getStepDescription() {
                if (stepDescription == null) {
                    stepDescription = new ArrayList<ParamType>();
                }
                return this.stepDescription;
            }
        }
    }
}
