package org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb;

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
@XmlType(name = "", propOrder = { "description", "sbbAbstractClass", "sbbLocalInterface", "sbbActivityContextInterface", "sbbUsageParametersInterface" })
@XmlRootElement(name = "sbb-classes")
public class SbbClasses {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    protected Description description;

    @XmlElement(name = "sbb-abstract-class", required = true)
    protected SbbAbstractClass sbbAbstractClass;

    @XmlElement(name = "sbb-local-interface")
    protected SbbLocalInterface sbbLocalInterface;

    @XmlElement(name = "sbb-activity-context-interface")
    protected SbbActivityContextInterface sbbActivityContextInterface;

    @XmlElement(name = "sbb-usage-parameters-interface")
    protected SbbUsageParametersInterface sbbUsageParametersInterface;

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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the sbbAbstractClass property.
     * 
     * @return
     *     possible object is
     *     {@link SbbAbstractClass }
     *     
     */
    public SbbAbstractClass getSbbAbstractClass() {
        return sbbAbstractClass;
    }

    /**
     * Sets the value of the sbbAbstractClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbAbstractClass }
     *     
     */
    public void setSbbAbstractClass(SbbAbstractClass value) {
        this.sbbAbstractClass = value;
    }

    /**
     * Gets the value of the sbbLocalInterface property.
     * 
     * @return
     *     possible object is
     *     {@link SbbLocalInterface }
     *     
     */
    public SbbLocalInterface getSbbLocalInterface() {
        return sbbLocalInterface;
    }

    /**
     * Sets the value of the sbbLocalInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbLocalInterface }
     *     
     */
    public void setSbbLocalInterface(SbbLocalInterface value) {
        this.sbbLocalInterface = value;
    }

    /**
     * Gets the value of the sbbActivityContextInterface property.
     * 
     * @return
     *     possible object is
     *     {@link SbbActivityContextInterface }
     *     
     */
    public SbbActivityContextInterface getSbbActivityContextInterface() {
        return sbbActivityContextInterface;
    }

    /**
     * Sets the value of the sbbActivityContextInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbActivityContextInterface }
     *     
     */
    public void setSbbActivityContextInterface(SbbActivityContextInterface value) {
        this.sbbActivityContextInterface = value;
    }

    /**
     * Gets the value of the sbbUsageParametersInterface property.
     * 
     * @return
     *     possible object is
     *     {@link SbbUsageParametersInterface }
     *     
     */
    public SbbUsageParametersInterface getSbbUsageParametersInterface() {
        return sbbUsageParametersInterface;
    }

    /**
     * Sets the value of the sbbUsageParametersInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbUsageParametersInterface }
     *     
     */
    public void setSbbUsageParametersInterface(SbbUsageParametersInterface value) {
        this.sbbUsageParametersInterface = value;
    }
}
