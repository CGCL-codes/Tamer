package org.gello.model.HL7RIM.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.gello.model.HL7RIM.generated.PPDTS;
import org.gello.model.HL7RIM.generated.SXCMPPDTS;
import org.gello.model.HL7RIM.generated.SetOperator;

/**
 * <p>Java class for SXCM_PPD_TS complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SXCM_PPD_TS">
 *   &lt;complexContent>
 *     &lt;extension base="{}PPD_TS">
 *       &lt;attribute name="operator" type="{}SetOperator" default="I" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SXCM_PPD_TS")
public class SXCMPPDTS extends PPDTS {

    @XmlAttribute
    protected SetOperator operator;

    /**
     * Gets the value of the operator property.
     *
     * @return
     *     possible object is
     *     {@link SetOperator }
     *
     */
    public SetOperator getOperator() {
        if (operator == null) {
            return SetOperator.I;
        } else {
            return operator;
        }
    }

    /**
     * Sets the value of the operator property.
     *
     * @param value
     *     allowed object is
     *     {@link SetOperator }
     *
     */
    public void setOperator(SetOperator value) {
        this.operator = value;
    }
}
