package owchat.source;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for addAsFriend complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addAsFriend">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="friendName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addAsFriend", propOrder = { "friendName", "keyString" })
public class AddAsFriend {

    protected String friendName;

    protected String keyString;

    /**
     * Gets the value of the friendName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFriendName() {
        return friendName;
    }

    /**
     * Sets the value of the friendName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFriendName(String value) {
        this.friendName = value;
    }

    /**
     * Gets the value of the keyString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyString() {
        return keyString;
    }

    /**
     * Sets the value of the keyString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyString(String value) {
        this.keyString = value;
    }
}
