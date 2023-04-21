package edu.purdue.rcac.cesm.ws.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for getUsedCPUTimeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getUsedCPUTimeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="used-cpu-time" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUsedCPUTimeResponse", propOrder = { "usedCpuTime" })
public class GetUsedCPUTimeResponse {

    @XmlElement(name = "used-cpu-time")
    protected String usedCpuTime;

    /**
     * Gets the value of the usedCpuTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsedCpuTime() {
        return usedCpuTime;
    }

    /**
     * Sets the value of the usedCpuTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsedCpuTime(String value) {
        this.usedCpuTime = value;
    }
}
