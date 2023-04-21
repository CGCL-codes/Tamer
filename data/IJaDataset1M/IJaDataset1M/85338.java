package net.sublight.webservice;

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
 *         &lt;element name="FindIMDBResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="result" type="{http://www.sublight.si/}ArrayOfIMDB" minOccurs="0"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "findIMDBResult", "result", "error" })
@XmlRootElement(name = "FindIMDBResponse")
public class FindIMDBResponse {

    @XmlElement(name = "FindIMDBResult")
    protected boolean findIMDBResult;

    protected ArrayOfIMDB result;

    protected String error;

    /**
     * Gets the value of the findIMDBResult property.
     * 
     */
    public boolean isFindIMDBResult() {
        return findIMDBResult;
    }

    /**
     * Sets the value of the findIMDBResult property.
     * 
     */
    public void setFindIMDBResult(boolean value) {
        this.findIMDBResult = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfIMDB }
     *     
     */
    public ArrayOfIMDB getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfIMDB }
     *     
     */
    public void setResult(ArrayOfIMDB value) {
        this.result = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }
}
