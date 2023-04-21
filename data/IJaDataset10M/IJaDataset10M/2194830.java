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
@XmlType(name = "", propOrder = { "previousIndexing" })
@XmlRootElement(name = "PreviousIndexingList")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:53:01+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class PreviousIndexingList {

    @XmlElement(name = "PreviousIndexing", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:53:01+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected List<PreviousIndexing> previousIndexing;

    /**
     * Gets the value of the previousIndexing property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the previousIndexing property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreviousIndexing().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PreviousIndexing }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:53:01+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public List<PreviousIndexing> getPreviousIndexing() {
        if (previousIndexing == null) {
            previousIndexing = new ArrayList<PreviousIndexing>();
        }
        return this.previousIndexing;
    }
}
