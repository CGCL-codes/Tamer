package org.javason.jsonrpc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for WsfCustomer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WsfCustomer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneType" type="{http://partner.services.dev.extendbenefits.com/}PhoneTypeList"/>
 *         &lt;element name="PhoneIsPreferred" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CustomerId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="AssociatedCampaignId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="QuoteConfigurationIds" type="{http://partner.services.dev.extendbenefits.com/}ArrayOfLong" minOccurs="0"/>
 *         &lt;element name="RelationshipType" type="{http://partner.services.dev.extendbenefits.com/}RelationshipTypeList"/>
 *         &lt;element name="DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="PregnancyDueDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="GenderCode" type="{http://partner.services.dev.extendbenefits.com/}GenderCodeList"/>
 *         &lt;element name="UsesTobacco" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="InterestedPlanType" type="{http://partner.services.dev.extendbenefits.com/}PlanTypeList"/>
 *         &lt;element name="IsStudent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="LastVisitedOnDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsfCustomer", propOrder = { "firstName", "lastName", "emailAddress", "phoneNumber", "phoneExtension", "phoneType", "phoneIsPreferred", "customerId", "associatedCampaignId", "quoteConfigurationIds", "relationshipType", "dateOfBirth", "pregnancyDueDate", "genderCode", "usesTobacco", "interestedPlanType", "isStudent", "lastVisitedOnDate" })
public class WsfCustomer {

    @XmlElement(name = "FirstName")
    protected String firstName;

    @XmlElement(name = "LastName")
    protected String lastName;

    @XmlElement(name = "EmailAddress")
    protected String emailAddress;

    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;

    @XmlElement(name = "PhoneExtension")
    protected String phoneExtension;

    @XmlElement(name = "PhoneIsPreferred")
    protected boolean phoneIsPreferred;

    @XmlElement(name = "CustomerId")
    protected long customerId;

    @XmlElement(name = "AssociatedCampaignId")
    protected int associatedCampaignId;

    @XmlElement(name = "DateOfBirth", required = true)
    protected XMLGregorianCalendar dateOfBirth;

    @XmlElement(name = "PregnancyDueDate", required = true, nillable = true)
    protected XMLGregorianCalendar pregnancyDueDate;

    @XmlElement(name = "UsesTobacco")
    protected boolean usesTobacco;

    @XmlElement(name = "IsStudent")
    protected boolean isStudent;

    @XmlElement(name = "LastVisitedOnDate", required = true)
    protected XMLGregorianCalendar lastVisitedOnDate;

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    /**
     * Gets the value of the phoneExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneExtension() {
        return phoneExtension;
    }

    /**
     * Sets the value of the phoneExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneExtension(String value) {
        this.phoneExtension = value;
    }

    /**
     * Gets the value of the phoneIsPreferred property.
     * 
     */
    public boolean isPhoneIsPreferred() {
        return phoneIsPreferred;
    }

    /**
     * Sets the value of the phoneIsPreferred property.
     * 
     */
    public void setPhoneIsPreferred(boolean value) {
        this.phoneIsPreferred = value;
    }

    /**
     * Gets the value of the customerId property.
     * 
     */
    public long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     */
    public void setCustomerId(long value) {
        this.customerId = value;
    }

    /**
     * Gets the value of the associatedCampaignId property.
     * 
     */
    public int getAssociatedCampaignId() {
        return associatedCampaignId;
    }

    /**
     * Sets the value of the associatedCampaignId property.
     * 
     */
    public void setAssociatedCampaignId(int value) {
        this.associatedCampaignId = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the pregnancyDueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPregnancyDueDate() {
        return pregnancyDueDate;
    }

    /**
     * Sets the value of the pregnancyDueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPregnancyDueDate(XMLGregorianCalendar value) {
        this.pregnancyDueDate = value;
    }

    /**
     * Gets the value of the usesTobacco property.
     * 
     */
    public boolean isUsesTobacco() {
        return usesTobacco;
    }

    /**
     * Sets the value of the usesTobacco property.
     * 
     */
    public void setUsesTobacco(boolean value) {
        this.usesTobacco = value;
    }

    /**
     * Gets the value of the isStudent property.
     * 
     */
    public boolean isIsStudent() {
        return isStudent;
    }

    /**
     * Sets the value of the isStudent property.
     * 
     */
    public void setIsStudent(boolean value) {
        this.isStudent = value;
    }

    /**
     * Gets the value of the lastVisitedOnDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastVisitedOnDate() {
        return lastVisitedOnDate;
    }

    /**
     * Sets the value of the lastVisitedOnDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastVisitedOnDate(XMLGregorianCalendar value) {
        this.lastVisitedOnDate = value;
    }
}
