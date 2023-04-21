package org.etexascode.j2735;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BOOLEAN complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BOOLEAN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.obj-sys.com/v1.0/XMLSchema}BooleanGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BOOLEAN", namespace = "http://www.obj-sys.com/v1.0/XMLSchema", propOrder = { "_true", "_false" })
public class BOOLEAN {

    @XmlElement(name = "true", namespace = "")
    protected BOOLEAN.True _true;

    @XmlElement(name = "false", namespace = "")
    protected BOOLEAN.False _false;

    /**
     * Gets the value of the true property.
     * 
     * @return
     *     possible object is
     *     {@link BOOLEAN.True }
     *     
     */
    public BOOLEAN.True getTrue() {
        return _true;
    }

    /**
     * Sets the value of the true property.
     * 
     * @param value
     *     allowed object is
     *     {@link BOOLEAN.True }
     *     
     */
    public void setTrue(BOOLEAN.True value) {
        this._true = value;
    }

    /**
     * Gets the value of the false property.
     * 
     * @return
     *     possible object is
     *     {@link BOOLEAN.False }
     *     
     */
    public BOOLEAN.False getFalse() {
        return _false;
    }

    /**
     * Sets the value of the false property.
     * 
     * @param value
     *     allowed object is
     *     {@link BOOLEAN.False }
     *     
     */
    public void setFalse(BOOLEAN.False value) {
        this._false = value;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class False {
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class True {
    }
}
