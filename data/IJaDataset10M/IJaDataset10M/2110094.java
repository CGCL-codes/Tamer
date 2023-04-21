package com.patientis.model.xml;

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
 *       &lt;sequence>
 *         &lt;element name="UserModel" type="{}UserXml"/>
 *         &lt;element name="Transaction" type="{}InterfaceTransaction"/>
 *         &lt;element name="BeforeUserModel" type="{}UserXml"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userModel", "transaction", "beforeUserModel" })
@XmlRootElement(name = "UserTransaction")
public class UserTransaction {

    @XmlElement(name = "UserModel", required = true)
    protected UserXml userModel;

    @XmlElement(name = "Transaction", required = true)
    protected InterfaceTransaction transaction;

    @XmlElement(name = "BeforeUserModel", required = true)
    protected UserXml beforeUserModel;

    /**
     * Gets the value of the userModel property.
     * 
     * @return
     *     possible object is
     *     {@link UserXml }
     *     
     */
    public UserXml getUserModel() {
        return userModel;
    }

    /**
     * Sets the value of the userModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserXml }
     *     
     */
    public void setUserModel(UserXml value) {
        this.userModel = value;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * @return
     *     possible object is
     *     {@link InterfaceTransaction }
     *     
     */
    public InterfaceTransaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the value of the transaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterfaceTransaction }
     *     
     */
    public void setTransaction(InterfaceTransaction value) {
        this.transaction = value;
    }

    /**
     * Gets the value of the beforeUserModel property.
     * 
     * @return
     *     possible object is
     *     {@link UserXml }
     *     
     */
    public UserXml getBeforeUserModel() {
        return beforeUserModel;
    }

    /**
     * Sets the value of the beforeUserModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserXml }
     *     
     */
    public void setBeforeUserModel(UserXml value) {
        this.beforeUserModel = value;
    }
}
