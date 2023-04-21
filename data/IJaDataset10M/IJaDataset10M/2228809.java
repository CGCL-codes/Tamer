package org.amhm.core.schemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for preferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="preferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://amhm.org/core/schemas}searches"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "preferenceType", propOrder = { "printer", "searches" })
public class PreferenceType {

    @XmlElement(required = true)
    protected PrinterPref printer;

    @XmlElement(required = true)
    protected SearchesType searches;

    /**
     * Gets the value of the searches property.
     * 
     * @return
     *     possible object is
     *     {@link SearchesType }
     *     
     */
    public SearchesType getSearches() {
        return searches;
    }

    /**
     * Sets the value of the searches property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchesType }
     *     
     */
    public void setSearches(SearchesType value) {
        this.searches = value;
    }

    public PrinterPref getPrinter() {
        return printer;
    }

    public void setPrinter(PrinterPref printer) {
        this.printer = printer;
    }
}
