package org.tolven.trim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NullFlavor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NullFlavor">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:tolven-org:trim:4.0}DataType">
 *       &lt;attribute name="type" type="{urn:tolven-org:trim:4.0}NullFlavorType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NullFlavor")
public class NullFlavor extends DataType implements Serializable {

    @XmlAttribute
    protected NullFlavorType type;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link NullFlavorType }
     *     
     */
    public NullFlavorType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullFlavorType }
     *     
     */
    public void setType(NullFlavorType value) {
        this.type = value;
    }
}
