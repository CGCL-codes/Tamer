package org.libretx.example.ws.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *       &lt;sequence>
 *         &lt;element name="outObj" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "outObj" })
@XmlRootElement(name = "exampleOut")
public class ExampleOut {

    @XmlElement(required = true)
    protected String outObj;

    /**
     * Gets the value of the outObj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutObj() {
        return outObj;
    }

    /**
     * Sets the value of the outObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutObj(String value) {
        this.outObj = value;
    }
}
