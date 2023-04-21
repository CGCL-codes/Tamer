package org.eucontract.agents.knowledge.ontology.contract;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for sender complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sender">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agent-identifier" type="{}agent-identifier"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sender", propOrder = { "agentIdentifier" })
public class Sender {

    @XmlElement(name = "agent-identifier", required = true)
    protected AgentIdentifier agentIdentifier;

    /**
     * Gets the value of the agentIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link AgentIdentifier }
     *     
     */
    public AgentIdentifier getAgentIdentifier() {
        return agentIdentifier;
    }

    /**
     * Sets the value of the agentIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentIdentifier }
     *     
     */
    public void setAgentIdentifier(AgentIdentifier value) {
        this.agentIdentifier = value;
    }
}
