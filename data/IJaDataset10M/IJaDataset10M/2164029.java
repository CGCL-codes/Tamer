package org.authsum.stitches.external;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ArrayOfAttributeValueHolder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfAttributeValueHolder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttributeValueHolder" type="{http://external.stitches.authsum.org}AttributeValueHolder" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfAttributeValueHolder", propOrder = { "attributeValueHolder" })
public class ArrayOfAttributeValueHolder implements Serializable {

    @XmlElement(name = "AttributeValueHolder", nillable = true)
    protected List<AttributeValueHolder> attributeValueHolder;

    /**
     * Gets the value of the attributeValueHolder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeValueHolder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeValueHolder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeValueHolder }
     * 
     * 
     */
    public List<AttributeValueHolder> getAttributeValueHolder() {
        if (attributeValueHolder == null) {
            attributeValueHolder = new ArrayList<AttributeValueHolder>();
        }
        return this.attributeValueHolder;
    }
}
