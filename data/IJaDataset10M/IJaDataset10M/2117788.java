package org.tolven.trim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for INTSlot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="INTSlot">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:tolven-org:trim:4.0}Slot">
 *       &lt;choice>
 *         &lt;element name="null" type="{urn:tolven-org:trim:4.0}NullFlavor" minOccurs="0"/>
 *         &lt;element name="INT" type="{urn:tolven-org:trim:4.0}INT" minOccurs="0"/>
 *         &lt;element name="URG_INT" type="{urn:tolven-org:trim:4.0}URG_INT" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "INTSlot", propOrder = { "urgint", "_int", "_null" })
public class INTSlot extends Slot implements Serializable {

    @XmlElement(name = "URG_INT")
    protected URGINT urgint;

    @XmlElement(name = "INT")
    protected INT _int;

    @XmlElement(name = "null")
    protected NullFlavor _null;

    /**
     * Gets the value of the urgint property.
     * 
     * @return
     *     possible object is
     *     {@link URGINT }
     *     
     */
    public URGINT getURGINT() {
        return urgint;
    }

    /**
     * Sets the value of the urgint property.
     * 
     * @param value
     *     allowed object is
     *     {@link URGINT }
     *     
     */
    public void setURGINT(URGINT value) {
        this.urgint = value;
    }

    /**
     * Gets the value of the int property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getINT() {
        return _int;
    }

    /**
     * Sets the value of the int property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setINT(INT value) {
        this._int = value;
    }

    /**
     * Gets the value of the null property.
     * 
     * @return
     *     possible object is
     *     {@link NullFlavor }
     *     
     */
    public NullFlavor getNull() {
        return _null;
    }

    /**
     * Sets the value of the null property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullFlavor }
     *     
     */
    public void setNull(NullFlavor value) {
        this._null = value;
    }
}
