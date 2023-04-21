package de.beas.explicanto.jaxb;

/**
 * Java content class for TableComponent complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/explicanto33/VidyaServer/bin/ExplicantoTypes.xsd line 87)
 * <p>
 * <pre>
 * &lt;complexType name="TableComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cols" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rows" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="style" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="_content" type="{http://www.bea-services.de/explicanto}Component" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface TableComponent {

    /**
     * Gets the value of the type property.
     * 
     */
    int getType();

    /**
     * Sets the value of the type property.
     * 
     */
    void setType(int value);

    /**
     * Gets the value of the cols property.
     * 
     */
    int getCols();

    /**
     * Sets the value of the cols property.
     * 
     */
    void setCols(int value);

    /**
     * Gets the value of the style property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStyle();

    /**
     * Sets the value of the style property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStyle(java.lang.String value);

    /**
     * Gets the value of the rows property.
     * 
     */
    int getRows();

    /**
     * Sets the value of the rows property.
     * 
     */
    void setRows(int value);

    /**
     * Gets the value of the Content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link de.beas.explicanto.jaxb.Component}
     * 
     */
    java.util.List getContent();
}
