package net.sf.istcontract.aws.input.contract;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Group complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Group">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="AgentRef" type="{http://ist-contract.org/schema/ISTContract}AgentRef"/>
 *       &lt;/sequence>
 *       &lt;attribute name="GroupName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Group", namespace = "http://ist-contract.org/schema/ISTContract", propOrder = { "agentRef" })
public class Group {

    @XmlElement(name = "AgentRef", required = true)
    protected List<AgentRef> agentRef;

    @XmlAttribute(name = "GroupName", required = true)
    protected String groupName;

    /**
     * Gets the value of the agentRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agentRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgentRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgentRef }
     * 
     * 
     */
    public List<AgentRef> getAgentRef() {
        if (agentRef == null) {
            agentRef = new ArrayList<AgentRef>();
        }
        return this.agentRef;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }
}
