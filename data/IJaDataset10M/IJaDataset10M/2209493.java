package resultviewer.xml.classes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}IOFVersion" minOccurs="0"/>
 *         &lt;element ref="{}ClubEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "iofVersion", "clubEntry" })
@XmlRootElement(name = "EntryList")
public class EntryList {

    @XmlElement(name = "IOFVersion")
    protected IOFVersion iofVersion;

    @XmlElement(name = "ClubEntry")
    protected List<ClubEntry> clubEntry;

    /**
     * Gets the value of the iofVersion property.
     * 
     * @return
     *     possible object is
     *     {@link IOFVersion }
     *     
     */
    public IOFVersion getIOFVersion() {
        return iofVersion;
    }

    /**
     * Sets the value of the iofVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link IOFVersion }
     *     
     */
    public void setIOFVersion(IOFVersion value) {
        this.iofVersion = value;
    }

    /**
     * Gets the value of the clubEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clubEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClubEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClubEntry }
     * 
     * 
     */
    public List<ClubEntry> getClubEntry() {
        if (clubEntry == null) {
            clubEntry = new ArrayList<ClubEntry>();
        }
        return this.clubEntry;
    }
}
