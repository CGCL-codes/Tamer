package org.slasoi.common.eventschema;

import java.io.Serializable;
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
 *         &lt;element name="Struct" type="{http://www.slaatsoi.org/eventschema}StructArgument"/>
 *         &lt;element name="Simple" type="{http://www.slaatsoi.org/eventschema}SimpleArgument"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArgumentType", propOrder = { "struct", "simple" })
public class ArgumentType implements Serializable {

    @XmlElement(name = "Struct")
    protected StructArgument struct;

    @XmlElement(name = "Simple")
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
