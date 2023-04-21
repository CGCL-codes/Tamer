package org.idmlinitiative.resources.dtds.aida22;

/**
 * Java content class for dateType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/mihai/programs/jwsdp13/jaxb/bin/aida22.xsd line 145)
 * <p>
 * <pre>
 * &lt;complexType name="dateType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="attributeAddedBy" type="{http://www.idmlinitiative.org/resources/dtds/AIDA22.xsd}orgRefKey" />
 *       &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}date" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface DateType {

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAttributeAddedBy();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAttributeAddedBy(java.lang.String value);

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.util.Date}
     */
    java.util.Date getDate();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Date}
     */
    void setDate(java.util.Date value);
}
