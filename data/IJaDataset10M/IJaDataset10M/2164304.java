package org.gello.model.HL7RIM.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.gello.model.HL7RIM.generated.INT;
import org.gello.model.HL7RIM.generated.IVXBINT;

/**
 * <p>Java class for IVXB_INT complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="IVXB_INT">
 *   &lt;complexContent>
 *     &lt;extension base="{}INT">
 *       &lt;attribute name="inclusive" type="{}bl" default="true" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVXB_INT")
public class IVXBINT extends INT {

    @XmlAttribute
    protected Boolean inclusive;

    /**
     * Gets the value of the inclusive property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isInclusive() {
        if (inclusive == null) {
            return true;
        } else {
            return inclusive;
        }
    }

    /**
     * Sets the value of the inclusive property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInclusive(Boolean value) {
        this.inclusive = value;
    }
}
