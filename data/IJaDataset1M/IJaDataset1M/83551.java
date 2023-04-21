package clients.registryservice.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/08/addressing}EndpointReference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefType", namespace = "http://www.infosys.tuwien.ac.at/ait09/registry", propOrder = { "endpointReference" })
public class RefType {

    @XmlElement(name = "EndpointReference", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing", required = true)
    protected EndpointReferenceType endpointReference;

    /**
     * Gets the value of the endpointReference property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointReferenceType }
     *     
     */
    public EndpointReferenceType getEndpointReference() {
        return endpointReference;
    }

    /**
     * Sets the value of the endpointReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointReferenceType }
     *     
     */
    public void setEndpointReference(EndpointReferenceType value) {
        this.endpointReference = value;
    }
}
