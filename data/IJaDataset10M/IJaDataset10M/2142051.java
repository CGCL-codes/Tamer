package de.beas.explicanto.screenplay.jaxb;

/**
 * Java content class for Scene complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Projects/Beck/explicanto33/VidyaServer/bin/explicantoScreenplay.xsd line 79)
 * <p>
 * <pre>
 * &lt;complexType name="Scene">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uid" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="plot" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timeOfDay" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timelines" type="{http://www.bea-services.de/explicanto}Frame" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="occurences" type="{http://www.bea-services.de/explicanto}Occurence" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="directions" type="{http://www.bea-services.de/explicanto}Direction" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Scene {

    /**
     * Gets the value of the Occurences property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Occurences property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOccurences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link de.beas.explicanto.screenplay.jaxb.Occurence}
     * 
     */
    java.util.List getOccurences();

    /**
     * Gets the value of the timeOfDay property.
     * 
     */
    int getTimeOfDay();

    /**
     * Sets the value of the timeOfDay property.
     * 
     */
    void setTimeOfDay(int value);

    /**
     * Gets the value of the order property.
     * 
     */
    int getOrder();

    /**
     * Sets the value of the order property.
     * 
     */
    void setOrder(int value);

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCode();

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCode(java.lang.String value);

    /**
     * Gets the value of the Timelines property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Timelines property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimelines().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link de.beas.explicanto.screenplay.jaxb.Frame}
     * 
     */
    java.util.List getTimelines();

    /**
     * Gets the value of the plot property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPlot();

    /**
     * Sets the value of the plot property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPlot(java.lang.String value);

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * Gets the value of the Directions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Directions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link de.beas.explicanto.screenplay.jaxb.Direction}
     * 
     */
    java.util.List getDirections();

    /**
     * Gets the value of the location property.
     * 
     */
    int getLocation();

    /**
     * Sets the value of the location property.
     * 
     */
    void setLocation(int value);

    /**
     * Gets the value of the uid property.
     * 
     */
    long getUid();

    /**
     * Sets the value of the uid property.
     * 
     */
    void setUid(long value);
}
