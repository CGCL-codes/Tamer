package org.slasoi.monitoring.manageability.xml.eventformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ArgumentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArgumentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Struct" type="{http://slasoi.org/monitoring/xml/eventformat}StructArgument"/>
 *         &lt;element name="Simple" type="{http://slasoi.org/monitoring/xml/eventformat}SimpleArgument"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArgumentType", namespace = "http://slasoi.org/monitoring/xml/eventformat", propOrder = { "struct", "simple" })
public class ArgumentType {

    @XmlElement(name = "Struct", namespace = "http://slasoi.org/monitoring/xml/eventformat")
    protected StructArgument struct;

    @XmlElement(name = "Simple", namespace = "http://slasoi.org/monitoring/xml/eventformat")
    protected SimpleArgument simple;

    /**
     * Gets the value of the struct property.
     * 
     * @return
     *     possible object is
     *     {@link StructArgument }
     *     
     */
    public StructArgument getStruct() {
        return struct;
    }

    /**
     * Sets the value of the struct property.
     * 
     * @param value
     *     allowed object is
     *     {@link StructArgument }
     *     
     */
    public void setStruct(StructArgument value) {
        this.struct = value;
    }

    /**
     * Gets the value of the simple property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleArgument }
     *     
     */
    public SimpleArgument getSimple() {
        return simple;
    }

    /**
     * Sets the value of the simple property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleArgument }
     *     
     */
    public void setSimple(SimpleArgument value) {
        this.simple = value;
    }
}
