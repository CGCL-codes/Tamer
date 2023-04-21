package ru.rsdn.janus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="changeRequest" type="{http://rsdn.ru/Janus/}ChangeRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "changeRequest" })
@XmlRootElement(name = "GetNewData")
public class GetNewData {

    protected ChangeRequest changeRequest;

    /**
     * Gets the value of the changeRequest property.
     * 
     * @return
     *     possible object is
     *     {@link ChangeRequest }
     *     
     */
    public ChangeRequest getChangeRequest() {
        return changeRequest;
    }

    /**
     * Sets the value of the changeRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangeRequest }
     *     
     */
    public void setChangeRequest(ChangeRequest value) {
        this.changeRequest = value;
    }
}
