package net.gpslite.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 				Type for the pingResponse element.
 * 			
 * 
 * <p>Java class for PingResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PingResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.gpslite.net/1.0/gips/}AbstractResponseType">
 *       &lt;all>
 *         &lt;element name="pingReceivedTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PingResponseType", propOrder = { "pingReceivedTime", "error" })
public class PingResponseType extends AbstractResponseType {

    protected String pingReceivedTime;

    protected String error;

    /**
     * Gets the value of the pingReceivedTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPingReceivedTime() {
        return pingReceivedTime;
    }

    /**
     * Sets the value of the pingReceivedTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPingReceivedTime(String value) {
        this.pingReceivedTime = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }
}
