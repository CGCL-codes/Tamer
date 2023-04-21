package edu.upc.lsi.kemlg.aws.gui.client.domain;

import java.io.Serializable;

/**
 * <p>Java class for ConversationPair complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConversationPair">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="participantClassname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="initiatorClassname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class ConversationPair implements Serializable {

    protected String participantClassname;

    protected String initiatorClassname;

    /**
     * Gets the value of the participantClassname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParticipantClassname() {
        return participantClassname;
    }

    /**
     * Sets the value of the participantClassname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParticipantClassname(String value) {
        this.participantClassname = value;
    }

    /**
     * Gets the value of the initiatorClassname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitiatorClassname() {
        return initiatorClassname;
    }

    /**
     * Sets the value of the initiatorClassname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitiatorClassname(String value) {
        this.initiatorClassname = value;
    }
}
