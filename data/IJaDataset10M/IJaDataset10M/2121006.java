package org.matsim.jaxb.signalsystemsconfig11;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * If there is an adaptive control mechanism implemented its id or name
 * 		can be set in the element of this type.
 * 
 * <p>Java class for adaptiveSignalSystemControlInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adaptiveSignalSystemControlInfoType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.matsim.org/files/dtd}signalSystemControlInfoType">
 *       &lt;sequence>
 *         &lt;element name="adaptiveControler" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signalGroup" type="{http://www.matsim.org/files/dtd}idRefType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adaptiveSignalSystemControlInfoType", propOrder = { "adaptiveControler", "signalGroup" })
@XmlSeeAlso({ XMLAdaptivePlanbasedSignalSystemControlInfoType.class })
public class XMLAdaptiveSignalSystemControlInfoType extends XMLSignalSystemControlInfoType {

    protected String adaptiveControler;

    @XmlElement(required = true)
    protected List<XMLIdRefType> signalGroup;

    /**
     * Gets the value of the adaptiveControler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdaptiveControler() {
        return adaptiveControler;
    }

    /**
     * Sets the value of the adaptiveControler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdaptiveControler(String value) {
        this.adaptiveControler = value;
    }

    /**
     * Gets the value of the signalGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signalGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignalGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLIdRefType }
     * 
     * 
     */
    public List<XMLIdRefType> getSignalGroup() {
        if (signalGroup == null) {
            signalGroup = new ArrayList<XMLIdRefType>();
        }
        return this.signalGroup;
    }
}
