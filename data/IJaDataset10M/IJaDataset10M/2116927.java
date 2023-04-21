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
 *         &lt;element name="subtitleId" type="{http://microsoft.com/wsdl/types/}guid"/>
 *         &lt;element name="reason" type="{http://www.sublight.si/}ReportReason"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "session", "subtitleId", "reason", "comment" })
@XmlRootElement(name = "ReportSubtitle2")
public class ReportSubtitle2 {

    @XmlElement(required = true)
    protected String session;

    @XmlElement(required = true)
    protected String subtitleId;

    @XmlElement(required = true)
    protected ReportReason reason;

    protected String comment;

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
     * Gets the value of the subtitleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubtitleId() {
        return subtitleId;
    }

    /**
     * Sets the value of the subtitleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubtitleId(String value) {
        this.subtitleId = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link ReportReason }
     *     
     */
    public ReportReason getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportReason }
     *     
     */
    public void setReason(ReportReason value) {
        this.reason = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }
}
