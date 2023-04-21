package com.uglygreencar.gum.xml.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for OptionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OptionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hideClosedProjects" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="hideCompletedItems" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OptionsType", propOrder = { "hideClosedProjects", "hideCompletedItems" })
public abstract class OptionsType {

    @XmlElement(defaultValue = "false")
    protected boolean hideClosedProjects;

    @XmlElement(defaultValue = "false")
    protected boolean hideCompletedItems;

    /**
     * Gets the value of the hideClosedProjects property.
     * 
     */
    public boolean isHideClosedProjects() {
        return hideClosedProjects;
    }

    /**
     * Sets the value of the hideClosedProjects property.
     * 
     */
    public void setHideClosedProjects(boolean value) {
        this.hideClosedProjects = value;
    }

    /**
     * Gets the value of the hideCompletedItems property.
     * 
     */
    public boolean isHideCompletedItems() {
        return hideCompletedItems;
    }

    /**
     * Sets the value of the hideCompletedItems property.
     * 
     */
    public void setHideCompletedItems(boolean value) {
        this.hideCompletedItems = value;
    }
}
