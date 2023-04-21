package org.hyperimage.client.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for updateURL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateURL">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hiURL" type="{http://ws.service.hyperimage.org/}hiurl" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateURL", propOrder = { "hiURL" })
public class UpdateURL {

    protected Hiurl hiURL;

    /**
     * Gets the value of the hiURL property.
     * 
     * @return
     *     possible object is
     *     {@link Hiurl }
     *     
     */
    public Hiurl getHiURL() {
        return hiURL;
    }

    /**
     * Sets the value of the hiURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Hiurl }
     *     
     */
    public void setHiURL(Hiurl value) {
        this.hiURL = value;
    }
}
