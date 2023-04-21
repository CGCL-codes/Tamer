package gov.nih.nlm.ncbi.soap.eutils.efetch_pubmed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PersonalNameSubjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonalNameSubjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}LastName"/>
 *           &lt;choice minOccurs="0">
 *             &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}ForeName"/>
 *             &lt;sequence>
 *               &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}FirstName"/>
 *               &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}MiddleName" minOccurs="0"/>
 *             &lt;/sequence>
 *           &lt;/choice>
 *           &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}Initials" minOccurs="0"/>
 *           &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}Suffix" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}DatesAssociatedWithName" minOccurs="0"/>
 *         &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}NameQualifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}OtherInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}TitleAssociatedWithName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonalNameSubjectType", propOrder = { "lastName", "foreName", "firstName", "middleName", "initials", "suffix", "datesAssociatedWithName", "nameQualifier", "otherInformation", "titleAssociatedWithName" })
public class PersonalNameSubjectType {

    @XmlElement(name = "LastName", required = true)
    protected String lastName;

    @XmlElement(name = "ForeName")
    protected String foreName;

    @XmlElement(name = "FirstName")
    protected String firstName;

    @XmlElement(name = "MiddleName")
    protected String middleName;

    @XmlElement(name = "Initials")
    protected String initials;

    @XmlElement(name = "Suffix")
    protected String suffix;

    @XmlElement(name = "DatesAssociatedWithName")
    protected String datesAssociatedWithName;

    @XmlElement(name = "NameQualifier")
    protected String nameQualifier;

    @XmlElement(name = "OtherInformation")
    protected String otherInformation;

    @XmlElement(name = "TitleAssociatedWithName")
    protected String titleAssociatedWithName;

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
     * Gets the value of the foreName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeName() {
        return foreName;
    }

    /**
     * Sets the value of the foreName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeName(String value) {
        this.foreName = value;
    }

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
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the initials property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitials() {
        return initials;
    }

    /**
     * Sets the value of the initials property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitials(String value) {
        this.initials = value;
    }

    /**
     * Gets the value of the suffix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the value of the suffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuffix(String value) {
        this.suffix = value;
    }

    /**
     * Gets the value of the datesAssociatedWithName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatesAssociatedWithName() {
        return datesAssociatedWithName;
    }

    /**
     * Sets the value of the datesAssociatedWithName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatesAssociatedWithName(String value) {
        this.datesAssociatedWithName = value;
    }

    /**
     * Gets the value of the nameQualifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameQualifier() {
        return nameQualifier;
    }

    /**
     * Sets the value of the nameQualifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameQualifier(String value) {
        this.nameQualifier = value;
    }

    /**
     * Gets the value of the otherInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherInformation() {
        return otherInformation;
    }

    /**
     * Sets the value of the otherInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherInformation(String value) {
        this.otherInformation = value;
    }

    /**
     * Gets the value of the titleAssociatedWithName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitleAssociatedWithName() {
        return titleAssociatedWithName;
    }

    /**
     * Sets the value of the titleAssociatedWithName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitleAssociatedWithName(String value) {
        this.titleAssociatedWithName = value;
    }
}
