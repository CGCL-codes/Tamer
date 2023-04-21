package org.slasoi.common.eventschema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PredictionInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PredictionInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StartingTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="EnddingTime" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ProbabilityOfViolation" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="NotificationThreshold" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PredictionInfoType", propOrder = { "startingTime", "enddingTime", "probabilityOfViolation", "notificationThreshold" })
public class PredictionInfoType implements Serializable {

    @XmlElement(name = "StartingTime")
    protected long startingTime;

    @XmlElement(name = "EnddingTime")
    protected long enddingTime;

    @XmlElement(name = "ProbabilityOfViolation")
    protected double probabilityOfViolation;

    @XmlElement(name = "NotificationThreshold")
    protected double notificationThreshold;

    /**
     * Gets the value of the startingTime property.
     * 
     */
    public long getStartingTime() {
        return startingTime;
    }

    /**
     * Sets the value of the startingTime property.
     * 
     */
    public void setStartingTime(long value) {
        this.startingTime = value;
    }

    /**
     * Gets the value of the enddingTime property.
     * 
     */
    public long getEnddingTime() {
        return enddingTime;
    }

    /**
     * Sets the value of the enddingTime property.
     * 
     */
    public void setEnddingTime(long value) {
        this.enddingTime = value;
    }

    /**
     * Gets the value of the probabilityOfViolation property.
     * 
     */
    public double getProbabilityOfViolation() {
        return probabilityOfViolation;
    }

    /**
     * Sets the value of the probabilityOfViolation property.
     * 
     */
    public void setProbabilityOfViolation(double value) {
        this.probabilityOfViolation = value;
    }

    /**
     * Gets the value of the notificationThreshold property.
     * 
     */
    public double getNotificationThreshold() {
        return notificationThreshold;
    }

    /**
     * Sets the value of the notificationThreshold property.
     * 
     */
    public void setNotificationThreshold(double value) {
        this.notificationThreshold = value;
    }
}
