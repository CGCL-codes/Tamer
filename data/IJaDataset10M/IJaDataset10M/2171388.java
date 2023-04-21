package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for location_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="location_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Cuboid" type="{}box_type"/>
 *       &lt;/choice>
 *       &lt;attribute name="unit" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="cm" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "location_type", propOrder = { "cuboid" })
public class LocationType {

    @XmlElement(name = "Cuboid")
    protected BoxType cuboid;

    @XmlAttribute(required = true)
    protected String unit;

    /**
     * Gets the value of the cuboid property.
     * 
     * @return
     *     possible object is
     *     {@link BoxType }
     *     
     */
    public BoxType getCuboid() {
        return cuboid;
    }

    /**
     * Sets the value of the cuboid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoxType }
     *     
     */
    public void setCuboid(BoxType value) {
        this.cuboid = value;
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
            return "cm";
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
