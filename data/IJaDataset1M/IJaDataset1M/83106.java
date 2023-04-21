package net.sourceforge.ondex.mesh.pa;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "descriptorUI", "descriptorName" })
@XmlRootElement(name = "DescriptorReferredTo")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class DescriptorReferredTo {

    @XmlElement(name = "DescriptorUI", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String descriptorUI;

    @XmlElement(name = "DescriptorName", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected DescriptorName descriptorName;

    /**
     * Gets the value of the descriptorUI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getDescriptorUI() {
        return descriptorUI;
    }

    /**
     * Sets the value of the descriptorUI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setDescriptorUI(String value) {
        this.descriptorUI = value;
    }

    /**
     * Gets the value of the descriptorName property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptorName }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public DescriptorName getDescriptorName() {
        return descriptorName;
    }

    /**
     * Sets the value of the descriptorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptorName }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:11+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setDescriptorName(DescriptorName value) {
        this.descriptorName = value;
    }
}
