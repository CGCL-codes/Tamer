package shu.cms.colorformat.cxf;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "description", "creator", "created", "modified", "additionalData", "sampleSet", "conditions", "iccProfile", "binaryObject" })
@XmlRootElement(name = "CXF")
public class CXF {

    @XmlAttribute(name = "Version", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String version;

    @XmlElement(name = "Name")
    protected String name;

    @XmlElement(name = "Description")
    protected String description;

    @XmlElement(name = "Creator")
    protected String creator;

    @XmlElement(name = "Created")
    protected String created;

    @XmlElement(name = "Modified")
    protected String modified;

    @XmlElement(name = "AdditionalData")
    protected AdditionalData additionalData;

    @XmlElement(name = "SampleSet")
    protected List<SampleSet> sampleSet;

    @XmlElement(name = "Conditions")
    protected List<Conditions> conditions;

    @XmlElement(name = "ICC-Profile")
    protected List<ICCProfile> iccProfile;

    @XmlElement(name = "BinaryObject")
    protected List<BinaryObject> binaryObject;

    /**
   * Gets the value of the version property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getVersion() {
        return version;
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

    /**
   * Gets the value of the creator property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getCreator() {
        return creator;
    }

    /**
   * Sets the value of the creator property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setCreator(String value) {
        this.creator = value;
    }

    /**
   * Gets the value of the created property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getCreated() {
        return created;
    }

    /**
   * Sets the value of the created property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setCreated(String value) {
        this.created = value;
    }

    /**
   * Gets the value of the modified property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getModified() {
        return modified;
    }

    /**
   * Sets the value of the modified property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setModified(String value) {
        this.modified = value;
    }

    /**
   * Gets the value of the additionalData property.
   *
   * @return
   *     possible object is
   *     {@link AdditionalData }
   *
   */
    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    /**
   * Sets the value of the additionalData property.
   *
   * @param value
   *     allowed object is
   *     {@link AdditionalData }
   *
   */
    public void setAdditionalData(AdditionalData value) {
        this.additionalData = value;
    }

    /**
   * Gets the value of the sampleSet property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the sampleSet property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getSampleSet().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link SampleSet }
   *
   *
   */
    public List<SampleSet> getSampleSet() {
        if (sampleSet == null) {
            sampleSet = new ArrayList<SampleSet>();
        }
        return this.sampleSet;
    }

    /**
   * Gets the value of the conditions property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the conditions property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getConditions().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Conditions }
   *
   *
   */
    public List<Conditions> getConditions() {
        if (conditions == null) {
            conditions = new ArrayList<Conditions>();
        }
        return this.conditions;
    }

    /**
   * Gets the value of the iccProfile property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the iccProfile property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getICCProfile().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ICCProfile }
   *
   *
   */
    public List<ICCProfile> getICCProfile() {
        if (iccProfile == null) {
            iccProfile = new ArrayList<ICCProfile>();
        }
        return this.iccProfile;
    }

    /**
   * Gets the value of the binaryObject property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the binaryObject property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getBinaryObject().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link BinaryObject }
   *
   *
   */
    public List<BinaryObject> getBinaryObject() {
        if (binaryObject == null) {
            binaryObject = new ArrayList<BinaryObject>();
        }
        return this.binaryObject;
    }

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("shu.cms.colorformat.cxf");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        CXF cxf = (CXF) unmarshaller.unmarshal(new File("cxf/lcd.cxf"));
        Attribute attr = new Attribute("3", "4");
        cxf.getConditions().get(0).getAttribute().add(attr);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.valueOf(true));
        marshaller.marshal(cxf, new FileOutputStream("cxf/test.cxf"));
    }
}
