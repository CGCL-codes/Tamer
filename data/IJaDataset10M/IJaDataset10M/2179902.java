package proxymusic;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>Comments from original DTD:
 * <pre>
 * The part-group element indicates groupings of parts in
 * the score, usually indicated by braces and brackets.
 * Braces that are used for multi-staff parts should be
 * defined in the attributes element for that part.
 * 
 * The number attribute is used to distinguish overlapping
 * and nested part-groups, not the sequence of groups. As
 * with parts, groups can have a name and abbreviation.
 * Formatting attributes for group-name and group-abbreviation
 * are deprecated in MusicXML 2.0 in favor of the new
 * group-name-display and group-abbreviation-display elements.
 * Formatting specified in the group-name-display and
 * group-abbreviation-display elements overrides formatting
 * specified in the group-name and group-abbreviation
 * elements, respectively.
 * 
 * The group-symbol element indicates how the symbol for
 * a group is indicated in the score. Values include none,
 * brace, line, and bracket; the default is none. The
 * group-barline element indicates if the group should have
 * common barlines. Values can be yes, no, or Mensurstrich.
 * The group-time element indicates that the displayed time
 * signatures should stretch across all parts and staves in
 * the group. Values for the child elements are ignored at
 * the stop of a group.
 * </pre>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}group-name" minOccurs="0"/>
 *         &lt;element ref="{}group-name-display" minOccurs="0"/>
 *         &lt;element ref="{}group-abbreviation" minOccurs="0"/>
 *         &lt;element ref="{}group-abbreviation-display" minOccurs="0"/>
 *         &lt;element ref="{}group-symbol" minOccurs="0"/>
 *         &lt;element ref="{}group-barline" minOccurs="0"/>
 *         &lt;element ref="{}group-time" minOccurs="0"/>
 *         &lt;sequence>
 *           &lt;element ref="{}footnote" minOccurs="0"/>
 *           &lt;element ref="{}level" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="1" />
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="start"/>
 *             &lt;enumeration value="stop"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "groupName", "groupNameDisplay", "groupAbbreviation", "groupAbbreviationDisplay", "groupSymbol", "groupBarline", "groupTime", "footnote", "level" })
@XmlRootElement(name = "part-group")
public class PartGroup implements Serializable {

    @XmlElement(name = "group-name")
    protected GroupName groupName;

    @XmlElement(name = "group-name-display")
    protected GroupNameDisplay groupNameDisplay;

    @XmlElement(name = "group-abbreviation")
    protected GroupAbbreviation groupAbbreviation;

    @XmlElement(name = "group-abbreviation-display")
    protected GroupAbbreviationDisplay groupAbbreviationDisplay;

    @XmlElement(name = "group-symbol")
    protected GroupSymbol groupSymbol;

    @XmlElement(name = "group-barline")
    protected GroupBarline groupBarline;

    @XmlElement(name = "group-time")
    protected GroupTime groupTime;

    protected Footnote footnote;

    protected Level level;

    @XmlAttribute
    protected java.lang.String number;

    @XmlAttribute(required = true)
    protected java.lang.String type;

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link GroupName }
     *     
     */
    public GroupName getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupName }
     *     
     */
    public void setGroupName(GroupName value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the groupNameDisplay property.
     * 
     * @return
     *     possible object is
     *     {@link GroupNameDisplay }
     *     
     */
    public GroupNameDisplay getGroupNameDisplay() {
        return groupNameDisplay;
    }

    /**
     * Sets the value of the groupNameDisplay property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupNameDisplay }
     *     
     */
    public void setGroupNameDisplay(GroupNameDisplay value) {
        this.groupNameDisplay = value;
    }

    /**
     * Gets the value of the groupAbbreviation property.
     * 
     * @return
     *     possible object is
     *     {@link GroupAbbreviation }
     *     
     */
    public GroupAbbreviation getGroupAbbreviation() {
        return groupAbbreviation;
    }

    /**
     * Sets the value of the groupAbbreviation property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupAbbreviation }
     *     
     */
    public void setGroupAbbreviation(GroupAbbreviation value) {
        this.groupAbbreviation = value;
    }

    /**
     * Gets the value of the groupAbbreviationDisplay property.
     * 
     * @return
     *     possible object is
     *     {@link GroupAbbreviationDisplay }
     *     
     */
    public GroupAbbreviationDisplay getGroupAbbreviationDisplay() {
        return groupAbbreviationDisplay;
    }

    /**
     * Sets the value of the groupAbbreviationDisplay property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupAbbreviationDisplay }
     *     
     */
    public void setGroupAbbreviationDisplay(GroupAbbreviationDisplay value) {
        this.groupAbbreviationDisplay = value;
    }

    /**
     * Gets the value of the groupSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link GroupSymbol }
     *     
     */
    public GroupSymbol getGroupSymbol() {
        return groupSymbol;
    }

    /**
     * Sets the value of the groupSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupSymbol }
     *     
     */
    public void setGroupSymbol(GroupSymbol value) {
        this.groupSymbol = value;
    }

    /**
     * Gets the value of the groupBarline property.
     * 
     * @return
     *     possible object is
     *     {@link GroupBarline }
     *     
     */
    public GroupBarline getGroupBarline() {
        return groupBarline;
    }

    /**
     * Sets the value of the groupBarline property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupBarline }
     *     
     */
    public void setGroupBarline(GroupBarline value) {
        this.groupBarline = value;
    }

    /**
     * Gets the value of the groupTime property.
     * 
     * @return
     *     possible object is
     *     {@link GroupTime }
     *     
     */
    public GroupTime getGroupTime() {
        return groupTime;
    }

    /**
     * Sets the value of the groupTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupTime }
     *     
     */
    public void setGroupTime(GroupTime value) {
        this.groupTime = value;
    }

    /**
     * Gets the value of the footnote property.
     * 
     * @return
     *     possible object is
     *     {@link Footnote }
     *     
     */
    public Footnote getFootnote() {
        return footnote;
    }

    /**
     * Sets the value of the footnote property.
     * 
     * @param value
     *     allowed object is
     *     {@link Footnote }
     *     
     */
    public void setFootnote(Footnote value) {
        this.footnote = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Level }
     *     
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Level }
     *     
     */
    public void setLevel(Level value) {
        this.level = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getNumber() {
        if (number == null) {
            return "1";
        } else {
            return number;
        }
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setNumber(java.lang.String value) {
        this.number = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setType(java.lang.String value) {
        this.type = value;
    }
}
