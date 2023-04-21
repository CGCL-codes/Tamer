package uips.tree.outer.impl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import uips.tree.outer.interfaces.ILayoutOut;
import uips.tree.outer.interfaces.IPropertiesOut;
import uips.tree.outer.interfaces.IPropertyOut;

/**
 * <p>Java class for layoutType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="layoutType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="property" type="{}propertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="properties" type="{}propertiesType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="class" use="required" type="{}fullyQualifiedIdentifier" />
 *       &lt;attribute name="model" type="{}fullyQualifiedIdentifier" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "layoutType", propOrder = { "properties", "property" })
public class LayoutOut implements ILayoutOut {

    @XmlElement(type = PropertyOut.class)
    protected List<IPropertyOut> property;

    @XmlElement(type = PropertiesOut.class)
    protected List<IPropertiesOut> properties;

    @XmlAttribute(name = "class", required = true)
    protected String clazz;

    @XmlAttribute
    protected String model;

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
     * {@link PropertyOut }
     *
     *
     */
    @Override
    public List<IPropertyOut> getProperty() {
        if (this.property == null) {
            this.property = new ArrayList<IPropertyOut>();
        }
        return this.property;
    }

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
     * {@link PropertiesOut }
     *
     *
     */
    @Override
    public List<IPropertiesOut> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList<IPropertiesOut>();
        }
        return this.properties;
    }

    /**
     * Gets the value of the clazz property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Override
    public String getClazz() {
        return this.clazz;
    }

    /**
     * Sets the value of the clazz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    @Override
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the model property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Override
    public String getModel() {
        return this.model;
    }

    /**
     * Sets the value of the model property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    @Override
    public void setModel(String value) {
        this.model = value;
    }
}
