package net.sourceforge.ondex.mesh.qual;

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
@XmlType(name = "", propOrder = { "string" })
@XmlRootElement(name = "DescriptorName")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class DescriptorName {

    @XmlElement(name = "String", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String string;

    /**
     * Gets the value of the string property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setString(String value) {
        this.string = value;
    }
}
