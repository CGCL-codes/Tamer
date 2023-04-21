package net.virtualearth.dev.webservices.v1.search;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;
import net.virtualearth.dev.webservices.v1.common.ArrayOfGeocodeLocation;
import net.virtualearth.dev.webservices.v1.common.Confidence;

/**
 * <p>Java class for LocationData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocationData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Confidence" type="{http://dev.virtualearth.net/webservices/v1/common}Confidence" minOccurs="0"/>
 *         &lt;element name="Locations" type="{http://dev.virtualearth.net/webservices/v1/common}ArrayOfGeocodeLocation" minOccurs="0"/>
 *         &lt;element name="MatchCodes" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocationData", propOrder = { "confidence", "locations", "matchCodes" })
public class LocationData implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "Confidence")
    protected Confidence confidence;

    @XmlElement(name = "Locations", nillable = true)
    protected ArrayOfGeocodeLocation locations;

    @XmlElement(name = "MatchCodes", nillable = true)
    protected ArrayOfstring matchCodes;

    /**
     * Gets the value of the confidence property.
     * 
     * @return
     *     possible object is
     *     {@link Confidence }
     *     
     */
    public Confidence getConfidence() {
        return confidence;
    }

    /**
     * Sets the value of the confidence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Confidence }
     *     
     */
    public void setConfidence(Confidence value) {
        this.confidence = value;
    }

    /**
     * Gets the value of the locations property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfGeocodeLocation }
     *     
     */
    public ArrayOfGeocodeLocation getLocations() {
        return locations;
    }

    /**
     * Sets the value of the locations property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfGeocodeLocation }
     *     
     */
    public void setLocations(ArrayOfGeocodeLocation value) {
        this.locations = value;
    }

    /**
     * Gets the value of the matchCodes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfstring }
     *     
     */
    public ArrayOfstring getMatchCodes() {
        return matchCodes;
    }

    /**
     * Sets the value of the matchCodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfstring }
     *     
     */
    public void setMatchCodes(ArrayOfstring value) {
        this.matchCodes = value;
    }
}
