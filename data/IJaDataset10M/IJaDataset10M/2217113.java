package com.ekeyman.shared.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="OperationSuccessful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {  })
@XmlRootElement(name = "DeleteVendorResponse")
public class DeleteVendorResponse {

    @XmlElement(name = "OperationSuccessful")
    protected boolean operationSuccessful;

    /**
     * Gets the value of the operationSuccessful property.
     * 
     */
    public boolean isOperationSuccessful() {
        return operationSuccessful;
    }

    /**
     * Sets the value of the operationSuccessful property.
     * 
     */
    public void setOperationSuccessful(boolean value) {
        this.operationSuccessful = value;
    }
}
