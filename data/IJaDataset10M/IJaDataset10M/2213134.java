package net.sourceforge.ondex.mesh.qual;

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
@XmlType(name = "", propOrder = { "treeNodeAllowed" })
@XmlRootElement(name = "TreeNodeAllowedList")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class TreeNodeAllowedList {

    @XmlElement(name = "TreeNodeAllowed", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected List<TreeNodeAllowed> treeNodeAllowed;

    /**
     * Gets the value of the treeNodeAllowed property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the treeNodeAllowed property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTreeNodeAllowed().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TreeNodeAllowed }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-09-10T08:52:36+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public List<TreeNodeAllowed> getTreeNodeAllowed() {
        if (treeNodeAllowed == null) {
            treeNodeAllowed = new ArrayList<TreeNodeAllowed>();
        }
        return this.treeNodeAllowed;
    }
}
