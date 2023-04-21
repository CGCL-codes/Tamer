package com.hisham.powerpark.ws.misc;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.hisham.powerpark.ws.misc.InsertUpdateNotes;

/**
 * <p>Java class for InsertUpdateNotes element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="InsertUpdateNotes">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="xmldata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "", propOrder = { "xmldata", "version", "username", "password" })
@XmlRootElement(name = "InsertUpdateNotes")
public class InsertUpdateNotes {

    @XmlElement(namespace = "http://www.t2systems.com/")
    protected String xmldata;

    @XmlElement(namespace = "http://www.t2systems.com/")
    protected String version;

    @XmlElement(namespace = "http://www.t2systems.com/")
    protected String username;

    @XmlElement(namespace = "http://www.t2systems.com/")
    protected String password;

    /**
     * Gets the value of the xmldata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmldata() {
        return xmldata;
    }

    /**
     * Sets the value of the xmldata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmldata(String value) {
        this.xmldata = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

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
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }
}
