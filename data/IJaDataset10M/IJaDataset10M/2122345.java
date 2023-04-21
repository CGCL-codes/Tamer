package com.columboid.protocol.syncml.representation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}CmdID"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}NoResp" minOccurs="0"/>
 *         &lt;element ref="{http://protocol.columboid.com/syncml/representation}Meta" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Add"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Replace"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Delete"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Copy"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Atomic"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Map"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Sequence"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Sync"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Get"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Exec"/>
 *           &lt;element ref="{http://protocol.columboid.com/syncml/representation}Alert"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cmdID", "noResp", "meta", "addOrReplaceOrDelete" })
@XmlRootElement(name = "Atomic")
public class Atomic {

    @XmlElement(name = "CmdID", required = true)
    protected String cmdID;

    @XmlElement(name = "NoResp")
    protected NoResp noResp;

    @XmlElement(name = "Meta")
    protected String meta;

    @XmlElements({ @XmlElement(name = "Atomic", type = Atomic.class), @XmlElement(name = "Copy", type = Copy.class), @XmlElement(name = "Add", type = Add.class), @XmlElement(name = "Sync", type = Sync.class), @XmlElement(name = "Get", type = Get.class), @XmlElement(name = "Map", type = Map.class), @XmlElement(name = "Delete", type = Delete.class), @XmlElement(name = "Alert", type = Alert.class), @XmlElement(name = "Sequence", type = Sequence.class), @XmlElement(name = "Exec", type = Exec.class), @XmlElement(name = "Replace", type = Replace.class) })
    protected List<Object> addOrReplaceOrDelete;

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
     * Gets the value of the addOrReplaceOrDelete property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addOrReplaceOrDelete property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddOrReplaceOrDelete().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Atomic }
     * {@link Copy }
     * {@link Add }
     * {@link Sync }
     * {@link Get }
     * {@link Map }
     * {@link Delete }
     * {@link Alert }
     * {@link Sequence }
     * {@link Exec }
     * {@link Replace }
     * 
     * 
     */
    public List<Object> getAddOrReplaceOrDelete() {
        if (addOrReplaceOrDelete == null) {
            addOrReplaceOrDelete = new ArrayList<Object>();
        }
        return this.addOrReplaceOrDelete;
    }
}
