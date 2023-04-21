package net.sf.istcontract.aws.input.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ManagerList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManagerList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CommunicationManager" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ContractManager" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DecisionMaker" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DialogueManager" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageManager" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkflowManager" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManagerList", propOrder = { "communicationManager", "contractManager", "decisionMaker", "dialogueManager", "messageManager", "workflowManager" })
public class ManagerList {

    @XmlElement(name = "CommunicationManager", required = true, defaultValue = "CommunicationManager")
    protected String communicationManager;

    @XmlElement(name = "ContractManager", required = true, defaultValue = "ContractManager")
    protected String contractManager;

    @XmlElement(name = "DecisionMaker", required = true, defaultValue = "DecisionMaker")
    protected String decisionMaker;

    @XmlElement(name = "DialogueManager", required = true, defaultValue = "DialogueManager")
    protected String dialogueManager;

    @XmlElement(name = "MessageManager", required = true, defaultValue = "MessageManager")
    protected String messageManager;

    @XmlElement(name = "WorkflowManager", required = true, defaultValue = "WorkflowManager")
    protected String workflowManager;

    /**
     * Gets the value of the communicationManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommunicationManager() {
        return communicationManager;
    }

    /**
     * Sets the value of the communicationManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommunicationManager(String value) {
        this.communicationManager = value;
    }

    /**
     * Gets the value of the contractManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractManager() {
        return contractManager;
    }

    /**
     * Sets the value of the contractManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractManager(String value) {
        this.contractManager = value;
    }

    /**
     * Gets the value of the decisionMaker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecisionMaker() {
        return decisionMaker;
    }

    /**
     * Sets the value of the decisionMaker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecisionMaker(String value) {
        this.decisionMaker = value;
    }

    /**
     * Gets the value of the dialogueManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDialogueManager() {
        return dialogueManager;
    }

    /**
     * Sets the value of the dialogueManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDialogueManager(String value) {
        this.dialogueManager = value;
    }

    /**
     * Gets the value of the messageManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageManager() {
        return messageManager;
    }

    /**
     * Sets the value of the messageManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageManager(String value) {
        this.messageManager = value;
    }

    /**
     * Gets the value of the workflowManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkflowManager() {
        return workflowManager;
    }

    /**
     * Sets the value of the workflowManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkflowManager(String value) {
        this.workflowManager = value;
    }
}
