package cz.fi.muni.xkremser.editor.server.mods;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * LDR/6
 * 
 * <p>Java class for typeOfResourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="typeOfResourceType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.loc.gov/mods/v3>resourceType">
 *       &lt;attribute name="collection" type="{http://www.loc.gov/mods/v3}yes" />
 *       &lt;attribute name="manuscript" type="{http://www.loc.gov/mods/v3}yes" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "typeOfResourceType", propOrder = { "value" })
public class TypeOfResourceType {

    /** The value. */
    @XmlValue
    protected String value;

    /** The collection. */
    @XmlAttribute
    protected Yes collection;

    /** The manuscript. */
    @XmlAttribute
    protected Yes manuscript;

    /**
     * Gets the value of the value property.
     *
     * @return the value
     * possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the collection property.
     *
     * @return the collection
     * possible object is
     * {@link Yes }
     */
    public Yes getCollection() {
        return collection;
    }

    /**
     * Sets the value of the collection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Yes }
     *     
     */
    public void setCollection(Yes value) {
        this.collection = value;
    }

    /**
     * Gets the value of the manuscript property.
     *
     * @return the manuscript
     * possible object is
     * {@link Yes }
     */
    public Yes getManuscript() {
        return manuscript;
    }

    /**
     * Sets the value of the manuscript property.
     * 
     * @param value
     *     allowed object is
     *     {@link Yes }
     *     
     */
    public void setManuscript(Yes value) {
        this.manuscript = value;
    }
}
