package de.ibis.permoto.model.basic.applicationmodel;

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
 *         &lt;element name="ApplicationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApplicationNameSave" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://ibis.de/PerMoToApplicationModel}Configuration"/>
 *         &lt;element ref="{http://ibis.de/PerMoToApplicationModel}ItemSection"/>
 *         &lt;element ref="{http://ibis.de/PerMoToApplicationModel}WorkflowSection"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "applicationName", "applicationNameSave", "configuration", "itemSection", "workflowSection" })
@XmlRootElement(name = "ApplicationModel")
public class ApplicationModel {

    @XmlElement(name = "ApplicationName", required = true)
    protected String applicationName;

    @XmlElement(name = "ApplicationNameSave")
    protected String applicationNameSave;

    @XmlElement(name = "Configuration", required = true)
    protected Configuration configuration;

    @XmlElement(name = "ItemSection", required = true)
    protected ItemSection itemSection;

    @XmlElement(name = "WorkflowSection", required = true)
    protected WorkflowSection workflowSection;

    /**
     * Gets the value of the applicationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the value of the applicationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationName(String value) {
        this.applicationName = value;
    }

    /**
     * Gets the value of the applicationNameSave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationNameSave() {
        return applicationNameSave;
    }

    /**
     * Sets the value of the applicationNameSave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationNameSave(String value) {
        this.applicationNameSave = value;
    }

    /**
     * Gets the value of the configuration property.
     * 
     * @return
     *     possible object is
     *     {@link Configuration }
     *     
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the value of the configuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Configuration }
     *     
     */
    public void setConfiguration(Configuration value) {
        this.configuration = value;
    }

    /**
     * Gets the value of the itemSection property.
     * 
     * @return
     *     possible object is
     *     {@link ItemSection }
     *     
     */
    public ItemSection getItemSection() {
        return itemSection;
    }

    /**
     * Sets the value of the itemSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemSection }
     *     
     */
    public void setItemSection(ItemSection value) {
        this.itemSection = value;
    }

    /**
     * Gets the value of the workflowSection property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowSection }
     *     
     */
    public WorkflowSection getWorkflowSection() {
        return workflowSection;
    }

    /**
     * Sets the value of the workflowSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowSection }
     *     
     */
    public void setWorkflowSection(WorkflowSection value) {
        this.workflowSection = value;
    }
}
