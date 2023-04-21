package org.mobicents.slee.container.component.deployment.jaxb.slee.sbb;

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
@XmlType(name = "", propOrder = { "description", "sbbLocalInterfaceName" })
@XmlRootElement(name = "sbb-local-interface")
public class SbbLocalInterface {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    protected Description description;

    @XmlElement(name = "sbb-local-interface-name", required = true)
    protected SbbLocalInterfaceName sbbLocalInterfaceName;

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
     * Gets the value of the sbbLocalInterfaceName property.
     * 
     * @return
     *     possible object is
     *     {@link SbbLocalInterfaceName }
     *     
     */
    public SbbLocalInterfaceName getSbbLocalInterfaceName() {
        return sbbLocalInterfaceName;
    }

    /**
     * Sets the value of the sbbLocalInterfaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbLocalInterfaceName }
     *     
     */
    public void setSbbLocalInterfaceName(SbbLocalInterfaceName value) {
        this.sbbLocalInterfaceName = value;
    }
}
