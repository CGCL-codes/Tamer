package uk.ac.ebi.rhea.ws.response.cmlreact;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

/**
 * 
 * 					
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;h:div xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.xml-cml.org/schema/cml2/react" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;h:p&gt;The semantics of the content model are&lt;/h:p&gt;&lt;h:ul&gt;&lt;h:li&gt;metadataList for general metadata&lt;/h:li&gt;&lt;h:li&gt;label for classifying or describing the reaction (e.g. "hydrolysis")&lt;/h:li&gt;&lt;h:li&gt;identifier for unique identification. This could be a classification such as EC (enzyme commission) or an IChI-like string generated from the components.&lt;/h:li&gt;&lt;h:li&gt;these are followed by the possible components of the reaction and/or a reactionList of further details.&lt;/h:li&gt;
 * 						&lt;/h:ul&gt;.&lt;/h:div&gt;
 * </pre>
 * 
 * 				
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}metadataList" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}name" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}reactiveCentre" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}mechanism" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}reactantList" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}spectatorList" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}substanceList" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}conditionList" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}transitionState" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}productList" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}propertyList" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}map" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}object" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}reactionRole"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}state"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}electronMap"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}dictRef"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}yield"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}title"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}convention"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}atomMap"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}ref"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}id"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}reactionFormat"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}reactionType"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}bondMap"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metadataList", "label", "nameOrIdentifier", "reactiveCentreAndMechanismAndReactantList" })
@XmlRootElement(name = "reaction")
public class Reaction {

    protected List<MetadataList> metadataList;

    protected List<Label> label;

    @XmlElements({ @XmlElement(name = "name", type = Name.class), @XmlElement(name = "identifier", type = Identifier.class) })
    protected List<java.lang.Object> nameOrIdentifier;

    @XmlElements({ @XmlElement(name = "object", type = uk.ac.ebi.rhea.ws.response.cmlreact.Object.class), @XmlElement(name = "map", type = Map.class), @XmlElement(name = "mechanism", type = Mechanism.class), @XmlElement(name = "transitionState", type = TransitionState.class), @XmlElement(name = "conditionList", type = ConditionList.class), @XmlElement(name = "productList", type = ProductList.class), @XmlElement(name = "reactantList", type = ReactantList.class), @XmlElement(name = "propertyList", type = PropertyList.class), @XmlElement(name = "substanceList", type = SubstanceList.class), @XmlElement(name = "spectatorList", type = SpectatorList.class), @XmlElement(name = "reactiveCentre", type = ReactiveCentre.class) })
    protected List<java.lang.Object> reactiveCentreAndMechanismAndReactantList;

    @XmlAttribute
    protected String role;

    @XmlAttribute
    protected String state;

    @XmlAttribute
    protected QName electronMap;

    @XmlAttribute
    protected String dictRef;

    @XmlAttribute
    protected Float yield;

    @XmlAttribute
    protected String title;

    @XmlAttribute
    protected String convention;

    @XmlAttribute
    protected QName atomMap;

    @XmlAttribute
    protected String ref;

    @XmlAttribute
    protected String id;

    @XmlAttribute
    protected String format;

    @XmlAttribute
    protected String type;

    @XmlAttribute
    protected QName bondMap;

    /**
     * Gets the value of the metadataList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadataList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadataList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadataList }
     * 
     * 
     */
    public List<MetadataList> getMetadataList() {
        if (metadataList == null) {
            metadataList = new ArrayList<MetadataList>();
        }
        return this.metadataList;
    }

    /**
     * Gets the value of the label property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the label property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Label }
     * 
     * 
     */
    public List<Label> getLabel() {
        if (label == null) {
            label = new ArrayList<Label>();
        }
        return this.label;
    }

    /**
     * Gets the value of the nameOrIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nameOrIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNameOrIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Name }
     * {@link Identifier }
     * 
     * 
     */
    public List<java.lang.Object> getNameOrIdentifier() {
        if (nameOrIdentifier == null) {
            nameOrIdentifier = new ArrayList<java.lang.Object>();
        }
        return this.nameOrIdentifier;
    }

    /**
     * Gets the value of the reactiveCentreAndMechanismAndReactantList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reactiveCentreAndMechanismAndReactantList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReactiveCentreAndMechanismAndReactantList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link uk.ac.ebi.rhea.ws.response.cmlreact.Object }
     * {@link Map }
     * {@link Mechanism }
     * {@link TransitionState }
     * {@link ConditionList }
     * {@link ProductList }
     * {@link ReactantList }
     * {@link PropertyList }
     * {@link SubstanceList }
     * {@link SpectatorList }
     * {@link ReactiveCentre }
     * 
     * 
     */
    public List<java.lang.Object> getReactiveCentreAndMechanismAndReactantList() {
        if (reactiveCentreAndMechanismAndReactantList == null) {
            reactiveCentreAndMechanismAndReactantList = new ArrayList<java.lang.Object>();
        }
        return this.reactiveCentreAndMechanismAndReactantList;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the electronMap property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getElectronMap() {
        return electronMap;
    }

    /**
     * Sets the value of the electronMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setElectronMap(QName value) {
        this.electronMap = value;
    }

    /**
     * Gets the value of the dictRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDictRef() {
        return dictRef;
    }

    /**
     * Sets the value of the dictRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDictRef(String value) {
        this.dictRef = value;
    }

    /**
     * Gets the value of the yield property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getYield() {
        return yield;
    }

    /**
     * Sets the value of the yield property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setYield(Float value) {
        this.yield = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the convention property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConvention() {
        return convention;
    }

    /**
     * Sets the value of the convention property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConvention(String value) {
        this.convention = value;
    }

    /**
     * Gets the value of the atomMap property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getAtomMap() {
        return atomMap;
    }

    /**
     * Sets the value of the atomMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setAtomMap(QName value) {
        this.atomMap = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
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
     * Gets the value of the bondMap property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getBondMap() {
        return bondMap;
    }

    /**
     * Sets the value of the bondMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setBondMap(QName value) {
        this.bondMap = value;
    }
}
