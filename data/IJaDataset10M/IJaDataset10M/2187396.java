package org.slasoi.monitoring.manageability.xml.eventformat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for regulationList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="regulationList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="regulatory" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="regOrganisation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="regReference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "regulationList", propOrder = { "regulatory" })
public class RegulationList {

    protected List<RegulationList.Regulatory> regulatory;

    /**
     * Gets the value of the regulatory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regulatory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegulatory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegulationList.Regulatory }
     * 
     * 
     */
    public List<RegulationList.Regulatory> getRegulatory() {
        if (regulatory == null) {
            regulatory = new ArrayList<RegulationList.Regulatory>();
        }
        return this.regulatory;
    }

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
     *         &lt;element name="regOrganisation" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="regReference" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "regOrganisation", "regReference" })
    public static class Regulatory {

        @XmlElement(required = true)
        protected String regOrganisation;

        @XmlElement(required = true)
        protected String regReference;

        /**
         * Gets the value of the regOrganisation property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegOrganisation() {
            return regOrganisation;
        }

        /**
         * Sets the value of the regOrganisation property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegOrganisation(String value) {
            this.regOrganisation = value;
        }

        /**
         * Gets the value of the regReference property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegReference() {
            return regReference;
        }

        /**
         * Sets the value of the regReference property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegReference(String value) {
            this.regReference = value;
        }
    }
}
