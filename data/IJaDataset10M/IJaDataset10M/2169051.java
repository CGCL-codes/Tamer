package net.sublight.webservice;

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
 *         &lt;element name="AddSubtitleThankResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addSubtitleThankResult", "error" })
@XmlRootElement(name = "AddSubtitleThankResponse")
public class AddSubtitleThankResponse {

    @XmlElement(name = "AddSubtitleThankResult")
    protected boolean addSubtitleThankResult;

    protected String error;

    /**
     * Gets the value of the addSubtitleThankResult property.
     * 
     */
    public boolean isAddSubtitleThankResult() {
        return addSubtitleThankResult;
    }

    /**
     * Sets the value of the addSubtitleThankResult property.
     * 
     */
    public void setAddSubtitleThankResult(boolean value) {
        this.addSubtitleThankResult = value;
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
