package phex.xml;

/**
 * Java content class for XJBUpdateResponse complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/C:/projects/sourceforge/phex-project/svn/src/phex/xml/phex.xsd line 38)
 * <p>
 * <pre>
 * &lt;complexType name="XJBUpdateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="version" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="isBeta" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="info" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="header" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
 */
public interface XJBUpdateResponse {

    /**
     * Gets the value of the VersionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the VersionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVersionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link phex.xml.XJBUpdateResponse.VersionType}
     * 
     */
    java.util.List getVersionList();

    /**
     * Gets the value of the InfoList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the InfoList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInfoList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link phex.xml.XJBUpdateResponse.InfoType}
     * 
     */
    java.util.List getInfoList();

    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/C:/projects/sourceforge/phex-project/svn/src/phex/xml/phex.xsd line 66)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="header" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface InfoType {

        /**
         * Gets the value of the text property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getText();

        /**
         * Sets the value of the text property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setText(java.lang.String value);

        /**
         * Gets the value of the header property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getHeader();

        /**
         * Sets the value of the header property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setHeader(java.lang.String value);

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getId();

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setId(java.lang.String value);
    }

    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/C:/projects/sourceforge/phex-project/svn/src/phex/xml/phex.xsd line 46)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="isBeta" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface VersionType {

        /**
         * Gets the value of the beta property.
         * 
         */
        boolean isBeta();

        /**
         * Sets the value of the beta property.
         * 
         */
        void setBeta(boolean value);

        /**
         * Gets the value of the text property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getText();

        /**
         * Sets the value of the text property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setText(java.lang.String value);

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getId();

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setId(java.lang.String value);
    }
}
