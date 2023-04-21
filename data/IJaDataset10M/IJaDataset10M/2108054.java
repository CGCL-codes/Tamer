package net.sublight.webservice;

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
 *         &lt;element name="session" type="{http://microsoft.com/wsdl/types/}guid"/>
 *         &lt;element name="subtitleID" type="{http://microsoft.com/wsdl/types/}guid"/>
 *         &lt;element name="language" type="{http://www.sublight.si/}SubtitleLanguage"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "session", "subtitleID", "language", "message" })
@XmlRootElement(name = "AddSubtitleComment")
public class AddSubtitleComment {

    @XmlElement(required = true)
    protected String session;

    @XmlElement(required = true)
    protected String subtitleID;

    @XmlElement(required = true)
    protected SubtitleLanguage language;

    protected String message;

    /**
     * Gets the value of the session property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSession() {
        return session;
    }

    /**
     * Sets the value of the session property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSession(String value) {
        this.session = value;
    }

    /**
     * Gets the value of the subtitleID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubtitleID() {
        return subtitleID;
    }

    /**
     * Sets the value of the subtitleID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubtitleID(String value) {
        this.subtitleID = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link SubtitleLanguage }
     *     
     */
    public SubtitleLanguage getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubtitleLanguage }
     *     
     */
    public void setLanguage(SubtitleLanguage value) {
        this.language = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }
}
