package org.imsglobal.xsd.imslip_v1p0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}comment" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}contentype" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}identification"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}goal"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}qcl"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}activity"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}competency"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}transcript"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}accessibility"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}interest"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}affiliation"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}securitykey"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}relationship"/>
 *           &lt;element ref="{http://www.imsglobal.org/xsd/imslip_v1p0}ext_learnerinfo"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imslip_v1p0}lang.attr"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "comment", "contentype", "identificationOrGoalOrQcl" })
@XmlRootElement(name = "learnerinformation")
public class Learnerinformation {

    protected Comment comment;

    protected Contentype contentype;

    @XmlElements({ @XmlElement(name = "accessibility", type = Accessibility.class), @XmlElement(name = "activity", type = Activity.class), @XmlElement(name = "affiliation", type = Affiliation.class), @XmlElement(name = "securitykey", type = Securitykey.class), @XmlElement(name = "goal", type = Goal.class), @XmlElement(name = "relationship", type = Relationship.class), @XmlElement(name = "competency", type = Competency.class), @XmlElement(name = "ext_learnerinfo"), @XmlElement(name = "transcript", type = Transcript.class), @XmlElement(name = "identification", type = Identification.class), @XmlElement(name = "interest", type = Interest.class), @XmlElement(name = "qcl", type = Qcl.class) })
    protected List<Object> identificationOrGoalOrQcl;

    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang;

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link Comment }
     *     
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comment }
     *     
     */
    public void setComment(Comment value) {
        this.comment = value;
    }

    /**
     * Gets the value of the contentype property.
     * 
     * @return
     *     possible object is
     *     {@link Contentype }
     *     
     */
    public Contentype getContentype() {
        return contentype;
    }

    /**
     * Sets the value of the contentype property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contentype }
     *     
     */
    public void setContentype(Contentype value) {
        this.contentype = value;
    }

    /**
     * Gets the value of the identificationOrGoalOrQcl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificationOrGoalOrQcl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificationOrGoalOrQcl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Accessibility }
     * {@link Activity }
     * {@link Affiliation }
     * {@link Securitykey }
     * {@link Goal }
     * {@link Relationship }
     * {@link Competency }
     * {@link Object }
     * {@link Transcript }
     * {@link Identification }
     * {@link Interest }
     * {@link Qcl }
     * 
     * 
     */
    public List<Object> getIdentificationOrGoalOrQcl() {
        if (identificationOrGoalOrQcl == null) {
            identificationOrGoalOrQcl = new ArrayList<Object>();
        }
        return this.identificationOrGoalOrQcl;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        if (lang == null) {
            return "en";
        } else {
            return lang;
        }
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }
}
