package fab.formatic.web.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for serviceDataList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceDataList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="discAmount" type="{http://fab/}number" minOccurs="0"/>
 *         &lt;element name="isCharged" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isExceed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="transAmt" type="{http://fab/}number" minOccurs="0"/>
 *         &lt;element name="transAttr1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transAttr2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transAttr3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="transName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transSeq" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceDataList", propOrder = { "discAmount", "isCharged", "isExceed", "transAmt", "transAttr1", "transAttr2", "transAttr3", "transDateTime", "transName", "transSeq" })
public class ServiceDataList {

    protected Number discAmount;

    protected boolean isCharged;

    protected boolean isExceed;

    protected Number transAmt;

    protected String transAttr1;

    protected String transAttr2;

    protected String transAttr3;

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar transDateTime;

    protected String transName;

    protected int transSeq;

    /**
     * Gets the value of the discAmount property.
     * 
     * @return
     *     possible object is
     *     {@link Number }
     *     
     */
    public Number getDiscAmount() {
        return discAmount;
    }

    /**
     * Sets the value of the discAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Number }
     *     
     */
    public void setDiscAmount(Number value) {
        this.discAmount = value;
    }

    /**
     * Gets the value of the isCharged property.
     * 
     */
    public boolean isIsCharged() {
        return isCharged;
    }

    /**
     * Sets the value of the isCharged property.
     * 
     */
    public void setIsCharged(boolean value) {
        this.isCharged = value;
    }

    /**
     * Gets the value of the isExceed property.
     * 
     */
    public boolean isIsExceed() {
        return isExceed;
    }

    /**
     * Sets the value of the isExceed property.
     * 
     */
    public void setIsExceed(boolean value) {
        this.isExceed = value;
    }

    /**
     * Gets the value of the transAmt property.
     * 
     * @return
     *     possible object is
     *     {@link Number }
     *     
     */
    public Number getTransAmt() {
        return transAmt;
    }

    /**
     * Sets the value of the transAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Number }
     *     
     */
    public void setTransAmt(Number value) {
        this.transAmt = value;
    }

    /**
     * Gets the value of the transAttr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransAttr1() {
        return transAttr1;
    }

    /**
     * Sets the value of the transAttr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransAttr1(String value) {
        this.transAttr1 = value;
    }

    /**
     * Gets the value of the transAttr2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransAttr2() {
        return transAttr2;
    }

    /**
     * Sets the value of the transAttr2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransAttr2(String value) {
        this.transAttr2 = value;
    }

    /**
     * Gets the value of the transAttr3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransAttr3() {
        return transAttr3;
    }

    /**
     * Sets the value of the transAttr3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransAttr3(String value) {
        this.transAttr3 = value;
    }

    /**
     * Gets the value of the transDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransDateTime() {
        return transDateTime;
    }

    /**
     * Sets the value of the transDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransDateTime(XMLGregorianCalendar value) {
        this.transDateTime = value;
    }

    /**
     * Gets the value of the transName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransName() {
        return transName;
    }

    /**
     * Sets the value of the transName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransName(String value) {
        this.transName = value;
    }

    /**
     * Gets the value of the transSeq property.
     * 
     */
    public int getTransSeq() {
        return transSeq;
    }

    /**
     * Sets the value of the transSeq property.
     * 
     */
    public void setTransSeq(int value) {
        this.transSeq = value;
    }
}
