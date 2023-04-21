package org.copasi.copasiws.services.parameterestimationws.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Species complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Species">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="modelId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="quantityType">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="concentration"/>
 *             &lt;enumeration value="particleNumber"/>
 *             &lt;enumeration value="concentrationRate"/>
 *             &lt;enumeration value="particleNumberRate"/>
 *             &lt;enumeration value="initialConcentration"/>
 *             &lt;enumeration value="initialParticleNumber"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Species", propOrder = { "modelId" })
public class Species {

    @XmlElement(required = true)
    protected String modelId;

    @XmlAttribute
    protected String quantityType;

    /**
     * Gets the value of the modelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelId() {
        return modelId;
    }

    /**
     * Sets the value of the modelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelId(String value) {
        this.modelId = value;
    }

    /**
     * Gets the value of the quantityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantityType() {
        return quantityType;
    }

    /**
     * Sets the value of the quantityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantityType(String value) {
        this.quantityType = value;
    }
}
