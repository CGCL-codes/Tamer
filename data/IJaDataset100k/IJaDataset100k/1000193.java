package de.ibis.permoto.model.basic.scenario;

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
 *         &lt;element ref="{http://ibis.in.tum.de/research/itsom/pm_oliver.html}StationSection"/>
 *         &lt;element ref="{http://ibis.in.tum.de/research/itsom/pm_oliver.html}ClassSection"/>
 *         &lt;element ref="{http://ibis.in.tum.de/research/itsom/pm_oliver.html}ModellingGuiSection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "stationSection", "classSection", "modellingGuiSection" })
@XmlRootElement(name = "BusinessCase")
public class BusinessCase {

    @XmlElement(name = "StationSection", required = true)
    protected StationSection stationSection;

    @XmlElement(name = "ClassSection", required = true)
    protected ClassSection classSection;

    @XmlElement(name = "ModellingGuiSection")
    protected ModellingGuiSection modellingGuiSection;

    /**
     * Gets the value of the stationSection property.
     * 
     * @return
     *     possible object is
     *     {@link StationSection }
     *     
     */
    public StationSection getStationSection() {
        return stationSection;
    }

    /**
     * Sets the value of the stationSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link StationSection }
     *     
     */
    public void setStationSection(StationSection value) {
        this.stationSection = value;
    }

    /**
     * Gets the value of the classSection property.
     * 
     * @return
     *     possible object is
     *     {@link ClassSection }
     *     
     */
    public ClassSection getClassSection() {
        return classSection;
    }

    /**
     * Sets the value of the classSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassSection }
     *     
     */
    public void setClassSection(ClassSection value) {
        this.classSection = value;
    }

    /**
     * Gets the value of the modellingGuiSection property.
     * 
     * @return
     *     possible object is
     *     {@link ModellingGuiSection }
     *     
     */
    public ModellingGuiSection getModellingGuiSection() {
        return modellingGuiSection;
    }

    /**
     * Sets the value of the modellingGuiSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModellingGuiSection }
     *     
     */
    public void setModellingGuiSection(ModellingGuiSection value) {
        this.modellingGuiSection = value;
    }
}
