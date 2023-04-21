package edu.upc.lsi.kemlg.aws.knowledge.ontology.contract;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ContractStoreRef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContractStoreRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ContractStoreName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractStoreRef")
public class ContractStoreRef {

    @XmlAttribute(name = "ContractStoreName", required = true)
    protected String contractStoreName;

    /**
     * Gets the value of the contractStoreName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractStoreName() {
        return contractStoreName;
    }

    /**
     * Sets the value of the contractStoreName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractStoreName(String value) {
        this.contractStoreName = value;
    }
}
