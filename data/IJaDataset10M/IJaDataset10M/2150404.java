package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v2_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines a default conductivities over the mesh in fibre, sheet and normal
 *         directions.
 * 
 * <p>Java class for conductivities_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conductivities_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="longi" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="trans" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="normal" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="unit" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="mS/cm" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conductivities_type")
public class ConductivitiesType {

    @XmlAttribute(required = true)
    protected double longi;

    @XmlAttribute(required = true)
    protected double trans;

    @XmlAttribute(required = true)
    protected double normal;

    @XmlAttribute(required = true)
    protected String unit;

    /**
     * Gets the value of the longi property.
     * 
     */
    public double getLongi() {
        return longi;
    }

    /**
     * Sets the value of the longi property.
     * 
     */
    public void setLongi(double value) {
        this.longi = value;
    }

    /**
     * Gets the value of the trans property.
     * 
     */
    public double getTrans() {
        return trans;
    }

    /**
     * Sets the value of the trans property.
     * 
     */
    public void setTrans(double value) {
        this.trans = value;
    }

    /**
     * Gets the value of the normal property.
     * 
     */
    public double getNormal() {
        return normal;
    }

    /**
     * Sets the value of the normal property.
     * 
     */
    public void setNormal(double value) {
        this.normal = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnit() {
        if (unit == null) {
            return "mS/cm";
        } else {
            return unit;
        }
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnit(String value) {
        this.unit = value;
    }
}
