package net.sublight.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for UserInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Created" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="SubtitlesPublished" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SubtitlesDeleted" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SubtitleThanks" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Points" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="AverageRate" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="SubtitleDownloads" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MySubtitleDownloads" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalSubtitleDownloads" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserInfo", propOrder = { "username", "email", "created", "subtitlesPublished", "subtitlesDeleted", "subtitleThanks", "points", "averageRate", "subtitleDownloads", "mySubtitleDownloads", "totalSubtitleDownloads" })
public class UserInfo {

    @XmlElement(name = "Username")
    protected String username;

    @XmlElement(name = "Email")
    protected String email;

    @XmlElement(name = "Created", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;

    @XmlElement(name = "SubtitlesPublished")
    protected int subtitlesPublished;

    @XmlElement(name = "SubtitlesDeleted")
    protected int subtitlesDeleted;

    @XmlElement(name = "SubtitleThanks")
    protected int subtitleThanks;

    @XmlElement(name = "Points")
    protected double points;

    @XmlElement(name = "AverageRate")
    protected double averageRate;

    @XmlElement(name = "SubtitleDownloads")
    protected int subtitleDownloads;

    @XmlElement(name = "MySubtitleDownloads")
    protected int mySubtitleDownloads;

    @XmlElement(name = "TotalSubtitleDownloads")
    protected int totalSubtitleDownloads;

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    /**
     * Gets the value of the subtitlesPublished property.
     * 
     */
    public int getSubtitlesPublished() {
        return subtitlesPublished;
    }

    /**
     * Sets the value of the subtitlesPublished property.
     * 
     */
    public void setSubtitlesPublished(int value) {
        this.subtitlesPublished = value;
    }

    /**
     * Gets the value of the subtitlesDeleted property.
     * 
     */
    public int getSubtitlesDeleted() {
        return subtitlesDeleted;
    }

    /**
     * Sets the value of the subtitlesDeleted property.
     * 
     */
    public void setSubtitlesDeleted(int value) {
        this.subtitlesDeleted = value;
    }

    /**
     * Gets the value of the subtitleThanks property.
     * 
     */
    public int getSubtitleThanks() {
        return subtitleThanks;
    }

    /**
     * Sets the value of the subtitleThanks property.
     * 
     */
    public void setSubtitleThanks(int value) {
        this.subtitleThanks = value;
    }

    /**
     * Gets the value of the points property.
     * 
     */
    public double getPoints() {
        return points;
    }

    /**
     * Sets the value of the points property.
     * 
     */
    public void setPoints(double value) {
        this.points = value;
    }

    /**
     * Gets the value of the averageRate property.
     * 
     */
    public double getAverageRate() {
        return averageRate;
    }

    /**
     * Sets the value of the averageRate property.
     * 
     */
    public void setAverageRate(double value) {
        this.averageRate = value;
    }

    /**
     * Gets the value of the subtitleDownloads property.
     * 
     */
    public int getSubtitleDownloads() {
        return subtitleDownloads;
    }

    /**
     * Sets the value of the subtitleDownloads property.
     * 
     */
    public void setSubtitleDownloads(int value) {
        this.subtitleDownloads = value;
    }

    /**
     * Gets the value of the mySubtitleDownloads property.
     * 
     */
    public int getMySubtitleDownloads() {
        return mySubtitleDownloads;
    }

    /**
     * Sets the value of the mySubtitleDownloads property.
     * 
     */
    public void setMySubtitleDownloads(int value) {
        this.mySubtitleDownloads = value;
    }

    /**
     * Gets the value of the totalSubtitleDownloads property.
     * 
     */
    public int getTotalSubtitleDownloads() {
        return totalSubtitleDownloads;
    }

    /**
     * Sets the value of the totalSubtitleDownloads property.
     * 
     */
    public void setTotalSubtitleDownloads(int value) {
        this.totalSubtitleDownloads = value;
    }
}
