package org.mcisb.beacon.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="nameID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="material_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="DNA"/>
 *               &lt;enumeration value="cell"/>
 *               &lt;enumeration value="cell_lysate"/>
 *               &lt;enumeration value="cytoplasmic_RNA"/>
 *               &lt;enumeration value="genomic_DNA"/>
 *               &lt;enumeration value="molecular_mixture"/>
 *               &lt;enumeration value="nuclear_RNA"/>
 *               &lt;enumeration value="organellar_DNA"/>
 *               &lt;enumeration value="organellar_RNA"/>
 *               &lt;enumeration value="organism_part"/>
 *               &lt;enumeration value="polyA_RNA"/>
 *               &lt;enumeration value="protein"/>
 *               &lt;enumeration value="synthetic_DNA"/>
 *               &lt;enumeration value="synthetic_RNA"/>
 *               &lt;enumeration value="total_RNA"/>
 *               &lt;enumeration value="virus"/>
 *               &lt;enumeration value="whole_organism"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element ref="{}Cell_Characteristics" minOccurs="0"/>
 *         &lt;element ref="{}Treatment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Plasmid" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Antibody" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Physical_Dye" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SampleSource" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "nameID", "materialType", "cellCharacteristics", "treatment", "plasmid", "antibody", "physicalDye", "sampleSource", "description" })
@XmlRootElement(name = "BioSample")
public class BioSample {

    @XmlElement(required = true)
    protected String nameID;

    @XmlElement(name = "material_type", required = true)
    protected String materialType;

    @XmlElement(name = "Cell_Characteristics")
    protected CellCharacteristics cellCharacteristics;

    @XmlElement(name = "Treatment")
    protected List<Treatment> treatment;

    @XmlElement(name = "Plasmid")
    protected List<Plasmid> plasmid;

    @XmlElement(name = "Antibody")
    protected List<Antibody> antibody;

    @XmlElement(name = "Physical_Dye")
    protected List<PhysicalDye> physicalDye;

    @XmlElement(name = "SampleSource")
    protected List<SampleSource> sampleSource;

    protected String description;

    /**
     * Gets the value of the nameID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameID() {
        return nameID;
    }

    /**
     * Sets the value of the nameID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameID(String value) {
        this.nameID = value;
    }

    /**
     * Gets the value of the materialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialType() {
        return materialType;
    }

    /**
     * Sets the value of the materialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialType(String value) {
        this.materialType = value;
    }

    /**
     * Gets the value of the cellCharacteristics property.
     * 
     * @return
     *     possible object is
     *     {@link CellCharacteristics }
     *     
     */
    public CellCharacteristics getCellCharacteristics() {
        return cellCharacteristics;
    }

    /**
     * Sets the value of the cellCharacteristics property.
     * 
     * @param value
     *     allowed object is
     *     {@link CellCharacteristics }
     *     
     */
    public void setCellCharacteristics(CellCharacteristics value) {
        this.cellCharacteristics = value;
    }

    /**
     * Gets the value of the treatment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the treatment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTreatment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Treatment }
     * 
     * 
     */
    public List<Treatment> getTreatment() {
        if (treatment == null) {
            treatment = new ArrayList<Treatment>();
        }
        return this.treatment;
    }

    /**
     * Gets the value of the plasmid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the plasmid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlasmid().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Plasmid }
     * 
     * 
     */
    public List<Plasmid> getPlasmid() {
        if (plasmid == null) {
            plasmid = new ArrayList<Plasmid>();
        }
        return this.plasmid;
    }

    /**
     * Gets the value of the antibody property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the antibody property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAntibody().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Antibody }
     * 
     * 
     */
    public List<Antibody> getAntibody() {
        if (antibody == null) {
            antibody = new ArrayList<Antibody>();
        }
        return this.antibody;
    }

    /**
     * Gets the value of the physicalDye property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the physicalDye property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhysicalDye().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PhysicalDye }
     * 
     * 
     */
    public List<PhysicalDye> getPhysicalDye() {
        if (physicalDye == null) {
            physicalDye = new ArrayList<PhysicalDye>();
        }
        return this.physicalDye;
    }

    /**
     * Gets the value of the sampleSource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sampleSource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSampleSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SampleSource }
     * 
     * 
     */
    public List<SampleSource> getSampleSource() {
        if (sampleSource == null) {
            sampleSource = new ArrayList<SampleSource>();
        }
        return this.sampleSource;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }
}
