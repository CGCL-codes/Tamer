package org.opencarto.kml.v22.xml.xal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

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
 *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber"/>
 *           &lt;element name="ThoroughfareNumberRange">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="ThoroughfareNumberFrom">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber" maxOccurs="unbounded"/>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;/sequence>
 *                             &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
 *                             &lt;anyAttribute namespace='##other'/>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="ThoroughfareNumberTo">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber" maxOccurs="unbounded"/>
 *                               &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;/sequence>
 *                             &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
 *                             &lt;anyAttribute namespace='##other'/>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
 *                   &lt;attribute name="RangeType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                         &lt;enumeration value="Odd"/>
 *                         &lt;enumeration value="Even"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/attribute>
 *                   &lt;attribute name="Indicator" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                   &lt;attribute name="Separator" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                   &lt;attribute name="IndicatorOccurrence">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                         &lt;enumeration value="Before"/>
 *                         &lt;enumeration value="After"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/attribute>
 *                   &lt;attribute name="NumberRangeOccurrence">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                         &lt;enumeration value="BeforeName"/>
 *                         &lt;enumeration value="AfterName"/>
 *                         &lt;enumeration value="BeforeType"/>
 *                         &lt;enumeration value="AfterType"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/attribute>
 *                   &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                   &lt;anyAttribute namespace='##other'/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ThoroughfarePreDirection" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfarePreDirectionType" minOccurs="0"/>
 *         &lt;element name="ThoroughfareLeadingType" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareLeadingTypeType" minOccurs="0"/>
 *         &lt;element name="ThoroughfareName" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNameType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ThoroughfareTrailingType" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareTrailingTypeType" minOccurs="0"/>
 *         &lt;element name="ThoroughfarePostDirection" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfarePostDirectionType" minOccurs="0"/>
 *         &lt;element name="DependentThoroughfare" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="ThoroughfarePreDirection" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfarePreDirectionType" minOccurs="0"/>
 *                   &lt;element name="ThoroughfareLeadingType" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareLeadingTypeType" minOccurs="0"/>
 *                   &lt;element name="ThoroughfareName" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNameType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="ThoroughfareTrailingType" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareTrailingTypeType" minOccurs="0"/>
 *                   &lt;element name="ThoroughfarePostDirection" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfarePostDirectionType" minOccurs="0"/>
 *                   &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;anyAttribute namespace='##other'/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="DependentLocality" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}DependentLocalityType"/>
 *           &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}Premise"/>
 *           &lt;element name="Firm" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}FirmType"/>
 *           &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}PostalCode"/>
 *         &lt;/choice>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="DependentThoroughfares">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="Yes"/>
 *             &lt;enumeration value="No"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="DependentThoroughfaresIndicator" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="DependentThoroughfaresConnector" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="DependentThoroughfaresType" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;anyAttribute namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addressLines", "thoroughfareNumbersAndThoroughfareNumberRanges", "thoroughfareNumberPrefixes", "thoroughfareNumberSuffixes", "thoroughfarePreDirection", "thoroughfareLeadingType", "thoroughfareNames", "thoroughfareTrailingType", "thoroughfarePostDirection", "dependentThoroughfare", "postalCode", "firm", "premise", "dependentLocality", "anies" })
@XmlRootElement(name = "Thoroughfare")
public class Thoroughfare {

    @XmlElement(name = "AddressLine")
    protected List<AddressLine> addressLines;

    @XmlElements({ @XmlElement(name = "ThoroughfareNumber", type = ThoroughfareNumber.class), @XmlElement(name = "ThoroughfareNumberRange", type = Thoroughfare.ThoroughfareNumberRange.class) })
    protected List<Object> thoroughfareNumbersAndThoroughfareNumberRanges;

    @XmlElement(name = "ThoroughfareNumberPrefix")
    protected List<ThoroughfareNumberPrefix> thoroughfareNumberPrefixes;

    @XmlElement(name = "ThoroughfareNumberSuffix")
    protected List<ThoroughfareNumberSuffix> thoroughfareNumberSuffixes;

    @XmlElement(name = "ThoroughfarePreDirection")
    protected ThoroughfarePreDirectionType thoroughfarePreDirection;

    @XmlElement(name = "ThoroughfareLeadingType")
    protected ThoroughfareLeadingTypeType thoroughfareLeadingType;

    @XmlElement(name = "ThoroughfareName")
    protected List<ThoroughfareNameType> thoroughfareNames;

    @XmlElement(name = "ThoroughfareTrailingType")
    protected ThoroughfareTrailingTypeType thoroughfareTrailingType;

    @XmlElement(name = "ThoroughfarePostDirection")
    protected ThoroughfarePostDirectionType thoroughfarePostDirection;

    @XmlElement(name = "DependentThoroughfare")
    protected Thoroughfare.DependentThoroughfare dependentThoroughfare;

    @XmlElement(name = "PostalCode")
    protected PostalCode postalCode;

    @XmlElement(name = "Firm")
    protected FirmType firm;

    @XmlElement(name = "Premise")
    protected Premise premise;

    @XmlElement(name = "DependentLocality")
    protected DependentLocalityType dependentLocality;

    @XmlAnyElement(lax = true)
    protected List<Object> anies;

    @XmlAttribute(name = "Type")
    @XmlSchemaType(name = "anySimpleType")
    protected String type;

    @XmlAttribute(name = "DependentThoroughfares")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dependentThoroughfares;

    @XmlAttribute(name = "DependentThoroughfaresIndicator")
    @XmlSchemaType(name = "anySimpleType")
    protected String dependentThoroughfaresIndicator;

    @XmlAttribute(name = "DependentThoroughfaresConnector")
    @XmlSchemaType(name = "anySimpleType")
    protected String dependentThoroughfaresConnector;

    @XmlAttribute(name = "DependentThoroughfaresType")
    @XmlSchemaType(name = "anySimpleType")
    protected String dependentThoroughfaresType;

    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the addressLines property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressLines property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressLines().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AddressLine }
     * 
     * 
     */
    public List<AddressLine> getAddressLines() {
        if (addressLines == null) {
            addressLines = new ArrayList<AddressLine>();
        }
        return this.addressLines;
    }

    /**
     * Gets the value of the thoroughfareNumbersAndThoroughfareNumberRanges property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the thoroughfareNumbersAndThoroughfareNumberRanges property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getThoroughfareNumbersAndThoroughfareNumberRanges().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThoroughfareNumber }
     * {@link Thoroughfare.ThoroughfareNumberRange }
     * 
     * 
     */
    public List<Object> getThoroughfareNumbersAndThoroughfareNumberRanges() {
        if (thoroughfareNumbersAndThoroughfareNumberRanges == null) {
            thoroughfareNumbersAndThoroughfareNumberRanges = new ArrayList<Object>();
        }
        return this.thoroughfareNumbersAndThoroughfareNumberRanges;
    }

    /**
     * Gets the value of the thoroughfareNumberPrefixes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the thoroughfareNumberPrefixes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getThoroughfareNumberPrefixes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThoroughfareNumberPrefix }
     * 
     * 
     */
    public List<ThoroughfareNumberPrefix> getThoroughfareNumberPrefixes() {
        if (thoroughfareNumberPrefixes == null) {
            thoroughfareNumberPrefixes = new ArrayList<ThoroughfareNumberPrefix>();
        }
        return this.thoroughfareNumberPrefixes;
    }

    /**
     * Gets the value of the thoroughfareNumberSuffixes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the thoroughfareNumberSuffixes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getThoroughfareNumberSuffixes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThoroughfareNumberSuffix }
     * 
     * 
     */
    public List<ThoroughfareNumberSuffix> getThoroughfareNumberSuffixes() {
        if (thoroughfareNumberSuffixes == null) {
            thoroughfareNumberSuffixes = new ArrayList<ThoroughfareNumberSuffix>();
        }
        return this.thoroughfareNumberSuffixes;
    }

    /**
     * Gets the value of the thoroughfarePreDirection property.
     * 
     * @return
     *     possible object is
     *     {@link ThoroughfarePreDirectionType }
     *     
     */
    public ThoroughfarePreDirectionType getThoroughfarePreDirection() {
        return thoroughfarePreDirection;
    }

    /**
     * Sets the value of the thoroughfarePreDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThoroughfarePreDirectionType }
     *     
     */
    public void setThoroughfarePreDirection(ThoroughfarePreDirectionType value) {
        this.thoroughfarePreDirection = value;
    }

    /**
     * Gets the value of the thoroughfareLeadingType property.
     * 
     * @return
     *     possible object is
     *     {@link ThoroughfareLeadingTypeType }
     *     
     */
    public ThoroughfareLeadingTypeType getThoroughfareLeadingType() {
        return thoroughfareLeadingType;
    }

    /**
     * Sets the value of the thoroughfareLeadingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThoroughfareLeadingTypeType }
     *     
     */
    public void setThoroughfareLeadingType(ThoroughfareLeadingTypeType value) {
        this.thoroughfareLeadingType = value;
    }

    /**
     * Gets the value of the thoroughfareNames property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the thoroughfareNames property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getThoroughfareNames().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThoroughfareNameType }
     * 
     * 
     */
    public List<ThoroughfareNameType> getThoroughfareNames() {
        if (thoroughfareNames == null) {
            thoroughfareNames = new ArrayList<ThoroughfareNameType>();
        }
        return this.thoroughfareNames;
    }

    /**
     * Gets the value of the thoroughfareTrailingType property.
     * 
     * @return
     *     possible object is
     *     {@link ThoroughfareTrailingTypeType }
     *     
     */
    public ThoroughfareTrailingTypeType getThoroughfareTrailingType() {
        return thoroughfareTrailingType;
    }

    /**
     * Sets the value of the thoroughfareTrailingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThoroughfareTrailingTypeType }
     *     
     */
    public void setThoroughfareTrailingType(ThoroughfareTrailingTypeType value) {
        this.thoroughfareTrailingType = value;
    }

    /**
     * Gets the value of the thoroughfarePostDirection property.
     * 
     * @return
     *     possible object is
     *     {@link ThoroughfarePostDirectionType }
     *     
     */
    public ThoroughfarePostDirectionType getThoroughfarePostDirection() {
        return thoroughfarePostDirection;
    }

    /**
     * Sets the value of the thoroughfarePostDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThoroughfarePostDirectionType }
     *     
     */
    public void setThoroughfarePostDirection(ThoroughfarePostDirectionType value) {
        this.thoroughfarePostDirection = value;
    }

    /**
     * Gets the value of the dependentThoroughfare property.
     * 
     * @return
     *     possible object is
     *     {@link Thoroughfare.DependentThoroughfare }
     *     
     */
    public Thoroughfare.DependentThoroughfare getDependentThoroughfare() {
        return dependentThoroughfare;
    }

    /**
     * Sets the value of the dependentThoroughfare property.
     * 
     * @param value
     *     allowed object is
     *     {@link Thoroughfare.DependentThoroughfare }
     *     
     */
    public void setDependentThoroughfare(Thoroughfare.DependentThoroughfare value) {
        this.dependentThoroughfare = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link PostalCode }
     *     
     */
    public PostalCode getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalCode }
     *     
     */
    public void setPostalCode(PostalCode value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the firm property.
     * 
     * @return
     *     possible object is
     *     {@link FirmType }
     *     
     */
    public FirmType getFirm() {
        return firm;
    }

    /**
     * Sets the value of the firm property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirmType }
     *     
     */
    public void setFirm(FirmType value) {
        this.firm = value;
    }

    /**
     * Gets the value of the premise property.
     * 
     * @return
     *     possible object is
     *     {@link Premise }
     *     
     */
    public Premise getPremise() {
        return premise;
    }

    /**
     * Sets the value of the premise property.
     * 
     * @param value
     *     allowed object is
     *     {@link Premise }
     *     
     */
    public void setPremise(Premise value) {
        this.premise = value;
    }

    /**
     * Gets the value of the dependentLocality property.
     * 
     * @return
     *     possible object is
     *     {@link DependentLocalityType }
     *     
     */
    public DependentLocalityType getDependentLocality() {
        return dependentLocality;
    }

    /**
     * Sets the value of the dependentLocality property.
     * 
     * @param value
     *     allowed object is
     *     {@link DependentLocalityType }
     *     
     */
    public void setDependentLocality(DependentLocalityType value) {
        this.dependentLocality = value;
    }

    /**
     * Gets the value of the anies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAnies() {
        if (anies == null) {
            anies = new ArrayList<Object>();
        }
        return this.anies;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the dependentThoroughfares property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependentThoroughfares() {
        return dependentThoroughfares;
    }

    /**
     * Sets the value of the dependentThoroughfares property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependentThoroughfares(String value) {
        this.dependentThoroughfares = value;
    }

    /**
     * Gets the value of the dependentThoroughfaresIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependentThoroughfaresIndicator() {
        return dependentThoroughfaresIndicator;
    }

    /**
     * Sets the value of the dependentThoroughfaresIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependentThoroughfaresIndicator(String value) {
        this.dependentThoroughfaresIndicator = value;
    }

    /**
     * Gets the value of the dependentThoroughfaresConnector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependentThoroughfaresConnector() {
        return dependentThoroughfaresConnector;
    }

    /**
     * Sets the value of the dependentThoroughfaresConnector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependentThoroughfaresConnector(String value) {
        this.dependentThoroughfaresConnector = value;
    }

    /**
     * Gets the value of the dependentThoroughfaresType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependentThoroughfaresType() {
        return dependentThoroughfaresType;
    }

    /**
     * Sets the value of the dependentThoroughfaresType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependentThoroughfaresType(String value) {
        this.dependentThoroughfaresType = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

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
     *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="ThoroughfarePreDirection" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfarePreDirectionType" minOccurs="0"/>
     *         &lt;element name="ThoroughfareLeadingType" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareLeadingTypeType" minOccurs="0"/>
     *         &lt;element name="ThoroughfareName" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNameType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="ThoroughfareTrailingType" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareTrailingTypeType" minOccurs="0"/>
     *         &lt;element name="ThoroughfarePostDirection" type="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfarePostDirectionType" minOccurs="0"/>
     *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;anyAttribute namespace='##other'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "addressLines", "thoroughfarePreDirection", "thoroughfareLeadingType", "thoroughfareNames", "thoroughfareTrailingType", "thoroughfarePostDirection", "anies" })
    public static class DependentThoroughfare {

        @XmlElement(name = "AddressLine")
        protected List<AddressLine> addressLines;

        @XmlElement(name = "ThoroughfarePreDirection")
        protected ThoroughfarePreDirectionType thoroughfarePreDirection;

        @XmlElement(name = "ThoroughfareLeadingType")
        protected ThoroughfareLeadingTypeType thoroughfareLeadingType;

        @XmlElement(name = "ThoroughfareName")
        protected List<ThoroughfareNameType> thoroughfareNames;

        @XmlElement(name = "ThoroughfareTrailingType")
        protected ThoroughfareTrailingTypeType thoroughfareTrailingType;

        @XmlElement(name = "ThoroughfarePostDirection")
        protected ThoroughfarePostDirectionType thoroughfarePostDirection;

        @XmlAnyElement(lax = true)
        protected List<Object> anies;

        @XmlAttribute(name = "Type")
        @XmlSchemaType(name = "anySimpleType")
        protected String type;

        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets the value of the addressLines property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the addressLines property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAddressLines().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AddressLine }
         * 
         * 
         */
        public List<AddressLine> getAddressLines() {
            if (addressLines == null) {
                addressLines = new ArrayList<AddressLine>();
            }
            return this.addressLines;
        }

        /**
         * Gets the value of the thoroughfarePreDirection property.
         * 
         * @return
         *     possible object is
         *     {@link ThoroughfarePreDirectionType }
         *     
         */
        public ThoroughfarePreDirectionType getThoroughfarePreDirection() {
            return thoroughfarePreDirection;
        }

        /**
         * Sets the value of the thoroughfarePreDirection property.
         * 
         * @param value
         *     allowed object is
         *     {@link ThoroughfarePreDirectionType }
         *     
         */
        public void setThoroughfarePreDirection(ThoroughfarePreDirectionType value) {
            this.thoroughfarePreDirection = value;
        }

        /**
         * Gets the value of the thoroughfareLeadingType property.
         * 
         * @return
         *     possible object is
         *     {@link ThoroughfareLeadingTypeType }
         *     
         */
        public ThoroughfareLeadingTypeType getThoroughfareLeadingType() {
            return thoroughfareLeadingType;
        }

        /**
         * Sets the value of the thoroughfareLeadingType property.
         * 
         * @param value
         *     allowed object is
         *     {@link ThoroughfareLeadingTypeType }
         *     
         */
        public void setThoroughfareLeadingType(ThoroughfareLeadingTypeType value) {
            this.thoroughfareLeadingType = value;
        }

        /**
         * Gets the value of the thoroughfareNames property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the thoroughfareNames property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getThoroughfareNames().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ThoroughfareNameType }
         * 
         * 
         */
        public List<ThoroughfareNameType> getThoroughfareNames() {
            if (thoroughfareNames == null) {
                thoroughfareNames = new ArrayList<ThoroughfareNameType>();
            }
            return this.thoroughfareNames;
        }

        /**
         * Gets the value of the thoroughfareTrailingType property.
         * 
         * @return
         *     possible object is
         *     {@link ThoroughfareTrailingTypeType }
         *     
         */
        public ThoroughfareTrailingTypeType getThoroughfareTrailingType() {
            return thoroughfareTrailingType;
        }

        /**
         * Sets the value of the thoroughfareTrailingType property.
         * 
         * @param value
         *     allowed object is
         *     {@link ThoroughfareTrailingTypeType }
         *     
         */
        public void setThoroughfareTrailingType(ThoroughfareTrailingTypeType value) {
            this.thoroughfareTrailingType = value;
        }

        /**
         * Gets the value of the thoroughfarePostDirection property.
         * 
         * @return
         *     possible object is
         *     {@link ThoroughfarePostDirectionType }
         *     
         */
        public ThoroughfarePostDirectionType getThoroughfarePostDirection() {
            return thoroughfarePostDirection;
        }

        /**
         * Sets the value of the thoroughfarePostDirection property.
         * 
         * @param value
         *     allowed object is
         *     {@link ThoroughfarePostDirectionType }
         *     
         */
        public void setThoroughfarePostDirection(ThoroughfarePostDirectionType value) {
            this.thoroughfarePostDirection = value;
        }

        /**
         * Gets the value of the anies property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the anies property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnies().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Object }
         * 
         * 
         */
        public List<Object> getAnies() {
            if (anies == null) {
                anies = new ArrayList<Object>();
            }
            return this.anies;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }
    }

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
     *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="ThoroughfareNumberFrom">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber" maxOccurs="unbounded"/>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
     *                 &lt;anyAttribute namespace='##other'/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ThoroughfareNumberTo">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber" maxOccurs="unbounded"/>
     *                   &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
     *                 &lt;anyAttribute namespace='##other'/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
     *       &lt;attribute name="RangeType">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *             &lt;enumeration value="Odd"/>
     *             &lt;enumeration value="Even"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="Indicator" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="Separator" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="IndicatorOccurrence">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *             &lt;enumeration value="Before"/>
     *             &lt;enumeration value="After"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="NumberRangeOccurrence">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *             &lt;enumeration value="BeforeName"/>
     *             &lt;enumeration value="AfterName"/>
     *             &lt;enumeration value="BeforeType"/>
     *             &lt;enumeration value="AfterType"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;anyAttribute namespace='##other'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "addressLines", "thoroughfareNumberFrom", "thoroughfareNumberTo" })
    public static class ThoroughfareNumberRange {

        @XmlElement(name = "AddressLine")
        protected List<AddressLine> addressLines;

        @XmlElement(name = "ThoroughfareNumberFrom", required = true)
        protected Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberFrom thoroughfareNumberFrom;

        @XmlElement(name = "ThoroughfareNumberTo", required = true)
        protected Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberTo thoroughfareNumberTo;

        @XmlAttribute(name = "RangeType")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String rangeType;

        @XmlAttribute(name = "Indicator")
        @XmlSchemaType(name = "anySimpleType")
        protected String indicator;

        @XmlAttribute(name = "Separator")
        @XmlSchemaType(name = "anySimpleType")
        protected String separator;

        @XmlAttribute(name = "IndicatorOccurrence")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String indicatorOccurrence;

        @XmlAttribute(name = "NumberRangeOccurrence")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String numberRangeOccurrence;

        @XmlAttribute(name = "Type")
        @XmlSchemaType(name = "anySimpleType")
        protected String type;

        @XmlAttribute(name = "Code")
        @XmlSchemaType(name = "anySimpleType")
        protected String code;

        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets the value of the addressLines property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the addressLines property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAddressLines().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AddressLine }
         * 
         * 
         */
        public List<AddressLine> getAddressLines() {
            if (addressLines == null) {
                addressLines = new ArrayList<AddressLine>();
            }
            return this.addressLines;
        }

        /**
         * Gets the value of the thoroughfareNumberFrom property.
         * 
         * @return
         *     possible object is
         *     {@link Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberFrom }
         *     
         */
        public Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberFrom getThoroughfareNumberFrom() {
            return thoroughfareNumberFrom;
        }

        /**
         * Sets the value of the thoroughfareNumberFrom property.
         * 
         * @param value
         *     allowed object is
         *     {@link Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberFrom }
         *     
         */
        public void setThoroughfareNumberFrom(Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberFrom value) {
            this.thoroughfareNumberFrom = value;
        }

        /**
         * Gets the value of the thoroughfareNumberTo property.
         * 
         * @return
         *     possible object is
         *     {@link Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberTo }
         *     
         */
        public Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberTo getThoroughfareNumberTo() {
            return thoroughfareNumberTo;
        }

        /**
         * Sets the value of the thoroughfareNumberTo property.
         * 
         * @param value
         *     allowed object is
         *     {@link Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberTo }
         *     
         */
        public void setThoroughfareNumberTo(Thoroughfare.ThoroughfareNumberRange.ThoroughfareNumberTo value) {
            this.thoroughfareNumberTo = value;
        }

        /**
         * Gets the value of the rangeType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRangeType() {
            return rangeType;
        }

        /**
         * Sets the value of the rangeType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRangeType(String value) {
            this.rangeType = value;
        }

        /**
         * Gets the value of the indicator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicator() {
            return indicator;
        }

        /**
         * Sets the value of the indicator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicator(String value) {
            this.indicator = value;
        }

        /**
         * Gets the value of the separator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSeparator() {
            return separator;
        }

        /**
         * Sets the value of the separator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSeparator(String value) {
            this.separator = value;
        }

        /**
         * Gets the value of the indicatorOccurrence property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndicatorOccurrence() {
            return indicatorOccurrence;
        }

        /**
         * Sets the value of the indicatorOccurrence property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndicatorOccurrence(String value) {
            this.indicatorOccurrence = value;
        }

        /**
         * Gets the value of the numberRangeOccurrence property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumberRangeOccurrence() {
            return numberRangeOccurrence;
        }

        /**
         * Sets the value of the numberRangeOccurrence property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumberRangeOccurrence(String value) {
            this.numberRangeOccurrence = value;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCode(String value) {
            this.code = value;
        }

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }

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
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber" maxOccurs="unbounded"/>
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
         *       &lt;anyAttribute namespace='##other'/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "content" })
        public static class ThoroughfareNumberFrom {

            @XmlElementRefs({ @XmlElementRef(name = "ThoroughfareNumber", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = ThoroughfareNumber.class), @XmlElementRef(name = "ThoroughfareNumberSuffix", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = ThoroughfareNumberSuffix.class), @XmlElementRef(name = "AddressLine", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = AddressLine.class), @XmlElementRef(name = "ThoroughfareNumberPrefix", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = ThoroughfareNumberPrefix.class) })
            @XmlMixed
            protected List<Object> content;

            @XmlAttribute(name = "Code")
            @XmlSchemaType(name = "anySimpleType")
            protected String code;

            @XmlAnyAttribute
            private Map<QName, String> otherAttributes = new HashMap<QName, String>();

            /**
             * Gets the value of the content property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the content property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getContent().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link AddressLine }
             * {@link ThoroughfareNumberPrefix }
             * {@link String }
             * {@link ThoroughfareNumber }
             * {@link ThoroughfareNumberSuffix }
             * 
             * 
             */
            public List<Object> getContent() {
                if (content == null) {
                    content = new ArrayList<Object>();
                }
                return this.content;
            }

            /**
             * Gets the value of the code property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCode() {
                return code;
            }

            /**
             * Sets the value of the code property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCode(String value) {
                this.code = value;
            }

            /**
             * Gets a map that contains attributes that aren't bound to any typed property on this class.
             * 
             * <p>
             * the map is keyed by the name of the attribute and 
             * the value is the string value of the attribute.
             * 
             * the map returned by this method is live, and you can add new attribute
             * by updating the map directly. Because of this design, there's no setter.
             * 
             * 
             * @return
             *     always non-null
             */
            public Map<QName, String> getOtherAttributes() {
                return otherAttributes;
            }
        }

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
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}AddressLine" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberPrefix" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumber" maxOccurs="unbounded"/>
         *         &lt;element ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}ThoroughfareNumberSuffix" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:xsdschema:xAL:2.0}grPostal"/>
         *       &lt;anyAttribute namespace='##other'/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "content" })
        public static class ThoroughfareNumberTo {

            @XmlElementRefs({ @XmlElementRef(name = "ThoroughfareNumber", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = ThoroughfareNumber.class), @XmlElementRef(name = "ThoroughfareNumberSuffix", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = ThoroughfareNumberSuffix.class), @XmlElementRef(name = "AddressLine", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = AddressLine.class), @XmlElementRef(name = "ThoroughfareNumberPrefix", namespace = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0", type = ThoroughfareNumberPrefix.class) })
            @XmlMixed
            protected List<Object> content;

            @XmlAttribute(name = "Code")
            @XmlSchemaType(name = "anySimpleType")
            protected String code;

            @XmlAnyAttribute
            private Map<QName, String> otherAttributes = new HashMap<QName, String>();

            /**
             * Gets the value of the content property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the content property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getContent().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link AddressLine }
             * {@link ThoroughfareNumberPrefix }
             * {@link String }
             * {@link ThoroughfareNumber }
             * {@link ThoroughfareNumberSuffix }
             * 
             * 
             */
            public List<Object> getContent() {
                if (content == null) {
                    content = new ArrayList<Object>();
                }
                return this.content;
            }

            /**
             * Gets the value of the code property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCode() {
                return code;
            }

            /**
             * Sets the value of the code property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCode(String value) {
                this.code = value;
            }

            /**
             * Gets a map that contains attributes that aren't bound to any typed property on this class.
             * 
             * <p>
             * the map is keyed by the name of the attribute and 
             * the value is the string value of the attribute.
             * 
             * the map returned by this method is live, and you can add new attribute
             * by updating the map directly. Because of this design, there's no setter.
             * 
             * 
             * @return
             *     always non-null
             */
            public Map<QName, String> getOtherAttributes() {
                return otherAttributes;
            }
        }
    }
}
