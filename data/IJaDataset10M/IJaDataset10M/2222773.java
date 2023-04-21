package uips.tree.outer.interfaces;

import java.util.List;

public interface IEventOut {

    /**
     * Gets the value of the property property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPropertyOut }
     *
     *
     */
    public List<IPropertyOut> getProperty();

    /**
     * Gets the value of the properties property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPropertiesOut }
     *
     *
     */
    public List<IPropertiesOut> getProperties();

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId();

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value);

    /**
     * Gets the value of the stamp property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStamp();

    /**
     * Sets the value of the stamp property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStamp(String value);

    /**
     * Gets the value of the time property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getTime();

    /**
     * Sets the value of the time property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setTime(Integer value);
}
