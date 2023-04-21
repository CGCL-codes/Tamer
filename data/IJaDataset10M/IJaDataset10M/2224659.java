package tr.com.srdc.isurf.gs1.ucc.ean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for TradeItemLocationProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TradeItemLocationProfileType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ean.ucc:plan:2}PlanDocumentType">
 *       &lt;sequence>
 *         &lt;element name="profileStatus" type="{urn:ean.ucc:plan:2}ProfileStatusCodeListType"/>
 *         &lt;element name="itemManagementProfile" type="{urn:ean.ucc:plan:2}ItemManagementProfileType" maxOccurs="unbounded"/>
 *         &lt;element name="extension" type="{urn:ean.ucc:2}ExtensionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradeItemLocationProfileType", namespace = "", propOrder = { "profileStatus", "itemManagementProfile", "extension" })
public class TradeItemLocationProfileType extends PlanDocumentType {

    @XmlElement(required = true)
    protected ProfileStatusCodeListType profileStatus;

    @XmlElement(required = true)
    protected List<ItemManagementProfileType> itemManagementProfile;

    protected ExtensionType extension;

    /**
     * Gets the value of the profileStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileStatusCodeListType }
     *     
     */
    public ProfileStatusCodeListType getProfileStatus() {
        return profileStatus;
    }

    /**
     * Sets the value of the profileStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileStatusCodeListType }
     *     
     */
    public void setProfileStatus(ProfileStatusCodeListType value) {
        this.profileStatus = value;
    }

    /**
     * Gets the value of the itemManagementProfile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemManagementProfile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemManagementProfile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemManagementProfileType }
     * 
     * 
     */
    public List<ItemManagementProfileType> getItemManagementProfile() {
        if (itemManagementProfile == null) {
            itemManagementProfile = new ArrayList<ItemManagementProfileType>();
        }
        return this.itemManagementProfile;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionType }
     *     
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionType }
     *     
     */
    public void setExtension(ExtensionType value) {
        this.extension = value;
    }
}
