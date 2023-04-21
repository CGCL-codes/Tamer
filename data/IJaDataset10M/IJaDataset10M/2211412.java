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
@XmlType(name = "", propOrder = { "description", "activityContextInterfaceFactoryInterfaceName" })
@XmlRootElement(name = "activity-context-interface-factory-interface")
public class ActivityContextInterfaceFactoryInterface {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    protected Description description;

    @XmlElement(name = "activity-context-interface-factory-interface-name", required = true)
    protected ActivityContextInterfaceFactoryInterfaceName activityContextInterfaceFactoryInterfaceName;

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
     * Gets the value of the activityContextInterfaceFactoryInterfaceName property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityContextInterfaceFactoryInterfaceName }
     *     
     */
    public ActivityContextInterfaceFactoryInterfaceName getActivityContextInterfaceFactoryInterfaceName() {
        return activityContextInterfaceFactoryInterfaceName;
    }

    /**
     * Sets the value of the activityContextInterfaceFactoryInterfaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityContextInterfaceFactoryInterfaceName }
     *     
     */
    public void setActivityContextInterfaceFactoryInterfaceName(ActivityContextInterfaceFactoryInterfaceName value) {
        this.activityContextInterfaceFactoryInterfaceName = value;
    }
}
