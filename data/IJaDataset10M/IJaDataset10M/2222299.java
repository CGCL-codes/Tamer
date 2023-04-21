package org.slasoi.common.eventschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Length complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Length">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.slaatsoi.org/coremodel}QuantitativeType">
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="unit" type="{http://www.slaatsoi.org/coremodel}LengthUnitKind" />
 *       &lt;attribute name="precision" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Length", namespace = "http://www.slaatsoi.org/coremodel")
public class Length extends QuantitativeType {

    @XmlAttribute
    protected Double value;

    @XmlAttribute
    protected LengthUnitKind unit;

    @XmlAttribute
    protected Double precision;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link LengthUnitKind }
     *     
     */
    public LengthUnitKind getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthUnitKind }
     *     
     */
    public void setUnit(LengthUnitKind value) {
        this.unit = value;
    }

    /**
     * Gets the value of the precision property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPrecision() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPrecision(Double value) {
        this.precision = value;
    }
}
