package org.mobicents.slee.sippresence.pojo.pidf.geopriv10.basicPolicy;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.Element;

/**
 * <p>Java class for locPolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="locPolicyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="retransmission-allowed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="retention-expiry" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="external-ruleset" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="note-well" type="{urn:ietf:params:xml:ns:pidf:geopriv10:basicPolicy}notewell" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "locPolicyType", propOrder = { "retransmissionAllowed", "retentionExpiry", "externalRuleset", "noteWell", "any" })
public class LocPolicyType {

    @XmlElement(name = "retransmission-allowed")
    protected Boolean retransmissionAllowed;

    @XmlElement(name = "retention-expiry")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar retentionExpiry;

    @XmlElement(name = "external-ruleset")
    @XmlSchemaType(name = "anyURI")
    protected String externalRuleset;

    @XmlElement(name = "note-well")
    protected Notewell noteWell;

    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the retransmissionAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRetransmissionAllowed() {
        return retransmissionAllowed;
    }

    /**
     * Sets the value of the retransmissionAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRetransmissionAllowed(Boolean value) {
        this.retransmissionAllowed = value;
    }

    /**
     * Gets the value of the retentionExpiry property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRetentionExpiry() {
        return retentionExpiry;
    }

    /**
     * Sets the value of the retentionExpiry property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRetentionExpiry(XMLGregorianCalendar value) {
        this.retentionExpiry = value;
    }

    /**
     * Gets the value of the externalRuleset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalRuleset() {
        return externalRuleset;
    }

    /**
     * Sets the value of the externalRuleset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalRuleset(String value) {
        this.externalRuleset = value;
    }

    /**
     * Gets the value of the noteWell property.
     * 
     * @return
     *     possible object is
     *     {@link Notewell }
     *     
     */
    public Notewell getNoteWell() {
        return noteWell;
    }

    /**
     * Sets the value of the noteWell property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notewell }
     *     
     */
    public void setNoteWell(Notewell value) {
        this.noteWell = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }
}
