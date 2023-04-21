package uk.org.ogsadai.service.rest.drer.jaxb.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workflow" type="{http://ogsadai.org.uk/namespaces/2007/04/types}WorkflowType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestType", propOrder = { "workflow" })
public class RequestType {

    @XmlElement(required = true)
    protected WorkflowType workflow;

    /**
     * Gets the value of the workflow property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowType }
     *     
     */
    public WorkflowType getWorkflow() {
        return workflow;
    }

    /**
     * Sets the value of the workflow property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowType }
     *     
     */
    public void setWorkflow(WorkflowType value) {
        this.workflow = value;
    }
}
