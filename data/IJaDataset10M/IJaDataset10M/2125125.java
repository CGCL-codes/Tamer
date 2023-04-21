package org.matsim.jaxb.signalsystems11;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://www.matsim.org/files/dtd}coordinate" minOccurs="0"/>
 *         &lt;element name="actLocation" type="{http://www.matsim.org/files/dtd}actLocationType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "coordinate", "actLocation" })
@XmlRootElement(name = "location")
public class XMLLocation {

    protected XMLCoordinateType coordinate;

    protected XMLActLocationType actLocation;

    /**
     * Gets the value of the coordinate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLCoordinateType }
     *     
     */
    public XMLCoordinateType getCoordinate() {
        return coordinate;
    }

    /**
     * Sets the value of the coordinate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLCoordinateType }
     *     
     */
    public void setCoordinate(XMLCoordinateType value) {
        this.coordinate = value;
    }

    /**
     * Gets the value of the actLocation property.
     * 
     * @return
     *     possible object is
     *     {@link XMLActLocationType }
     *     
     */
    public XMLActLocationType getActLocation() {
        return actLocation;
    }

    /**
     * Sets the value of the actLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLActLocationType }
     *     
     */
    public void setActLocation(XMLActLocationType value) {
        this.actLocation = value;
    }
}
