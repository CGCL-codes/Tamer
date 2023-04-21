package com.acgvision.core.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for alive complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="alive">
 *   &lt;complexContent>
 *     &lt;extension base="{http://view.core.acgvision.com/}control">
 *       &lt;sequence>
 *         &lt;element name="waitingPeriod" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "alive", propOrder = { "waitingPeriod" })
public class Alive extends Control {

    protected int waitingPeriod;

    /**
     * Gets the value of the waitingPeriod property.
     * 
     */
    public int getWaitingPeriod() {
        return waitingPeriod;
    }

    /**
     * Sets the value of the waitingPeriod property.
     * 
     */
    public void setWaitingPeriod(int value) {
        this.waitingPeriod = value;
    }
}
