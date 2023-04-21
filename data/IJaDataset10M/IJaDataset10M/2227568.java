package at.arcsmed.mpower.communicator.soap.status.server.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetMyAccountStateMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetMyAccountStateMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sipId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetMyAccountStateMessage", propOrder = { "sipId" })
public class GetMyAccountStateMessage {

    @XmlElement(required = true)
    protected String sipId;

    /**
     * Gets the value of the sipId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSipId() {
        return sipId;
    }

    /**
     * Sets the value of the sipId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSipId(String value) {
        this.sipId = value;
    }
}
