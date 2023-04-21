package org.slasoi.monitoring.manageability.xml.eventformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for EventContextType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventContextType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Time" type="{http://slasoi.org/monitoring/xml/eventformat}EventTime"/>
 *         &lt;element name="Notifier" type="{http://slasoi.org/monitoring/xml/eventformat}EventNotifier"/>
 *         &lt;element name="Source" type="{http://slasoi.org/monitoring/xml/eventformat}EventSource"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventContextType", namespace = "http://slasoi.org/monitoring/xml/eventformat", propOrder = { "time", "notifier", "source" })
public class EventContextType {

    @XmlElement(name = "Time", namespace = "http://slasoi.org/monitoring/xml/eventformat", required = true)
    protected EventTime time;

    @XmlElement(name = "Notifier", namespace = "http://slasoi.org/monitoring/xml/eventformat", required = true)
    protected EventNotifier notifier;

    @XmlElement(name = "Source", namespace = "http://slasoi.org/monitoring/xml/eventformat", required = true)
    protected EventSource source;

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link EventTime }
     *     
     */
    public EventTime getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventTime }
     *     
     */
    public void setTime(EventTime value) {
        this.time = value;
    }

    /**
     * Gets the value of the notifier property.
     * 
     * @return
     *     possible object is
     *     {@link EventNotifier }
     *     
     */
    public EventNotifier getNotifier() {
        return notifier;
    }

    /**
     * Sets the value of the notifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventNotifier }
     *     
     */
    public void setNotifier(EventNotifier value) {
        this.notifier = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link EventSource }
     *     
     */
    public EventSource getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventSource }
     *     
     */
    public void setSource(EventSource value) {
        this.source = value;
    }
}
