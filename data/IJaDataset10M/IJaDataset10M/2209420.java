package net.sourceforge.hlagile.fomgen.jaxbhla;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "enumeratedData" })
@XmlRootElement(name = "enumeratedDataTypes")
public class EnumeratedDataTypes {

    @XmlElement(required = true)
    protected List<EnumeratedData> enumeratedData;

    /**
     * Gets the value of the enumeratedData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enumeratedData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnumeratedData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnumeratedData }
     * 
     * 
     */
    public List<EnumeratedData> getEnumeratedData() {
        if (enumeratedData == null) {
            enumeratedData = new ArrayList<EnumeratedData>();
        }
        return this.enumeratedData;
    }
}
