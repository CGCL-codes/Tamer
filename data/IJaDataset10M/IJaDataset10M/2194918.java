package org.hupo.psi.ms.traml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * Controlled vocabulary term adding information to the parent term
 * 
 * <p>Java class for cvParamType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cvParamType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="cvRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accession" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitAccession" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitCvRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cvParamType")
public class CvParamType {

    @XmlAttribute(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object cvRef;

    @XmlAttribute(required = true)
    protected String name;

    @XmlAttribute(required = true)
    protected String accession;

    @XmlAttribute
    protected String value;

    @XmlAttribute
    protected String unitAccession;

    @XmlAttribute
    protected String unitName;

    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object unitCvRef;

    /**
     * Gets the value of the cvRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCvRef() {
        return cvRef;
    }

    /**
     * Sets the value of the cvRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCvRef(Object value) {
        this.cvRef = value;
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
     * Gets the value of the accession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccession() {
        return accession;
    }

    /**
     * Sets the value of the accession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccession(String value) {
        this.accession = value;
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

    /**
     * Gets the value of the unitAccession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitAccession() {
        return unitAccession;
    }

    /**
     * Sets the value of the unitAccession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitAccession(String value) {
        this.unitAccession = value;
    }

    /**
     * Gets the value of the unitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Sets the value of the unitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitName(String value) {
        this.unitName = value;
    }

    /**
     * Gets the value of the unitCvRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUnitCvRef() {
        return unitCvRef;
    }

    /**
     * Sets the value of the unitCvRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUnitCvRef(Object value) {
        this.unitCvRef = value;
    }
}
