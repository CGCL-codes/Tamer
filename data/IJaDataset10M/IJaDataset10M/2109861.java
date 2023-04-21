package org.wybecom.talkportal.cti.stateserver;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for LineControlConnection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LineControlConnection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="terminalState" type="{http://wybecom.org/talkportal/cti/stateserver}TerminalState"/>
 *         &lt;element name="state" type="{http://wybecom.org/talkportal/cti/stateserver}ConnectionState"/>
 *         &lt;element name="remoteState" type="{http://wybecom.org/talkportal/cti/stateserver}ConnectionState"/>
 *         &lt;element name="contact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LineControlConnection", propOrder = { "terminalState", "state", "remoteState", "contact", "callid" })
public class LineControlConnection {

    @XmlElement(required = true)
    protected TerminalState terminalState;

    @XmlElement(required = true)
    protected ConnectionState state;

    @XmlElement(required = true)
    protected ConnectionState remoteState;

    protected String contact;

    protected String callid;

    /**
     * Gets the value of the terminalState property.
     * 
     * @return
     *     possible object is
     *     {@link TerminalState }
     *     
     */
    public TerminalState getTerminalState() {
        return terminalState;
    }

    /**
     * Sets the value of the terminalState property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminalState }
     *     
     */
    public void setTerminalState(TerminalState value) {
        this.terminalState = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionState }
     *     
     */
    public ConnectionState getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionState }
     *     
     */
    public void setState(ConnectionState value) {
        this.state = value;
    }

    /**
     * Gets the value of the remoteState property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionState }
     *     
     */
    public ConnectionState getRemoteState() {
        return remoteState;
    }

    /**
     * Sets the value of the remoteState property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionState }
     *     
     */
    public void setRemoteState(ConnectionState value) {
        this.remoteState = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContact(String value) {
        this.contact = value;
    }

    /**
     * Gets the value of the callid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallid() {
        return callid;
    }

    /**
     * Sets the value of the callid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallid(String value) {
        this.callid = value;
    }
}
