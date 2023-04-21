package com.columboid.protocol.syncml.representation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}CmdID"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}NoResp" minOccurs="0"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}Archive" minOccurs="0"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}SftDel" minOccurs="0"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}Cred" minOccurs="0"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}Meta" minOccurs="0"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}Item" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cmdID", "noResp", "archive", "sftDel", "cred", "meta", "item" })
@XmlRootElement(name = "Delete")
public class Delete {

    @XmlElement(name = "CmdID", required = true)
    protected String cmdID;

    @XmlElement(name = "NoResp")
    protected NoResp noResp;

    @XmlElement(name = "Archive")
    protected Archive archive;

    @XmlElement(name = "SftDel")
    protected SftDel sftDel;

    @XmlElement(name = "Cred")
    protected Cred cred;

    @XmlElement(name = "Meta")
    protected String meta;

    @XmlElement(name = "Item", required = true)
    protected List<Item> item;

    /**
     * Gets the value of the cmdID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdID() {
        return cmdID;
    }

    /**
     * Sets the value of the cmdID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdID(String value) {
        this.cmdID = value;
    }

    /**
     * Gets the value of the noResp property.
     * 
     * @return
     *     possible object is
     *     {@link NoResp }
     *     
     */
    public NoResp getNoResp() {
        return noResp;
    }

    /**
     * Sets the value of the noResp property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoResp }
     *     
     */
    public void setNoResp(NoResp value) {
        this.noResp = value;
    }

    /**
     * Gets the value of the archive property.
     * 
     * @return
     *     possible object is
     *     {@link Archive }
     *     
     */
    public Archive getArchive() {
        return archive;
    }

    /**
     * Sets the value of the archive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Archive }
     *     
     */
    public void setArchive(Archive value) {
        this.archive = value;
    }

    /**
     * Gets the value of the sftDel property.
     * 
     * @return
     *     possible object is
     *     {@link SftDel }
     *     
     */
    public SftDel getSftDel() {
        return sftDel;
    }

    /**
     * Sets the value of the sftDel property.
     * 
     * @param value
     *     allowed object is
     *     {@link SftDel }
     *     
     */
    public void setSftDel(SftDel value) {
        this.sftDel = value;
    }

    /**
     * Gets the value of the cred property.
     * 
     * @return
     *     possible object is
     *     {@link Cred }
     *     
     */
    public Cred getCred() {
        return cred;
    }

    /**
     * Sets the value of the cred property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cred }
     *     
     */
    public void setCred(Cred value) {
        this.cred = value;
    }

    /**
     * Gets the value of the meta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeta() {
        return meta;
    }

    /**
     * Sets the value of the meta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeta(String value) {
        this.meta = value;
    }

    /**
     * Gets the value of the item property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the item property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Item }
     * 
     * 
     */
    public List<Item> getItem() {
        if (item == null) {
            item = new ArrayList<Item>();
        }
        return this.item;
    }
}
