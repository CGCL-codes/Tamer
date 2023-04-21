package ru.rsdn.janus;

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
 *         &lt;element name="GetNewUsersResult" type="{http://rsdn.ru/Janus/}UserResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getNewUsersResult" })
@XmlRootElement(name = "GetNewUsersResponse")
public class GetNewUsersResponse {

    @XmlElement(name = "GetNewUsersResult")
    protected UserResponse getNewUsersResult;

    /**
     * Gets the value of the getNewUsersResult property.
     * 
     * @return
     *     possible object is
     *     {@link UserResponse }
     *     
     */
    public UserResponse getGetNewUsersResult() {
        return getNewUsersResult;
    }

    /**
     * Sets the value of the getNewUsersResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserResponse }
     *     
     */
    public void setGetNewUsersResult(UserResponse value) {
        this.getNewUsersResult = value;
    }
}
