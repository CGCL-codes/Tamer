package org.tolven.ccr;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AuthorizationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuthorizationType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:astm-org:CCR}CCRCodedDataObjectType">
 *       &lt;sequence>
 *         &lt;element name="Purpose" type="{urn:astm-org:CCR}PurposeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}Practitioners" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}Procedures" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}Products" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}Medications" minOccurs="0"/>
 *         &lt;element name="Immunizations" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Immunization" type="{urn:astm-org:CCR}StructuredProductType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{urn:astm-org:CCR}Services" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}Encounters" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthorizationType", propOrder = { "purpose", "practitioners", "procedures", "products", "medications", "immunizations", "services", "encounters" })
public class AuthorizationType extends CCRCodedDataObjectType {

    @XmlElement(name = "Purpose")
    protected List<PurposeType> purpose;

    @XmlElement(name = "Practitioners")
    protected Practitioners practitioners;

    @XmlElement(name = "Procedures")
    protected Procedures procedures;

    @XmlElement(name = "Products")
    protected Products products;

    @XmlElement(name = "Medications")
    protected Medications medications;

    @XmlElement(name = "Immunizations")
    protected AuthorizationType.Immunizations immunizations;

    @XmlElement(name = "Services")
    protected Services services;

    @XmlElement(name = "Encounters")
    protected Encounters encounters;

    /**
     * Gets the value of the purpose property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the purpose property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPurpose().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PurposeType }
     * 
     * 
     */
    public List<PurposeType> getPurpose() {
        if (purpose == null) {
            purpose = new ArrayList<PurposeType>();
        }
        return this.purpose;
    }

    /**
     * Gets the value of the practitioners property.
     * 
     * @return
     *     possible object is
     *     {@link Practitioners }
     *     
     */
    public Practitioners getPractitioners() {
        return practitioners;
    }

    /**
     * Sets the value of the practitioners property.
     * 
     * @param value
     *     allowed object is
     *     {@link Practitioners }
     *     
     */
    public void setPractitioners(Practitioners value) {
        this.practitioners = value;
    }

    /**
     * Gets the value of the procedures property.
     * 
     * @return
     *     possible object is
     *     {@link Procedures }
     *     
     */
    public Procedures getProcedures() {
        return procedures;
    }

    /**
     * Sets the value of the procedures property.
     * 
     * @param value
     *     allowed object is
     *     {@link Procedures }
     *     
     */
    public void setProcedures(Procedures value) {
        this.procedures = value;
    }

    /**
     * Gets the value of the products property.
     * 
     * @return
     *     possible object is
     *     {@link Products }
     *     
     */
    public Products getProducts() {
        return products;
    }

    /**
     * Sets the value of the products property.
     * 
     * @param value
     *     allowed object is
     *     {@link Products }
     *     
     */
    public void setProducts(Products value) {
        this.products = value;
    }

    /**
     * Gets the value of the medications property.
     * 
     * @return
     *     possible object is
     *     {@link Medications }
     *     
     */
    public Medications getMedications() {
        return medications;
    }

    /**
     * Sets the value of the medications property.
     * 
     * @param value
     *     allowed object is
     *     {@link Medications }
     *     
     */
    public void setMedications(Medications value) {
        this.medications = value;
    }

    /**
     * Gets the value of the immunizations property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorizationType.Immunizations }
     *     
     */
    public AuthorizationType.Immunizations getImmunizations() {
        return immunizations;
    }

    /**
     * Sets the value of the immunizations property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorizationType.Immunizations }
     *     
     */
    public void setImmunizations(AuthorizationType.Immunizations value) {
        this.immunizations = value;
    }

    /**
     * Gets the value of the services property.
     * 
     * @return
     *     possible object is
     *     {@link Services }
     *     
     */
    public Services getServices() {
        return services;
    }

    /**
     * Sets the value of the services property.
     * 
     * @param value
     *     allowed object is
     *     {@link Services }
     *     
     */
    public void setServices(Services value) {
        this.services = value;
    }

    /**
     * Gets the value of the encounters property.
     * 
     * @return
     *     possible object is
     *     {@link Encounters }
     *     
     */
    public Encounters getEncounters() {
        return encounters;
    }

    /**
     * Sets the value of the encounters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Encounters }
     *     
     */
    public void setEncounters(Encounters value) {
        this.encounters = value;
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
     *         &lt;element name="Immunization" type="{urn:astm-org:CCR}StructuredProductType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "immunization" })
    public static class Immunizations {

        @XmlElement(name = "Immunization", required = true)
        protected List<StructuredProductType> immunization;

        /**
         * Gets the value of the immunization property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the immunization property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImmunization().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StructuredProductType }
         * 
         * 
         */
        public List<StructuredProductType> getImmunization() {
            if (immunization == null) {
                immunization = new ArrayList<StructuredProductType>();
            }
            return this.immunization;
        }
    }
}
