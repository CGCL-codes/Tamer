package uk.icat3.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for datafileParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="datafileParameter">
 *   &lt;complexContent>
 *     &lt;extension base="{client.icat3.uk}entityBaseBean">
 *       &lt;sequence>
 *         &lt;element name="datafileParameterPK" type="{client.icat3.uk}datafileParameterPK" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeric" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="numericValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="rangeBottom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rangeTop" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stringValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datafileParameter", propOrder = { "datafileParameterPK", "description", "error", "numeric", "numericValue", "rangeBottom", "rangeTop", "stringValue" })
public class DatafileParameter extends EntityBaseBean {

    protected DatafileParameterPK datafileParameterPK;

    protected String description;

    protected String error;

    protected boolean numeric;

    protected Double numericValue;

    protected String rangeBottom;

    protected String rangeTop;

    protected String stringValue;

    /**
     * Gets the value of the datafileParameterPK property.
     * 
     * @return
     *     possible object is
     *     {@link DatafileParameterPK }
     *     
     */
    public DatafileParameterPK getDatafileParameterPK() {
        return datafileParameterPK;
    }

    /**
     * Sets the value of the datafileParameterPK property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatafileParameterPK }
     *     
     */
    public void setDatafileParameterPK(DatafileParameterPK value) {
        this.datafileParameterPK = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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

    /**
     * Gets the value of the numeric property.
     * 
     */
    public boolean isNumeric() {
        return numeric;
    }

    /**
     * Sets the value of the numeric property.
     * 
     */
    public void setNumeric(boolean value) {
        this.numeric = value;
    }

    /**
     * Gets the value of the numericValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getNumericValue() {
        return numericValue;
    }

    /**
     * Sets the value of the numericValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setNumericValue(Double value) {
        this.numericValue = value;
    }

    /**
     * Gets the value of the rangeBottom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRangeBottom() {
        return rangeBottom;
    }

    /**
     * Sets the value of the rangeBottom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeBottom(String value) {
        this.rangeBottom = value;
    }

    /**
     * Gets the value of the rangeTop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRangeTop() {
        return rangeTop;
    }

    /**
     * Sets the value of the rangeTop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeTop(String value) {
        this.rangeTop = value;
    }

    /**
     * Gets the value of the stringValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the value of the stringValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringValue(String value) {
        this.stringValue = value;
    }
}
