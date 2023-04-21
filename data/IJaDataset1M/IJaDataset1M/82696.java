package org.idmlinitiative.resources.dtds.aida22;

/**
 * Java content class for categoryType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/mihai/programs/jwsdp13/jaxb/bin/aida22.xsd line 300)
 * <p>
 * <pre>
 * &lt;complexType name="categoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="category" type="{http://www.idmlinitiative.org/resources/dtds/AIDA22.xsd}freeTextType"/>
 *         &lt;element name="catTotal" type="{http://www.idmlinitiative.org/resources/dtds/AIDA22.xsd}moneyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="catYearly" type="{http://www.idmlinitiative.org/resources/dtds/AIDA22.xsd}yearlyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CategoryType {

    /**
     * Gets the value of the CatYearly property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the CatYearly property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCatYearly().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link org.idmlinitiative.resources.dtds.aida22.YearlyType}
     * 
     */
    java.util.List getCatYearly();

    /**
     * Gets the value of the CatTotal property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the CatTotal property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCatTotal().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link org.idmlinitiative.resources.dtds.aida22.MoneyType}
     * 
     */
    java.util.List getCatTotal();

    /**
     * 
     * @return
     *     possible object is
     *     {@link org.idmlinitiative.resources.dtds.aida22.FreeTextType}
     */
    org.idmlinitiative.resources.dtds.aida22.FreeTextType getCategory();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link org.idmlinitiative.resources.dtds.aida22.FreeTextType}
     */
    void setCategory(org.idmlinitiative.resources.dtds.aida22.FreeTextType value);
}
