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
@XmlType(name = "", propOrder = { "qualifierUI", "qualifierName" })
@XmlRootElement(name = "QualifierReferredTo")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class QualifierReferredTo {

    @XmlElement(name = "QualifierUI", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String qualifierUI;

    @XmlElement(name = "QualifierName", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected QualifierName qualifierName;

    /**
     * Gets the value of the qualifierUI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getQualifierUI() {
        return qualifierUI;
    }

    /**
     * Sets the value of the qualifierUI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setQualifierUI(String value) {
        this.qualifierUI = value;
    }

    /**
     * Gets the value of the qualifierName property.
     * 
     * @return
     *     possible object is
     *     {@link QualifierName }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public QualifierName getQualifierName() {
        return qualifierName;
    }

    /**
     * Sets the value of the qualifierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifierName }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setQualifierName(QualifierName value) {
        this.qualifierName = value;
    }
}
