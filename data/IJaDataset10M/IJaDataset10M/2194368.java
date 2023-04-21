package com.mu.rai.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for tcpWindow complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcpWindow">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.rai.mu.com}modelObject">
 *       &lt;sequence>
 *         &lt;element name="randomize" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcpWindow", propOrder = { "randomize", "value" })
public class TcpWindow extends ModelObject {

    protected boolean randomize;

    protected int value;

    /**
     * Gets the value of the randomize property.
     * 
     */
    public boolean isRandomize() {
        return randomize;
    }

    /**
     * Sets the value of the randomize property.
     * 
     */
    public void setRandomize(boolean value) {
        this.randomize = value;
    }

    /**
     * Gets the value of the value property.
     * 
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(int value) {
        this.value = value;
    }
}
