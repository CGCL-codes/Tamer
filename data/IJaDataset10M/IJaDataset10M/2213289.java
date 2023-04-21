package it.piacenza1733.isurf.pilot.rfid;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MobileReadResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "mobileReadResult" })
@XmlRootElement(name = "MobileReadResponse")
public class MobileReadResponse {

    @XmlElement(name = "MobileReadResult")
    protected boolean mobileReadResult;

    /**
	 * Gets the value of the mobileReadResult property.
	 * 
	 */
    public boolean isMobileReadResult() {
        return mobileReadResult;
    }

    /**
	 * Sets the value of the mobileReadResult property.
	 * 
	 */
    public void setMobileReadResult(boolean value) {
        this.mobileReadResult = value;
    }
}
