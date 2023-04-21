package net.sf.istcontract.aws.observer.ontology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for ContractStatusReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContractStatusReport">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contractId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fulfillmentStatus" type="{ontology.observer.aws.istcontract.sf.net}ContractFulfillmentStatus"/>
 *         &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractStatusReport", propOrder = { "contractId", "fulfillmentStatus", "timeStamp" })
public class ContractStatusReport {

    @XmlElement(required = true)
    protected String contractId;

    @XmlElement(required = true)
    protected ContractFulfillmentStatus fulfillmentStatus;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;

    /**
     * Gets the value of the contractId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * Sets the value of the contractId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractId(String value) {
        this.contractId = value;
    }

    /**
     * Gets the value of the fulfillmentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ContractFulfillmentStatus }
     *     
     */
    public ContractFulfillmentStatus getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    /**
     * Sets the value of the fulfillmentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContractFulfillmentStatus }
     *     
     */
    public void setFulfillmentStatus(ContractFulfillmentStatus value) {
        this.fulfillmentStatus = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
    }
}
