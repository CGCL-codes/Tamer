package net.sourceforge.ondex.mesh.supp;

import java.util.ArrayList;
import java.util.List;
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
@XmlType(name = "", propOrder = { "semanticType" })
@XmlRootElement(name = "SemanticTypeList")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:53:01+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class SemanticTypeList {

    @XmlElement(name = "SemanticType", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:53:01+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected List<SemanticType> semanticType;

    /**
     * Gets the value of the semanticType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the semanticType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSemanticType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SemanticType }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:53:01+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public List<SemanticType> getSemanticType() {
        if (semanticType == null) {
            semanticType = new ArrayList<SemanticType>();
        }
        return this.semanticType;
    }
}
