package org.tolven.api.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for XTolvenUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XTolvenUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userCanCreate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="status" type="{urn:tolven-org:admin:1.0}XStatus" minOccurs="0"/>
 *         &lt;element name="property" type="{urn:tolven-org:admin:1.0}XProperty" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lastLogin" type="{http://www.w3.org/2001/XMLSchema}dateTime" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tolvenPerson" type="{urn:tolven-org:admin:1.0}XTolvenPerson" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="uid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="action" type="{urn:tolven-org:admin:1.0}XAction" />
 *       &lt;attribute name="result" type="{urn:tolven-org:admin:1.0}XResult" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XTolvenUser", propOrder = { "description", "userCanCreate", "status", "properties", "lastLogins", "tolvenPerson" })
public class XTolvenUser implements Serializable {

    protected String description;

    @XmlElement(defaultValue = "false")
    protected Boolean userCanCreate;

    protected XStatus status;

    @XmlElement(name = "property")
    protected List<XProperty> properties;

    @XmlElement(name = "lastLogin", type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected List<Date> lastLogins;

    protected XTolvenPerson tolvenPerson;

    @XmlAttribute
    protected String uid;

    @XmlAttribute
    protected Long id;

    @XmlAttribute
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Date timestamp;

    @XmlAttribute
    protected XAction action;

    @XmlAttribute
    protected XResult result;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the userCanCreate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUserCanCreate() {
        return userCanCreate;
    }

    /**
     * Sets the value of the userCanCreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUserCanCreate(Boolean value) {
        this.userCanCreate = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link XStatus }
     *     
     */
    public XStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link XStatus }
     *     
     */
    public void setStatus(XStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XProperty }
     * 
     * 
     */
    public List<XProperty> getProperties() {
        if (properties == null) {
            properties = new ArrayList<XProperty>();
        }
        return this.properties;
    }

    /**
     * Gets the value of the lastLogins property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lastLogins property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLastLogins().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<Date> getLastLogins() {
        if (lastLogins == null) {
            lastLogins = new ArrayList<Date>();
        }
        return this.lastLogins;
    }

    /**
     * Gets the value of the tolvenPerson property.
     * 
     * @return
     *     possible object is
     *     {@link XTolvenPerson }
     *     
     */
    public XTolvenPerson getTolvenPerson() {
        return tolvenPerson;
    }

    /**
     * Sets the value of the tolvenPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link XTolvenPerson }
     *     
     */
    public void setTolvenPerson(XTolvenPerson value) {
        this.tolvenPerson = value;
    }

    /**
     * Gets the value of the uid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the value of the uid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUid(String value) {
        this.uid = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(Date value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link XAction }
     *     
     */
    public XAction getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link XAction }
     *     
     */
    public void setAction(XAction value) {
        this.action = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link XResult }
     *     
     */
    public XResult getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link XResult }
     *     
     */
    public void setResult(XResult value) {
        this.result = value;
    }
}
