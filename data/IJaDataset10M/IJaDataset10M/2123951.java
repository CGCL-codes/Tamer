package org.proteored.miapeapi.xml.gi.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *  A subclass of MaterialMeasurement to be used without being extended in conjunction with GenericProtocolApplication and GenericMaterial to model measured sources of materials. 
 * 
 * <p>Java class for FuGE.Bio.Material.GenericMaterialMeasurementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Bio.Material.GenericMaterialMeasurementType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Bio.Material.MaterialMeasurementType">
 *       &lt;attribute name="Material_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Bio.Material.GenericMaterialMeasurementType")
public class FuGEBioMaterialGenericMaterialMeasurementType extends FuGEBioMaterialMaterialMeasurementType {

    @XmlAttribute(name = "Material_ref", required = true)
    protected String materialRef;

    /**
     * Gets the value of the materialRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialRef() {
        return materialRef;
    }

    /**
     * Sets the value of the materialRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialRef(String value) {
        this.materialRef = value;
    }
}
