package net.seismon.seismolinkClient.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 			    Use of wildcard "*" is allowed on all field except of timespan 
 * 			    Station, channel and locId are optional. If station or channel are not specified the respective elements are not added to the XML tree
 * 			    If locId is missing or blank, only streams with empty location ID are included 
 * 				Compression format can be bzip2 or none (not implemented yet)
 * 				Instruments (default false) indicates whether instrument data should be added to XML
 * 				ModifiedAfter(ISO dateTime) if set, only entries after given time will be returned   
 * 			
 * 
 * <p>Java class for InventoryRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InventoryRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:xml:seisml:orfeus:neries:org}AbstractRequestType">
 *       &lt;sequence>
 *         &lt;element name="SpatialBounds" type="{urn:xml:seisml:orfeus:neries:org}SpatialBoundsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Instruments" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ModifiedAfter" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InventoryRequestType", namespace = "urn:xml:seisml:orfeus:neries:org", propOrder = { "spatialBounds" })
public class InventoryRequestType extends AbstractRequestType {

    @XmlElement(name = "SpatialBounds")
    protected SpatialBoundsType spatialBounds;

    @XmlAttribute(name = "Instruments")
    protected String instruments;

    @XmlAttribute(name = "ModifiedAfter")
    protected String modifiedAfter;

    /**
     * Gets the value of the spatialBounds property.
     * 
     * @return
     *     possible object is
     *     {@link SpatialBoundsType }
     *     
     */
    public SpatialBoundsType getSpatialBounds() {
        return spatialBounds;
    }

    /**
     * Sets the value of the spatialBounds property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpatialBoundsType }
     *     
     */
    public void setSpatialBounds(SpatialBoundsType value) {
        this.spatialBounds = value;
    }

    /**
     * Gets the value of the instruments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstruments() {
        return instruments;
    }

    /**
     * Sets the value of the instruments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstruments(String value) {
        this.instruments = value;
    }

    /**
     * Gets the value of the modifiedAfter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedAfter() {
        return modifiedAfter;
    }

    /**
     * Sets the value of the modifiedAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedAfter(String value) {
        this.modifiedAfter = value;
    }
}
