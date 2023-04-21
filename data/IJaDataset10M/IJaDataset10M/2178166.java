package org.systemsbiology.openms.consensusxml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="map" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rt" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="mz" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="it" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="charge" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "element")
public class Element {

    @XmlAttribute(required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long map;

    @XmlAttribute(required = true)
    protected String id;

    @XmlAttribute(required = true)
    protected double rt;

    @XmlAttribute(required = true)
    protected double mz;

    @XmlAttribute(required = true)
    protected double it;

    @XmlAttribute
    protected BigInteger charge;

    /**
     * Gets the value of the map property.
     * 
     */
    public long getMap() {
        return map;
    }

    /**
     * Sets the value of the map property.
     * 
     */
    public void setMap(long value) {
        this.map = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the rt property.
     * 
     */
    public double getRt() {
        return rt;
    }

    /**
     * Sets the value of the rt property.
     * 
     */
    public void setRt(double value) {
        this.rt = value;
    }

    /**
     * Gets the value of the mz property.
     * 
     */
    public double getMz() {
        return mz;
    }

    /**
     * Sets the value of the mz property.
     * 
     */
    public void setMz(double value) {
        this.mz = value;
    }

    /**
     * Gets the value of the it property.
     * 
     */
    public double getIt() {
        return it;
    }

    /**
     * Sets the value of the it property.
     * 
     */
    public void setIt(double value) {
        this.it = value;
    }

    /**
     * Gets the value of the charge property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCharge() {
        return charge;
    }

    /**
     * Sets the value of the charge property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCharge(BigInteger value) {
        this.charge = value;
    }
}
