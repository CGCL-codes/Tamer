package org.mobicents.slee.container.component.deployment.jaxb.slee.ratype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "eventTypeName", "eventTypeVendor", "eventTypeVersion" })
@XmlRootElement(name = "event-type-ref")
public class EventTypeRef {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    @XmlElement(name = "event-type-name", required = true)
    protected EventTypeName eventTypeName;

    @XmlElement(name = "event-type-vendor", required = true)
    protected EventTypeVendor eventTypeVendor;

    @XmlElement(name = "event-type-version", required = true)
    protected EventTypeVersion eventTypeVersion;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the eventTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link EventTypeName }
     *     
     */
    public EventTypeName getEventTypeName() {
        return eventTypeName;
    }

    /**
     * Sets the value of the eventTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventTypeName }
     *     
     */
    public void setEventTypeName(EventTypeName value) {
        this.eventTypeName = value;
    }

    /**
     * Gets the value of the eventTypeVendor property.
     * 
     * @return
     *     possible object is
     *     {@link EventTypeVendor }
     *     
     */
    public EventTypeVendor getEventTypeVendor() {
        return eventTypeVendor;
    }

    /**
     * Sets the value of the eventTypeVendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventTypeVendor }
     *     
     */
    public void setEventTypeVendor(EventTypeVendor value) {
        this.eventTypeVendor = value;
    }

    /**
     * Gets the value of the eventTypeVersion property.
     * 
     * @return
     *     possible object is
     *     {@link EventTypeVersion }
     *     
     */
    public EventTypeVersion getEventTypeVersion() {
        return eventTypeVersion;
    }

    /**
     * Sets the value of the eventTypeVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventTypeVersion }
     *     
     */
    public void setEventTypeVersion(EventTypeVersion value) {
        this.eventTypeVersion = value;
    }
}
