package org.slasoi.common.eventschema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for EventPayloadType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventPayloadType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="InteractionEvent" type="{http://www.slaatsoi.org/eventschema}InteractionEventType"/>
 *         &lt;element name="MonitoringResultEvent" type="{http://www.slaatsoi.org/eventschema}MonitoringResultEventType"/>
 *         &lt;element name="PredictionResultEvent" type="{http://www.slaatsoi.org/eventschema}PredictionResultEventType"/>
 *         &lt;element name="infrastructureMonitoringEventType" type="{http://www.slaatsoi.org/eventschema}InfrastructureMonitoringEventType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventPayloadType", propOrder = { "interactionEvent", "monitoringResultEvent", "predictionResultEvent", "infrastructureMonitoringEventType" })
public class EventPayloadType implements Serializable {

    @XmlElement(name = "InteractionEvent")
    protected InteractionEventType interactionEvent;

    @XmlElement(name = "MonitoringResultEvent")
    protected MonitoringResultEventType monitoringResultEvent;

    @XmlElement(name = "PredictionResultEvent")
    protected PredictionResultEventType predictionResultEvent;

    protected InfrastructureMonitoringEventType infrastructureMonitoringEventType;

    /**
     * Gets the value of the interactionEvent property.
     * 
     * @return
     *     possible object is
     *     {@link InteractionEventType }
     *     
     */
    public InteractionEventType getInteractionEvent() {
        return interactionEvent;
    }

    /**
     * Sets the value of the interactionEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link InteractionEventType }
     *     
     */
    public void setInteractionEvent(InteractionEventType value) {
        this.interactionEvent = value;
    }

    /**
     * Gets the value of the monitoringResultEvent property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoringResultEventType }
     *     
     */
    public MonitoringResultEventType getMonitoringResultEvent() {
        return monitoringResultEvent;
    }

    /**
     * Sets the value of the monitoringResultEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoringResultEventType }
     *     
     */
    public void setMonitoringResultEvent(MonitoringResultEventType value) {
        this.monitoringResultEvent = value;
    }

    /**
     * Gets the value of the predictionResultEvent property.
     * 
     * @return
     *     possible object is
     *     {@link PredictionResultEventType }
     *     
     */
    public PredictionResultEventType getPredictionResultEvent() {
        return predictionResultEvent;
    }

    /**
     * Sets the value of the predictionResultEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link PredictionResultEventType }
     *     
     */
    public void setPredictionResultEvent(PredictionResultEventType value) {
        this.predictionResultEvent = value;
    }

    /**
     * Gets the value of the infrastructureMonitoringEventType property.
     * 
     * @return
     *     possible object is
     *     {@link InfrastructureMonitoringEventType }
     *     
     */
    public InfrastructureMonitoringEventType getInfrastructureMonitoringEventType() {
        return infrastructureMonitoringEventType;
    }

    /**
     * Sets the value of the infrastructureMonitoringEventType property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfrastructureMonitoringEventType }
     *     
     */
    public void setInfrastructureMonitoringEventType(InfrastructureMonitoringEventType value) {
        this.infrastructureMonitoringEventType = value;
    }
}
